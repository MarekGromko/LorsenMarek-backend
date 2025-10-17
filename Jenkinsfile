pipeline {
    agent any
    environment {
        ENV         = 'dev'
        DB_USERNAME = 'ci-user'
        DB_PASSWORD = 'ci-password'
        DB_DBNAME   = 'ci-db'

        BUILD_TAG   = "jenkins-${env.BUILD_NUMBER}"

        git_branch  = 'ci'
        keep_alive  = true
        db_port_ex  = '52000'
        app_port_ex = '52100'
    }
    stages {
        stage('Build') {
            steps {
                sh 'make build'
                sh 'make run exec="git branch git branch --show-current"'
                sh 'make once exec="mvn clean compile -ntp"'
            }
        }
        stage('Test') {
            steps {
                sh 'make once exec="mvn clean test -ntp"'
            }
        }
        stage('Reports') {
            steps {
                sh 'make once exec="mvn clean package"'
                sh 'make once exec="mvn javadoc:javadoc"'
                sh 'make once exec="mvn jacoco:report"'
            }
        }
    }
    post {
        always {
            sh 'make copy src="./target/site"    dst="$WORKSPACE/reports"'
            sh 'make copy src="./target/reports" dst="$WORKSPACE/reports"'
            sh "make prune"
        }
    }
}