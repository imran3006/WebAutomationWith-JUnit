import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;

public class JUnitTutorial {
    WebDriver driver;
    @Before // before annotation means when we run the testcase , it will load the browser first
    public void setup(){


        System.setProperty("webdriver.chrome.driver","./src/test/resources/chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headed"); // here pass headless arguments means when we run the test cases it will not open any browser but run in the background.
        /* why we use headless mode
        * for performance improvement. suppose we have 100 test cases , so there each time it will load meories ram which is space and time consuming.
        * and we integrate cicd pipelines*/
        driver = new ChromeDriver(options);// overriding webdrivers common peroperties with chrom own properties
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));

        //implicitwait: when we acces e element , we wait maximum 30s for loading the element. after 30s it will provide time error.
        //explicit: override the implicit wait. like when a specific web element is taking more time thn 30s to load but rest of the elemnt are working fine.
        // then we provide extra time for that element to load
    }

    //implicitwait:
    @Test // annotation. it will make it run from here
    public void getTitle(){
        driver.get("https://demoqa.com/");
        String title=driver.getTitle();
        Assert.assertEquals("ToolsQA",title ); //matching the actual and expected result
        //Assert.assertTrue(title.contains("ToolsQA"));

//        System.out.println(title);
    }
    @Test
    public void writeText(){
        driver.get("https://demoqa.com/text-box");
//        driver.findElement(By.id("userName")).sendKeys("Imran");
//        driver.findElement(By.cssSelector("[type=text]")).sendKeys("Imran");
//        driver.findElement(By.cssSelector("[id=userName]")).sendKeys("Imran");
//        driver.findElement(By.className("form-control")).sendKeys("Imran");
        WebElement txtFirstName= driver.findElement(By.id("userName")); //return an webelement aassigned in a variable name txtFirstName. Webelement is a data type
        txtFirstName.sendKeys("Imran");
//        WebElement txtEmail = driver.findElement(By.xpath("//input[@id='userEmail']"));
//        txtEmail.sendKeys("abc@gmail.com");
        WebElement txtEmail=driver.findElement(By.cssSelector("[type='email']"));
        txtEmail.sendKeys("salman@test.com");

        List<WebElement> button=driver.findElements(By.tagName("button"));
        button.get(1).click();

        //driver.findElements(By.tagName("button")).get(0).click();



    }
    @Test
    public  void handleAlert() throws InterruptedException {
      driver.get("https://demoqa.com/alerts");
//        driver.findElement(By.id("alertButton")).click();
//        Thread.sleep(2000);
//        driver.switchTo().alert().accept();
       /* timer */

//        driver.findElement(By.id("timerAlertButton")).click();
//        Thread.sleep(6000);
//        driver.switchTo().alert().accept();
        /* assertion*/
//         driver.findElement(By.id("confirmButton")).click();
//        driver.switchTo().alert().dismiss(); //cancel
//        String txt = driver.findElement(By.className("text-success")).getText();
//        Assert.assertTrue(txt.contains("Cancel"));

        driver.findElement(By.id("promtButton")).click();

        driver.switchTo().alert().sendKeys("Imran");
        Thread.sleep(2000);
        driver.switchTo().alert().accept();

        String result= driver.findElement(By.id("promptResult")).getText();
        Assert.assertTrue(result.contains("Imran"));

    }
    @Test
    public void selectDate(){
        driver.get("https://demoqa.com/date-picker");
        Actions actions = new Actions(driver);
        WebElement txtDate= driver.findElement(By.id("dateOfBirthInput"));
//        actions.moveToElement(txtDate).
//                doubleClick().click().
//                keyDown(Keys.BACK_SPACE).
//                keyUp(Keys.BACK_SPACE).
//                perform();

        /* ANOTHER WAY */
        txtDate.sendKeys(Keys.CONTROL+"a");
//        txtDate.sendKeys(Keys.BACK_SPACE);
//        driver.findElement(By.id("datePickerMonthYearInput")).clear();
        driver.findElement(By.id("datePickerMonthYearInput")).sendKeys("09 Aug 2023");
        txtDate.sendKeys(Keys.ENTER);

    }
    @Test
    public void selectDropdown(){
        driver.get("https://demoqa.com/select-menu");
        Select select = new Select(driver.findElement(By.id("oldSelectMenu")));
        select.selectByValue("3");
        /* for multiselect */
        Select cars=new Select(driver.findElement(By.id("cars")));
        if (cars.isMultiple()) {
            cars.selectByValue("volvo");
            cars.selectByValue("audi");
        }

    }
    @Test
    public void mouseHover(){
        driver.get("https://green.edu.bd/");
        Actions actions = new Actions(driver);
        List<WebElement> list =  driver.findElements(By.xpath("//a[contains(text(), \"About Us\")]"));
        actions.moveToElement(list.get(2)).perform();

    }
    @Test
    public void keyboardEvent() throws InterruptedException {
        driver.get("https://www.google.com/");
        WebElement searchElement = driver.findElement(By.name("q"));
        Actions action = new Actions(driver);
        action.moveToElement(searchElement);
        action.keyDown(Keys.SHIFT);
        action.sendKeys("Selenium Webdriver")
                .keyUp(Keys.SHIFT)
                .doubleClick().click()
                .contextClick()
                .perform();

        Thread.sleep(5000);
    }
    @Test
    public void actionClick(){
        driver.get("https://demoqa.com/buttons");
        List<WebElement> buttons=driver.findElements(By.cssSelector("[type=button]"));
        Actions actions=new Actions(driver);
        actions.doubleClick(buttons.get(1)).perform();
        actions.contextClick(buttons.get(2)).perform();
    }
    @Test
    public void takeScreenShot() throws IOException {
        driver.get("https://demoqa.com");
        File screenshotFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        String time = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss-aa").format(new Date());
        String fileWithPath = "./src/test/resources/screenshots/" + time + ".png";
        File DestFile = new File(fileWithPath);
        FileUtils.copyFile(screenshotFile, DestFile);
    }
    @Test
    public void uploadFile(){
        driver.get("https://demoqa.com/upload-download");
        driver.findElement(By.id("uploadFile")).sendKeys("E:\\aa.png");
    }
    @Test
    public void downloadFile() {
        driver.get("https://demoqa.com/upload-download");
        driver.findElement(By.id("downloadButton")).click();


    }


    @Test
    public void handleTab() throws InterruptedException {
        driver.get("https://demoqa.com/browser-windows");
        driver.findElement(By.id("tabButton")).click();
        Thread.sleep(3000);
        ArrayList<String> w = new ArrayList(driver.getWindowHandles());
//switch to open tab
        driver.switchTo().window(w.get(1));
        System.out.println("New tab title: " + driver.getTitle());
        String text = driver.findElement(By.id("sampleHeading")).getText();
        Assert.assertEquals(text,"This is a sample page");
        driver.close();
        driver.switchTo().window(w.get(0));
    }

    @Test
    public void handleWindow() throws InterruptedException {
        driver.get("https://demoqa.com/browser-windows");
        //Thread.sleep(2000);
        /* explicit wait*/
        WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(50));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("windowButton")));

        driver.findElement(By.id(("windowButton"))).click();
        //get parent window
        String mainWindowHandle = driver.getWindowHandle();
        //handling the child windows and store those in set(unorder way te thake)
        Set<String> allWindowHandles = driver.getWindowHandles();
        //get the element one by one by iterator method and store it in iterator variable/object
        Iterator<String> iterator = allWindowHandles.iterator();

        while (iterator.hasNext()) {
            String ChildWindow = iterator.next();
            if (!mainWindowHandle.equalsIgnoreCase(ChildWindow)) { // differentiate chilwindow and main window. remove main window
                driver.switchTo().window(ChildWindow);
                String text = driver.findElement(By.id("sampleHeading")).getText();
                Assert.assertTrue(text.contains("This is a sample page"));
            }
        }
        driver.close();
    }

    @Test
    public void dataScrap(){
        driver.get("https://demoqa.com/webtables");
        WebElement table = driver.findElement(By.className("rt-tbody"));
        List<WebElement> allRows = table.findElements(By.className("rt-tr"));
        int i=0;
        for (WebElement row : allRows) {
            List<WebElement> cells = row.findElements(By.className("rt-td"));
            for (WebElement cell : cells) {
                i++;
                System.out.println("num["+i+"] "+ cell.getText());

            }
        }
    }

    @Test
    public void handleIframe(){
        driver.get("https://demoqa.com/frames");
        driver.switchTo().frame("frame2");
        String text= driver.findElement(By.id("sampleHeading")).getText();
        Assert.assertTrue(text.contains("This is a sample page"));
        driver.switchTo().defaultContent();
    }

    @After
    public void closeDriver(){

        //  driver.close();
    }



}
