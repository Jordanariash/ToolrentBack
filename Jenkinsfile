pipeline {
    agent any
    
    environment {
        // Nombre de la imagen Docker
        DOCKER_IMAGE = "toolrent-backend"
        // Tag basado en el número de build
        DOCKER_TAG = "${env.BUILD_NUMBER}"
        // Usar el workspace de Jenkins
        WORKSPACE = "${env.WORKSPACE}"
    }
    
    stages {
        stage('Checkout') {
            steps {
                // Clonar el repositorio
                git branch: 'main', 
                url: 'https://github.com/Jordanariash/ToolrentBack.git',
                credentialsId: 'github-token'  // Si es privado
            }
        }
        
        stage('Build Gradle') {
            steps {
                sh '''
                    echo "Construyendo proyecto Spring Boot..."
                    ./gradlew build -x test
                '''
            }
        }
        
        stage('Run Tests') {
            steps {
                sh '''
                    echo "Ejecutando tests..."
                    ./gradlew test
                '''
            }
        }
        
        stage('Build Docker Image') {
            steps {
                script {
                    // Construir la imagen Docker
                    docker.build("${DOCKER_IMAGE}:${DOCKER_TAG}")
                }
            }
        }
        
        stage('Deploy with Docker Compose') {
            steps {
                sh '''
                    echo "Desplegando con Docker Compose..."
                    # Navegar al directorio del proyecto
                    cd ${WORKSPACE}
                    # Levantar los servicios
                    docker-compose up -d backend postgres
                '''
            }
        }
    }
    
    post {
        always {
            // Limpiar imágenes sin usar
            sh 'docker system prune -f'
        }
        success {
            echo '¡Pipeline ejecutado con éxito!'
            // Aquí podrías agregar notificaciones (email, Slack, etc.)
        }
        failure {
            echo 'Pipeline falló'
            // Notificaciones de error
        }
    }
}
