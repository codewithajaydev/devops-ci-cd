pipeline {
    agent any

    environment {
        DOCKER_IMAGE = "ajaybecse03/devops-cicd"
        DOCKER_TAG = "${BUILD_NUMBER}"
    }

    tools {
        maven 'Maven'   // must match Jenkins tool name
        jdk '17'
    }

    stages {

        stage('Clone Code') {
            steps {
                git branch: 'main', url: 'https://github.com/codewithajaydev/devops-ci-cd.git'
            }
        }

        stage('Build JAR') {
            steps {
                bat 'mvn clean package -DskipTests'
            }
        }

        stage('Build Docker Image') {
            steps {
                bat 'docker build -t $DOCKER_IMAGE:$DOCKER_TAG .'
            }
        }

        stage('Login to DockerHub') {
            steps {
                withCredentials([usernamePassword(
                    credentialsId: 'docker-creds',
                    usernameVariable: 'DOCKER_USER',
                    passwordVariable: 'DOCKER_PASS'
                )]) {
                    bat '''
                    echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin
                    '''
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                bat 'docker push $DOCKER_IMAGE:$DOCKER_TAG'
            }
        }

        stage('Update Deployment Image') {
            steps {
                bat '''
                sed -i "s|image: .*|image: $DOCKER_IMAGE:$DOCKER_TAG|g" deployment.yaml
                '''
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                bat '''
                kubectl apply -f deployment.yaml
                kubectl apply -f service.yaml
                '''
            }
        }
    }

    post {
        success {
            echo '✅ Pipeline executed successfully!'
        }
        failure {
            echo '❌ Pipeline failed. Check logs.'
        }
    }
}