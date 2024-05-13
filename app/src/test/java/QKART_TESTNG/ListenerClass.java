package QKART_TESTNG;

import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class ListenerClass implements ITestListener {

    RemoteWebDriver driver;

    public void onStart(ITestContext context) {
        System.out.println("onStart method started");
    }

    public void onFinish(ITestContext context) {
        System.out.println("onFinish method started");
    }

    public void onTestStart(ITestResult result) {
        try {
			driver =(RemoteWebDriver)result.getTestClass().getRealClass().getDeclaredField("driver").get(result.getInstance());
		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
		}
        System.out.println("Test Started : "+ result.getName());
        //QKART_Tests.takeScreenshot(driver, "Test Started", result.getName());
    }

    public void onTestSuccess(ITestResult result) {
        System.out.println("Test Passed : "+ result.getName());
        //QKART_Tests.takeScreenshot(driver, "Test Passed", result.getName());
    }

    public void onTestFailure(ITestResult result) {      
        System.out.println("Test Failed : "+ result.getName()+" Failure Reason : "+ result.getThrowable().toString());
        QKART_Tests.takeScreenshot(driver, "Test Failed", result.getThrowable().toString());
    }

    public void onTestSkipped(ITestResult result) {
        System.out.println("onTestSkipped Method : "+ result.getName());
    }

    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        System.out.println("onTestFailedButWithinSuccessPercentage : "+ result.getName());
    }
}