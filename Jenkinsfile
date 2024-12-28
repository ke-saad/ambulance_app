pipeline {
    agent any

    tools {
        maven 'maven'
        jdk 'jdk17'
        nodejs 'nodejs18'
        gradle 'gradle'
    }

    environment {
        DOCKER_IMAGE_PREFIX = "kellalisaad256/ambulance-app"
        DOCKER_REGISTRY = "docker.io"
        GIT_CRED_ID = 'saadjenkinsid1234'
        DOCKER_CRED_ID = 'kellalisaad256:La3binela3bine@' //todo:hide these later

    }
    
    stages {
        stage('Checkout') {
            steps {
                checkout([$class: 'GitSCM',
                         branches: [[name: '*/main']],
                         userRemoteConfigs: [[credentialsId: "${GIT_CRED_ID}", url: 'https://github.com/ke-saad/ambulance_app.git']]])
            }
        }

        stage('Build Backend') {
            steps {
              dir('backend'){
                script {
                   sh "./mvnw clean install -DskipTests"
                 }
               }
            }
        }

        stage('Test Backend') {
            steps {
                dir('backend') {
                    script {
                        sh "./mvnw test" 
                    }
                }
            }
        }

        stage('Build Frontend') {
            steps {
                dir('frontend') {
                    script {
                       sh "npm install"
                       sh "npm run build"
                   }
                }
            }
        }

         stage('Build Mobile') {
            steps {
              dir('mobile'){
                script {
                   sh "./gradlew assembleDebug --stacktrace -Dorg.gradle.daemon=false"
                 }
               }
            }
        }

        stage('Build Docker Images') {
            steps {
                script {
                    def dockerLoginStep = docker.withRegistry("${DOCKER_REGISTRY}", "${DOCKER_CRED_ID}")
                    
                    
                   dockerLoginStep {
                        dir('backend/eureka-server') {
                           sh "docker build -t ${DOCKER_IMAGE_PREFIX}-eureka-server:latest ."
                           sh "docker tag ${DOCKER_IMAGE_PREFIX}-eureka-server:latest  ${DOCKER_REGISTRY}/${DOCKER_IMAGE_PREFIX}-eureka-server:latest"
                           sh "docker push ${DOCKER_REGISTRY}/${DOCKER_IMAGE_PREFIX}-eureka-server:latest"
                        }
                     }

                    
                    dockerLoginStep {
                         dir('backend/gateway') {
                           sh "docker build -t ${DOCKER_IMAGE_PREFIX}-gateway:latest ."
                           sh "docker tag ${DOCKER_IMAGE_PREFIX}-gateway:latest  ${DOCKER_REGISTRY}/${DOCKER_IMAGE_PREFIX}-gateway:latest"
                           sh "docker push ${DOCKER_REGISTRY}/${DOCKER_IMAGE_PREFIX}-gateway:latest"
                        }
                    }
                    
                    
                   dockerLoginStep {
                       dir('backend/ambulance-service') {
                            sh "docker build -t ${DOCKER_IMAGE_PREFIX}-ambulance-service:latest ."
                            sh "docker tag ${DOCKER_IMAGE_PREFIX}-ambulance-service:latest ${DOCKER_REGISTRY}/${DOCKER_IMAGE_PREFIX}-ambulance-service:latest"
                            sh "docker push ${DOCKER_REGISTRY}/${DOCKER_IMAGE_PREFIX}-ambulance-service:latest"
                           
                        }
                   }
                  
                   dockerLoginStep {
                        dir('backend/hospital-service') {
                            sh "docker build -t ${DOCKER_IMAGE_PREFIX}-hospital-service:latest ."
                            sh "docker tag ${DOCKER_IMAGE_PREFIX}-hospital-service:latest ${DOCKER_REGISTRY}/${DOCKER_IMAGE_PREFIX}-hospital-service:latest"
                            sh "docker push ${DOCKER_REGISTRY}/${DOCKER_IMAGE_PREFIX}-hospital-service:latest"
                        }
                    }
                   
                   dockerLoginStep {
                       dir('backend/patient-service') {
                            sh "docker build -t ${DOCKER_IMAGE_PREFIX}-patient-service:latest ."
                            sh "docker tag ${DOCKER_IMAGE_PREFIX}-patient-service:latest ${DOCKER_REGISTRY}/${DOCKER_IMAGE_PREFIX}-patient-service:latest"
                            sh "docker push ${DOCKER_REGISTRY}/${DOCKER_IMAGE_PREFIX}-patient-service:latest"
                         }
                    }
                   
                   dockerLoginStep {
                      dir('backend/routing-service') {
                           sh "docker build -t ${DOCKER_IMAGE_PREFIX}-routing-service:latest ."
                           sh "docker tag ${DOCKER_IMAGE_PREFIX}-routing-service:latest ${DOCKER_REGISTRY}/${DOCKER_IMAGE_PREFIX}-routing-service:latest"
                           sh "docker push ${DOCKER_REGISTRY}/${DOCKER_IMAGE_PREFIX}-routing-service:latest"
                         }
                    }

                   
                   dockerLoginStep {
                        dir('frontend') {
                            sh "docker build -t ${DOCKER_IMAGE_PREFIX}-frontend:latest ."
                            sh "docker tag ${DOCKER_IMAGE_PREFIX}-frontend:latest ${DOCKER_REGISTRY}/${DOCKER_IMAGE_PREFIX}-frontend:latest"
                            sh "docker push ${DOCKER_REGISTRY}/${DOCKER_IMAGE_PREFIX}-frontend:latest"
                        }
                    }
                   
                    
                    dockerLoginStep {
                        dir('mobile') {
                             sh "docker build -t ${DOCKER_IMAGE_PREFIX}-mobile:latest ."
                             sh "docker tag ${DOCKER_IMAGE_PREFIX}-mobile:latest ${DOCKER_REGISTRY}/${DOCKER_IMAGE_PREFIX}-mobile:latest"
                             sh "docker push ${DOCKER_REGISTRY}/${DOCKER_IMAGE_PREFIX}-mobile:latest"
                        }
                    }
                }
            }
        }
    
         stage('Deploy') {
            steps {
                
                echo "Deployment is not configured in this example Jenkinsfile"
            }
        }
    }
}