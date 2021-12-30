package firstSelemium;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
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

	public static void main(String[] args) throws InterruptedException, ParseException {
		// TODO Auto-generated method stub

		int sku = Constant.sku;
		
		// ========= Driver =========//
		String url = "https://admin-demo.nopcommerce.com/Admin";
		WebDriver driver = new ChromeDriver();
		driver.get(url);
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		driver.manage().window().maximize();

		// ======== Action trigger =========/
		Actions action = new Actions(driver);

		// ======== Start Login page =========/
		Assert.assertTrue(driver.getCurrentUrl().contains("login"));

		WebElement emailField = driver.findElement(By.cssSelector("input[id=\"Email\"]"));
		emailField.clear();
		emailField.sendKeys(Constant.email);
		Assert.assertFalse(emailField.getAttribute("value").isEmpty());

		WebElement passwordField = driver.findElement(By.cssSelector("input[id=\"Password\"]"));
		passwordField.clear();
		passwordField.sendKeys(Constant.password);
		Assert.assertFalse(passwordField.getAttribute("value").isEmpty());

		WebElement loginBtn = driver.findElement(By.cssSelector("button[type=\"submit\"]"));
		action.moveToElement(loginBtn).build().perform();
		Thread.sleep(1500);
		System.out.println(loginBtn.getCssValue("background-color"));
		Assert.assertTrue(loginBtn.getCssValue("background-color").equals("rgba(36, 142, 206, 1)"));
		loginBtn.click();

		// ======== Start Product page =========/
		WebElement usernameElement = driver.findElement(By.xpath("//nav[contains(@class, 'main-header')]/div/ul[1]"));
		Assert.assertTrue(usernameElement.getText().contains(Constant.usernameContent));

		Assert.assertTrue(driver.getCurrentUrl().contains("Admin"));

		WebElement aside = driver.findElement(By.cssSelector("aside"));
		Assert.assertTrue(aside.isDisplayed(), "Aside visibility");
		HelperFunction.isHeading(driver, Constant.dashboardTitle);

		navigateToProduct(driver, Constant.catalog);

		Assert.assertTrue(driver.getCurrentUrl().contains("Product/List"));
		HelperFunction.isHeading(driver, Constant.productListTitle);
//		isLoading(driver);

		WebElement addNewBtn = driver.findElement(By.xpath("//div[@class=\"content-wrapper\"]/form/div[1]//a"));
		Assert.assertEquals(addNewBtn.getText(), Constant.addNew);
		HelperFunction.isHover(addNewBtn, action, Constant.hoverButtonColor);
		HelperFunction.isActive(addNewBtn, action, Constant.activeButtonColor);
		addNewBtn.click();

		HelperFunction.isHeading(driver, Constant.addProductTitle);

		// ==================== //
		HelperFunction.cardCollapse(driver, "product-info");
		// ==================== //

		WebElement productNameElement = driver.findElement(By.id("Name"));
		HelperFunction.checkToolTip(driver, action, "Name", Constant.productNameTooltip);
		productNameElement.sendKeys(Constant.productName);
		Assert.assertEquals(productNameElement.getAttribute("value"), Constant.productName);

		WebElement shortDescription = driver.findElement(By.id("ShortDescription"));
		HelperFunction.checkToolTip(driver, action, "ShortDescription", Constant.productDescriptionTooltip);
		shortDescription.sendKeys(Constant.productDescription);
		Assert.assertEquals(shortDescription.getAttribute("value"), Constant.productDescription);

		driver.switchTo().frame(driver.findElement(By.xpath("//iframe[@id='FullDescription_ifr']")));
		WebElement fullDescription = driver.findElement(By.tagName("body"));
		fullDescription.sendKeys(Constant.productDescription);
		Assert.assertEquals(fullDescription.getText(), Constant.productDescription);
		driver.switchTo().defaultContent();

		WebElement skuElement = driver.findElement(By.name("Sku"));
		skuElement.sendKeys(Integer.toString(sku));
		Assert.assertEquals(skuElement.getAttribute("value"), Integer.toString(sku));

		WebElement category = driver.findElement(By.xpath("//select[@id=\"SelectedCategoryIds\"]/parent::div"));
		category.click();

		List<WebElement> selectCategoryListItems = driver
				.findElements(By.xpath("//ul[@id=\"SelectedCategoryIds_listbox\"]/li"));
		for (WebElement element : selectCategoryListItems) {
			System.out.println(element.getText());
			if (element.getText().equals(Constant.selectedCategory)) {
				element.click();
			}
		}

		List<WebElement> selectedCategoryList = driver
				.findElements(By.xpath("//ul[@id='SelectedCategoryIds_taglist']/li"));

		boolean isContainComputer = false;
		for (WebElement element : selectedCategoryList) {
			if (element.getText().equals(Constant.selectedCategory)) {
				isContainComputer = true;
				System.out.println(element.getText());
			}
		}
		Assert.assertTrue(isContainComputer, "Check if the list contain Computer");

		// ==================== //
		HelperFunction.cardCollapse(driver, "product-price");
		// ==================== //

		WebElement priceField = driver.findElement(By.xpath("//input[@id=\"Price\"]/preceding-sibling::input"));
		new Actions(driver).moveToElement(priceField).click().perform();

		WebElement price = driver.findElement(By.xpath("//input[@id=\"Price\"]"));
		price.sendKeys(Constant.price);

		// ====================//
		HelperFunction.cardCollapse(driver, "product-inventory");
		// ====================//

		WebElement manageInventoryMethodId = driver.findElement(By.id("ManageInventoryMethodId"));
		Select dropdown = new Select(manageInventoryMethodId);
		dropdown.selectByValue(Constant.inventoryOption);

		Assert.assertEquals(dropdown.getFirstSelectedOption().getText(), Constant.inventoryMethod);

		WebElement saveProductBtn = driver.findElement(By.name("save"));
		HelperFunction.isHover(saveProductBtn, action, Constant.hoverButtonColor);
		HelperFunction.isActive(saveProductBtn, action, Constant.activeButtonColor);
		saveProductBtn.click();

		Assert.assertTrue(driver.getCurrentUrl().contains("Product/List"));
		HelperFunction.isHeading(driver, Constant.productListTitle);

		WebElement successAlert = driver.findElement(By.className("alert-success"));
		System.out.println(successAlert.getCssValue("background-color"));
		boolean isSuccess = (successAlert.getCssValue("background-color").equals("rgba(23, 183, 109, 1)"));
		Assert.assertTrue(isSuccess, "Check the alert back color");

		boolean isAlertContainText = successAlert.getText().contains(Constant.productSuccessAleart);
		System.out.println(successAlert.getText());
		Assert.assertTrue(isAlertContainText, "Check the alert content");
		HelperFunction.isLoading(driver);

		// ======================= Check if product added to the list ===== //
		HelperFunction.handleSearchBox(driver);

		searchForProduct(driver, action, Constant.productName, sku, Constant.price, Constant.quantity);

		// ============== Promotion ====================//

		WebElement promotionLink = driver
				.findElement(By.xpath("//aside//nav/ul/li/a/*[contains(text(),'Promotions')]/ancestor::a"));
		promotionLink.click();

		HelperFunction.checkNestedList(driver, Constant.promotion);

		WebElement discountLink = driver.findElement(By.linkText("Discounts"));
		discountLink.click();

		Assert.assertTrue(driver.getCurrentUrl().contains("Discount/List"));
		HelperFunction.isLoading(driver);

		HelperFunction.isHeading(driver, Constant.discountTitle);

		WebElement addNewDiscount = driver.findElement(By.linkText(Constant.addNew));
		addNewDiscount.click();

		Assert.assertTrue(driver.getCurrentUrl().contains("Discount/Create"));
		HelperFunction.isHeading(driver, Constant.addDiscountTitle);

		HelperFunction.cardCollapse(driver, "discount-info");
		WebElement discountNameElement = driver.findElement(By.id("Name"));
		discountNameElement.sendKeys(Constant.discountName);
		Assert.assertEquals(discountNameElement.getAttribute("value"), Constant.discountName);

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
		DatePicker.selectDate("2021", "December", "31", driver, "StartDateUtc_dateview", "StartDateUtc");

		// Second date picker
		DatePicker.selectDate("2022", "February", "28", driver, "EndDateUtc_dateview", "EndDateUtc");

		
		WebElement saveDiscountBtn = driver.findElement(By.name("save"));
		saveDiscountBtn.click();

		// Discount successfully added
		Assert.assertTrue(driver.getCurrentUrl().contains("Discount/List"));
		HelperFunction.isLoading(driver);

		WebElement discountSuccessAlert = driver.findElement(By.className("alert-success"));
		System.out.println(discountSuccessAlert.getCssValue("background-color"));
		boolean isDiscountSuccess = (discountSuccessAlert.getCssValue("background-color")
				.equals("rgba(23, 183, 109, 1)"));
		Assert.assertTrue(isDiscountSuccess, "Check the alert back color");

		boolean isDiscountAlertContainText = discountSuccessAlert.getText().contains(Constant.discountAddedAlert);
		System.out.println(discountSuccessAlert.getText());
		Assert.assertTrue(isDiscountAlertContainText, "Check the alert content");

		// Check if discount added using search form, then edit
		HelperFunction.handleSearchBox(driver);

		WebElement searchDiscountName = driver.findElement(By.id("SearchDiscountName"));
		searchDiscountName.sendKeys(Constant.discountName);
		Assert.assertEquals(searchDiscountName.getAttribute("value"), Constant.discountName);

		WebElement discountSearchBtn = driver.findElement(By.id("search-discounts"));
//		isActive(discountSearchBtn, action, activeButtonColor);
		discountSearchBtn.click();
		HelperFunction.isLoading(driver);

		Thread.sleep(1000);

		List<WebElement> tableData = driver.findElements(By.xpath("//table[@id=\"discounts-grid\"]//td"));
		Assert.assertEquals(tableData.get(0).getText(), Constant.discountName);

		WebElement editDiscountBtn = driver
				.findElement(By.xpath("//table[@id=\"discounts-grid\"]//td[contains(@class, 'button-column')]/a"));
		editDiscountBtn.click();

		Assert.assertTrue(driver.getCurrentUrl().contains("Discount/Edit"));
		HelperFunction.isHeading(driver, Constant.editDiscountTitle);

		HelperFunction.cardCollapse(driver, "discount-applied-to-products");

		WebElement addProductDiscount = driver.findElement(By.id("btnAddNewProduct"));
		addProductDiscount.click();

		// ===================== popup windows ================//
		String mainWindowHandle = driver.getWindowHandle();
		Set allWindowHandles = driver.getWindowHandles(); // this method will gives you the handles of all opened
															// windows
		Iterator iterator = allWindowHandles.iterator();

		while (iterator.hasNext()) {
			String popupHandle = iterator.next().toString();
			if (!popupHandle.contains(mainWindowHandle)) {
				driver.switchTo().window(popupHandle);

				WebElement popSearchProductName = driver.findElement(By.id("SearchProductName"));
				popSearchProductName.sendKeys(Constant.productName);

				WebElement popSearchBtn = driver.findElement(By.id("search-products"));
				// isAction function not working here element is not attached to the page
				// document
//				isActive(popSearchBtn, action, activeButtonColor);
				popSearchBtn.click();

				WebElement productsTable = driver.findElement(By.xpath("//table[@id='products-grid']//td[2]"));

				WebElement checkboxElement = driver.findElement(By.name("SelectedProductIds"));
				checkboxElement.click();

				WebElement saveToDiscountBtn = driver.findElement(By.name("save"));
				saveToDiscountBtn.click();

				driver.switchTo().window(mainWindowHandle);
			}
		}

		// ================= Check if product added to discount =========//
		Thread.sleep(1000);
		WebElement productDiscountTable = driver.findElement(By.xpath("//table[@id='products-grid']/tbody/tr/td[1]"));
		Assert.assertEquals(productDiscountTable.getText(), Constant.productName);

		WebElement saveEditDiscountBtn = driver.findElement(By.name("save"));
//		isHover(saveDiscountBtn, action, hoverButtonColor);
		HelperFunction.isActive(saveEditDiscountBtn, action, Constant.activeButtonColor);
		saveEditDiscountBtn.click();

		Assert.assertTrue(driver.getCurrentUrl().contains("Discount/List"));
		HelperFunction.isLoading(driver);

		WebElement discountUpdateSuccessAlert = driver.findElement(By.className("alert-success"));
		System.out.println(discountUpdateSuccessAlert.getCssValue("background-color"));
		boolean isDiscountUpdatedSuccess = (discountUpdateSuccessAlert.getCssValue("background-color")
				.equals("rgba(23, 183, 109, 1)"));
		Assert.assertTrue(isDiscountUpdatedSuccess, "Check the alert back color");

		boolean isDiscountUpdatedAlertContainText = discountUpdateSuccessAlert.getText().contains(Constant.discountAlertUpdate);
		System.out.println(discountUpdateSuccessAlert.getText());
		Assert.assertTrue(isDiscountUpdatedAlertContainText, "Check the alert content");

		// Back to product page to check the discount
		navigateToProduct(driver, Constant.catalog);
		searchForProduct(driver, action, Constant.productName, sku, Constant.price, Constant.quantity);

		// Click on edit button for the product
		WebElement editProduct = driver.findElement(By.xpath("//table[@id='products-grid']//td[8]/a"));
		editProduct.click();
		HelperFunction.isHeading(driver, Constant.editProductTitle);

		HelperFunction.cardCollapse(driver, "product-price");

		WebElement bodyElement = driver.findElement(By.xpath("//body"));
		boolean bodyClass = bodyElement.getAttribute("class").contains("basic-settings-mode");
		if (bodyClass) {
			WebElement advance = driver.findElement(By.xpath("//label[@for=\"advanced-settings-mode\"]"));
			advance.click();

			WebElement discountList = driver.findElement(By.xpath("//ul[@id=\"SelectedDiscountIds_taglist\"]/li"));
			Assert.assertTrue(discountList.getText().contains(Constant.discountName));
		}

		driver.close();

	}

	private static void searchForProduct(WebDriver driver, Actions action, String productName, int sku, String price, String quantity) throws InterruptedException {
		// TODO Auto-generated method stub
		WebElement productSearchNameField = driver.findElement(By.id("SearchProductName"));
		productSearchNameField.sendKeys(productName);
		Assert.assertEquals(productSearchNameField.getAttribute("value"), productName);

		WebElement productSearchBtn = driver.findElement(By.id("search-products"));
		HelperFunction.isHover(productSearchBtn, action, Constant.hoverButtonColor);
		HelperFunction.isActive(productSearchBtn, action, Constant.activeButtonColor);
		productSearchBtn.click();
		Thread.sleep(1500);
		
		WebElement tableDataProductName = driver.findElement(By.xpath("//table[@id='products-grid']//td[3]"));
		Assert.assertEquals(tableDataProductName.getText(), productName);
		
//		WebElement tableDataProductSKU = driver.findElement(By.xpath("//table[@id='products-grid']//td[4]"));
//		Assert.assertEquals(tableDataProductSKU.getText(), Integer.toString(sku));
		
		WebElement tableDataProductPrice = driver.findElement(By.xpath("//table[@id='products-grid']//td[5]"));
		Assert.assertEquals(tableDataProductPrice.getText(), price);

		WebElement tableDataProductQuantity = driver.findElement(By.xpath("//table[@id='products-grid']//td[6]"));		
		Assert.assertEquals(tableDataProductQuantity.getText(), quantity);
	}

	private static void navigateToProduct(WebDriver driver, String eleTitle) throws InterruptedException {
		WebElement catalogLink = driver
				.findElement(By.xpath("//aside//nav/ul/li/a/*[contains(text(),'Catalog')]/ancestor::a"));
		catalogLink.click();

		WebElement catalogListItem = driver
				.findElement(By.xpath("//aside//nav/ul/li/a/*[contains(text(),'Catalog')]/ancestor::li"));
		Thread.sleep(1000);
		Assert.assertTrue(catalogListItem.getAttribute("class").contains("menu-open"));

		WebElement arrow = driver
				.findElement(By.xpath("//aside//nav/ul/li/a/*[contains(text(),'Catalog')]/ancestor::a/p/i"));
		
//		System.out.println(arrow.getCssValue("transform"));
//		System.out.println(Math.cos(90*Math.PI/180));
		
		HelperFunction.checkNestedList(driver, eleTitle);

		WebElement productLink = catalogLink.findElement(By.xpath("//aside//nav/ul/li/a/*[contains(text(),'Catalog')]"
				+ "/ancestor::a/following-sibling::ul/li/a/*[contains(text(), 'Products')]/" + "ancestor::a"));

		productLink.click();
	}


}
