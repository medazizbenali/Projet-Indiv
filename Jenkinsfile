pipeline {
  agent any

  options { timestamps() }

  environment {
    // Pour ZAP/k6
    FRONT_URL = "http://localhost:3000"
    BACK_URL  = "http://localhost:8080"
  }

  stages {
    stage('Checkout') {
      steps { checkout scm }
    }

    stage('Backend - unit + integration tests') {
      agent { docker { image 'maven:3.9-eclipse-temurin-17' } }
      steps {
        dir('backend') {
          sh 'mvn -B clean test'
        }
      }
    }

    stage('Frontend - tests + build') {
      agent { docker { image 'node:20-alpine' } }
      steps {
        dir('frontend') {
          sh 'npm install --no-audit --no-fund'
          sh 'npm run test'
          sh 'npm run build'
        }
      }
    }

    stage('Security - Trivy scan (repo)') {
      agent { docker { image 'aquasec/trivy:latest' args "--entrypoint=''" } }
      steps {
        sh 'trivy fs --severity HIGH,CRITICAL --exit-code 1 .'
      }
    }

    stage('Security - OWASP Dependency-Check (backend)') {
      agent any
      steps {
        sh '''
          set -eu
          rm -rf dependency-check-report || true
          docker run --rm             -v "$PWD:/src"             owasp/dependency-check:latest             --scan /src/backend             --format "HTML"             --out /src/dependency-check-report || true
        '''
        archiveArtifacts artifacts: 'dependency-check-report/**', allowEmptyArchive: true
      }
    }

    stage('DAST (ZAP) + Load (k6)') {
      agent any
      steps {
        sh '''
          set -eu

          docker compose up -d --build

          # wait backend
          for i in $(seq 1 40); do
            if curl -fsS "http://localhost:8080/actuator/health" >/dev/null; then
              echo "Backend is ready"
              break
            fi
            echo "Waiting backend... ($i/40)"
            sleep 2
          done

          # ZAP baseline (ne fait pas échouer la CI pour une démo)
          docker run --rm --network host owasp/zap2docker-stable zap-baseline.py             -t "http://localhost:3000" -r zap-report.html || true

          # k6
          docker run --rm --network host -v "$PWD:/src" grafana/k6 run /src/tests/load/k6-smoke.js
          docker run --rm --network host -v "$PWD:/src" grafana/k6 run /src/tests/load/k6-spike.js
        '''
      }
      post {
        always {
          sh '''
            set +e
            docker compose down -v || true
          '''
          archiveArtifacts artifacts: 'zap-report.html', allowEmptyArchive: true
        }
      }
    }
  }

  post {
    success { echo 'CI Jenkins OK' }
    failure { echo 'CI Jenkins FAILED' }
  }
}
