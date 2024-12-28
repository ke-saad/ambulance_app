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
        DOCKER_CRED_ID = 'kellalisaad256:La3binela3bine@'
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
                dir('backend') {
                    script {
                        sh 'ls -l mvnw'
                        sh 'pwd'
                        sh './mvnw clean install -DskipTests'
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
                        sh 'npm run build'
                    }
                }
            }
        }

        stage('Build Mobile') {
            steps {
                dir('mobile') {
                    script {
                        sh './gradlew assembleDebug --stacktrace -Dorg.gradle.daemon=false'
                    }
                }
            }
        }

        stage('Build Docker Images') {
            steps {
                script {
                    def dockerLoginStep = docker.withRegistry("${DOCKER_REGISTRY}", "${DOCKER_CRED_ID}")

                    def services = [
                        ['backend/eureka-server', 'eureka-server'],
                        ['backend/gateway', 'gateway'],
                        ['backend/ambulance-service', 'ambulance-service'],
                        ['backend/hospital-service', 'hospital-service'],
                        ['backend/patient-service', 'patient-service'],
                        ['backend/routing-service', 'routing-service'],
                        ['frontend', 'frontend'],
                        ['mobile', 'mobile']
                    ]
                    
                    services.each { service ->
                        dockerLoginStep {
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
