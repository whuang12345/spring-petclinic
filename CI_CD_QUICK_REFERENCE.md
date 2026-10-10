# Spring PetClinic CI/CD Quick Reference

## 🚀 Quick Start

### 1. Set Up GitHub Secrets (One-time)

Go to **Settings** → **Secrets and variables** → **Actions**

**For Kubernetes Deployment (Optional):**
```bash
# Generate base64 encoded kubeconfig
cat ~/.kube/config | base64 | tr -d '\n' > kube_config_b64.txt
```

Add as secret: `KUBE_CONFIG_BASE64` with the base64 string

**Docker Hub (Optional - if not using GHCR):**
- `DOCKERHUB_USERNAME`: your Docker Hub username
- `DOCKERHUB_TOKEN`: your Docker Hub token

### 2. Verify Workflows

Check `.github/workflows/` contains:
- ✅ `backend-ci.yml` - Backend build/test/deploy
- ✅ `frontend-ci.yml` - Frontend build/test/deploy  
- ✅ `integration-tests.yml` - E2E testing
- ✅ `main.yml` - Optional Docker Hub pipeline

### 3. Push Code to Trigger Pipeline

```bash
git add .
git commit -m "Update code"
git push origin main  # or develop
```

Workflow runs automatically! Monitor in **Actions** tab.

---

## 📋 Workflow Checklist

### Before First Deployment

- [ ] GitHub repository created
- [ ] GitHub Actions enabled
- [ ] Secrets configured (if using K8s)
- [ ] Dockerfile present in both directories
- [ ] `pom.xml` has correct JDK version (21)
- [ ] `package.json` has valid scripts

### Backend Checks

```bash
cd spring-petclinic

# Verify build works
./mvnw clean package -DskipTests

# Run tests
./mvnw test

# Run validation
./mvnw validate

# Test Docker build
docker build -t petclinic:test .
```

### Frontend Checks

```bash
cd petclinic-ui

# Install dependencies
npm ci

# Build
npm run build

# Test Docker build
docker build -t petclinic-ui:test .
```

---

## 🔄 Pipeline Flow

```
Push to main/develop
    ↓
┌─────────────────────┐
│ BACKEND CI/CD       │
├─────────────────────┤
│ 1. build-and-test   │ (Parallel)
│    - Validate       │     ┌─────────────────────┐
│    - Test          │     │ FRONTEND CI/CD      │
│    - Package       │     ├─────────────────────┤
│ 2. build-and-push  │     │ 1. lint-and-build   │
│ 3. deploy-to-k8s   │     │ 2. security-scan    │
└─────────────────────┘     │ 3. build-and-push   │
         ↓                   │ 4. deploy-to-k8s    │
      ✅ Deployed           └─────────────────────┘
```

---

## 🎯 Common Commands

### View Logs
```bash
# In GitHub Actions UI
Actions → Workflow name → Click run → Expand job step

# Or via CLI (requires GitHub CLI)
gh run view <run-id> --log
```

### Manually Trigger Workflow
```bash
# Via GitHub CLI
gh workflow run backend-ci.yml

# Or use GitHub UI:
# Actions → Backend CI/CD → Run workflow → Choose branch
```

### Test Locally Before Pushing
```bash
# Backend
cd spring-petclinic
./mvnw clean verify

# Frontend  
cd petclinic-ui
npm ci && npm run build && npm run lint
```

### View Pushed Images
```bash
# GHCR (GitHub Container Registry)
docker pull ghcr.io/username/spring-petclinic:latest
docker pull ghcr.io/username/petclinic-ui:latest

# Docker Hub (if using)
docker pull username/spring-petclinic:latest
docker pull username/petclinic-ui:latest
```

---

## 🐛 Quick Troubleshooting

| Issue | Solution |
|-------|----------|
| Build fails - "Java 21 not found" | Update `.github/workflows/backend-ci.yml` Java version |
| Tests fail locally but pass in CI | Run `./mvnw clean` then rebuild locally |
| Docker push fails | Check GHCR login: `echo ${{ secrets.GITHUB_TOKEN }} \| docker login ghcr.io -u ${{ github.actor }} --password-stdin` |
| K8s deployment skipped | Add `KUBE_CONFIG_BASE64` secret and ensure on `main` branch |
| Frontend build fails | Run `npm audit fix` and commit changes |
| Workflow doesn't trigger | Check branch name and paths in workflow `on:` section |

---

## 📊 Monitoring

### Health Checks
```bash
# Backend health
curl http://localhost:8080/actuator/health

# Frontend (if running)
curl http://localhost:80
```

### View Artifacts
**GitHub Actions UI** → Workflow Run → Artifacts section
- `backend-test-results` - JUnit XML reports
- `frontend-build` - Built Vue.js app
- `spring-petclinic-jar` - Built JAR file

### Real-time Logs
```bash
# GitHub CLI
gh run watch <run-id>

# Or via GitHub UI - Auto-refreshes
```

---

## 🔐 Security Checklist

- [ ] GitHub secrets are stored, not in code
- [ ] Kubeconfig not committed to repo
- [ ] Environment-specific configs use GitHub variables
- [ ] HTTPS used for all registry connections
- [ ] Secrets rotated periodically
- [ ] Access limited via GitHub CODEOWNERS
- [ ] Audit log reviewed: Settings → Audit log

---

## 📞 Need Help?

1. **Check logs** in GitHub Actions UI
2. **Read** `CI_CD_SETUP_GUIDE.md` for detailed steps
3. **Review** workflow files in `.github/workflows/`
4. **Test locally** to isolate issues
5. **GitHub Docs**: https://docs.github.com/en/actions

---

## 🎓 Learning Resources

- [GitHub Actions Best Practices](https://docs.github.com/en/actions/guides)
- [Docker Best Practices](https://docs.docker.com/develop/dev-best-practices/)
- [Kubernetes Basics](https://kubernetes.io/docs/tutorials/kubernetes-basics/)
- [Spring Boot CI/CD](https://spring.io/guides)

---

**Last Updated**: 2026-10-10
