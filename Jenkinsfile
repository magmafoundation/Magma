def msg

pipeline {
    agent {
        docker { image 'openjdk:8-jdk' }
    }
    stages {
        stage('Setup') {
          steps {
            sh 'git submodule update --init --recursive'
            sh 'chmod +x gradlew'
          }
        }
        
        stage('Build') {
          steps {
           sh './gradlew launch4j --console=plain'              
          }
        }

        stage('Release') {
          steps {
            withCredentials([string(credentialsId: 'GITHUB_TOKEN', variable: 'GITHUB_TOKEN')]) {
              sh 'chmod +x gradlew && ./gradlew githubRelease --console=plain'
            }
          }
        }
        stage('Notify') {
          steps {
            script {
            msg = "**Status:** " + currentBuild.currentResult.toLowerCase() + "\n"
            msg += "**Branch:** ${branch}\n"
            msg += "**Changes:** \n"
            if (!currentBuild.changeSets.isEmpty()) {
                currentBuild.changeSets.first().getLogs().each {
                    msg += "- `" + it.getCommitId().substring(0, 8) + "` *" + it.getComment().substring(0, it.getComment().length()-1) + "*\n"
                }
            } else {
                msg += "no changes for this run\n"
            }

            if (msg.length() > 1024) msg.take(msg.length() - 1024)

            }
            withCredentials([string(credentialsId: 'DISCORD_WEBHOOK', variable: 'discordWebhook')]) {
              discordSend thumbnail: "https://img.hexeption.co.uk/Magma_Block.png", successful: currentBuild.resultIsBetterOrEqualTo('SUCCESS'), description: "${msg}", link: env.BUILD_URL, title: "Magma:${branch} #${BUILD_NUMBER}", webhookURL: "${discordWebhook}"
            }
          }
        }
    }
}