pipeline {
    agent any
    environment {
        ENV         = 'dev'
        DB_USERNAME = 'ci-user'
        DB_PASSWORD = 'ci-password'
        DB_DBNAME   = 'ci-db'

        build_tag   = "jenkins-${env.BUILD_NUMBER}"
        keep_alive  = true
        db_port_ex  = "52000"
        app_port_ex = '52100'
    }
    stages {
        stage('Build') {
            steps {
                sh 'make build'
                sh 'make run    exec="mvn clean package"'
                sh 'make once   exec="mvn javadoc:javadoc"'
            }
        }
        stage('Test') {
            steps {
                sh 'make test'
                sh 'make once exec="mvn jacoco:report"'
            }
        }
        stage('Deploy') {
            steps {
                sh 'make once exec="mvn package"'
            }
        }
    }
    post {
        always {
            sh "make prune"
        }
    }
}