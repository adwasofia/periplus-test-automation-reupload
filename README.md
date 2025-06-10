# periplus-test-automation-reupload

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
```
or
```bash
mvn clean test
```
