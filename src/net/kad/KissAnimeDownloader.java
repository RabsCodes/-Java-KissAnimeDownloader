package net.kad;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import net.kad.io.TextReader;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class KissAnimeDownloader {

	public static void main(String[] args) throws Exception {
//		Downloader downloader = new Downloader("http://kissanime.com/");
		System.setProperty("webdriver.chrome.driver", "./chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		WebDriverWait wait = new WebDriverWait(driver, 30, 1000);
		driver.get("http://kissanime.com/Login");
		//ID・passの読み込み
		String[] account = new TextReader(new File("account.txt")).read();
		if (account == null || account.length < 2)
			throw new Exception();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")));
		driver.findElement(By.id("username")).sendKeys(account[0]);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("password")));
		driver.findElement(By.id("password")).sendKeys(account[1]);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("btnSubmit")));
		driver.findElement(By.id("btnSubmit")).click();

		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		String[] animeURL = new TextReader(new File("url.txt")).read();
		if (account == null || account.length < 1)
			throw new Exception();
		driver.get(animeURL[0]);
		wait.until(ExpectedConditions.visibilityOfElementLocated(/*By.linkText("Click here to download all")*/By.partialLinkText("Episode")));
		List<WebElement> list = driver.findElements(By.partialLinkText("Episode"));
		ArrayList<String> urls = new ArrayList<String>();
		System.out.println("Episode List");
		for (WebElement element : list) {
			System.out.println(element.getText());
			urls.add(element.getAttribute("href"));
		}
		String urlList = new String();
		ListIterator<String> iterator = urls.listIterator(urls.size());;
		while (iterator.hasPrevious()) {
			String url = iterator.previous();
			System.out.println(url);
			driver.get(url);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.partialLinkText("mp4")));
			list = driver.findElements(By.partialLinkText("mp4"));
			//一番最初の要素が最も高画質の動画のリンクとなっている
			WebElement element = list.get(0);
			urlList += element.getAttribute("href") + "\n";
			System.out.println("[" + element.getText() + "]" + element.getAttribute("href"));
			//全部の画質のリンクを表示する場合
			/*
			for (WebElement element : list) {
				System.out.println("[" + element.getText() + "]" + element.getAttribute("href"));
			}
			*/
		}
		driver.quit();
		try {
			FileWriter fileWriter = new FileWriter(new File("URLs.txt"));
			fileWriter.write(urlList);
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

}
