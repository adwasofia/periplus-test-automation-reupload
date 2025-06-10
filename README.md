# periplus-test-automation-reupload

# Periplus Test Automation

This repository contains automated test scripts for testing the Periplus online shopping platform, specifically focusing on shopping cart functionality.

## Overview

This test automation framework is built using Java and Selenium WebDriver to perform end-to-end testing of the Periplus e-commerce website's shopping cart features. The tests validate various shopping cart operations including adding items, updating quantities, removing items, and checkout processes.

## Prerequisites
- **Java Development Kit (JDK)** - Version 8 or higher
- **Maven**
- **Chrome Web Browser**
- **WebDriver**

## Project Structure

```
├── src/
│   ├── main/java/
│   └── test/java/
│       └── PeriplusShoppingCartTest.java    # Main test file
├── pom.xml                                  # Maven dependencies
└── README.md
```

## Dependencies

The project uses the following key dependencies:

- **Selenium WebDriver** - For browser automation
- **TestNG** - For test framework
- **Maven** - For build and dependency management

## Setup Instructions

1. **Clone the repository:**
   ```bash
   git clone https://github.com/adwasofia/periplus-test-automation-reupload.git
   cd periplus-test-automation-reupload
   ```

2. **Install dependencies:**
   ```bash
   mvn clean install
   ```

## Running the Tests

### Command Line Execution

To run the main shopping cart test file:

```bash
mvn test -Dtest=PeriplusShoppingCartTest
or
mvn clean test
