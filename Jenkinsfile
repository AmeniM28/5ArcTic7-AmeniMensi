pipeline {
    agent any

    tools {
        jdk 'JDK17'
        maven 'Maven3'
    }

    environment {
        DOCKER_IMAGE = 'ameni1/5arctict7-amenimensi-backend'
        DOCKER_CREDENTIALS = credentials('docker-hub-credentials')
    }

    stages {
        stage('Checkout') {
            steps {
                echo 'Checkout du code...'
                checkout scm
            }
        }

        stage('Build') {
            steps {
                echo 'Compilation avec Maven...'
                dir('backend') {
                    sh 'mvn clean package -DskipTests'
                }
            }
        }

        stage('SonarQube Analysis') {
            steps {
                echo 'Analyse SonarQube...'
                withSonarQubeEnv('SonarQube') {
                    dir('backend') {
                        sh 'mvn sonar:sonar'
                    }
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                echo 'Construction de l image Docker...'
                dir('backend') {
                    sh "docker build -t ${DOCKER_IMAGE}:${BUILD_NUMBER} -t ${DOCKER_IMAGE}:latest ."
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                echo 'Push vers Docker Hub...'
                sh "echo ${DOCKER_CREDENTIALS_PSW} | docker login -u ${DOCKER_CREDENTIALS_USR} --password-stdin"
                catchError(buildResult: 'SUCCESS', stageResult: 'UNSTABLE') {
                    retry(3) {
                        sh "docker push ${DOCKER_IMAGE}:${BUILD_NUMBER}"
                        sh "docker push ${DOCKER_IMAGE}:latest"
                    }
                }
            }
        }
    }

    post {
        success {
            echo 'Pipeline reussi !'
            emailext(
                to: 'amenimensi91@gmail.com',
                subject: "Jenkins : Build #${BUILD_NUMBER} REUSSI",
                body: """
                    <h2>Build Jenkins REUSSI</h2>
                    <p><strong>Job :</strong> ${JOB_NAME}</p>
                    <p><strong>Build :</strong> #${BUILD_NUMBER}</p>
                    <p><strong>Statut :</strong> SUCCESS</p>
                    <p><strong>Duree :</strong> ${currentBuild.durationString}</p>
                    <p><a href="${BUILD_URL}console">Voir la console output</a></p>
                """,
                mimeType: 'text/html'
            )
        }
        unstable {
            echo 'Pipeline termine avec avertissements.'
            emailext(
                to: 'amenimensi91@gmail.com',
                subject: "Jenkins : Build #${BUILD_NUMBER} INSTABLE",
                body: """
                    <h2>Build Jenkins INSTABLE</h2>
                    <p><strong>Job :</strong> ${JOB_NAME}</p>
                    <p><strong>Build :</strong> #${BUILD_NUMBER}</p>
                    <p><strong>Statut :</strong> UNSTABLE (push Docker partiel)</p>
                    <p><a href="${BUILD_URL}console">Voir la console output</a></p>
                """,
                mimeType: 'text/html'
            )
        }
        failure {
            echo 'Pipeline echoue.'
            emailext(
                to: 'amenimensi91@gmail.com',
                subject: "Jenkins : Build #${BUILD_NUMBER} ECHOUE",
                body: """
                    <h2>Build Jenkins ECHOUE</h2>
                    <p><strong>Job :</strong> ${JOB_NAME}</p>
                    <p><strong>Build :</strong> #${BUILD_NUMBER}</p>
                    <p><strong>Statut :</strong> FAILURE</p>
                    <p><a href="${BUILD_URL}console">Voir la console output</a></p>
                """,
                mimeType: 'text/html'
            )
        }
    }
}