pipeline {
    agent {
        docker { image 'openjdk:8-jdk' }
    }
    stages {
        stage('Build') {
            steps {
                sh 'chmod +x gradlew'
                sh './gradlew launch4j --console=plain'
            }
        }
    }
}