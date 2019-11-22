@Library('forge-shared-library')_

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
          withCredentials([string(credentialsId: 'DISCORD_WEBHOOK', variable: 'discordWebhook')]) {
            discordSend(
              title: "Magma: ${branch} #${BUILD_NUMBER} Finished ${currentBuild.currentResult}",
              description: '```\n' + getChanges(currentBuild) + '\n```',
              successful: currentBuild.resultIsBetterOrEqualTo("SUCCESS"),
              result: currentBuild.currentResult,
              thumbnail: JENKINS_HEAD,
              webhookURL: "${discordWebhook}"
            )
          }
        }
      }
    }
  }
}