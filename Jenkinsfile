pipeline {
    agent any
    environment {
        ENV         = 'dev'
        DB_USERNAME = 'ci-user'
        DB_PASSWORD = 'ci-password'
        DB_DBNAME   = 'ci-db'
        SERVER_PORT = '52100'
        build_tag   = "jenkins-${env.BUILD_NUMBER}"
        keep_alive  = true
        db_port_ex  = "52000"
    }
    stages {
        stage('Build') {
            steps {
                sh 'make build'
                sh 'make run exec="mvn package"'
            }
        }
        stage('Test') {
            steps {
                sh 'make test'
            }
        }
        stage('Deploy') {
            steps {
                echo 'Deploying...'
            }
        }
    }
    post {
        always {
            sh "make prune"
        }
    }
}