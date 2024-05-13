package QKART_SANITY_LOGIN.Module1;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Home {
    RemoteWebDriver driver;
    String url = "https://crio-qkart-frontend-qa.vercel.app";

    public Home(RemoteWebDriver driver) {
        this.driver = driver;
    }

    public void navigateToHome() {
        if (!this.driver.getCurrentUrl().equals(this.url)) {
            this.driver.get(this.url);
        }
    }

    public Boolean PerformLogout() throws InterruptedException {
        try {
            // Find and click on the Logout Button
            WebElement logout_button = driver.findElement(By.className("MuiButton-text"));
            logout_button.click();

             // SLEEP_STMT_11: Wait for Logout button gets disappear
            //Thread.sleep(3000);
            WebDriverWait wait = new WebDriverWait(driver, 10);
            wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//button[text()='Login']"))));

            return true;
        } catch (Exception e) {
            // Error while logout
            return false;
        }
    }

    /*
     * Returns Boolean if searching for the given product name occurs without any
     * errors
     */
    public Boolean searchForProduct(String product) {
        try {
            // TODO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 03: MILESTONE 1
            // Clear the contents of the search box and Enter the product name in the search
            // box
            WebElement searchBox = driver.findElement(By.xpath("(//input[@name='search'])[1]"));
            searchBox.clear();
            searchBox.sendKeys(product);
            //WebElement searchButton = driver.findElement(By.xpath("(//*[local-name()='svg'])[1]"));
            //searchButton.click();

             // SLEEP_STMT_12: wait till atleast one product gets displayed or no product message displayed
            //Thread.sleep(5000);
            //Declare and initialise a fluent wait
            FluentWait<RemoteWebDriver> wait = new FluentWait<RemoteWebDriver>(driver);
            wait.withTimeout(Duration.ofSeconds(1L));
            wait.pollingEvery(Duration.ofMillis(250L));
            wait.ignoring(NoSuchElementException.class);
            
            ExpectedCondition<WebElement> resFound = ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(@class,'MuiPaper-root')]"));
            ExpectedCondition<WebElement> noResultFound = ExpectedConditions.presenceOfElementLocated(By.xpath("//h4[text()=' No products found ']"));
            wait.until(ExpectedConditions.or(noResultFound, resFound));

            return true;
        } catch (Exception e) {
            System.out.println("Error while searching for a product: " + e.getMessage());
            return false;
        }
    }

    /*
     * Returns Array of Web Elements that are search results and return the same
     */
    public List<WebElement> getSearchResults() {
        List<WebElement> searchResults = new ArrayList<WebElement>() {
        };
        try {
            // TODO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 03: MILESTONE 1
            // Find all webelements corresponding to the card content section of each of
            // search results
            searchResults = driver.findElements(By.xpath("//div[contains(@class,'MuiPaper-root')]"));

            return searchResults;
        } catch (Exception e) {
            System.out.println("There were no search results: " + e.getMessage());
            return searchResults;

        }
    }

    /*
     * Returns Boolean based on if the "No products found" text is displayed
     */
    public Boolean isNoResultFound() {
        Boolean status = false;
        try {
            // TODO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 03: MILESTONE 1
            // Check the presence of "No products found" text in the web page. Assign status
            // = true if the element is *displayed* else set status = false
            status = driver.findElement(By.xpath("//h4[text()=' No products found ']")).isDisplayed();

            return status;
        } catch (Exception e) {
            return status;
        }
    }

    /*
     * Return Boolean if add product to cart is successful
     */
    public Boolean addProductToCart(String productName) {
        Boolean status = false;
        try {
            // TODO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 05: MILESTONE 4
            /*
             * Iterate through each product on the page to find the WebElement corresponding
             * to the matching productName
             * 
             * Click on the "ADD TO CART" button for that element
             * 
             * Return true if these operations succeeds
             */

             List<String> productList = new ArrayList<>();
             productList.add(productName);
            
            List<WebElement> searchResults = getSearchResults();
            //System.out.println("search result size : "+ searchResults.size());

            for (WebElement searchResult : searchResults){
                SearchResult result = new SearchResult(searchResult);
                
                //System.out.println("product title : "+ result.getTitleofResult());

                if (result.getTitleofResult().equals(productName)){
                   result.addToCart(); 
                    // SLEEP_STMT_13: Wait till product gets added to cart
                   //Thread.sleep(3000);
                   status = verifyCartContents(productList);

                }
            }

            //System.out.println("Unable to find the given product");
            return status;
        } catch (Exception e) {
            System.out.println("Exception while performing add to cart: " + e.getMessage());
            return false;
        }
    }

    /*
     * Return Boolean denoting the status of clicking on the checkout button
     */
    public Boolean clickCheckout() {
        Boolean status = false;
        try {
            // TODO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 05: MILESTONE 4
            // Find and click on the the Checkout button
            WebElement checkOutButton = driver.findElement(By.xpath("//button[text()='Checkout']"));
            checkOutButton.click();
            return true;
        } catch (Exception e) {
            System.out.println("Exception while clicking on Checkout: " + e.getMessage());
            return status;
        }
    }

    /*
     * Return Boolean denoting the status of change quantity of product in cart
     * operation
     */
    public Boolean changeProductQuantityinCart(String productName, int quantity) {
        try {
            // TODO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 06: MILESTONE 5

            // Find the item on the cart with the matching productName

            // Increment or decrement the quantity of the matching product until the current
            // quantity is reached (Note: Keep a look out when then input quantity is 0,
            // here we need to remove the item completely from the cart)

            List<WebElement> checkOutProducts = driver.findElements(By.xpath("//div[contains(@class,'1gjj37g')]"));

            for ( WebElement checkOutProduct : checkOutProducts){
                WebElement checkOutProductName = checkOutProduct.findElement(By.xpath("./div[1]"));
                //System.out.println("checkOutProductName : "+ checkOutProductName.getText());
                if(checkOutProductName.getText().equals(productName)){
                    //System.out.println("checkout product match found");

                    WebElement productQuantity = checkOutProduct.findElement(By.xpath(".//div[contains(@class,'css-olyig7')]"));
                    WebElement minusButton = checkOutProduct.findElement(By.xpath(".//button[1]"));
                    WebElement plusButton = checkOutProduct.findElement(By.xpath(".//button[2]"));

                    FluentWait<RemoteWebDriver> wait = new FluentWait<RemoteWebDriver>(driver);
                    wait.withTimeout(Duration.ofSeconds(5L));
                    wait.pollingEvery(Duration.ofSeconds(1L));
                    wait.ignoring(Exception.class);

                    //System.out.println("Expected quantiy : "+ quantity);   
                    while (Integer.valueOf(productQuantity.getText()) != quantity){
                        //System.out.println("productQuantity : "+  productQuantity.getText());
                        if(Integer.valueOf(productQuantity.getText()) > quantity){
                            //System.out.println("clicking on minus button");
                            minusButton.click();
                             // SLEEP_STMT_14: Wait till product quantity get changed
                            //Thread.sleep(3000);
                            
                            wait.until(ExpectedConditions.textToBe(By.xpath(".//div[contains(@class,'css-olyig7')]"), 
                                                                    String.valueOf(Integer.valueOf(productQuantity.getText()) -1 )));

                        } else if (Integer.valueOf(productQuantity.getText()) < quantity) {
                            //System.out.println("clicking on plus button");
                            plusButton.click();
                            // SLEEP_STMT_15: Wait till product quantity get changed
                            //Thread.sleep(3000);  
                            wait.until(ExpectedConditions.textToBe(By.xpath(".//div[contains(@class,'css-olyig7')]"), 
                                                                    String.valueOf(Integer.valueOf(productQuantity.getText()) + 1 )));
                        }
                    }    
                }
            }

            return true;
        } catch (Exception e) {
            if (quantity == 0)
                return true;
            System.out.println("exception occurred when updating cart: " + e.getMessage());
            return false;
        }
    }

    /*
     * Return Boolean denoting if the cart contains items as expected
     */
    public Boolean verifyCartContents(List<String> expectedCartContents) {
        try {
            WebElement cartParent = driver.findElement(By.className("cart"));
            List<WebElement> cartContents = cartParent.findElements(By.className("css-zgtx0t"));

            //System.out.println("cart size : "+ cartContents.size());

            List<String> actualCartContents = new ArrayList<String>();
                    
            for (WebElement cartItem : cartContents) {
                actualCartContents.add(cartItem.findElement(By.className("css-1gjj37g")).getText().split("\n")[0]);
                //System.out.println(cartItem.findElement(By.className("css-1gjj37g")).getText().split("\n")[0]);
            }

            for (String expected : expectedCartContents) {
                if (!actualCartContents.contains(expected)) {
                    return false;
                }
            }

            return true;

        } catch (Exception e) {
            System.out.println("Exception while verifying cart contents: " + e.getMessage());
            return false;
        }
    }

}
