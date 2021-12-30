package firstSelemium;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

public class DatePicker {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public static void selectDate(String year, String monthName, String day, WebDriver driver,
			String cal, String calButton) throws ParseException, InterruptedException {
		
		WebElement calendar = driver
				.findElement(By.xpath("//input[@id='"+ calButton +"']/following-sibling::span/span[1]"));
		calendar.click();
		
		WebElement currentYear = driver.findElement(By.xpath("//div[@id='" + cal + "']//a[@aria-live='assertive']"));
		String curYear = currentYear.getAttribute("innerText");
		
		Thread.sleep(1000);
		
		if (!curYear.contains(year)) {
			do {
				driver.findElement(By.xpath("//div[@id='" + cal + "']//a[@aria-label=\"Next\"]")).click();
			} while (!driver.findElement(By.xpath("//div[@id='" + cal + "']//a[@aria-live='assertive']")).getAttribute("innerText")
					.contains(year));
		}
		
		WebElement curMonth = driver.findElement(By.xpath("//div[@id='" + cal + "']//a[@aria-live='assertive']"));
		String currentMonth = curMonth.getAttribute("innerText");
		
		if (!currentMonth.contains(monthName)) {
			do {
				driver.findElement(By.xpath("//div[@id='" + cal + "']//a[@aria-label=\"Next\"]")).click();
			} while (!driver.findElement(By.xpath("//div[@id='" + cal + "']//a[@aria-live='assertive']")).getAttribute("innerText")
					.contains(monthName));

		}

		List<WebElement> allDateOfDesiredMonth = driver
				.findElements(By.xpath("//div[@id='" + cal + "']//table/tbody//a"));
		for (WebElement d : allDateOfDesiredMonth) {
			if (d.getAttribute("innerText").equals(day)) {
				d.click();
				break;
			}
		}

	}
}
