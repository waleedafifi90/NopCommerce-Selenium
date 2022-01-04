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
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

public class NopCommerce {

	private static int sku = Constant.sku;
	static String url = "https://admin-demo.nopcommerce.com/Admin";
	static WebDriver driver = new ChromeDriver();
	static Actions action = new Actions(driver);

	public static void main(String[] args) throws InterruptedException, ParseException {
		// TODO Auto-generated method stub

		// ========= Driver =========//
//		System.setProperty("webdriver.gecko.driver", "/opt/homebrew/bin/geckodriver");
//		System.setProperty("webdriver.chrome.driver", "/usr/local/bin/chromedriver");

		driver.get(url);
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		driver.manage().window().maximize();

		// ======== Start Login page =========/
		HelperFunction.validatePageOnLoad(driver, "Admin area demo", "login", Constant.loginPageTitle);

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

		boolean bodyElementOnLoad = driver.findElement(By.tagName("body")).getAttribute("class")
				.contains("sidebar-collapse");
		Assert.assertFalse(bodyElementOnLoad, "Check body element if it's not have the collapse aside page");

		WebElement usernameElement = driver.findElement(By.xpath("//nav[contains(@class, 'main-header')]/div/ul[1]"));
		Assert.assertTrue(usernameElement.getText().contains(Constant.usernameContent));

		WebElement aside = driver.findElement(By.cssSelector("aside"));
		Assert.assertTrue(aside.isDisplayed(), "Aside visibility");
		HelperFunction.validatePageOnLoad(driver, Constant.dashboardTitle, "Admin", Constant.dashboardPageTitle);

		navigateToProduct(driver, Constant.catalog);
		HelperFunction.checkActiveNavItem(driver, "Products");

		HelperFunction.checkLeftBarMenu(driver, Constant.catalog);

		HelperFunction.validatePageOnLoad(driver, Constant.productListTitle, "Product/List", Constant.productPageTitle);
//		isLoading(driver);

		WebElement addNewBtn = driver.findElement(By.xpath("//div[@class=\"content-wrapper\"]/form/div[1]//a"));
		Assert.assertEquals(addNewBtn.getText(), Constant.addNew);
		HelperFunction.isHover(addNewBtn, action, Constant.hoverButtonColor);
		HelperFunction.isActive(addNewBtn, action, Constant.activeButtonColor);
		addNewBtn.click();

		HelperFunction.validatePageOnLoad(driver, Constant.addProductTitle, "Product/Create",
				Constant.addNewProductPageTitle);
		HelperFunction.advanceMode(driver, false);
		HelperFunction.isAdvance(driver, false);
		// ==================== //
		HelperFunction.cardCollapse(driver, "product-info");
		// ==================== //

		HelperFunction.fillAndAssertField(driver, action, "Name", Constant.productName);
		HelperFunction.checkToolTip(driver, action, "Name", Constant.productNameTooltip);

		HelperFunction.fillAndAssertField(driver, action, "ShortDescription", Constant.productDescription);
		HelperFunction.checkToolTip(driver, action, "ShortDescription", Constant.productDescriptionTooltip);

		driver.switchTo().frame(driver.findElement(By.xpath("//iframe[@id='FullDescription_ifr']")));
		WebElement fullDescription = driver.findElement(By.tagName("body"));
		fullDescription.sendKeys(Constant.productDescription);
		Assert.assertEquals(fullDescription.getText(), Constant.productDescription);
		driver.switchTo().defaultContent();

		HelperFunction.fillAndAssertField(driver, action, "Sku", Integer.toString(sku));

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
			if (element.getText().trim().equals(Constant.selectedCategory)) {
				isContainComputer = true;
				System.out.println(element.getText());
			}
		}
		Assert.assertTrue(isContainComputer, "Check if the list contain Computer");

		// ==================== //
		HelperFunction.cardCollapse(driver, "product-price");
		// ==================== //

		WebElement priceField = driver.findElement(By.xpath("//input[@id=\"Price\"]/preceding-sibling::input"));
		Assert.assertTrue(priceField.getAttribute("title").contains("USD"));
		new Actions(driver).moveToElement(priceField).click().perform();

		WebElement price = driver.findElement(By.xpath("//input[@id=\"Price\"]"));
		price.sendKeys(Constant.price);
		Assert.assertTrue(price.getAttribute("value").contains(Constant.price));
//		HelperFunction.fillAndAssertField(driver, action, "Price", Constant.price);

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

		HelperFunction.validatePageOnLoad(driver, Constant.productListTitle, "Product/List", Constant.productPageTitle);

		HelperFunction.alertMessageChecker(driver, Constant.productSuccessAleart);
		HelperFunction.isLoading(driver);

		// ======================= Check if product added to the list ===== //
		HelperFunction.handleSearchBox(driver);

		searchForProduct(driver, action, Constant.productName, sku, Constant.price, Constant.quantity);

		// ============== Promotion ====================//
		HelperFunction.checkParentMenuItemOnHover(driver, action, Constant.promotion);

		WebElement promotionLink = driver
				.findElement(By.xpath("//aside//nav/ul/li/a/*[contains(text(),'Promotions')]/ancestor::a"));
		promotionLink.click();

		HelperFunction.checkNestedList(driver, Constant.promotion);

		WebElement discountLink = driver.findElement(By.linkText("Discounts"));
		discountLink.click();
		HelperFunction.checkActiveNavItem(driver, "Discounts");
		HelperFunction.checkLeftBarMenu(driver, Constant.promotion);
		HelperFunction.isLoading(driver);

		HelperFunction.validatePageOnLoad(driver, Constant.discountTitle, "Discount/List", Constant.discountPageTitle);

		WebElement addNewDiscount = driver.findElement(By.linkText(Constant.addNew));
		addNewDiscount.click();

		HelperFunction.validatePageOnLoad(driver, Constant.addDiscountTitle, "Discount/Create",
				Constant.addDiscountPageTitle);

		HelperFunction.cardCollapse(driver, "discount-info");
		HelperFunction.fillAndAssertField(driver, action, "Name", Constant.discountName);

		WebElement discountAmount = driver
				.findElement(By.xpath("//input[@id=\"DiscountAmount\"]/preceding-sibling::input"));
		new Actions(driver).moveToElement(discountAmount).click().perform();

		WebElement discount = driver.findElement(By.xpath("//input[@id=\"DiscountAmount\"]"));
		discount.sendKeys("10");
//		HelperFunction.fillAndAssertField(driver, action, "DiscountAmount", "10");

		// Select discount type
		WebElement discountType = driver.findElement(By.id("DiscountTypeId"));
		Select dropdownDiscountType = new Select(discountType);
		dropdownDiscountType.selectByValue("2");

		Assert.assertEquals(dropdownDiscountType.getFirstSelectedOption().getText(), "Assigned to products");

		// First date picker
		DatePicker.selectDate("2022", "January", "1", driver, "StartDateUtc_dateview", "StartDateUtc");

		// Second date picker
		DatePicker.selectDate("2022", "February", "24", driver, "EndDateUtc_dateview", "EndDateUtc");

		WebElement saveDiscountBtn = driver.findElement(By.name("save"));
		saveDiscountBtn.click();

		// Discount successfully added
		HelperFunction.validatePageOnLoad(driver, Constant.discountTitle, "Discount/List", Constant.discountPageTitle);
		HelperFunction.isLoading(driver);

		HelperFunction.alertMessageChecker(driver, Constant.discountAddedAlert);

		// Check if discount added using search form, then edit
		HelperFunction.handleSearchBox(driver);

		HelperFunction.fillAndAssertField(driver, action, "SearchDiscountName", Constant.discountName);

		WebElement discountSearchBtn = driver.findElement(By.id("search-discounts"));
//		HelperFunction.isActive(discountSearchBtn, action, Constant.activeButtonColor);
		discountSearchBtn.click();
		HelperFunction.isLoading(driver);

		Thread.sleep(1000);

		List<WebElement> tableData = driver.findElements(By.xpath("//table[@id=\"discounts-grid\"]//td"));
		Assert.assertEquals(tableData.get(0).getText(), Constant.discountName);

		WebElement editDiscountBtn = driver
				.findElement(By.xpath("//table[@id=\"discounts-grid\"]//td[contains(@class, 'button-column')]/a"));
		editDiscountBtn.click();

		HelperFunction.validatePageOnLoad(driver, Constant.editDiscountTitle, "Discount/Edit",
				Constant.editDiscountPageTitle);

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

				HelperFunction.fillAndAssertField(driver, action, "SearchProductName", Constant.productName);

				WebElement popSearchBtn = driver.findElement(By.id("search-products"));
				// isAction function not working here element is not attached to the page
				// document
//				isActive(popSearchBtn, action, activeButtonColor);
				popSearchBtn.click();

				Thread.sleep(500);
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
//		HelperFunction.isActive(saveEditDiscountBtn, action, Constant.activeButtonColor);
		saveEditDiscountBtn.click();

		HelperFunction.validatePageOnLoad(driver, Constant.discountTitle, "Discount/List", Constant.discountPageTitle);
		HelperFunction.isLoading(driver);

		HelperFunction.alertMessageChecker(driver, Constant.discountAlertUpdate);

		// Back to product page to check the discount
		navigateToProduct(driver, Constant.catalog);
		HelperFunction.checkActiveNavItem(driver, "Products");
		searchForProduct(driver, action, Constant.productName, sku, Constant.price, Constant.quantity);

		// Click on edit button for the product
		WebElement editProduct = driver.findElement(By.xpath("//table[@id='products-grid']//td[8]/a"));
		String editProductLink = editProduct.getAttribute("href");
		editProduct.click();

		HelperFunction.validatePageOnLoad(driver, Constant.editProductTitle, editProductLink,
				Constant.editProductPageTitle);
		HelperFunction.cardCollapse(driver, "product-price");

		HelperFunction.advanceMode(driver, true);
		HelperFunction.isAdvance(driver, true);
		WebElement discountList = driver.findElement(By.xpath("//ul[@id=\"SelectedDiscountIds_taglist\"]/li"));
		Assert.assertTrue(discountList.getText().contains(Constant.discountName));

		WebElement uploadFile = driver.findElement(By.cssSelector("[name=\"qqfile\"]"));
//		action.moveToElement(uploadFile).build().perform();

		uploadFile.sendKeys("/Users/waleedafifi/Desktop/speed.png");
		WebElement uploadedListItem = driver.findElement(By.cssSelector("div ul.qq-upload-list li"));

		WebDriverWait wait = new WebDriverWait(driver, 60);
		wait.until(ExpectedConditions.attributeContains(uploadedListItem, "class", "qq-upload-success"));

		WebElement addImageToProduct = driver.findElement(By.id("addProductPicture"));
		addImageToProduct.click();

		WebElement imageTable = driver.findElement(By.xpath("//table[@id='productpictures-grid']/tbody//img"));
		Assert.assertFalse(imageTable.getSize().equals(0));

		driver.close();

	}

	private static void searchForProduct(WebDriver driver, Actions action, String productName, int sku, String price,
			String quantity) throws InterruptedException {
		HelperFunction.fillAndAssertField(driver, action, "SearchProductName", productName);

		WebElement productSearchBtn = driver.findElement(By.id("search-products"));
//		HelperFunction.isHover(productSearchBtn, action, Constant.hoverButtonColor);
//		HelperFunction.isActive(productSearchBtn, action, Constant.activeButtonColor);
		productSearchBtn.click();
		Thread.sleep(1500);

		WebElement tableDataProductName = driver.findElement(By.xpath("//table[@id='products-grid']//td[3]"));
		action.moveToElement(tableDataProductName);
		Assert.assertEquals(tableDataProductName.getText(), productName);

		WebElement tableDataProductSKU = driver.findElement(By.xpath("//table[@id='products-grid']//td[4]"));
		Assert.assertEquals(tableDataProductSKU.getText(), Integer.toString(sku));

		WebElement tableDataProductPrice = driver.findElement(By.xpath("//table[@id='products-grid']//td[5]"));
		Assert.assertEquals(tableDataProductPrice.getText(), price);

		WebElement tableDataProductQuantity = driver.findElement(By.xpath("//table[@id='products-grid']//td[6]"));
		Assert.assertEquals(tableDataProductQuantity.getText(), quantity);
	}

	private static void navigateToProduct(WebDriver driver, String eleTitle) throws InterruptedException {
		HelperFunction.checkParentMenuItemOnHover(driver, action, Constant.catalog);
		WebElement catalogLink = driver
				.findElement(By.xpath("//aside//nav/ul/li/a/*[contains(text(),'Catalog')]/ancestor::a"));
		catalogLink.click();

		WebElement catalogListItem = driver
				.findElement(By.xpath("//aside//nav/ul/li/a/*[contains(text(),'Catalog')]/ancestor::li"));
		Thread.sleep(1000);
		Assert.assertTrue(catalogListItem.getAttribute("class").contains("menu-open"));

		WebElement arrow = driver
				.findElement(By.xpath("//aside//nav/ul/li/a/*[contains(text(),'Catalog')]/ancestor::a/p/i"));

		int angleInDegrees = 90;
		double angleInRadians = (angleInDegrees * Math.PI) / 180;
		double cos = Math.cos(angleInRadians);

		System.out.println(cos);
		System.out.println(arrow.getCssValue("transform"));

		HelperFunction.checkNestedList(driver, eleTitle);

		WebElement productLink = catalogLink.findElement(By.xpath("//aside//nav/ul/li/a/*[contains(text(),'Catalog')]"
				+ "/ancestor::a/following-sibling::ul/li/a/*[contains(text(), 'Products')]/" + "ancestor::a"));

		productLink.click();
	}

}
