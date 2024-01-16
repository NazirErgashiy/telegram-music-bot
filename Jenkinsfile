pipeline{
    agent any

    stages{
        stage('Build'){
            steps{
                bat './gradlew clean build'
            }
        }
        stage('Test'){
            steps{
                bat './gradlew test'
            }
        }
        stage('Deploy') {
            steps {
                build 'my-project'
            }
        }
    }
}