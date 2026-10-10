# Spring PetClinic CI/CD Setup Guide

This guide explains how to set up and configure the automated CI/CD pipeline for building, testing, and deploying the Spring PetClinic application.

## 📋 Table of Contents

1. [Overview](#overview)
2. [CI/CD Workflows](#cicd-workflows)
3. [Prerequisites](#prerequisites)
4. [GitHub Secrets Configuration](#github-secrets-configuration)
5. [Environment Variables](#environment-variables)
6. [Running the Pipelines](#running-the-pipelines)
7. [Deployment Options](#deployment-options)
8. [Troubleshooting](#troubleshooting)

## 🎯 Overview

The CI/CD pipeline automates the following processes:

### Backend Pipeline (spring-petclinic)
- **Code Validation**: Checkstyle validation
- **Testing**: Unit tests execution
- **Building**: Maven package creation
- **Docker Build**: Multi-platform Docker image (linux/amd64, linux/arm64)
- **Kubernetes Deployment**: Automated rollout to K8s cluster

### Frontend Pipeline (petclinic-ui)
- **Linting**: Code quality checks
- **Security Scan**: npm audit for dependencies
- **Building**: Vue.js application build
- **Docker Build**: Frontend container image
- **Kubernetes Deployment**: Automated UI rollout

## 🔄 CI/CD Workflows

### Workflow Files
- `.github/workflows/backend-ci.yml` - Backend build, test, and deploy
- `.github/workflows/frontend-ci.yml` - Frontend build and deploy
- `.github/workflows/main.yml` - Optional: Docker Hub deployment

### Triggered On
- **Push** to `main` or `develop` branches
- **Pull Requests** to `main` or `develop` branches
- **Manual Trigger** via GitHub Actions UI (`workflow_dispatch`)

### Jobs in Each Workflow

#### Backend CI/CD Pipeline
1. **build-and-test**
   - Checks out code
   - Sets up JDK 21
   - Runs checkstyle validation
   - Executes unit tests
   - Builds application (JAR)
   - Publishes test results

2. **build-and-push-image**
   - Depends on: `build-and-test`
   - Builds multi-platform Docker images
   - Pushes to GitHub Container Registry (GHCR)
   - Caches layers for faster builds

3. **deploy-to-k8s**
   - Depends on: `build-and-push-image`
   - Only runs on `main` branch
   - Deploys to Kubernetes cluster
   - Verifies rollout status

#### Frontend CI/CD Pipeline
1. **lint-and-build**
   - Checks out code
   - Sets up Node.js 18
   - Installs dependencies
   - Runs linter (if configured)
   - Builds Vue.js application

2. **security-scan**
   - Runs npm audit
   - Checks for vulnerabilities

3. **build-and-push-image**
   - Depends on: `lint-and-build`, `security-scan`
   - Builds Docker image for UI
   - Pushes to GHCR

4. **deploy-to-k8s**
   - Depends on: `build-and-push-image`
   - Only runs on `main` branch
   - Deploys UI to Kubernetes

## 📋 Prerequisites

### Required
- GitHub repository with the code
- GitHub Actions enabled
- JDK 21 (for backend)
- Node.js 18 (for frontend)
- Maven/Gradle build tools

### For Docker/Registry Features
- Docker Hub or GitHub Container Registry (GHCR) account
- Docker installed locally (for testing)

### For Kubernetes Deployment
- Active Kubernetes cluster
- kubectl access configured
- Kubeconfig file available

## 🔐 GitHub Secrets Configuration

GitHub Secrets are encrypted environment variables used in workflows. Configure them in your repository:

**Path**: `Settings` → `Secrets and variables` → `Actions`

### Required Secrets for CI/CD

#### 1. Docker Registry (GHCR - Default)
**GHCR uses your GitHub token automatically**, so no additional secrets are needed!

```bash
# GitHub automatically provides:
- github.token (GITHUB_TOKEN) - Already available in workflows
```

#### 2. For Kubernetes Deployment (Optional)

```bash
Name: KUBE_CONFIG_BASE64
Value: <base64-encoded-kubeconfig>

# To create this:
# 1. Get your kubeconfig file:
cat ~/.kube/config | base64

# 2. On Windows PowerShell:
[Convert]::ToBase64String([System.Text.Encoding]::UTF8.GetBytes((Get-Content ~/.kube/config))) | Set-Clipboard

# 3. Paste the output as the secret value
```

#### 3. Docker Hub (Alternative to GHCR - Optional)

If you prefer Docker Hub instead of GHCR:

```bash
Name: DOCKERHUB_USERNAME
Value: <your-docker-hub-username>

Name: DOCKERHUB_TOKEN
Value: <your-docker-hub-token>
```

To generate Docker Hub token:
1. Log in to [Docker Hub](https://hub.docker.com)
2. Go to Account Settings → Security → Access Tokens
3. Create a new token
4. Copy and save it

### 4. Environment Variables (GitHub Variables)

**Path**: `Settings` → `Secrets and variables` → `Variables`

```bash
# For Docker Hub (if using)
DOCKER_USERNAME=<your-docker-hub-username>

# For image registry references
REGISTRY_URL=ghcr.io  # or docker.io if using Docker Hub
```

## 🌍 Environment Variables

### Backend Environment Variables

In your application `application.properties` or deployment:

```properties
# Database Configuration
spring.datasource.url=jdbc:postgresql://db:5432/petclinic
spring.datasource.username=petclinic
spring.datasource.password=petclinic
spring.jpa.hibernate.ddl-auto=update

# Server Configuration
server.port=8080
server.servlet.context-path=/api

# Logging
logging.level.root=INFO
logging.level.org.springframework=INFO
```

### Frontend Environment Variables

Create `petclinic-ui/.env` or `petclinic-ui/.env.production`:

```env
VUE_APP_API_URL=http://api.example.com/api
VUE_APP_ENVIRONMENT=production
```

## 🚀 Running the Pipelines

### Automatic Triggers

Pipelines run automatically when you:

1. **Push to main or develop branches**
   ```bash
   git add .
   git commit -m "Feature: Add new capability"
   git push origin main
   ```

2. **Create a Pull Request**
   - PR to `main` or `develop`
   - Pipeline runs for validation

### Manual Trigger

Run workflows manually from GitHub UI:

1. Go to **Actions** tab in your repository
2. Select workflow: "Backend CI/CD" or "Frontend CI/CD"
3. Click **"Run workflow"** button
4. Choose branch
5. Click **"Run workflow"**

### Local Testing

**Test backend build locally:**
```bash
cd spring-petclinic
./mvnw clean package -DskipTests
./mvnw test
./mvnw validate  # checkstyle
```

**Test frontend build locally:**
```bash
cd petclinic-ui
npm install
npm run lint
npm run build
```

**Test Docker build:**
```bash
# Backend
cd spring-petclinic
docker build -t petclinic:latest .

# Frontend
cd petclinic-ui
docker build -t petclinic-ui:latest .
```

## 🐳 Deployment Options

### Option 1: Docker Compose (Local)

```bash
cd spring-petclinic
docker-compose up -d
```

This starts:
- Backend service on port 8080
- PostgreSQL database on port 5432
- Frontend accessible through nginx

### Option 2: Docker Hub

To use Docker Hub instead of GHCR:

1. Add Docker Hub secrets (see [Secrets Configuration](#github-secrets-configuration))
2. Uncomment Docker Hub section in workflows:

```yaml
# In .github/workflows/backend-ci.yml
- name: Login to Docker Hub
  uses: docker/login-action@v3
  with:
    username: ${{ secrets.DOCKERHUB_USERNAME }}
    password: ${{ secrets.DOCKERHUB_TOKEN }}
```

3. Update image tag in workflow:
```yaml
tags: ${{ secrets.DOCKERHUB_USERNAME }}/spring-petclinic:latest
```

### Option 3: Kubernetes Deployment

#### Prerequisites
```bash
# 1. Have kubectl configured
kubectl cluster-info

# 2. Verify access
kubectl get nodes

# 3. Set up KUBE_CONFIG_BASE64 secret (see Secrets Configuration)
```

#### Deploy Manually
```bash
kubectl apply -f spring-petclinic/k8s/
kubectl apply -f petclinic-ui/k8s/

# Check deployment status
kubectl get deployments
kubectl get pods
kubectl describe deployment petclinic
```

#### Update K8s Manifests

Edit files in:
- `spring-petclinic/k8s/petclinic.yml` - Backend deployment
- `spring-petclinic/k8s/db.yml` - Database configuration

Example deployment manifest:
```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: petclinic
spec:
  replicas: 2
  selector:
    matchLabels:
      app: petclinic
  template:
    metadata:
      labels:
        app: petclinic
    spec:
      containers:
      - name: petclinic
        image: ghcr.io/username/spring-petclinic:latest
        ports:
        - containerPort: 8080
        env:
        - name: SPRING_DATASOURCE_URL
          value: jdbc:postgresql://db:5432/petclinic
```

## 🔍 Monitoring & Debugging

### View Workflow Runs

1. Go to **Actions** tab
2. Click on workflow name
3. View:
   - Status (✅ Success, ❌ Failed)
   - Execution time
   - Logs for each job
   - Artifacts

### Common Issues & Solutions

#### Issue: "No model configuration found"
**Solution**: Ensure Java version matches (21 for backend)

#### Issue: "npm audit failures"
**Solution**: Update dependencies
```bash
cd petclinic-ui
npm audit fix
npm update
```

#### Issue: "Docker push failed"
**Solution**: Check GHCR login
```bash
echo ${{ secrets.GITHUB_TOKEN }} | docker login ghcr.io -u ${{ github.actor }} --password-stdin
```

#### Issue: "Kubernetes deployment failed"
**Solution**: Verify kubeconfig
```bash
echo "${KUBE_CONFIG_BASE64}" | base64 --decode > kubeconfig
kubectl --kubeconfig=kubeconfig cluster-info
```

### Viewing Logs

**In GitHub Actions:**
1. Go to Actions → Workflow run
2. Click on job name
3. Expand step to see logs

**Local logs:**
```bash
# Docker build
docker build --progress=plain -t test .

# Maven
./mvnw -X clean package

# npm
npm run build --verbose
```

## 📊 Performance Optimization

### Build Caching

The workflows include Docker layer caching:
```yaml
cache-from: type=registry,ref=ghcr.io/username/image:buildcache
cache-to: type=registry,ref=ghcr.io/username/image:buildcache,mode=max
```

This significantly speeds up subsequent builds.

### Maven Caching

Backend workflow caches Maven repository:
```yaml
- name: Set up JDK 21
  uses: actions/setup-java@v4
  with:
    cache: maven
```

### NPM Caching

Frontend workflow caches npm dependencies:
```yaml
- name: Setup Node.js
  uses: actions/setup-node@v4
  with:
    cache: 'npm'
```

## 📝 Best Practices

1. **Always test locally** before pushing
2. **Use develop branch** for feature development
3. **Create pull requests** for code review before merging to main
4. **Monitor pipeline** in Actions tab
5. **Keep dependencies updated** with security patches
6. **Use semantic versioning** for tags
7. **Document** significant changes
8. **Review logs** when pipelines fail

## 🔗 Useful Resources

- [GitHub Actions Documentation](https://docs.github.com/en/actions)
- [GitHub Container Registry](https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-container-registry)
- [Kubernetes Documentation](https://kubernetes.io/docs/)
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Vue.js Documentation](https://vuejs.org/)

## ❓ Support & Troubleshooting

If you encounter issues:

1. **Check workflow logs** in Actions tab
2. **Review this guide** for configuration steps
3. **Verify secrets** are properly set
4. **Test locally** to isolate issues
5. **Check dependencies** are compatible
6. **Review GitHub Status** for any service issues

---

**Last Updated**: 2026-10-10  
**Pipeline Version**: 1.0.0
