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
                bat 'docker build -t %DOCKER_IMAGE%:%DOCKER_TAG% .'
            }
        }

        stage('Login to DockerHub') {
            steps {
                withCredentials([usernamePassword(
                    credentialsId: 'docker-creds',
                    usernameVariable: 'DOCKER_USER',
                    passwordVariable: 'DOCKER_PASS'
                )]) {
                    powershell '''
                    docker logout
                    docker login -u $env:DOCKER_USER -p $env:DOCKER_PASS
                    '''
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                bat 'docker push %DOCKER_IMAGE%:%DOCKER_TAG%'
            }
        }

        stage('Update Deployment Image') {
            steps {
                powershell '''
                (Get-Content deployment.yaml) -replace 'image: .*', 'image: ${env:DOCKER_IMAGE}:${env:DOCKER_TAG}' | Set-Content deployment.yaml
                '''
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                powershell '''
                minikube start --driver=docker
                Start-Sleep -Seconds 5
                '''
                bat '''
                kubectl apply -f deployment.yaml --validate=false
                kubectl apply -f service.yaml --validate=false
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