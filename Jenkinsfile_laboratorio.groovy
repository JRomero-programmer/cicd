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
        always {
            emailext (
                subject: "Build ${currentBuild.fullDisplayName}",
                body: """<p>Build result: ${currentBuild.currentResult}</p>
                <p>More details: ${env.BUILD_URL}</p>""",
                recipientProviders: [[$class: 'DevelopersRecipientProvider'], [$class: 'RequesterRecipientProvider']],
                to: 'jonatanrm35@gmail.com'
                )
            junit 'results/*.xml'
            script {
                def coverageResult = cobertura(coberturaReportFile: 'results/coverage.xml', failUnhealthy: false, failBuild: false, healthyTarget: [methodCoverage: 70, lineCoverage: 70])
                if (coverageResult.result != 'SUCCESS') {
                    currentBuild.result = 'FAILURE'
                    error('Coverage por debajo del threshold')
                    
                }
                
            }
            cleanWs()
        }
    }
}