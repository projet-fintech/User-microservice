pipeline {
  agent any
    tools {
        maven 'maven'
        docker 'docker'
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
                      def dockerImage = "my-${JOB_NAME}"
                      sh "docker build -t ${dockerImage}:${BUILD_NUMBER} ."
                  }
              }
          }
       }
}
