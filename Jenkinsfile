pipeline {
    agent any
    tools {
        maven 'maven'
        jdk 'Java'
        dockerTool 'Docker'
    }
   environment {
    LIB_PATH = '/var/jenkins_home/workspace/events-lib/target/events-lib-1.0-SNAPSHOT.jar'
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
                        fileExists(env.LIB_PATH)
                    }
                }
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

    

