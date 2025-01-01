pipeline {
    agent any
    tools {
        maven 'maven'
        jdk 'Java'
        dockerTool 'Docker'
    }
    environment {
        AWS_REGION = 'eu-west-3'
        ECR_REGISTRY = '329599629502.dkr.ecr.eu-west-3.amazonaws.com'
        IMAGE_NAME = 'user-microservice'
    }
    stages {
        stage('Checkout') {
            steps {
                checkout scmGit(
                    branches: [[name: '*/main']],
                    extensions: [],
                    userRemoteConfigs: [[credentialsId: 'ser3elah', url: 'https://github.com/projet-fintech/User-microservice.git']]
                )
            }
        }
        
        stage('Prepare Dependencies') {
            steps {
                script {
                    sh '''
                        mkdir -p repo/com/banque/events-lib/1.0-SNAPSHOT
                        cp /var/jenkins_home/shared-artifacts/repo/com/banque/events-lib/1.0-SNAPSHOT/events-lib-1.0-SNAPSHOT.jar ./events-lib-1.0-SNAPSHOT.jar
                    '''
                }
            }
        }
        
        stage('Build Docker Image') {
            steps {
                script {
                    def localImageName = "${IMAGE_NAME}:${BUILD_NUMBER}"
                    sh "docker build -t ${localImageName} ."
                }
            }
        }
        
        stage('Push to ECR') {
            steps {
                script {
                    // Login à ECR
                    sh "aws ecr get-login-password --region ${AWS_REGION} | docker login --username AWS --password-stdin ${ECR_REGISTRY}"
                    
                    // Tag et push de l'image
                    def localImageName = "${IMAGE_NAME}:${BUILD_NUMBER}"
                    def ecrImageLatest = "${ECR_REGISTRY}/${IMAGE_NAME}:latest"
                    def ecrImageVersioned = "${ECR_REGISTRY}/${IMAGE_NAME}:${BUILD_NUMBER}"
                    
                    sh """
                        docker tag ${localImageName} ${ecrImageLatest}
                        docker tag ${localImageName} ${ecrImageVersioned}
                        docker push ${ecrImageLatest}
                        docker push ${ecrImageVersioned}
                    """
                }
            }
        }
        
        stage('Cleanup') {
            steps {
                script {
                    // Nettoyage des images locales
                    sh """
                        docker rmi ${IMAGE_NAME}:${BUILD_NUMBER} || true
                        docker rmi ${ECR_REGISTRY}/${IMAGE_NAME}:${BUILD_NUMBER} || true
                        docker rmi ${ECR_REGISTRY}/${IMAGE_NAME}:latest || true
                    """
                }
            }
        }
    }
}
