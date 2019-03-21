package Instagram_demo.Instagram_demo;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	//WebDriver driver = new FirefoxDriver();
        System.out.println( "Hello World!" );
        

        URL imageURL;
		try {
			imageURL = new URL("https://pbs.twimg.com/profile_images/461167614/Icon_hirez.jpg");
			BufferedImage saveImage = ImageIO.read(imageURL);
			String u="https://pbs.twimg.com/profile_images/461167614/Icon_hirez.jpg";
			String []arr=null;
			if(u.contains("/")) {
				arr=u.split("/");
			} else {
				arr=u.split("\\");
			}
			
	        ImageIO.write(saveImage, "png", new File(arr[arr.length-1]));

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
                
    }
}
