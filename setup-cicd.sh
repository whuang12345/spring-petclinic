#!/bin/bash
# CI/CD Setup Helper Script
# This script helps you set up the GitHub secrets needed for CI/CD

set -e

echo "🚀 Spring PetClinic CI/CD Setup Helper"
echo "======================================"
echo ""

# Colors for output
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
RED='\033[0;31m'
NC='\033[0m' # No Color

# Function to print colored output
print_green() {
    echo -e "${GREEN}✓ $1${NC}"
}

print_yellow() {
    echo -e "${YELLOW}⚠ $1${NC}"
}

print_blue() {
    echo -e "${BLUE}ℹ $1${NC}"
}

print_red() {
    echo -e "${RED}✗ $1${NC}"
}

# Check if GitHub CLI is installed
check_github_cli() {
    if command -v gh &> /dev/null; then
        print_green "GitHub CLI found"
        return 0
    else
        print_yellow "GitHub CLI not found. Visit: https://cli.github.com"
        return 1
    fi
}

# Generate Kubernetes config secret
generate_k8s_secret() {
    echo ""
    print_blue "Generating Kubernetes config secret..."
    echo ""
    
    KUBECONFIG_PATH="${1:~/.kube/config}"
    
    if [ ! -f "$KUBECONFIG_PATH" ]; then
        print_red "Kubeconfig not found at: $KUBECONFIG_PATH"
        echo "Please provide the correct path to your kubeconfig file"
        return 1
    fi
    
    print_blue "Kubeconfig path: $KUBECONFIG_PATH"
    
    # Check OS and create base64 accordingly
    if [[ "$OSTYPE" == "darwin"* ]]; then
        # macOS
        KUBE_CONFIG_B64=$(cat "$KUBECONFIG_PATH" | base64)
    elif [[ "$OSTYPE" == "linux-gnu"* ]]; then
        # Linux
        KUBE_CONFIG_B64=$(cat "$KUBECONFIG_PATH" | base64 -w 0)
    elif [[ "$OSTYPE" == "msys" ]] || [[ "$OSTYPE" == "cygwin" ]]; then
        # Windows
        KUBE_CONFIG_B64=$([System.Convert]::ToBase64String([System.Text.Encoding]::UTF8.GetBytes((Get-Content $KUBECONFIG_PATH))) 2>/dev/null || cat "$KUBECONFIG_PATH" | base64 -w 0)
    else
        print_red "Unsupported OS: $OSTYPE"
        return 1
    fi
    
    echo ""
    print_green "Base64 encoded kubeconfig generated"
    echo ""
    echo "To set the secret via GitHub CLI, run:"
    echo "gh secret set KUBE_CONFIG_BASE64 --body '${KUBE_CONFIG_B64}'"
    echo ""
    echo "Or manually add in GitHub:"
    echo "1. Settings → Secrets and variables → Actions"
    echo "2. Click 'New repository secret'"
    echo "3. Name: KUBE_CONFIG_BASE64"
    echo "4. Value: (paste the base64 string above)"
    echo ""
    
    # Ask if user wants to set it via GitHub CLI
    if check_github_cli; then
        read -p "Set secret via GitHub CLI? (y/n) " -n 1 -r
        echo
        if [[ $REPLY =~ ^[Yy]$ ]]; then
            gh secret set KUBE_CONFIG_BASE64 --body "$KUBE_CONFIG_B64"
            print_green "Secret set successfully!"
        fi
    fi
}

# Generate Docker Hub secret
generate_dockerhub_secret() {
    echo ""
    print_blue "Setting up Docker Hub authentication..."
    echo ""
    
    read -p "Enter your Docker Hub username: " DOCKER_USERNAME
    read -sp "Enter your Docker Hub token: " DOCKER_TOKEN
    echo ""
    
    if check_github_cli; then
        gh secret set DOCKERHUB_USERNAME --body "$DOCKER_USERNAME"
        gh secret set DOCKERHUB_TOKEN --body "$DOCKER_TOKEN"
        print_green "Docker Hub secrets set successfully!"
    else
        echo "Set these secrets manually in GitHub:"
        echo "1. DOCKERHUB_USERNAME: $DOCKER_USERNAME"
        echo "2. DOCKERHUB_TOKEN: $DOCKER_TOKEN"
    fi
}

# Verify workflows
verify_workflows() {
    echo ""
    print_blue "Verifying workflow files..."
    echo ""
    
    workflows=(
        ".github/workflows/backend-ci.yml"
        ".github/workflows/frontend-ci.yml"
        ".github/workflows/integration-tests.yml"
        ".github/workflows/main.yml"
    )
    
    for workflow in "${workflows[@]}"; do
        if [ -f "$workflow" ]; then
            print_green "Found: $workflow"
        else
            print_yellow "Optional: $workflow not found"
        fi
    done
}

# Verify local build
verify_local_build() {
    echo ""
    print_blue "Verifying local builds..."
    echo ""
    
    # Check backend
    if [ -f "spring-petclinic/pom.xml" ]; then
        print_blue "Testing backend build..."
        cd spring-petclinic
        if ./mvnw clean package -DskipTests -q 2>/dev/null; then
            print_green "Backend build successful"
        else
            print_yellow "Backend build had issues (may need dependencies installed)"
        fi
        cd ..
    fi
    
    # Check frontend
    if [ -f "petclinic-ui/package.json" ]; then
        print_blue "Testing frontend build..."
        cd petclinic-ui
        if npm ci -q 2>/dev/null && npm run build -q 2>/dev/null; then
            print_green "Frontend build successful"
        else
            print_yellow "Frontend build had issues (may need dependencies installed)"
        fi
        cd ..
    fi
}

# Main menu
show_menu() {
    echo ""
    echo "What would you like to set up?"
    echo "================================"
    echo "1. Generate Kubernetes secret (KUBE_CONFIG_BASE64)"
    echo "2. Set up Docker Hub secrets"
    echo "3. Verify workflow files"
    echo "4. Test local builds"
    echo "5. Full setup (all of above)"
    echo "6. Exit"
    echo ""
    read -p "Enter option (1-6): " -n 1 -r
    echo
}

# Main script
main() {
    # Check if we're in the right directory
    if [ ! -d ".github/workflows" ]; then
        print_red "Error: .github/workflows directory not found"
        print_blue "Please run this script from the repository root"
        exit 1
    fi
    
    while true; do
        show_menu
        case $REPLY in
            1) generate_k8s_secret ;;
            2) generate_dockerhub_secret ;;
            3) verify_workflows ;;
            4) verify_local_build ;;
            5) 
                generate_k8s_secret
                generate_dockerhub_secret
                verify_workflows
                verify_local_build
                ;;
            6) 
                print_green "Setup complete! Happy deploying!"
                exit 0
                ;;
            *)
                print_red "Invalid option"
                ;;
        esac
    done
}

# Run main function
main
