# Spring PetClinic - CI/CD Pipeline Setup

Welcome! This document explains how to get your Spring PetClinic CI/CD pipeline up and running.

## 📚 Documentation Structure

This repository includes comprehensive CI/CD documentation:

### 📖 Main Guides
1. **[CI_CD_SETUP_GUIDE.md](./CI_CD_SETUP_GUIDE.md)** - Complete setup and configuration guide
   - Detailed step-by-step instructions
   - All secret configurations
   - Deployment options
   - Troubleshooting guide

2. **[CI_CD_QUICK_REFERENCE.md](./CI_CD_QUICK_REFERENCE.md)** - Quick reference card
   - Common commands
   - Workflow checklist
   - Quick troubleshooting
   - Monitoring tips

### 🔧 Setup Scripts
- **setup-cicd.sh** - Bash script (Linux/macOS)
- **setup-cicd.ps1** - PowerShell script (Windows)

## 🚀 Quick Start (5 minutes)

### Step 1: Run Setup Script

**Windows PowerShell:**
```powershell
.\setup-cicd.ps1
```

**Linux/macOS:**
```bash
chmod +x setup-cicd.sh
./setup-cicd.sh
```

The script will guide you through:
- ✅ Generating Kubernetes secret (optional)
- ✅ Setting up Docker Hub credentials (optional)
- ✅ Verifying workflow files
- ✅ Testing local builds

### Step 2: Push Code to Trigger Pipeline

```bash
git add .
git commit -m "Feature: Add new capability"
git push origin main  # or develop
```

### Step 3: Monitor Pipeline

Go to **Actions** tab in GitHub and watch your pipeline run!

## 📋 What Gets Automated

### Backend (Spring Boot)
```
Push to main/develop
    ↓
✓ Code Validation (Checkstyle)
✓ Unit Tests
✓ Package Build (Maven)
✓ Docker Image Build (multi-platform)
✓ Image Push to GHCR
✓ Kubernetes Deployment
```

### Frontend (Vue.js)
```
Push to main/develop
    ↓
✓ Lint Code
✓ Security Scan (npm audit)
✓ Build Application
✓ Docker Image Build
✓ Image Push to GHCR
✓ Kubernetes Deployment
```

### Integration Tests (Optional)
- End-to-end testing
- Performance benchmarks
- Docker Compose validation

## 🔐 Secrets Configuration

### Minimal Setup (GHCR - GitHub Default)
No secrets needed! GitHub provides `GITHUB_TOKEN` automatically.

### For Kubernetes Deployment
Set `KUBE_CONFIG_BASE64` secret with your base64-encoded kubeconfig:

```bash
# macOS/Linux
cat ~/.kube/config | base64 | tr -d '\n'

# Windows PowerShell
[Convert]::ToBase64String([System.Text.Encoding]::UTF8.GetBytes((Get-Content $env:USERPROFILE\.kube\config))) | Set-Clipboard
```

Then add as repository secret in GitHub Settings.

### Alternative: Docker Hub
If you prefer Docker Hub instead of GHCR:
- Add `DOCKERHUB_USERNAME` secret
- Add `DOCKERHUB_TOKEN` secret
- Uncomment Docker Hub section in workflows

## 📁 Workflow Files

All workflows are in `.github/workflows/`:

| File | Purpose | Triggers |
|------|---------|----------|
| `backend-ci.yml` | Backend build & deploy | Push to main/develop |
| `frontend-ci.yml` | Frontend build & deploy | Push to main/develop |
| `integration-tests.yml` | E2E tests & performance | Push to main, daily |
| `main.yml` | Docker Hub build (optional) | Push to main |

## ✅ Verification Checklist

Before your first deployment:

- [ ] Repository is on GitHub
- [ ] GitHub Actions is enabled (Settings → Actions)
- [ ] All workflow files exist in `.github/workflows/`
- [ ] Dockerfile present in both `spring-petclinic/` and `petclinic-ui/`
- [ ] `pom.xml` has correct JDK version (21)
- [ ] `package.json` exists in `petclinic-ui/`
- [ ] Local builds work:
  ```bash
  cd spring-petclinic && ./mvnw clean package
  cd petclinic-ui && npm install && npm run build
  ```

## 🎯 Common Tasks

### View Pipeline Status
1. Go to **Actions** tab
2. Select workflow name
3. View latest run
4. Expand job to see logs

### Manually Trigger Workflow
1. Go to **Actions** tab
2. Select workflow (Backend CI/CD or Frontend CI/CD)
3. Click **"Run workflow"**
4. Select branch
5. Click **"Run workflow"**

### Pull Built Images
```bash
# GHCR (GitHub Container Registry)
docker pull ghcr.io/username/spring-petclinic:latest
docker pull ghcr.io/username/petclinic-ui:latest

# Docker Hub (if configured)
docker pull username/spring-petclinic:latest
docker pull username/petclinic-ui:latest
```

### Deploy Locally
```bash
cd spring-petclinic
docker-compose up -d

# Access at:
# Backend: http://localhost:8080
# Database: localhost:5432
# Frontend: http://localhost:80 (if UI container added)
```

### Deploy to Kubernetes
```bash
# Ensure kubeconfig is set via KUBE_CONFIG_BASE64 secret
git push origin main

# Pipeline will automatically deploy to K8s cluster
# Verify deployment:
kubectl get deployments
kubectl get pods
```

## 🔍 Monitoring & Debugging

### View Test Results
1. Go to **Actions** → Workflow run
2. Expand **build-and-test** job
3. Expand **Publish test results** step
4. View test summary

### Download Artifacts
1. Go to **Actions** → Workflow run
2. Scroll to **Artifacts** section
3. Download:
   - `backend-test-results` - JUnit XML reports
   - `frontend-build` - Built Vue.js app
   - `spring-petclinic-jar` - Built JAR file

### Check Latest Deployment
```bash
# Via kubectl
kubectl logs deployment/petclinic
kubectl describe deployment/petclinic

# Via GitHub Actions
# Actions tab → Latest run → deploy-to-k8s job → logs
```

## 🐛 Troubleshooting

### Build Fails - "Java 21 not found"
**Solution**: Update `backend-ci.yml` to use correct Java version
```yaml
java-version: '21'  # Must match pom.xml
```

### Tests Fail Locally But Pass in CI
**Solution**: Run clean rebuild
```bash
cd spring-petclinic
./mvnw clean verify
```

### Docker Push Fails
**Solution**: Check registry login
```bash
echo ${{ secrets.GITHUB_TOKEN }} | docker login ghcr.io -u ${{ github.actor }} --password-stdin
```

### K8s Deployment Skipped
**Solution**: Ensure all conditions are met:
- ✓ On `main` branch
- ✓ `KUBE_CONFIG_BASE64` secret configured
- ✓ K8s manifests exist in `k8s/` directory

### Frontend Build Fails
**Solution**: Update dependencies
```bash
cd petclinic-ui
npm audit fix
npm update
git commit -am "Update dependencies"
git push origin develop
```

**More help**: See [CI_CD_SETUP_GUIDE.md](./CI_CD_SETUP_GUIDE.md) for detailed troubleshooting.

## 📊 Performance Tips

1. **Use branch caching**: Workflows cache dependencies
2. **Multi-platform builds**: Images build for amd64 and arm64
3. **Layer caching**: Docker build cache speeds up rebuilds
4. **Parallel jobs**: Backend and frontend build simultaneously

## 🔐 Security Best Practices

- ✅ Store secrets in GitHub, not in code
- ✅ Use short-lived tokens when possible
- ✅ Rotate credentials periodically
- ✅ Review GitHub audit log: Settings → Audit log
- ✅ Limit branch protection rules to main
- ✅ Require status checks before PR merge

## 📞 Getting Help

1. **Read the guides** - Start with [CI_CD_SETUP_GUIDE.md](./CI_CD_SETUP_GUIDE.md)
2. **Check logs** - Most answers are in GitHub Actions logs
3. **Test locally** - Isolate issues before pushing
4. **GitHub Docs** - [actions.github.com](https://docs.github.com/en/actions)

## 🎓 Next Steps

1. ✅ Run `setup-cicd.ps1` (Windows) or `setup-cicd.sh` (Linux/macOS)
2. ✅ Verify all secrets are configured
3. ✅ Push code to trigger first pipeline
4. ✅ Monitor Actions tab and celebrate! 🎉

## 📖 Full Documentation

- [Complete Setup Guide](./CI_CD_SETUP_GUIDE.md)
- [Quick Reference](./CI_CD_QUICK_REFERENCE.md)
- [GitHub Actions Docs](https://docs.github.com/en/actions)

---

**Ready to get started?** Run the setup script now:

**Windows:**
```powershell
.\setup-cicd.ps1
```

**Linux/macOS:**
```bash
chmod +x setup-cicd.sh && ./setup-cicd.sh
```

Happy deploying! 🚀
