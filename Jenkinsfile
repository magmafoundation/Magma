pipeline {
  agent any

  stages {
  
    stage('build') {
  
      agent {
        docker { image 'openjdk:8-jdk' }
      }
  
      steps {
        sh 'chmod +x gradlew && ./gradlew launch4j --console=plain'
      }
  
    }
  
  }
}