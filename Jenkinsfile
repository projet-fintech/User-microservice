pipeline {
            agent any
            tools {
                maven 'maven'
                jdk 'Java'
              dockerTool 'Docker'
             }
             parameters {
              string(name: 'BUILD_LIB_SUCCESS', defaultValue: 'false', description: 'Check if the lib build is a success')
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
               stage('Check Lib build') {
                  when {
                    expression {
                        params.BUILD_LIB_SUCCESS == "true"
                        }
                    }
                    steps {
                       script{
                         def repoPath = "/var/jenkins_home/workspace/User-Micorservice/repo"
                         while (!fileExists(repoPath)) {
                             echo "Waiting for repo folder to be created..."
                              sleep 10
                            }
                          echo "repo folder created, launching docker build"
                         }
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
