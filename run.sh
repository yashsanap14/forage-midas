#!/bin/bash
# Quick Build and Run Script for Forage Midas Project
# This script sets up the Java environment and provides common commands

# Set Java 17 as the Java Home
export JAVA_HOME=/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home

echo "🚀 Forage Midas - Quick Commands"
echo "================================="
echo ""
echo "Java Version:"
$JAVA_HOME/bin/java -version
echo ""
echo "Available commands:"
echo "  1. Build project (skip tests):  ./mvnw clean install -DskipTests"
echo "  2. Build project (with tests):  ./mvnw clean install"
echo "  3. Run application:             ./mvnw spring-boot:run"
echo "  4. Run tests only:              ./mvnw test"
echo ""
echo "Choose an option (1-4) or press Ctrl+C to exit:"
read -r option

case $option in
    1)
        echo "Building project (skipping tests)..."
        ./mvnw clean install -DskipTests
        ;;
    2)
        echo "Building project with tests..."
        ./mvnw clean install
        ;;
    3)
        echo "Running application..."
        ./mvnw spring-boot:run
        ;;
    4)
        echo "Running tests..."
        ./mvnw test
        ;;
    *)
        echo "Invalid option"
        exit 1
        ;;
esac
