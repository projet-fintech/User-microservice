pipeline {
    agent any
    tools {
        maven 'maven'
        jdk 'Java'
        dockerTool 'Docker'
    }
    environment {
        MAVEN_REPO_PATH = '/var/jenkins_home/shared-artifacts/repo'
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
        stage('Wait for events-lib') {
            steps {
                script {
                    waitUntil {
                        fileExists("${env.MAVEN_REPO_PATH}/com/banque/events-lib/1.0-SNAPSHOT/events-lib-1.0-SNAPSHOT.jar")
                    }
                }
            }
        }
        stage('Build Microservice') {
            steps {
                sh '''
                mvn clean install -DskipTests \
                -Dmaven.repo.local=${MAVEN_REPO_PATH}
                '''
            }
        }
        stage('Build Docker Image') {
            steps {
                script {
                    def imageName = "user-microservice:${BUILD_NUMBER}"
                    sh "docker build -t ${imageName} ."
                }
            }
        }
    }
}
