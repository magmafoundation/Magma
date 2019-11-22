pipeline {
    agent {
        docker { image 'openjdk:8-jdk' }
    }
    stages {
        stage('Build') {
            steps {
                sh 'git submodule update --init --recursive'
                sh 'chmod +x gradlew'
                sh './gradlew launch4j --console=plain'

                withCredentials([string(credentialsId: 'GITHUB_TOKEN', variable: 'GITHUB_TOKEN') {
                  sh 'chmod +x gradlew && ./gradlew githubRelease --console=plain'
                }

            }
        }
    }
}