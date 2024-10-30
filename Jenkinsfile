pipeline {
    agent any

    environment {
        SONAR_TOKEN = credentials('sonar-token')
        MAVEN_HOME = 'C:\\apache-maven-3.9.9\\bin'
        SONAR_SCANNER_HOME = 'D:\\Haythem\\Telechargements\\Polytec\\ING_IRM_3\\Les fondamentaux du DevOps\\progs\\sonar-scanner-cli-5.0.1.3006-windows\\sonar-scanner-5.0.1.3006-windows\\bin'
    }

    stages {
        stage('Clone repository') {
            steps {
                git 'https://github.com/HaythemKchouk/Projet_Devops.git'
            }
        }

        stage('Build with Maven') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'maven-credentials', usernameVariable: 'MAVEN_USERNAME', passwordVariable: 'MAVEN_PASSWORD')]) {
                    sh "${env.MAVEN_HOME}/mvn clean install -s ${env.WORKSPACE}/settings.xml -Dusername=${MAVEN_USERNAME} -Dpassword=${MAVEN_PASSWORD}"
                }
            }
        }

        stage('Run Tests') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'maven-credentials', usernameVariable: 'MAVEN_USERNAME', passwordVariable: 'MAVEN_PASSWORD')]) {
                    sh "${env.MAVEN_HOME}/mvn test -s ${env.WORKSPACE}/settings.xml -Dusername=${MAVEN_USERNAME} -Dpassword=${MAVEN_PASSWORD}"
                }
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('SonarQube') {
                    sh "${env.SONAR_SCANNER_HOME}/sonar-scanner -Dsonar.login=${SONAR_TOKEN} -Dsonar.projectBaseDir=${env.WORKSPACE}"
                }
            }
        }

        stage('Deploy to Nexus') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'maven-credentials', usernameVariable: 'MAVEN_USERNAME', passwordVariable: 'MAVEN_PASSWORD')]) {
                    sh "${env.MAVEN_HOME}/mvn deploy -s ${env.WORKSPACE}/settings.xml -DskipTests -Dusername=${MAVEN_USERNAME} -Dpassword=${MAVEN_PASSWORD}"
                }
            }
        }
    }

    post {
        success {
            echo "Pipeline terminé avec succès."
        }
        failure {
            echo "Le pipeline a échoué."
        }
    }
}
