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
            cobertura coberturaReportFile: 'results/coverage.xml', 
            failUnhealthy: true, 
            failUnstable: true, 
            autoUpdateHealth: false, 
            autoUpdateStability: false, 
            zoomCoverageChart: false, 
            methodCoverageTargets: '70, 0, 0', 
            lineCoverageTargets: '70, 0, 0'
            cleanWs()
        }
    }
}