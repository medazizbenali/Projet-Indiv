pipeline {
  agent any

  options {
    timestamps()
  }

  stages {
    stage('Checkout') {
      steps {
        checkout scm
      }
    }

    stage('Backend - tests') {
      agent { docker { image 'maven:3.9-eclipse-temurin-17' } }
      steps {
        dir('backend') {
          sh 'mvn -B clean test'
        }
      }
    }

    stage('Frontend - build') {
      agent { docker { image 'node:20-alpine' } }
      steps {
        dir('frontend') {
          sh 'npm ci'
          sh 'npm run build'
        }
      }
    }

    stage('Security - Trivy scan') {
      agent { docker { image 'aquasec/trivy:latest' args "--entrypoint=''" } }
      steps {
        sh 'trivy fs --severity HIGH,CRITICAL --exit-code 1 .'
      }
    }
  }

  post {
    success {
      echo 'CI Jenkins OK'
    }
    failure {
      echo 'CI Jenkins FAILED'
    }
  }
}
