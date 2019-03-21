/**
 * 
 */
package Instagram_demo.Instagram_demo;

import static org.testng.Assert.assertTrue;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.remote.MobilePlatform;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;

/**
 * @author TrungTH_CA
 *
 */
public class Instagram {

	String port;
	String id;
	String appPackage;
	
	AppiumDriver<WebElement> driver;
    DesiredCapabilities cap = new DesiredCapabilities();
	String line=" ========================================================= ";
	String comment="This comment is genreated by automation test";
	
	@AfterMethod
	public void teadDown(){
		Quit();
	}
	
	public void Quit(){
		String os=System.getProperty("os.name");
		String cmdWin="taskkill /F /IM node.exe";
		String cmdMac="killall -9 node";
		if(os.contains("Mac")) {
			FunctionsUtils.executeCommand(cmdMac);
		} else {
			FunctionsUtils.executeCommand(cmdWin);
		}
		
	}
	
	private void loginApp(String Accout, String Pass) {
		FunctionsUtils.sleep(5);
		WebElement btnlogin= FunctionsUtils.WaitPresentbyId(driver, "com.instagram.android:id/log_in_button");
		btnlogin.click();

		FunctionsUtils.sleep(5);
		WebElement txtusername= driver.findElement(By.id("com.instagram.android:id/login_username"));
		txtusername.sendKeys(Accout);
		
		WebElement txtpassword= driver.findElement(By.id("com.instagram.android:id/password"));
		txtpassword.sendKeys(Pass);
		
		WebElement btnsubmit= driver.findElement(By.id("com.instagram.android:id/button_text"));
		btnsubmit.click();
		
		WebElement tabBar= FunctionsUtils.presenceOfTheElementID(driver, "com.instagram.android:id/tab_bar");
//		String actual=lblWellcome.getAttribute("text");
//		String expected="Chào mừng bạn đến với Instagram";
		Assert.assertTrue(tabBar.isDisplayed());
		
	}
	
	private void installApp(String id, String port, String appPackage,String appActivity){
		/*File appdir = new File("src/main/resources/");
		File app = new File(appdir, "instagram47.apk");*/
		// Get version of device and uninstall appium setting and unlock if it is version 7
		String cmd = "adb -s " +id +" shell getprop ro.build.version.release";
		String osVersion = FunctionsUtils.executeCommand(cmd);

		if (osVersion.contains("7")) {
			// uninstall io.appium.settings
			cmd = "adb -s " +id + "  uninstall  io.appium.settings";
			FunctionsUtils.executeCommand(cmd);

			// uninstall io.appium.unlock
			cmd = "adb -s " +id + "  uninstall  io.appium.unlock";
			FunctionsUtils.executeCommand(cmd);
			
		}	
		
		cap.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 100000);
		cap.setCapability(MobileCapabilityType.PLATFORM_NAME, MobilePlatform.ANDROID);
		cap.setCapability(MobileCapabilityType.DEVICE_NAME, id);
		cap.setCapability(MobileCapabilityType.UDID, id);
		//cap.setCapability(MobileCapabilityType.APP, app);
		cap.setCapability(MobileCapabilityType.FULL_RESET, false);
		cap.setCapability(MobileCapabilityType.NO_RESET,true);
		cap.setCapability("appActivity", appActivity);
		//cap.setCapability(MobileCapabilityType.APP, "bookmyshow");
		cap.setCapability("automationName", "Appium");
		cap.setCapability("appPackage", appPackage);
		cap.setCapability("unicodeKeyboard", "true");                                     
		cap.setCapability("resetKeyboard", "true");
		try {
			AppiumDriverLocalService service = AppiumDriverLocalService.buildService(new AppiumServiceBuilder().usingPort(Integer.parseInt(port)));
			service.start();
			service.getUrl();
			
            driver = new AndroidDriver<WebElement>(new URL("http://0.0.0.0:" + port + "/wd/hub"), cap);
            driver.manage().timeouts().implicitlyWait(15000, TimeUnit.SECONDS);
            
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
	}
	
	 @Test(priority=1)
	 @Parameters({ "port","id","appPackage","appActivity","Accout", "Pass", "Contents"})
	 public void TC1_postNews(String port,String id, String appPackage,String appActivity,String Accout, String Pass,String Contents) {
		// run("4723", "192.168.242.102:5555","com.bt.bms");
		 System.out.println(line+"Opening App"+line);
		 installApp(id,port,appPackage,appActivity);
		 System.out.println(line+"Loging App"+line);
		// loginApp(Accout,Pass);
	
		 System.out.println(line+"Posting an artical"+line);
		 //post news
		 postNews(Contents);
    }
	
	 @Test(priority=2)
	 @Parameters({ "port","id","appPackage","appActivity","Accout", "Pass", "AccoutFollow", "ContentsLike"})
	 public void TC1_LikeNews(String port,String id, String appPackage,String appActivity,String Accout, String Pass,String AccoutFollow, String ContentsLike) {
		 System.out.println(line+" Test case 2 "+line);
		// run("4723", "192.168.242.102:5555","com.bt.bms");
		 System.out.println(line+"Opening App"+line);
		 installApp(id,port,appPackage,appActivity);
		/* System.out.println(line+"Loging App"+line);
		 loginApp(Accout,Pass);*/
	
		 System.out.println(line+"Like and comment on randomly post"+line);
		 likeRandom();
		 
		 System.out.println(line+"Search a News then like"+line);
		 likeNews(AccoutFollow, ContentsLike);
    }
	 
	 
	public void postNews(String content) {
		String xpath="//android.widget.FrameLayout[contains(@content-desc,'Camera') or contains(@content-desc,'Máy ảnh')]";
		FunctionsUtils.sleep(5);
		WebElement btncamera= FunctionsUtils.WaitPresentbyXpath(driver, xpath);
		btncamera.click();
		FunctionsUtils.sleep(5);
		try {
			FunctionsUtils.sendKeyEnter(driver);
			FunctionsUtils.sleep(5);
			//com.instagram.android:id/media_picker_grid_view
			
			WebElement btnNext= driver.findElement(By.id("com.instagram.android:id/next_button_textview"));
			btnNext.click();
			FunctionsUtils.WaitPresentbyId(driver, "com.instagram.android:id/creation_secondary_actions");
			
			btnNext=driver.findElement(By.id("com.instagram.android:id/next_button_textview"));
			btnNext.click();
			
			WebElement txtContent=FunctionsUtils.WaitPresentbyId(driver,"com.instagram.android:id/action_bar_textview_title");
			txtContent.sendKeys(content+" " + LocalDateTime.now().toString());
			FunctionsUtils.sleep(5);
			btnNext=driver.findElement(By.id("com.instagram.android:id/next_button_textview"));
			btnNext.click();
			
			FunctionsUtils.sleep(5);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		FunctionsUtils.sleep(5);
	 }
	 
	
	 public void likeNews(String account, String contentLike){
		System.out.println(account + contentLike);
		String xpath="//android.widget.FrameLayout[contains(@content-desc,'Search and explore')]";
		FunctionsUtils.sleep(5);
		WebElement btncamera= FunctionsUtils.WaitPresentbyXpath(driver, xpath);
		btncamera.click();
		FunctionsUtils.sleep(5);
		
		WebElement editSearch=FunctionsUtils.WaitPresentbyId(driver, "com.instagram.android:id/action_bar_search_edit_text");
		editSearch.click();
		
		//accept pop up
		try {
			FunctionsUtils.sendKeyEnter(driver);
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}
		FunctionsUtils.sleep(5);
		editSearch.sendKeys(account);
		
		
		//click to account follower
		WebElement viewFollower=FunctionsUtils.WaitPresentbyId(driver, "com.instagram.android:id/row_search_user_username");
		viewFollower.click();
		
		
		WebElement viewProfile=FunctionsUtils.WaitPresentbyId(driver, "com.instagram.android:id/profile_scoreboard_header");
		assertTrue(viewProfile.isDisplayed());
		FunctionsUtils.sleep(5);
		//change View type as list
		WebElement btnList=driver.findElement(By.id("com.instagram.android:id/layout_button_group_view_switcher_button_list"));
		btnList.click();
		FunctionsUtils.sleep(5);
		System.out.println(line + " Search " + contentLike + line);
		
		//FunctionsUtils.scrollToEle(driver,"com.instagram.android:id/row_feed_textview_comments", contentLike);
		FunctionsUtils.scrollToEle(driver,"com.instagram.ui.widget.textview.IgTextLayoutView", contentLike);
		FunctionsUtils.sleep(5);
		
		//click like if not yet
		WebElement btnLike= driver.findElement(By.id("com.instagram.android:id/row_feed_button_like"));
		if(btnLike.getAttribute("name").equals("Like")) {
			System.out.println(line+ " Clicking like "+line);
			btnLike.click();
		}
		FunctionsUtils.sleep(5);
		// click a post to comment
		driver.findElement(By.id("com.instagram.android:id/row_feed_button_comment")).click();
		FunctionsUtils.sleep(5);
		
		WebElement editThread=FunctionsUtils.WaitPresentbyId(driver, "com.instagram.android:id/layout_comment_thread_edittext");
		editThread.sendKeys(comment+" " + LocalDateTime.now().toString());
		
		WebElement btnPost=FunctionsUtils.WaitPresentbyId(driver, "com.instagram.android:id/layout_comment_thread_post_button");
		btnPost.click();
		
		FunctionsUtils.sleep(5);
		
		driver.findElement(By.id("com.instagram.android:id/action_bar_button_back")).click();
	 }
	 
	 public void likeRandom(){
			String xpath="//android.widget.FrameLayout[contains(@content-desc,'Search and explore')]";
			FunctionsUtils.sleep(5);
			WebElement btncamera= FunctionsUtils.WaitPresentbyXpath(driver, xpath);
			btncamera.click();
			FunctionsUtils.sleep(5);
			
			Random ran= new Random();
			int times =  ran.nextInt(5);
			
			//FunctionsUtils.scrollToEle(driver,"com.instagram.android:id/row_feed_textview_comments", contentLike);
			FunctionsUtils.scrollManytimes(driver, times);
			FunctionsUtils.sleep(5);
			driver.findElement(By.className("android.widget.ImageView")).click();
			FunctionsUtils.sleep(5);
			//click like if not yet
			WebElement btnLikeran= driver.findElement(By.id("com.instagram.android:id/row_feed_button_like"));
			if(btnLikeran.getAttribute("name").equals("Like")) {
				System.out.println(line+ " Clicking like "+line);
				btnLikeran.click();
				FunctionsUtils.sleep(5);
			}
			// click a post to comment
			driver.findElement(By.id("com.instagram.android:id/row_feed_button_comment")).click();
			FunctionsUtils.sleep(5);
			
			WebElement editThread=FunctionsUtils.WaitPresentbyId(driver, "com.instagram.android:id/layout_comment_thread_edittext");
			editThread.sendKeys(". " );
			
			WebElement btnPost=FunctionsUtils.WaitPresentbyId(driver, "com.instagram.android:id/layout_comment_thread_post_button");
			btnPost.click();
			
			FunctionsUtils.sleep(5);
			driver.findElement(By.id("com.instagram.android:id/action_bar_button_back")).click();
	}
	
	 
	@BeforeClass
	@Parameters({ "id", "ImgUrl" })
	public void beforeTest(String id, String ImgUrl) {
		System.out.println(line+"Preparing data"+line);
		FunctionsUtils.downloadImg(id,ImgUrl);
		System.out.println(line+"Restarting server"+line);
	}

}
