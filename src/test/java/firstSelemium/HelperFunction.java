package firstSelemium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;

public class HelperFunction {
	public static void isHeading(WebDriver driver, String title) {
		WebElement headingTag = driver.findElement(By.xpath("//div[contains(@class, 'content-header')]//h1"));
		Assert.assertTrue(headingTag.getText().contains(title));
	}

	public static void checkToolTip(WebDriver driver, Actions action, String ele, String title) {
		WebElement element = driver.findElement(By.xpath("//label[@for='" + ele + "']/following-sibling::div"));
		action.moveToElement(element).build().perform();
		Assert.assertTrue(element.getAttribute("data-original-title").contains(title), "Tooltip for " + ele);
	}

	// Check of loader appears
	public static void isLoading(WebDriver driver) {
		WebElement loader = driver.findElement(By.id("ajaxBusy"));
		Assert.assertTrue(loader.getCssValue("display").equals("block"));
	}

	// Check if active style effected on links
	public static void isActive(WebElement ele, Actions action, String eq) throws InterruptedException {
		action.clickAndHold(ele).build().perform();
		Thread.sleep(1500);
		System.out.println(ele.getCssValue("background-color"));
		Assert.assertTrue(ele.getCssValue("background-color").equals(eq));

	}

	public static void isHover(WebElement ele, Actions action, String eq) throws InterruptedException {
		action.moveToElement(ele).build().perform();
		Thread.sleep(1500);
		System.out.println(ele.getCssValue("background-color"));
		Assert.assertTrue(ele.getCssValue("background-color").equals(eq));

	}

	public static void checkNestedList(WebDriver driver, String listName) {
		WebElement ele = driver.findElement(By.xpath(
				"//aside//nav/ul/li/a/*[contains(text(),'" + listName + "')]/ancestor::a/following-sibling::ul"));
		Assert.assertTrue(ele.getCssValue("display").equals("block"));

	}

	// Check collapsed div
	public static void cardCollapse(WebDriver driver, String divName) {
		WebElement ele = driver.findElement(By.id(divName));
		if (ele.getAttribute("class").contains("collapsed-card")) {
			ele.click();
			WebElement collapseBtn = driver
					.findElement(By.xpath("//div[@id='" + divName + "']//div[contains(@class, \"card-tools\")]//i"));
			Assert.assertTrue(collapseBtn.getAttribute("class").contains("fa-minus"));

			WebElement cardBody = driver
					.findElement(By.xpath("//div[@id='" + divName + "']//div[contains(@class, \"card-body\")]"));
			Assert.assertTrue(cardBody.getCssValue("display").contains("block"));
		}
	}

	// Handle search box expanding
	public static void handleSearchBox(WebDriver driver) {
		WebElement ele = driver.findElement(By.xpath("//div[contains(@class, 'card-search')]/div/div[1]"));
		if (!ele.getAttribute("class").contains("opened")) {
			ele.click();
		}
	}
}
