
pipeline {
    agent any

    environment {
        // Configurez vos variables d'environnement ici
        SONAR_TOKEN = credentials('sonar-token')
        MAVEN_CREDENTIALS = credentials('maven-credentials')
        MAVEN_HOME = 'C:\\apache-maven-3.9.9\\bin'
        SONAR_SCANNER_HOME = 'D:\\Haythem\\Telechargements\\Polytec\\ING_IRM_3\\Les fondamentaux du DevOps\\progs\\sonar-scanner-cli-5.0.1.3006-windows\\sonar-scanner-5.0.1.3006-windows\\bin'
    }

    stages {
        stage('Clone repository') {
            steps {
                // Cloner le dépôt
                git 'https://github.com/HaythemKchouk/Projet_Devops.git'
            }
        }

        stage('Build with Maven') {
            steps {
                // Compilation du projet avec Maven
                sh "${env.MAVEN_HOME}/mvn clean install -s ${env.WORKSPACE}/settings.xml" 
            }
        }

        stage('Run Tests') {
            steps {
                // Lancer les tests avec Maven
                sh "${env.MAVEN_HOME}/mvn test -s ${env.WORKSPACE}/settings.xml" 
            }
        }

        stage('SonarQube Analysis') {
            steps {
                // Analyser la qualité du code avec SonarQube
                withSonarQubeEnv('SonarQube') {
                    sh "${env.SONAR_SCANNER_HOME}/sonar-scanner -Dsonar.login=${SONAR_TOKEN} -Dsonar.projectBaseDir=${env.WORKSPACE}"
                }
            }
        }

        stage('Deploy to Nexus') {
            steps {
                // Déployer l'artefact dans Nexus
                sh "${env.MAVEN_HOME}/mvn deploy -s ${env.WORKSPACE}/settings.xml -DskipTests"
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
