@Library('forge-shared-library') _

pipeline {
    agent {
        docker { image 'openjdk:8-jdk' }
    }
    environment {
        DISCORD_PREFIX = "Magma: ${BRANCH_NAME} #${BUILD_NUMBER}"
    }
    stages {
        stage('Setup') {
            steps {
                withCredentials([string(credentialsId: 'DISCORD_WEBHOOK', variable: 'discordWebhook')]) {
                    discordSend(
                            title: "${DISCORD_PREFIX} Started",
                            successful: true,
                            result: 'ABORTED',
                            thumbnail: "https://img.hexeption.co.uk/Magma_Block.png",
                            webhookURL: "${discordWebhook}"
                    )
                }
                sh 'git submodule update --init --recursive'
                sh 'chmod +x gradlew'
            }
        }

        stage('Build') {
            steps {
              withCredentials([string(credentialsId: 'MAGMA_GRADLE_PRO', variable: 'MAGMA_GRADLE_PRO'),string(credentialsId: 'MAGMA_KEY_STORE', variable: 'MAGMA_KEY_STORE')]) {
                sh 'base64 -d ${MAGMA_GRADLE_PRO} >> gradle.properties'
                sh 'base64 -d ${MAGMA_KEY_STORE} > keystore.jks'
              }
                sh './gradlew launch4j --console=plain'
            }
        }

        stage('Release') {
            when {
                not {
                    changeRequest()
                }
            }
            steps {
                withCredentials([string(credentialsId: 'GITHUB_TOKEN', variable: 'GITHUB_TOKEN')]) {
                    sh 'chmod +x gradlew && ./gradlew githubRelease --console=plain'
                }
            }
        }
    }
    post {
        always {
            script {
                archiveArtifacts artifacts: 'build/distributions/*server.*', fingerprint: true, onlyIfSuccessful: true, allowEmptyArchive: true
                withCredentials([string(credentialsId: 'DISCORD_WEBHOOK', variable: 'discordWebhook')]) {
                    discordSend(
                            title: "Finished ${currentBuild.currentResult}",
                            description: '```\n' + getChanges(currentBuild) + '\n```',
                            successful: currentBuild.resultIsBetterOrEqualTo("SUCCESS"),
                            result: currentBuild.currentResult,
                            thumbnail: "https://img.hexeption.co.uk/Magma_Block.png",
                            webhookURL: "${discordWebhook}"
                    )
                }
                cleanWs()
            }
        }
    }
}
