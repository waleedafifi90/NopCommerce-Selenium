package firstSelemium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;

public class NopCommerce {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String url = "https://admin-demo.nopcommerce.com/Admin";
		WebDriver driver = new ChromeDriver();
		driver.get(url);
		driver.manage().window().maximize();
		
		Assert.assertTrue(driver.getCurrentUrl().contains("login"));
		
		WebElement emailField = driver.findElement(By.cssSelector("input[id=\"Email\"]"));
		WebElement passwordField = driver.findElement(By.cssSelector("input[id=\"Password\"]"));
		
		WebElement loginBtn = driver.findElement(By.cssSelector("button[type=\"submit\"]"));
		loginBtn.click();
		
		Assert.assertTrue(driver.getCurrentUrl().contains("Admin"));
		
		WebElement aside = driver.findElement(By.cssSelector("aside"));
		
		Assert.assertTrue(aside.isDisplayed(), "Aside visibility");
		
		WebElement catalogLink = aside.findElement(By.xpath("//aside//nav/ul/li/a/*[contains(text(),'Catalog')]/ancestor::a"));
		catalogLink.click();
		
	}

}
