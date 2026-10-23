pipeline {
    agent any                                   // Solo en el taller: en producción, agentes aislados

    tools {
        maven 'maven-3'                         // Configuración 1: herramienta Maven
    }

    options {
        timeout(time: 30, unit: 'MINUTES')
        buildDiscarder(logRotator(numToKeepStr: '10'))
        disableConcurrentBuilds()
    }

    environment {
        QA_DIR   = "${env.JENKINS_HOME}/ambientes/qa"
        PROD_DIR = "${env.JENKINS_HOME}/ambientes/prod"
    }

    stages {
        stage('1. Secretos (Gitleaks, toda la historia)') {
            steps {
                sh 'gitleaks git --redact --config .gitleaks.toml -r gitleaks.json .'          // Reto 1: revisa toda la historia
            }
            post {
                always { archiveArtifacts artifacts: 'gitleaks.json', allowEmptyArchive: true }
            }
        }

        stage('2. Build y pruebas unitarias') {
            steps {
                sh 'mvn -B clean verify'
            }
            post {
                always { junit 'target/surefire-reports/*.xml' }
            }
        }

        stage('3. Publicar artefacto') {
            steps {
                archiveArtifacts artifacts: 'target/solicitudes-api.jar', fingerprint: true
            }
        }

        stage('4. Desplegar en QA (simulado)') {
            steps {
                sh 'mkdir -p "$QA_DIR" && cp target/solicitudes-api.jar "$QA_DIR/"'
                sh 'java -jar "$QA_DIR/solicitudes-api.jar" --salud'     // prueba de humo
            }
        }

        stage('5. Notificar con credencial') {
            steps {
                // Configuración 2: el secreto viene de Jenkins, nunca del repositorio
                withCredentials([string(credentialsId: 'token-notificaciones', variable: 'TOKEN')]) {
                    sh 'echo "Notificando al canal del equipo. El token tiene ${#TOKEN} caracteres."'
                }
            }
        }

        stage('6. Aprobación para producción') {
            steps {
                timeout(time: 15, unit: 'MINUTES') {
                    input message: '¿Desplegar solicitudes-api en producción?', ok: 'Desplegar'
                }
            }
        }

        stage('7. Desplegar en producción (simulado)') {
            steps {
                // Build once, deploy many: se copia el MISMO jar que se probó en QA
                sh 'mkdir -p "$PROD_DIR" && cp "$QA_DIR/solicitudes-api.jar" "$PROD_DIR/"'
                sh 'java -jar "$PROD_DIR/solicitudes-api.jar" --salud'
            }
        }
    }

    post {
        success { echo 'Pipeline en verde: solicitudes-api desplegada.' }
        failure { echo 'Pipeline en rojo: revise la etapa que falló en la vista de etapas.' }
    }
}
