pipeline {
    agent { label 'docker-agent' }

    tools {
        maven 'maven'
        jdk 'jdk17'
        nodejs 'nodejs18'
        gradle 'gradle'
    }

    environment {
        DOCKER_IMAGE_PREFIX = "kellalisaad256/ambulance-app"
        DOCKER_REGISTRY = "https://docker.io"
        GIT_CRED_ID = 'saadjenkinsid1234'
        
        DOCKER_CRED_ID = 'saaddockerhubcreds1234'
        
    }

    stages {
        stage('Checkout') {
            steps {
                checkout([$class: 'GitSCM',
                         branches: [[name: '*/master']],
                         userRemoteConfigs: [[credentialsId: "${GIT_CRED_ID}", url: 'https://github.com/ke-saad/ambulance_app.git']]])
            }
        }

        stage('Build Backend') {
            steps {
                dir('backend') {
                    script {
                        sh 'ls -l mvnw'
                        sh 'chmod +x mvnw'
                        sh 'pwd'
                        sh './mvnw clean install'
                    }
                }
            }
        }

        stage('Test Backend') {
            steps {
                dir('backend') {
                    script {
                        sh './mvnw test'
                    }
                }
            }
        }

        stage('Build Frontend') {
            steps {
                dir('frontend') {
                    script {
                        sh 'npm install'
                        
                    }
                }
            }
        }

         stage('Build Docker Images') {
            steps {
                script {
                   docker.withRegistry("${DOCKER_REGISTRY}", "${DOCKER_CRED_ID}") {
                        def services = [
                           ['backend/eureka-server', 'eureka-server'],
                            ['backend/gateway', 'gateway'],
                            ['backend/ambulance-service', 'ambulance-service'],
                            ['backend/hospital-service', 'hospital-service'],
                            ['backend/patient-service', 'patient-service'],
                            ['backend/routing-service', 'routing-service'],
                             
                             
                          ]

                            services.each { service ->

                                dir(service[0]) {
                                        sh "docker build -t ${DOCKER_IMAGE_PREFIX}-${service[1]}:latest ."
                                        sh "docker tag ${DOCKER_IMAGE_PREFIX}-${service[1]}:latest ${DOCKER_REGISTRY}/${DOCKER_IMAGE_PREFIX}-${service[1]}:latest"
                                        sh "docker push ${DOCKER_REGISTRY}/${DOCKER_IMAGE_PREFIX}-${service[1]}:latest"
                                }

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