pipeline {
    agent {
        dockerfile true 
    }
    tools {
        maven 'maven'
        jdk 'Java'
        dockerTool 'Docker' // Use the configured docker tool
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
       stage('Build Docker Image') {
            steps {
                 withDockerContainer(toolName: 'docker', image:'openjdk:21-jdk-slim') {
                       sh 'docker build -t my-${JOB_NAME}:${BUILD_NUMBER} .'
                    }
            }
         }
       }
    }
