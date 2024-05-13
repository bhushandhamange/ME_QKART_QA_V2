package QKART_SANITY_LOGIN.Module1;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SearchResult {
    WebElement parentElement;

    public SearchResult(WebElement SearchResultElement) {
        this.parentElement = SearchResultElement;
    }

    /*
     * Return title of the parentElement denoting the card content section of a
     * search result
     */
    public String getTitleofResult() {
        String titleOfSearchResult = "";
        // TODO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 03: MILESTONE 1
        // Find the element containing the title (product name) of the search result and
        // assign the extract title text to titleOfSearchResult
        titleOfSearchResult = parentElement.findElement(By.tagName("p")).getText().trim();

        return titleOfSearchResult;
    }

    /*
     * Return Boolean denoting if the open size chart operation was successful
     */
    public Boolean openSizechart(WebDriver driver) {
        try {

            // TODO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 04: MILESTONE 2
            // Find the link of size chart in the parentElement and click on it
            WebElement sizeChartButton = parentElement.findElement(By.tagName("button"));    
            sizeChartButton.click();    

             // SLEEP_STMT_19 : wait till size chart gets open     
            //Thread.sleep(4000);
            WebDriverWait wait = new WebDriverWait(driver, 10);
            wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//table[contains(@class,'MuiTable-root')]"))));

            return true;
        } catch (Exception e) {
            System.out.println("Exception while opening Size chart: " + e.getMessage());
            return false;
        }
    }

    /*
     * Return Boolean denoting if the close size chart operation was successful
     */
    public Boolean closeSizeChart(WebDriver driver) {
        try {
            Actions action = new Actions(driver);

            // Clicking on "ESC" key closes the size chart modal
            action.sendKeys(Keys.ESCAPE);
            action.perform();

            // SLEEP_STMT_20 : wait till size chart gets closed 
            //Thread.sleep(2000);
            WebDriverWait wait = new WebDriverWait(driver, 10);
            wait.until(ExpectedConditions.invisibilityOf(driver.findElement(By.xpath("//table[contains(@class,'MuiTable-root')]"))));

            return true;
        } catch (Exception e) {
            System.out.println("Exception while closing the size chart: " + e.getMessage());
            return false;
        }
    }

    /*
     * Return Boolean based on if the size chart exists
     */
    public Boolean verifySizeChartExists() {
        Boolean status = false;
        try {
            // TODO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 04: MILESTONE 2
            /*
             * Check if the size chart element exists. If it exists, check if the text of
             * the element is "SIZE CHART". If the text "SIZE CHART" matches for the
             * element, set status = true , else set to false
             */

             WebElement sizeChartButton = parentElement.findElement(By.tagName("button"));
             if(sizeChartButton.isDisplayed() && sizeChartButton.getText().toUpperCase().equals("SIZE CHART")){
                status = true;
             } else {
                status = false;
             }

            return status;
        } catch (Exception e) {
            return status;
        }
    }

    /*
     * Return Boolean if the table headers and body of the size chart matches the
     * expected values
     */
    public Boolean validateSizeChartContents(List<String> expectedTableHeaders, List<List<String>> expectedTableBody,
            WebDriver driver) {
        Boolean status = true;
        try {
            // TODO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 04: MILESTONE 2
            /*
             * Locate the table element when the size chart modal is open
             * 
             * Validate that the contents of expectedTableHeaders is present as the table
             * header in the same order
             * 
             * Validate that the contents of expectedTableBody are present in the table body
             * in the same order
             */
            WebElement sizeChartTable = driver.findElement(By.xpath("//table[contains(@class,'MuiTable-root')]"));
            
            List<WebElement> headers = sizeChartTable.findElements(By.xpath(".//thead/tr/th"));
            //System.out.println("headers count : "+ headers.size());

            //System.out.println("Verifying headers");
            int i=0;   
            boolean headersMatch = true;
            for(WebElement head : headers){
                //System.out.println("i : "+ i);
                //System.out.println("head : "+ head.getText());
                //System.out.println("expected text : "+ expectedTableHeaders.get(i));
                
                if(!head.getText().equals(expectedTableHeaders.get(i))){
                    headersMatch = false;
                    break;
                }
                i++;
            }

            //System.out.println("headerMatch : "+ headersMatch);

            boolean bodyMatch = true;

            int j= 0;

            List<WebElement> bodyRows = sizeChartTable.findElements(By.xpath(".//tbody/tr"));
            //System.out.println("bodyRows count : "+ bodyRows.size());

            for (WebElement row : bodyRows){
                //System.out.println("expected table body row : "+ expectedTableBody.get(j));
                List<String> expectedCellValues = expectedTableBody.get(j);
                List<WebElement> cells = row.findElements(By.xpath(".//td"));

                int k= 0;
                for (WebElement cell : cells){
                    //System.out.println("cell : "+ cell.getText());
                    //System.out.println("expected cell value : "+ expectedCellValues.get(k));

                    if(!cell.getText().equals(expectedCellValues.get(k))){
                        bodyMatch = false;
                        break;
                    }
                    k++;    
                }
                if(!bodyMatch)
                    break;
                j++;
            }
            

            if(headersMatch && bodyMatch){
                status = true;
            } else {
                status = false;
            }

            return status;

        } catch (Exception e) {
            System.out.println("Error while validating chart contents");
            return false;
        }
    }

    /*
     * Return Boolean based on if the Size drop down exists
     */
    public Boolean verifyExistenceofSizeDropdown(WebDriver driver) {
        Boolean status = false;
        try {
            // TODO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 04: MILESTONE 2
            // If the size dropdown exists and is displayed return true, else return false
            status = parentElement.findElement(By.tagName("select")).isDisplayed();

            return status;
        } catch (Exception e) {
            return status;
        }
    }

    public Boolean addToCart(){
        Boolean status = false;
        try{
            
            WebElement addToCartButton = parentElement.findElement(By.xpath(".//button[text()='Add to cart']"));
            addToCartButton.click();

            return true;
        } catch (Exception e){
            return false;
        }
    }
}