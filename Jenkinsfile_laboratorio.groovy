pipeline {
    agent {
        label 'docker'
    }
    stages {
        stage('Build') {
            steps {
                echo 'Building stage!'
                sh 'make build'
            }
        }
        stage('Unit tests') {
            steps {
                sh 'make test-unit'
                archiveArtifacts artifacts: 'results/unit_result.xml'
                archiveArtifacts artifacts: 'results/coverage.xml'
            }
        }
        stage('API tests') {
            steps {
                sh 'make test-api'
                archiveArtifacts artifacts: 'results/api_result.xml'
            }
        }
        stage('E2E tests') {
            steps {
                sh 'make test-e2e'
                archiveArtifacts artifacts: 'results/cypress_result.xml', allowEmptyArchive: true
            }
        }
    }
    post {
        failure {
            emailext (
                subject: "Job fallido: ${currentBuild.fullDisplayName}",
                body: """<p>Resultado del Job: ${currentBuild.currentResult}</p>
                <p>Nombre del Job: ${env.JOB_NAME}</p>
                <p>Número de ejecución: ${env.BUILD_NUMBER}</p>
                <p>Para ver más detalles accede a la URL: ${env.BUILD_URL}</p>""",
                recipientProviders: [[$class: 'DevelopersRecipientProvider'], [$class: 'RequesterRecipientProvider']],
                to: 'jonatanrm35@gmail.com'
                )
            
        }
        always {
            recordCoverage qualityGates: [[criticality: 'FAILURE', integerThreshold: 85, metric: 'LINE', threshold: 85.0]], tools: [[parser: 'COBERTURA', pattern: 'results/coverage.xml']]
            junit 'results/*.xml'
            cleanWs()
        }
    }
}