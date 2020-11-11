package com.nba.stats;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Scanner;

public class NbaStatsScrape {

    public static void main(String[] args) throws InterruptedException {


        // Scanner Scan = new Scanner(System.in);
        // System.out.println("Enter player name: ");

        // String imeIgralca = Scan.nextLine();



        FirefoxDriver driver;

        final FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions.setHeadless(false);
        driver = new FirefoxDriver(firefoxOptions);

        WebDriverWait wait = new WebDriverWait(driver, 30);

        driver.navigate().to("https://nba.com/players");

        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#onetrust-accept-btn-handler")));

        WebElement btnAcpt = driver.findElementByCssSelector("#onetrust-accept-btn-handler");

        btnAcpt.click();

        Thread.sleep(3000);

        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@aria-label='Player Natural Search Bar']")));

        WebElement playerSearchInput = driver.findElementByXPath("//input[@aria-label='Player Natural Search Bar']");

        //playerSearchInput.sendKeys(imeIgralca);
        playerSearchInput.sendKeys("dragic");

        List<WebElement> players;

        do {
            players = driver.findElements(By.cssSelector("[class='players-list'] tr"));
        } while (players.size() != 2);


        WebElement firstPlayer = driver.findElementByXPath("//a[@class='flex items-center t6']");
        firstPlayer.click();

        WebElement statsPlayer = driver.findElementByXPath("//a[@class='flex px-2 border-b-4 h-full justify-center items-center hover:no-underline hover:border-black focus:border-cerulean focus:outline-none border-transparent']");
        statsPlayer.click();

        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[name='PerMode']")));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[name='SeasonType']")));

        Thread.sleep(3000);

        Select gameMode = new Select(driver.findElementByCssSelector("[name='PerMode']"));
        gameMode.selectByVisibleText("Per 40 Minutes");

        Select seasonMode = new Select(driver.findElementByCssSelector("[name='SeasonType']"));
        seasonMode.selectByVisibleText("Regular Season");

        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[template='player/player-traditional']")));

        WebElement table = driver.findElement(By.cssSelector("[template='player/player-traditional']"));
        for (WebElement element : table.findElements(By.tagName("tr"))) {
            List<WebElement> rowElements = element.findElements(By.tagName("td"));
            String year = rowElements.get(0).getText();
            String threePA = rowElements.get(9).getText();
            System.out.println("ROW: " + element.getText());
            System.out.println("Year:" + year + " " + "3PA: " + threePA);
        }


//        driver.close();
    }

}
