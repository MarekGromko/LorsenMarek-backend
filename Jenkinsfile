pipeline {
    agent any
    environment {
        ENV         = 'dev'
        DB_USERNAME = 'ci-user'
        DB_PASSWORD = 'ci-password'
        DB_DBNAME   = 'ci-db'

        BUILD_TAG   = "jenkins-${env.BUILD_NUMBER}"

        keep_alive  = true
        db_port_ex  = '52000'
        app_port_ex = '52100'
    }
    stages {
        stage('Build') {
            steps {
                sh 'make build'
                sh 'make run exec="git branch --show-current" git_branch=ci'
                sh 'make once exec="mvn clean compile -ntp"'
            }
        }
        stage('Test') {
            steps {
                sh 'make once exec="mvn test -ntp"'
            }
        }
        stage('Reports') {
            steps {
                sh 'make once exec="mvn javadoc:javadoc -ntp"'
                sh 'make once exec="mvn jacoco:report -ntp"'
            }
        }
    }
    post {
        success {
            sh 'make copy src="/home/app/target/site" dst="$WORKSPACE/reports"'
            sh 'make copy src="/home/app/target/reports/apidocs" dst="$WORKSPACE/reports"'
            publishHTML(target: [
                reportName: 'JaCoCo',
                reportDir: 'reports/jacoco',
                reportFiles: 'index.html',
                keepAll: true
            ])
            publishHTML(target: [
                reportName: 'JavaDoc',
                reportDir: 'reports/apidocs',
                reportFiles: 'index.html',
                keepAll: true
            ])
        }
        cleanup {
            sh "make prune"
        }
    }
}