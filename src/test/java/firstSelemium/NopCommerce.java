package firstSelemium;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

public class NopCommerce {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		
		
		
		// ===== Constant ===== //
		String productName = "PREMIUM CARE Cat & Dog Calming";
		String productDescription = "ADVANCED STRESS RELIEF FORMULA: Our pheromones";
		

		String url = "https://admin-demo.nopcommerce.com/Admin";
		WebDriver driver = new ChromeDriver();
		driver.get(url);
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		driver.manage().window().maximize();

		Assert.assertTrue(driver.getCurrentUrl().contains("login"));

		WebElement emailField = driver.findElement(By.cssSelector("input[id=\"Email\"]"));
		WebElement passwordField = driver.findElement(By.cssSelector("input[id=\"Password\"]"));

		WebElement loginBtn = driver.findElement(By.cssSelector("button[type=\"submit\"]"));
		loginBtn.click();

		Assert.assertTrue(driver.getCurrentUrl().contains("Admin"));

		WebElement aside = driver.findElement(By.cssSelector("aside"));

		Assert.assertTrue(aside.isDisplayed(), "Aside visibility");

		WebElement catalogLink = aside
				.findElement(By.xpath("//aside//nav/ul/li/a/*[contains(text(),'Catalog')]/ancestor::a"));
		catalogLink.click();

		WebElement productLink = catalogLink.findElement(By.xpath(
				"//aside//nav/ul/li/a/*[contains(text(),'Catalog')]/ancestor::a/following-sibling::ul/li/a/*[contains(text(), 'Products')]/ancestor::a"));
		productLink.click();

		Assert.assertTrue(driver.getCurrentUrl().contains("Product/List"));
		isLoading(driver);

		WebElement addNewBtn = driver.findElement(By.xpath("//div[@class=\"content-wrapper\"]/form/div[1]//a"));
		Assert.assertEquals(addNewBtn.getText(), "Add new");
		addNewBtn.click();

		WebElement addNewProductHeading = driver.findElement(By.xpath("//form[@id=\"product-form\"]//h1"));
		Assert.assertTrue(addNewProductHeading.getText().contains("Add a new product"));

		// ====================//
		cardCollapse(driver, "product-info");
		// ====================//

		WebElement productNameElement = driver.findElement(By.id("Name"));
		productNameElement.sendKeys(productName);
		Assert.assertEquals(productNameElement.getAttribute("value"), productName);

		WebElement shortDescription = driver.findElement(By.id("ShortDescription"));
		shortDescription.sendKeys(productDescription);
		Assert.assertEquals(shortDescription.getAttribute("value"), productDescription);

		driver.switchTo().frame(driver.findElement(By.xpath("//iframe[@id='FullDescription_ifr']")));
		WebElement fullDescription = driver.findElement(By.tagName("body"));
		fullDescription.sendKeys(productDescription);
		Assert.assertEquals(fullDescription.getText(), productDescription);
		driver.switchTo().defaultContent();

		WebElement sku = driver.findElement(By.name("Sku"));
		sku.sendKeys("123123123");
		Assert.assertEquals(sku.getAttribute("value"), "123123123");

		WebElement category = driver.findElement(By.xpath("//select[@id=\"SelectedCategoryIds\"]/parent::div"));
		category.click();

		List<WebElement> selectCategoryListItems = driver
				.findElements(By.xpath("//ul[@id=\"SelectedCategoryIds_listbox\"]/li"));
		selectCategoryListItems.get(0).click();

		WebElement selectedCategoryList = driver
				.findElement(By.xpath("//ul[@id='SelectedCategoryIds_taglist']//*[contains(text(), 'Computers')]"));
		Assert.assertEquals(selectCategoryListItems.get(0).getText(), "Computers");

		// ====================//
		cardCollapse(driver, "product-price");
		// ====================//

		WebElement priceField = driver.findElement(By.xpath("//input[@id=\"Price\"]/preceding-sibling::input"));
		new Actions(driver).moveToElement(priceField).click().perform();

		WebElement price = driver.findElement(By.xpath("//input[@id=\"Price\"]"));
		price.sendKeys("100");

		// ====================//
		cardCollapse(driver, "product-inventory");
		// ====================//

		WebElement manageInventoryMethodId = driver.findElement(By.id("ManageInventoryMethodId"));
		Select dropdown = new Select(manageInventoryMethodId);
		dropdown.selectByValue("2");

		Assert.assertEquals(dropdown.getFirstSelectedOption().getText(), "Track inventory by product attributes");

		WebElement saveProductBtn = driver.findElement(By.name("save"));
		saveProductBtn.click();

		Assert.assertTrue(driver.getCurrentUrl().contains("Product/List"));

		WebElement successAlert = driver.findElement(By.className("alert-success"));
		System.out.println(successAlert.getCssValue("background-color"));
		boolean isSuccess = (successAlert.getCssValue("background-color").equals("rgba(23, 183, 109, 1)"));
		Assert.assertTrue(isSuccess, "Check the alert back color");

		boolean isAlertContainText = successAlert.getText().contains("The new product has been added successfully.");
		System.out.println(successAlert.getText());
		Assert.assertTrue(isAlertContainText, "Check the alert content");
		isLoading(driver);
		
		//======================= Check if product added to the list ===== //
		WebElement productSearchNameField = driver.findElement(By.id("SearchProductName"));
		

		// ============== Promotion ====================//

		WebElement promotionLink = driver
				.findElement(By.xpath("//aside//nav/ul/li/a/*[contains(text(),'Promotions')]/ancestor::a"));
		promotionLink.click();

		WebElement discountLink = driver.findElement(By.linkText("Discounts"));
		discountLink.click();

		Assert.assertTrue(driver.getCurrentUrl().contains("Discount/List"));
		isLoading(driver);

		WebElement discountHeadingTitle = driver
				.findElement(By.xpath("//div[contains(@class, \"content-header\")]/h1"));
//		WebElement discountHeadingTitle = driver.findElement(By.tagName("h1"));
		System.out.println(discountHeadingTitle.getText());
		Assert.assertTrue(discountHeadingTitle.getText().contains("Discounts"));

		WebElement addNewDiscount = driver.findElement(By.linkText("Add new"));
		addNewDiscount.click();

		Assert.assertTrue(driver.getCurrentUrl().contains("Discount/Create"));

//		WebElement newDiscountHeadingTitle = driver.findElement(By.tagName("h1"));
		WebElement newDiscountHeadingTitle = driver
				.findElement(By.xpath("//div[contains(@class, \"content-header\")]/h1"));
		Assert.assertTrue(newDiscountHeadingTitle.getText().contains("Add a new discount"));

		WebElement discountName = driver.findElement(By.id("Name"));
		discountName.sendKeys("New Year Discount");
		Assert.assertEquals(discountName.getAttribute("value"), "New Year Discount");

		WebElement discountAmount = driver
				.findElement(By.xpath("//input[@id=\"DiscountAmount\"]/preceding-sibling::input"));
		new Actions(driver).moveToElement(discountAmount).click().perform();

		WebElement discount = driver.findElement(By.xpath("//input[@id=\"DiscountAmount\"]"));
		discount.sendKeys("10");

		// Select discount type
		WebElement discountType = driver.findElement(By.id("DiscountTypeId"));
		Select dropdownDiscountType = new Select(discountType);
		dropdownDiscountType.selectByValue("2");

		Assert.assertEquals(dropdownDiscountType.getFirstSelectedOption().getText(), "Assigned to products");

		// First date picker
		WebElement startDatePickerDateSelect = driver
				.findElement(By.xpath("//input[@id=\"StartDateUtc\"]/following-sibling::span/span[1]"));
		startDatePickerDateSelect.click();

		WebElement nextMonth = driver
				.findElement(By.xpath("//div[@id=\"StartDateUtc_dateview\"]//a[@aria-label=\"Next\"]"));
		nextMonth.click();

		WebElement firstDate = driver
				.findElement(By.xpath("//div[@id=\"StartDateUtc_dateview\"]//a[@data-value=\"2022/0/1\"]"));
		firstDate.click();

		// Second date picker
		WebElement endDatePickerDateSelect = driver
				.findElement(By.xpath("//input[@id=\"EndDateUtc\"]/following-sibling::span/span[1]"));
		endDatePickerDateSelect.click();

		WebElement nextMonth1 = driver
				.findElement(By.xpath("//div[@id=\"EndDateUtc_dateview\"]//a[@aria-label=\"Next\"]"));
		nextMonth1.click();

		WebElement endDate = driver
				.findElement(By.xpath("//div[@id=\"EndDateUtc_dateview\"]//a[@data-value=\"2022/0/10\"]"));
		endDate.click();

		WebElement saveDiscountBtn = driver.findElement(By.name("save"));
		saveDiscountBtn.click();

		// Discount successfully added
		Assert.assertTrue(driver.getCurrentUrl().contains("Discount/List"));
		isLoading(driver);

		WebElement discountSuccessAlert = driver.findElement(By.className("alert-success"));
		System.out.println(discountSuccessAlert.getCssValue("background-color"));
		boolean isDiscountSuccess = (discountSuccessAlert.getCssValue("background-color")
				.equals("rgba(23, 183, 109, 1)"));
		Assert.assertTrue(isDiscountSuccess, "Check the alert back color");

		boolean isDiscountAlertContainText = discountSuccessAlert.getText()
				.contains("The new discount has been added successfully.");
		System.out.println(discountSuccessAlert.getText());
		Assert.assertTrue(isDiscountAlertContainText, "Check the alert content");

		// Check if discount added using search form, then edit
		WebElement searchDiscountName = driver.findElement(By.id("SearchDiscountName"));
		searchDiscountName.sendKeys("New Year Discount");
		Assert.assertEquals(searchDiscountName.getAttribute("value"), "New Year Discount");

		WebElement discountSearchBtn = driver.findElement(By.id("search-discounts"));
		discountSearchBtn.click();
		isLoading(driver);

		List<WebElement> tableData = driver.findElements(By.xpath("//table[@id=\"discounts-grid\"]//td"));
		Assert.assertEquals(tableData.get(0).getText(), "New Year Discount");

		WebElement editDiscountBtn = driver
				.findElement(By.xpath("//table[@id=\"discounts-grid\"]//td[contains(@class, 'button-column')]/a"));
		editDiscountBtn.click();

		Assert.assertTrue(driver.getCurrentUrl().contains("Discount/Edit"));

		cardCollapse(driver, "discount-applied-to-products");

		WebElement addProductDiscount = driver.findElement(By.id("btnAddNewProduct"));
		addProductDiscount.click();

		// ===================== popup windows ================//
		String mwh = driver.getWindowHandle();
		Set s = driver.getWindowHandles(); // this method will gives you the handles of all opened windows
		Iterator ite = s.iterator();

		while (ite.hasNext()) {
			String popupHandle = ite.next().toString();
			if (!popupHandle.contains(mwh)) {
				driver.switchTo().window(popupHandle);

				WebElement popSearchProductName = driver.findElement(By.id("SearchProductName"));
				popSearchProductName.sendKeys(productName);

				WebElement popSearchBtn = driver.findElement(By.id("search-products"));
				popSearchBtn.click();

				WebElement productsTable = driver.findElement(By.xpath("//table[@id='products-grid']//td[2]"));

				WebElement checkboxElement = driver.findElement(By.name("SelectedProductIds"));
				checkboxElement.click();

				WebElement saveToDiscountBtn = driver.findElement(By.name("save"));

				driver.switchTo().window(mwh);
			}
		}

		// ================= Check if product added to discount =========//
		WebElement productDiscountTable = driver.findElement(By.xpath("//table[@id='products-grid']/tbody/tr/td[1]"));
		Assert.assertEquals(productDiscountTable.getText(), productName);

	}

	// Check collapsed div
	public static void cardCollapse(WebDriver driver, String divName) {
		WebElement ele = driver.findElement(By.id(divName));
		if (ele.getAttribute("class").contains("collapsed-card")) {
			ele.click();
		}
	}

	public static void isLoading(WebDriver driver) {
		WebElement loader = driver.findElement(By.id("ajaxBusy"));
		Assert.assertTrue(loader.getCssValue("display").equals("block"));
	}

}
