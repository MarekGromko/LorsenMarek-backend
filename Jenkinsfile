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
                sh 'make once exec="mvn jacoco:report -ntp"'
            }
        }
        stage('Packaging') {
            steps {
                sh 'make once exec="mvn package -ntp"'
                sh 'make once exec="mvn javadoc:javadoc -ntp"'
            }
        }
    }
    post {
        success {
            sh 'make copy src="./target/site" dst="$WORKSPACE/reports"'
            sh 'make copy src="./target/reports" dst="$WORKSPACE/reports"'
        }
        cleanup {
            sh "make prune"
        }
    }
}