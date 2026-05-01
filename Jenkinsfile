pipeline {
    agent any

    environment {
        PROJECT_DIR = ''
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Detect Project Directory') {
            steps {
                script {
                    if (fileExists('pom.xml')) {
                        env.PROJECT_DIR = '.'
                    } else if (fileExists('java/pom.xml')) {
                        env.PROJECT_DIR = 'java'
                    } else {
                        error('No supported build file found. Add pom.xml or build.gradle[.kts].')
                    }
                }
            }
        }

        stage('Test') {
            steps {
                dir(env.PROJECT_DIR) {
                    script {
                        if (isUnix()) {
                            sh './mvnw clean test'
                        } else {
                            bat 'mvnw.cmd clean test'
                        }
                    }
                }
            }
        }

        stage('Build') {
            steps {
                dir(env.PROJECT_DIR) {
                    script {
                        if (isUnix()) {
                            sh './mvnw clean package -DskipTests'
                        } else {
                            bat 'mvnw.cmd clean package -DskipTests'
                        }
                    }
                }
            }
        }

        stage('Archive Artifact') {
            steps {
                dir(env.PROJECT_DIR) {
                    archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
                }
            }
        }
    }
}