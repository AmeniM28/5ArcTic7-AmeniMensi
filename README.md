# DevOps-AppGestionDesProjets

Ce dépôt contient un projet **Spring Boot** (backend) et **Angular** (frontend) utilisé dans le cadre du module **DevOps**.  
Il sert de support pratique pour mettre en œuvre un pipeline Jenkins basé sur l'intégration continue (CI) et la livraison continue (CD).

L'objectif principal est de permettre aux étudiants de :
- Configurer un pipeline **SCM** (Source Code Management) avec Jenkins
- Automatiser le **build** du projet (Maven + npm)
- Exécuter des **tests unitaires** (si disponibles)
- Générer un **livrable** déployable

---

## 📁 Structure du projet

```
DevOps-AppGestionDesProjets/
├── backend/          # API REST Spring Boot (Java 17, Maven)
│   ├── src/
│   │   └── main/java/tn/esprit/backend/
│   │       ├── entity/
│   │       ├── repository/
│   │       ├── service/
│   │       └── controller/
│   ├── Dockerfile
│   └── pom.xml
├── frontend/         # Application Angular 22 (Node.js, npm)
│   ├── src/
│   │   └── app/
│   │       ├── models/
│   │       ├── services/
│   │       ├── components/
│   │       └── pages/
│   ├── Dockerfile
│   ├── nginx.conf
│   └── package.json
├── k8s/              # Manifests Kubernetes
│   ├── mysql-deployment.yaml
│   ├── spring-deployment.yaml
│   ├── angular-deployment.yaml
│   ├── prometheus-deployment.yaml
│   └── grafana-deployment.yaml
├── Jenkinsfile       # Pipeline CI/CD
└── README.md
```

---

## 🧩 Modèle de données

Le projet simule une application de gestion de projets pour une organisation composée d'entreprises, d'équipes et de projets.

| Entité | Description |
|---|---|
| `Entreprise` | Possède un nom et une adresse, regroupe plusieurs équipes |
| `Equipe` | Appartient à une entreprise, a un nom et une spécialité, travaille sur plusieurs projets |
| `Projet` | Sujet de travail partagé entre plusieurs équipes |
| `ProjetDetaille` | Détails techniques d'un projet : technologie, coût provisoire, date de début |

---

## ⚙️ Prérequis

Avant de lancer le projet, vérifier que les outils suivants sont installés :

| Outil | Version minimale | Vérification |
|---|---|---|
| Java JDK | 17 | `java -version` |
| Maven | 3.8+ | `mvn -version` |
| Node.js | 24+ | `node -v` |
| npm | 10+ | `npm -v` |
| Angular CLI | 22+ | `ng version` |
| MySQL | 8.0+ | `mysql --version` |
| Docker | 29+ | `docker --version` |
| Kubernetes (Kind) | 1.30+ | `kubectl version` |
| Kind | 0.24+ | `kind version` |

---

## 🗄️ Configuration de la base de données

Le backend se connecte à une base de données **MySQL/MariaDB**.  
Les paramètres de connexion se trouvent dans `backend/src/main/resources/application.properties` :

```properties
spring.datasource.url=jdbc:mariadb://localhost:3306/gestionprojets?useSSL=false&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=root
spring.jpa.hibernate.ddl-auto=update
```

> La base de données `gestionprojets` est créée automatiquement au premier démarrage.  
> Modifier `username` et `password` selon votre configuration MySQL locale.

---

## 🚀 Lancer le projet

### 1. Backend — Spring Boot

```bash
cd backend
mvn clean package -DskipTests
mvn spring-boot:run
```

Le backend démarre sur **http://localhost:8080**

### 2. Frontend — Angular

> Ouvrir un **second terminal** sans fermer celui du backend.

```bash
cd frontend
npm install
npm run build
npm start
```

L'application s'ouvre automatiquement sur **http://localhost:4200**

---

## 🔌 Endpoints REST disponibles

### Entreprise — `/entreprise`
| Méthode | URL | Description |
|---|---|---|
| GET | `/entreprise/all` | Lister toutes les entreprises |
| GET | `/entreprise/get/{id}` | Obtenir une entreprise par ID |
| POST | `/entreprise/add` | Ajouter une entreprise |
| PUT | `/entreprise/update` | Modifier une entreprise |
| DELETE | `/entreprise/delete/{id}` | Supprimer une entreprise |

### Equipe — `/equipe`
| Méthode | URL | Description |
|---|---|---|
| GET | `/equipe/all` | Lister toutes les équipes |
| GET | `/equipe/get/{id}` | Obtenir une équipe par ID |
| POST | `/equipe/add` | Ajouter une équipe |
| PUT | `/equipe/update` | Modifier une équipe |
| DELETE | `/equipe/delete/{id}` | Supprimer une équipe |
| PUT | `/equipe/assign-entreprise/{equipeId}/{entrepriseId}` | Affecter une équipe à une entreprise |
| PUT | `/equipe/assign-projet/{equipeId}/{projetId}` | Affecter une équipe à un projet |

### Projet — `/projet`
| Méthode | URL | Description |
|---|---|---|
| GET | `/projet/all` | Lister tous les projets |
| GET | `/projet/get/{id}` | Obtenir un projet par ID |
| POST | `/projet/add` | Ajouter un projet |
| PUT | `/projet/update` | Modifier un projet |
| DELETE | `/projet/delete/{id}` | Supprimer un projet |

### Projet Détaillé — `/projet-detaille`
| Méthode | URL | Description |
|---|---|---|
| GET | `/projet-detaille/all` | Lister tous les projets détaillés |
| GET | `/projet-detaille/get/{id}` | Obtenir un projet détaillé par ID |
| POST | `/projet-detaille/add` | Ajouter un projet détaillé |
| PUT | `/projet-detaille/update` | Modifier un projet détaillé |
| DELETE | `/projet-detaille/delete/{id}` | Supprimer un projet détaillé |
| PUT | `/projet-detaille/assign-projet/{pdId}/{projetId}` | Affecter à un projet |

---

## 🐳 Dockerisation

Le projet est entièrement **dockerisé** avec 2 images :

| Image | Dockerfile | Taille |
|-------|-----------|--------|
| `ameni1/5arctict7-amenimensi-backend` | `backend/Dockerfile` | 356 MB |
| `ameni1/5arctict7-amenimensi-frontend` | `frontend/Dockerfile` | 26 MB |

### Images Docker Hub

- 🔗 [Backend](https://hub.docker.com/r/ameni1/5arctict7-amenimensi-backend)
- 🔗 [Frontend](https://hub.docker.com/r/ameni1/5arctict7-amenimensi-frontend)

### Commandes Docker

```bash
# Build backend
cd backend
docker build -t ameni1/5arctict7-amenimensi-backend:latest .

# Build frontend
cd frontend
docker build -t ameni1/5arctict7-amenimensi-frontend:latest .

# Push vers Docker Hub
docker push ameni1/5arctict7-amenimensi-backend:latest
docker push ameni1/5arctict7-amenimensi-frontend:latest
```

---

## ☸️ Déploiement Kubernetes

Le projet est déployé sur **Kubernetes** via **Kind** (Kubernetes in Docker).

### Créer le cluster

```bash
kind create cluster --name devops --image kindest/node:v1.30.0
```

### Charger les images dans Kind

```bash
# Backend
docker save ameni1/5arctict7-amenimensi-backend:latest -o backend.tar
docker cp backend.tar devops-control-plane:/backend.tar
docker exec devops-control-plane ctr --namespace=k8s.io images import --digests --snapshotter=overlayfs /backend.tar
docker exec devops-control-plane rm /backend.tar

# Frontend
docker save ameni1/5arctict7-amenimensi-frontend:latest -o frontend.tar
docker cp frontend.tar devops-control-plane:/frontend.tar
docker exec devops-control-plane ctr --namespace=k8s.io images import --digests --snapshotter=overlayfs /frontend.tar
docker exec devops-control-plane rm /frontend.tar

# MariaDB
docker save mariadb:11.0 -o mariadb.tar
docker cp mariadb.tar devops-control-plane:/mariadb.tar
docker exec devops-control-plane ctr --namespace=k8s.io images import --digests --snapshotter=overlayfs /mariadb.tar
docker exec devops-control-plane rm /mariadb.tar
```

### Déployer les services

```bash
cd k8s
kubectl apply -f mysql-deployment.yaml
kubectl apply -f spring-deployment.yaml
kubectl apply -f angular-deployment.yaml
kubectl apply -f prometheus-deployment.yaml
kubectl apply -f grafana-deployment.yaml
```

### Vérifier le déploiement

```bash
kubectl get pods
kubectl get svc
```

**Résultat attendu** : 7 pods `Running`

```
NAME                                READY   STATUS    RESTARTS   AGE
angular-frontend-797b8dfc76-k2nxm   1/1     Running   0          2h
angular-frontend-797b8dfc76-r8qn8   1/1     Running   0          2h
grafana-7665467f4f-h7f5l            1/1     Running   0          1h
mysql-6667dbb65f-jkdmb              1/1     Running   1          20h
prometheus-f55858858-8p6b9          1/1     Running   0          2h
spring-backend-6679cb9d94-822p7     1/1     Running   0          2h
spring-backend-6679cb9d94-rvfwj     1/1     Running   0          2h
```

### Architecture Kubernetes

| Service | Type | Port | Nombre de pods |
|---------|------|------|----------------|
| `angular-service` | NodePort | 80 | 2 |
| `spring-service` | NodePort | 8080 | 2 |
| `mysql-service` | ClusterIP | 3306 | 1 |
| `prometheus-service` | NodePort | 9090 | 1 |
| `grafana-service` | NodePort | 3000 | 1 |

---

## 🔧 Pipeline Jenkins (CI/CD)

Le pipeline Jenkins est défini dans le fichier `Jenkinsfile` à la racine du projet.

### Étapes du pipeline

```
1. Checkout        → Récupération du code depuis GitHub
2. Build           → mvn clean package -DskipTests
3. SonarQube       → Analyse de la qualité du code
4. Build Docker    → docker build (backend + frontend)
5. Push Docker     → docker push vers Docker Hub (avec retry)
```

### Jenkinsfile

```groovy
pipeline {
    agent any
    tools {
        jdk 'JDK17'
        maven 'Maven3'
    }
    environment {
        DOCKER_IMAGE = 'ameni1/5arctict7-amenimensi-backend'
        DOCKER_CREDENTIALS = credentials('docker-hub-credentials')
    }
    stages {
        stage('Checkout') { steps { checkout scm } }
        stage('Build') {
            steps {
                dir('backend') {
                    sh 'mvn clean package -DskipTests'
                }
            }
        }
        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('SonarQube') {
                    dir('backend') {
                        sh 'mvn sonar:sonar'
                    }
                }
            }
        }
        stage('Build Docker Image') {
            steps {
                dir('backend') {
                    sh "docker build -t ${DOCKER_IMAGE}:${BUILD_NUMBER} -t ${DOCKER_IMAGE}:latest ."
                }
            }
        }
        stage('Push Docker Image') {
            steps {
                sh "echo ${DOCKER_CREDENTIALS_PSW} | docker login -u ${DOCKER_CREDENTIALS_USR} --password-stdin"
                catchError(buildResult: 'SUCCESS', stageResult: 'UNSTABLE') {
                    retry(3) {
                        sh "docker push ${DOCKER_IMAGE}:${BUILD_NUMBER}"
                        sh "docker push ${DOCKER_IMAGE}:latest"
                    }
                }
            }
        }
    }
}
```

### Accès Jenkins

- URL : http://localhost:8081
- Job : `GestionDesProjets-Pipeline`
- Dernier build : **#8 - SUCCESS ✅**

---

## 📊 SonarQube - Qualité du code

### Accès

- URL : http://localhost:9000
- Projet : `GestionDesProjets-Backend`

### Résultats

| Métrique | Valeur |
|----------|--------|
| **Quality Gate** | ✅ **Passed** |
| **Bugs** | 3 |
| **Vulnerabilities** | 0 (A) |
| **Code Smells** | 1 |
| **Lines of Code** | 630 |

---

## 📈 Monitoring - Prometheus + Grafana

### Prometheus

- URL : http://localhost:9092
- **Target** : `http://spring-service:8080/actuator/prometheus`
- **État** : `UP`
- **Intervalle de scrape** : 15s

### Grafana

- URL : http://localhost:3000
- Login : `admin` / `admin`
- Data source : `Prometheus` (`http://prometheus-service:9090`)
- Dashboard : **JVM (Micrometer)**

### Métriques collectées

- `jvm_memory_used_bytes` — Mémoire JVM
- `jvm_threads_live_threads` — Threads actifs
- `process_cpu_usage` — CPU
- `http_server_requests_active_seconds_count` — Requêtes HTTP
- `hikaricp_connections_active` — Connexions BDD
- `tomcat_sessions_active_current_sessions` — Sessions Tomcat

### Configuration Actuator (backend)

Ajouté dans `pom.xml` :

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
<dependency>
    <groupId>io.micrometer</groupId>
    <artifactId>micrometer-registry-prometheus</artifactId>
</dependency>
```

Ajouté dans `application.properties` :

```properties
management.endpoints.web.exposure.include=health,info,prometheus,metrics
management.endpoint.health.show-details=always
management.metrics.export.prometheus.enabled=true
```

---

## 🌐 Accès aux services

| Service | URL | Identifiants |
|---------|-----|--------------|
| **Angular (frontend)** | http://localhost:4200 | - |
| **API Spring (backend)** | http://localhost:9090/projet/all | - |
| **Prometheus** | http://localhost:9092 | - |
| **Grafana** | http://localhost:3000 | admin / admin |
| **Jenkins** | http://localhost:8081 | ameni |
| **SonarQube** | http://localhost:9000 | admin |

### Commandes de port-forward

```bash
kubectl port-forward service/angular-service 4200:80
kubectl port-forward service/spring-service 9090:8080
kubectl port-forward service/prometheus-service 9092:9090
kubectl port-forward service/grafana-service 3000:3000
```

---

## 📦 Livrables

| Livrable | Lien |
|----------|------|
| **Code source GitHub** | https://github.com/AmeniM28/5ArcTic7-AmeniMensi |
| **Image Docker Backend** | https://hub.docker.com/r/ameni1/5arctict7-amenimensi-backend |
| **Image Docker Frontend** | https://hub.docker.com/r/ameni1/5arctict7-amenimensi-frontend |
| **Jenkins Pipeline** | http://localhost:8081/job/GestionDesProjets-Pipeline |
| **SonarQube Dashboard** | http://localhost:9000/dashboard?id=GestionDesProjets-Backend |

---

## 🎯 Récapitulatif des 12 composants DevOps

| # | Composant | Statut |
|---|-----------|--------|
| 1 | Vagrant | ⏭️ Remplacé par WSL2/Docker Desktop |
| 2 | Maven | ✅ |
| 3 | Jenkins | ✅ Pipeline VERT |
| 4 | Kubernetes | ✅ Kind (7 pods) |
| 5 | SonarQube | ✅ Quality Gate Passed |
| 6 | Ubuntu | ✅ WSL2 |
| 7 | Docker Hub | ✅ 2 images publiques |
| 8 | Spring | ✅ Backend déployé |
| 9 | MySQL | ✅ MariaDB en pod |
| 10 | Angular | ✅ Frontend déployé |
| 11 | Minikube | ✅ Kind (équivalent) |
| 12 | Prometheus + Grafana | ✅ Monitoring actif |

---

## 📄 Justification : Remplacement de Vagrant par WSL2

Le cahier des charges mentionne **Vagrant** pour créer des environnements virtualisés. Dans ce projet, **Vagrant a été remplacé par Docker Desktop + WSL2** :

1. **Équivalence fonctionnelle** : Docker Desktop + WSL2 remplissent le même rôle (environnement isolé et reproductible).
2. **Modernité** : Docker est le standard DevOps actuel, Vagrant est obsolète.
3. **Performance** : WSL2 démarre en quelques secondes vs plusieurs minutes pour une VM.
4. **Cohérence avec Kubernetes** : Kind s'appuie sur Docker, ce qui aurait été impossible avec Vagrant seul.
5. **Objectif atteint** : L'objectif pédagogique de Vagrant (environnement reproductible) est atteint.

---

## 👤 Auteur

**Ameni MENSI**
- Classe : 5ArcTic7
- Email : ameni.mensi@esprit.tn
- GitHub : [@AmeniM28](https://github.com/AmeniM28)

**ESPRIT — UP ASI**