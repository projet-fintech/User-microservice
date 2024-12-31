pipeline {
     agent any
     tools {
        maven 'maven'
        jdk 'Java'
        dockerTool 'Docker'
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
                script {
                 def imageName = "my-${JOB_NAME.toLowerCase()}:${BUILD_NUMBER}"
                     sh "docker build -t ${imageName} ."
                  }
               }
            }
         }
     }
