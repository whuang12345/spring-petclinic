# CI/CD Setup Helper Script (Windows PowerShell)
# This script helps you set up the GitHub secrets needed for CI/CD

param(
    [ValidateSet('k8s', 'dockerhub', 'verify', 'test', 'all', 'interactive')]
    [string]$Action = 'interactive'
)

# Colors and formatting
function Write-Success {
    param([string]$Message)
    Write-Host "✓ $Message" -ForegroundColor Green
}

function Write-Warning {
    param([string]$Message)
    Write-Host "⚠ $Message" -ForegroundColor Yellow
}

function Write-Info {
    param([string]$Message)
    Write-Host "ℹ $Message" -ForegroundColor Cyan
}

function Write-Error {
    param([string]$Message)
    Write-Host "✗ $Message" -ForegroundColor Red
}

# Check if GitHub CLI is installed
function Test-GitHubCLI {
    $ghPath = Get-Command gh -ErrorAction SilentlyContinue
    if ($ghPath) {
        Write-Success "GitHub CLI found"
        return $true
    }
    else {
        Write-Warning "GitHub CLI not found. Visit: https://cli.github.com"
        return $false
    }
}

# Generate Kubernetes config secret
function Generate-K8sSecret {
    param(
        [string]$KubeConfigPath = "$env:USERPROFILE\.kube\config"
    )
    
    Write-Host ""
    Write-Info "Generating Kubernetes config secret..."
    Write-Host ""
    
    if (-not (Test-Path $KubeConfigPath)) {
        Write-Error "Kubeconfig not found at: $KubeConfigPath"
        Write-Info "Please provide the correct path to your kubeconfig file"
        return $false
    }
    
    Write-Info "Kubeconfig path: $KubeConfigPath"
    Write-Host ""
    
    # Read and encode kubeconfig
    try {
        $KubeConfigContent = Get-Content -Raw $KubeConfigPath
        $KubeConfigBytes = [System.Text.Encoding]::UTF8.GetBytes($KubeConfigContent)
        $KubeConfigB64 = [Convert]::ToBase64String($KubeConfigBytes)
        
        Write-Success "Base64 encoded kubeconfig generated"
        Write-Host ""
        
        Write-Host "To set the secret via GitHub CLI, run:"
        Write-Host "gh secret set KUBE_CONFIG_BASE64 -b '$KubeConfigB64'" -ForegroundColor White
        Write-Host ""
        
        Write-Host "Or manually add in GitHub:"
        Write-Host "1. Settings → Secrets and variables → Actions"
        Write-Host "2. Click 'New repository secret'"
        Write-Host "3. Name: KUBE_CONFIG_BASE64"
        Write-Host "4. Value: (paste the base64 string above)"
        Write-Host ""
        
        # Ask if user wants to set it via GitHub CLI
        if (Test-GitHubCLI) {
            $response = Read-Host "Set secret via GitHub CLI? (y/n)"
            if ($response -eq 'y' -or $response -eq 'Y') {
                gh secret set KUBE_CONFIG_BASE64 -b $KubeConfigB64
                Write-Success "Secret set successfully!"
            }
        }
        
        return $true
    }
    catch {
        Write-Error "Failed to process kubeconfig: $_"
        return $false
    }
}

# Generate Docker Hub secret
function Generate-DockerHubSecret {
    Write-Host ""
    Write-Info "Setting up Docker Hub authentication..."
    Write-Host ""
    
    $username = Read-Host "Enter your Docker Hub username"
    $token = Read-Host "Enter your Docker Hub token (will not be displayed)" -AsSecureString
    $tokenPlain = [System.Runtime.InteropServices.Marshal]::PtrToStringAuto([System.Runtime.InteropServices.Marshal]::SecureStringToCoTaskMemUnicode($token))
    
    if (Test-GitHubCLI) {
        gh secret set DOCKERHUB_USERNAME -b $username
        gh secret set DOCKERHUB_TOKEN -b $tokenPlain
        Write-Success "Docker Hub secrets set successfully!"
    }
    else {
        Write-Host "Set these secrets manually in GitHub:"
        Write-Host "1. DOCKERHUB_USERNAME: $username"
        Write-Host "2. DOCKERHUB_TOKEN: (your token)"
    }
}

# Verify workflows
function Verify-Workflows {
    Write-Host ""
    Write-Info "Verifying workflow files..."
    Write-Host ""
    
    $workflows = @(
        ".github\workflows\backend-ci.yml",
        ".github\workflows\frontend-ci.yml",
        ".github\workflows\integration-tests.yml",
        ".github\workflows\main.yml"
    )
    
    foreach ($workflow in $workflows) {
        if (Test-Path $workflow) {
            Write-Success "Found: $workflow"
        }
        else {
            Write-Warning "Optional: $workflow not found"
        }
    }
}

# Verify local builds
function Verify-LocalBuilds {
    Write-Host ""
    Write-Info "Verifying local builds..."
    Write-Host ""
    
    # Check backend
    if (Test-Path "spring-petclinic\pom.xml") {
        Write-Info "Testing backend build..."
        Push-Location spring-petclinic
        try {
            $output = & .\mvnw.cmd clean package -DskipTests -q 2>&1
            Write-Success "Backend build successful"
        }
        catch {
            Write-Warning "Backend build had issues (may need dependencies installed): $_"
        }
        Pop-Location
    }
    
    # Check frontend
    if (Test-Path "petclinic-ui\package.json") {
        Write-Info "Testing frontend build..."
        Push-Location petclinic-ui
        try {
            npm ci --silent | Out-Null
            npm run build 2>&1 | Out-Null
            Write-Success "Frontend build successful"
        }
        catch {
            Write-Warning "Frontend build had issues (may need dependencies installed): $_"
        }
        Pop-Location
    }
}

# Show menu
function Show-Menu {
    Write-Host ""
    Write-Host "What would you like to set up?" -ForegroundColor White
    Write-Host "================================"
    Write-Host "1. Generate Kubernetes secret (KUBE_CONFIG_BASE64)"
    Write-Host "2. Set up Docker Hub secrets"
    Write-Host "3. Verify workflow files"
    Write-Host "4. Test local builds"
    Write-Host "5. Full setup (all of above)"
    Write-Host "6. Exit"
    Write-Host ""
}

# Main function
function Main {
    # Check if we're in the right directory
    if (-not (Test-Path ".github\workflows")) {
        Write-Error "Error: .github\workflows directory not found"
        Write-Info "Please run this script from the repository root"
        exit 1
    }
    
    Write-Host ""
    Write-Host "🚀 Spring PetClinic CI/CD Setup Helper" -ForegroundColor Cyan
    Write-Host "======================================" -ForegroundColor Cyan
    Write-Host ""
    
    if ($Action -eq 'interactive') {
        while ($true) {
            Show-Menu
            $choice = Read-Host "Enter option (1-6)"
            
            switch ($choice) {
                '1' { Generate-K8sSecret }
                '2' { Generate-DockerHubSecret }
                '3' { Verify-Workflows }
                '4' { Verify-LocalBuilds }
                '5' { 
                    Generate-K8sSecret
                    Generate-DockerHubSecret
                    Verify-Workflows
                    Verify-LocalBuilds
                }
                '6' {
                    Write-Success "Setup complete! Happy deploying!"
                    exit 0
                }
                default {
                    Write-Error "Invalid option"
                }
            }
        }
    }
    else {
        switch ($Action) {
            'k8s' { Generate-K8sSecret }
            'dockerhub' { Generate-DockerHubSecret }
            'verify' { Verify-Workflows }
            'test' { Verify-LocalBuilds }
            'all' {
                Generate-K8sSecret
                Generate-DockerHubSecret
                Verify-Workflows
                Verify-LocalBuilds
            }
        }
        Write-Success "Setup complete!"
    }
}

# Run main function
Main
