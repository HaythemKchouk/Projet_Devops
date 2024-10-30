pipeline {
    agent any

    environment {
        SONAR_TOKEN = credentials('sonar-token')  
        MAVEN_HOME = 'C:\\apache-maven-3.9.9'  
        SONAR_SCANNER_HOME = 'D:\\Haythem\\Telechargements\\Polytec\\ING_IRM_3\\Les fondamentaux du DevOps\\progs\\sonar-scanner-cli-5.0.1.3006-windows'
    }

    stages {
        stage('Clone repository') {
            steps {
                script {
                    // Vérifiez si la branche spécifiée existe dans le dépôt
                    def branchExists = bat(script: "cmd.exe /c git ls-remote --heads https://github.com/HaythemKchouk/Projet_Devops.git branch_haythem", returnStatus: true) == 0
                    if (!branchExists) {
                        error "La branche 'branch_haythem' n'existe pas dans le dépôt."
                    }
                }
                // Utilisez les identifiants pour accéder au dépôt
                git branch: 'branch_haythem', credentialsId: 'github-credentials', url: 'https://github.com/HaythemKchouk/Projet_Devops.git'
            }
        }

        stage('Build with Maven') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'maven-credentials', usernameVariable: 'MAVEN_USERNAME', passwordVariable: 'MAVEN_PASSWORD')]) {
                    bat "cmd.exe /c ${env.MAVEN_HOME}\\bin\\mvn clean install -s ${env.WORKSPACE}\\kaddem\\settings.xml -Dusername=${MAVEN_USERNAME} -Dpassword=${MAVEN_PASSWORD}"
                }
            }
        }

        stage('Run Tests') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'maven-credentials', usernameVariable: 'MAVEN_USERNAME', passwordVariable: 'MAVEN_PASSWORD')]) {
                    bat "cmd.exe /c ${env.MAVEN_HOME}\\bin\\mvn test -s ${env.WORKSPACE}\\kaddem\\settings.xml -Dusername=${MAVEN_USERNAME} -Dpassword=${MAVEN_PASSWORD}"
                }
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('SonarQube') {
                    bat "cmd.exe /c ${env.SONAR_SCANNER_HOME}\\bin\\sonar-scanner -Dsonar.login=${SONAR_TOKEN} -Dsonar.projectBaseDir=${env.WORKSPACE}\\kaddem"
                }
            }
        }

        stage('Deploy to Nexus') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'maven-credentials', usernameVariable: 'MAVEN_USERNAME', passwordVariable: 'MAVEN_PASSWORD')]) {
                    bat "cmd.exe /c ${env.MAVEN_HOME}\\bin\\mvn deploy -s ${env.WORKSPACE}\\kaddem\\settings.xml -DskipTests -Dusername=${MAVEN_USERNAME} -Dpassword=${MAVEN_PASSWORD}"
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
