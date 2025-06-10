package com.periplus.openwaytechnicaltest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.Duration;
import java.util.List;

public class PeriplusShoppingCartTest {

    private static final Logger logger = LoggerFactory.getLogger(PeriplusShoppingCartTest.class);
    private WebDriver driver;
    private WebDriverWait wait;

    // Test credentials
    private static final String TEST_EMAIL = "adwasofiaa@gmail.com";
    private static final String TEST_PASSWORD = "OpenwayTechnicalTest";
    private static final String PERIPLUS_URL = "https://www.periplus.com/";

    @BeforeMethod
    public void setUp() {
        logger.info("Setting up WebDriver..");
        // Initialize WebDriver
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        // Set timeouts
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        logger.info("WebDriver has been setup successfully.");
    }

    @Test(description = "Test to add a product to cart with login")
    public void testAddProductToCart() {
        try {
            logger.info("Test Case: Login and add product to cart");
            goToWebsite(); // Go to Periplus website
            login(); // Login process
            findAndSelectProduct(); // Find first product (product with index 0)
            addProductToCart(); // Add product to cart
            verifyProductInCart(); // Verify product in cart
            logger.info("Test is executed successfully.");
        } catch (Exception e) {
            logger.error("Test failed with exception: {}", e.getMessage(), e);
            Assert.fail("Test execution failed: " + e.getMessage());
        }
    }

    private void goToWebsite() {
        logger.info("Going to Periplus website..");
        driver.get(PERIPLUS_URL);
        waitForPageLoad();
        logger.info("Successfully went to: {}", PERIPLUS_URL);
    }

    private void login() {
        logger.info("Login process..");
        try {
            WebElement signInButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//a[contains(text(),'Sign in') or contains(text(),'Sign In')]")));
            if (signInButton != null) {
                signInButton.click();
                waitForPageLoad();
                logger.info("Login page accessed.");
            } else {
                logger.warn("Login button not found.");
                return;
            }
            fillLogin(); // Fill login username and password
            verifyLogin(); // Verify login
            logger.info("Login process completed successfully");
        } catch (Exception e) {
            logger.error("Login failed: {}", e.getMessage());
            throw new RuntimeException("Login failed: " + e.getMessage(), e);
        }
    }

    private void fillLogin() {
        logger.info("Filling email and password..");
        try {
            // Find and fill email field
            WebElement emailField = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//input[@type='email' or @name='email' or @id='email' or contains(@placeholder,'email')]")));
            type(emailField, TEST_EMAIL);
            // Find and fill password field
            WebElement passwordField = driver.findElement(
                    By.xpath("//input[@type='password' or @name='password' or @id='password' or contains(@placeholder,'password')]"));
            type(passwordField, TEST_PASSWORD);
            // Click login button
            WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[contains(text(),'Login') or contains(text(),'Sign In') or @type='submit'] | //input[@type='submit']")));
            loginButton.click();
            waitForPageLoad();
            logger.info("Email and password are successfully submitted.");
        } catch (Exception e) {
            logger.error("Error filling login: {}", e.getMessage());
            throw new RuntimeException("Failed to fill login: " + e.getMessage());
        }
    }

    private void verifyLogin() {
        logger.info("Verifying login process..");
        try {
            boolean loginSuccess = false;
            WebElement indicator = driver.findElement(By.xpath("//a[contains(text(),'Account') or contains(text(),'Profile') or contains(text(),'Logout')]"));
            if (indicator.isDisplayed()) {
                loginSuccess = true;
            }
            if (!loginSuccess) {
                // If loginSuccess is False and user is still in login page then login is assumed to be failed
                try {
                    driver.findElement(By.xpath("//input[@type='email' or @type='password']"));
                    throw new RuntimeException("Login is indicated to be failed.");
                } catch (NoSuchElementException e) {
                    // Assume login is success if user is not in login page anymore
                    loginSuccess = true;
                    logger.info("Login is assumed to be true because the user is not in login page anymore.");
                }
            }
            Assert.assertTrue(loginSuccess, "Login verification process failed.");
        } catch (Exception e) {
            throw new RuntimeException("Login verification is interrupted.");
        }
    }

    private void findAndSelectProduct() {
        logger.info("Find and select any product.");
        try {
            goToProductCategory(); // Go to product category page
            List<WebElement> products = findProducts(); // Find available products
            if (products.isEmpty()) {
                throw new RuntimeException("No products found.");
            }
            WebElement selectedProduct = products.getFirst(); // Select the first available product
            logger.info("Selected product: {}", selectedProduct);
            clickElement(selectedProduct);
            waitForPageLoad();
        } catch (Exception e) {
            logger.error("Failed to find and select product: {}", e.getMessage());
            throw new RuntimeException("Failed to find and select product: " + e.getMessage());
        }
    }

    private void goToProductCategory() {
        logger.info("Go to product category page..");
        try {
            WebElement categoryLink = driver.findElement(By.xpath("//nav//a[contains(text(),'Categories') or contains(text(),'Shop')]"));
            if (categoryLink.isDisplayed()) {
                categoryLink.click();
                waitForPageLoad();
                logger.info("Successfully went to product category");
            }
        } catch (NoSuchElementException e) {
            logger.error("Error going to product category page: {}", e.getMessage());
            throw new RuntimeException("Failed to go to product category page: " + e.getMessage());
        }
    }

    private List<WebElement> findProducts() {
        logger.info("Finding available products..");
        try {
            List<WebElement> products = driver.findElements(By.xpath("//div[contains(@class,'product')]//a[contains(@href,'product') or contains(@href,'book')]"));
            if (!products.isEmpty()) {
                logger.info("Found {} products.", products.size());
                return products;
            }
        } catch (Exception e) {
            logger.error("Error finding products: {}", e.getMessage());
            throw new RuntimeException("Failed to find products: " + e.getMessage());
        }
        return List.of(); // Return empty list if no available products found
    }

    private void addProductToCart() {
        logger.info("Adding product to cart..");
        try {
            WebElement addToCartButton = driver.findElement(By.xpath("//button[contains(text(),'Add to Cart') or contains(text(),'Add to Bag')]"));
            if (addToCartButton.isDisplayed() && addToCartButton.isEnabled()) {
                logger.info("Found 'Add to Cart' button.");
                addToCartButton.click();
                logger.info("One product is added to cart successfully.");
            }
        } catch (Exception e) {
            logger.error("Error adding product to cart: {}", e.getMessage());
            throw new RuntimeException("Failed to add product to cart: " + e.getMessage());
        }
    }

    private void verifyProductInCart() {
        logger.info("Verifying product in cart..");
        try {
            goToCart(); // Go to cart page
            boolean hasItems = checkCartHasItems(); // Verify cart has items
            Assert.assertTrue(hasItems, "Product was not successfully added to cart");
            logger.info("Product successfully verified in cart");
        } catch (Exception e) {
            logger.error("Failed to verify product in cart: {}", e.getMessage());
            throw new RuntimeException("Failed to verify product in cart: " + e.getMessage());
        }
    }

    private void goToCart() {
        logger.info("Going to Cart page..");
        try {
            WebElement cartLink = driver.findElement(By.xpath("//*[@id='show-your-cart']//a"));
            if (cartLink.isDisplayed()) {
                clickElement(cartLink);
                waitForPageLoad();
                logger.info("Successfully went to Cart page.");
            }
        } catch (NoSuchElementException e) {
            logger.error("Error going to Cart page: {}", e.getMessage());
            throw new RuntimeException("Failed to go to Cart page: " + e.getMessage());
        }
    }

    private boolean checkCartHasItems() {
        logger.info("Finding added product in cart..");
        try {
            List<WebElement> items = driver.findElements(By.xpath("//div[contains(@class,'cart-item') or contains(@class,'cart-product')]"));
            if (!items.isEmpty()) {
                logger.info("Found {} items in cart.", items.size());
                return true;
            }
        } catch (Exception e) {
            logger.error("Error finding added product in cart: {}", e.getMessage());
            throw new RuntimeException("Failed to find added product in cart: " + e.getMessage());
        }
        return false;
    }

    private void waitForPageLoad() {
        try {
            Thread.sleep(2000);
            wait.until(webDriver -> ((JavascriptExecutor) webDriver)
                    .executeScript("return document.readyState").equals("complete"));
        } catch (Exception e) {
            logger.warn("Page load wait interrupted: {}", e.getMessage());
        }
    }

    private void clickElement(WebElement element) {
        try {
            element.click();
        } catch (Exception e) {
            // Try JavaScript click as fallback
            try {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
            } catch (Exception jsError) {
                logger.error("Failed to click element with both normal and JS click: {}", jsError.getMessage());
                throw new RuntimeException("Failed to click element");
            }
        }
    }

    private void type(WebElement element, String text) {
        try {
            element.clear();
            Thread.sleep(500);
            element.sendKeys(text);
        } catch (Exception e) {
            logger.error("Error typing into element: {}", e.getMessage());
            throw new RuntimeException("Failed to type into element");
        }
    }

    @AfterMethod
    public void tearDown() {
        logger.info("Closing WebDriver..");
        if (driver != null) {
            try {
                driver.quit();
                logger.info("WebDriver closed successfully.");
            } catch (Exception e) {
                logger.error("Error closing WebDriver: {}", e.getMessage());
            }
        }
    }
}