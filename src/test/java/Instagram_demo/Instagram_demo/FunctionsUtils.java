package Instagram_demo.Instagram_demo;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.base.Function;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidKeyCode;


public class FunctionsUtils {

	//Execute adb commands
		public static String executeCommand(String cmd)
		{
		    String commandresponse="";
		    try
		    {
		        Runtime run = Runtime.getRuntime();
		        Process proc=run.exec(cmd);
		        BufferedReader stdInput = new BufferedReader(new 
		                InputStreamReader(proc.getInputStream()));

		        BufferedReader stdError = new BufferedReader(new 
		                InputStreamReader(proc.getErrorStream()));

		        String response=null;
		        while ((response = stdInput.readLine()) != null) 
		        {
		            if(response.length()>0)
		            {
		                commandresponse=commandresponse+response;
		            }
		        }

		        while ((response = stdError.readLine()) != null) 
		        {
		            commandresponse=commandresponse+response;

		        }
		    }
		    catch(Exception e)
		    {
		        e.printStackTrace();
		    }
		    //System.out.println(commandresponse);
		    return commandresponse;
		}
		
		//sleep
		public static void sleep(int timeInSeconds){
			try {
				Thread.sleep(timeInSeconds * 1000);
//				System.out.println("SLEEP IN " +timeInSeconds+ " SECONDS");
			} catch (InterruptedException e) {
				System.out.println(e.getMessage());
			}
		}
		
		public static WebElement WaitPresentbyId(WebDriver driver, String id ) {
			WebDriverWait wait= new WebDriverWait(driver, 15);
			wait.until(ExpectedConditions.presenceOfElementLocated(By.id(id)));
			
			return driver.findElement(By.id(id));
			
		}
		
		public static WebElement WaitPresentbyXpath(WebDriver driver, String xpath ) {
			WebDriverWait wait= new WebDriverWait(driver, 15);
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));
			
			return driver.findElement(By.xpath(xpath));
			
		}
		
		//Wait for visible of element(FindElement By,Element)
		public static WebElement presenceOfTheElementID(WebDriver driver, final String id) {
			//final WebElement ele;	
			Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(30, TimeUnit.SECONDS)
					.pollingEvery(1, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
			return wait.until(new Function<WebDriver, WebElement>() {
				public WebElement apply(WebDriver driver) {
					return  driver.findElement(By.id(id));	
				}
			});
		}
		
		//Verify two string
		public static boolean verifyTwoString(String ActualStr, String ExpectStr) {
			return ActualStr.equalsIgnoreCase(ExpectStr);
		}
		
		//send key Enter
		public static void sendKeyEnter(AppiumDriver driver) throws IOException{
			try {
				
				((AndroidDriver)driver).pressKeyCode(AndroidKeyCode.ENTER);
				((AndroidDriver)driver).pressKeyCode(AndroidKeyCode.KEYCODE_DPAD_RIGHT);
				((AndroidDriver)driver).pressKeyCode(AndroidKeyCode.ENTER);
			
			} catch (Exception e) {
				System.out.println("Cannot send enter key!" +e.getMessage());
					
			}
		}
		
		
		//download image to pc
		public static void downloadImg(String id,String imgUrl) {
			URL imageURL;
			try {
				//imageURL = new URL("https://pbs.twimg.com/profile_images/461167614/Icon_hirez.jpg");
				String []arr=null;
				if(imgUrl.contains("/")) {
					arr=imgUrl.split("/");
				} else {
					arr=imgUrl.split("\\");
				}
				imageURL = new URL(imgUrl);
				BufferedImage saveImage = ImageIO.read(imageURL);
				String name=arr[arr.length-1];
		        ImageIO.write(saveImage, "png", new File(name));
		        
		        File f = new File(name);

		        String path = f.getAbsolutePath();
		        
		        String adbpushcmd="adb -s "+id+ " push " + path + " /sdcard/DCIM";
		        System.out.println("push: " + adbpushcmd);
		        executeCommand(adbpushcmd);
		        
		        sleep(10);
		        
		        String adbreboot="adb -s "+id+" root";
		        executeCommand(adbreboot);
		        System.out.println("root: " + adbreboot);
		        sleep(10);
		        
		        String cmdKill_server,cmdStart_server;
		        cmdKill_server="adb -s "+id+" kill-server";
		        executeCommand(cmdKill_server);
		        sleep(10);
		        
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//scrolling
		
		public static void scrollToEle(AppiumDriver<WebElement> driver,String id, String contentSearch)  {
	        try {
	        	driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
	        	String xpath="//"+id+"[contains(@text,'"+contentSearch+"')]";
	        	System.out.println(xpath);
	        	while (driver.findElements(By.xpath(xpath)).size()==0){
	
	        		swipeVertical((AndroidDriver) driver,0.8,0.2,0.2,2000);
        		}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
	        driver.manage().timeouts().implicitlyWait(15000, TimeUnit.SECONDS);
		}
		
		public static void scrollManytimes(AppiumDriver<WebElement> driver,int times)  {
	        try {
	        	//System.out.println("scrolling............."+times);
	        	swipeVertical((AndroidDriver) driver,0.8,0.2,0.2,2000);
	        	for(int i=1;i<times;i++){
	        		swipeVertical((AndroidDriver) driver,0.8,0.2,0.2,2000);
        		}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
	      
		}
		
		public static void swipeVertical(AndroidDriver<WebElement> driver, double startPercentage, double finalPercentage, double anchorPercentage, int duration) throws Exception {
			Dimension size = driver.manage().window().getSize();
		    int anchor = (int) (size.width * anchorPercentage);
		    int startPoint = (int) (size.height * startPercentage);
		    int endPoint = (int) (size.height * finalPercentage);
		    new TouchAction(driver).press(anchor, startPoint).waitAction(duration).moveTo(anchor, endPoint).release().perform();
		  }

		
}
