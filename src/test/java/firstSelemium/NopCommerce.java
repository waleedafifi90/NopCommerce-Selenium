package firstSelemium;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class NopCommerce {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String url = "https://admin-demo.nopcommerce.com/Admin";
		WebDriver driver = new ChromeDriver();
		driver.get(url);
		driver.manage().window().maximize();
		
		
	}

}
