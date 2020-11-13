package com.nba.stats;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.Scanner;

public class NbaStatsScrape {

    private static final Scanner scanner = new Scanner(System.in);
    private static String playerName;

    public static void main(String[] args) {

        // new WebDriver, using headless=false for debug
        FirefoxDriver driver = new FirefoxDriver(new FirefoxOptions().setHeadless(false));
        // new WebDriverWait, used to wait for loading of elements
        WebDriverWait wait = new WebDriverWait(driver, 12);

        // loop while no result found, on error try again ('-1' for exit)
        while (getPlayer()) {
            try {
                driver.navigate().to("https://nba.com/players"); // go to nba.com/players

                // accept cookies...
                wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#onetrust-accept-btn-handler")));
                WebElement btnAcpt = driver.findElementByCssSelector("#onetrust-accept-btn-handler");
                btnAcpt.click();

                // wait for dirty page reloading
                Thread.sleep(3000);

                // wait/find search bar and type players name from input
                wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@aria-label='Player Natural Search Bar']")));
                WebElement playerSearchInput = driver.findElementByXPath("//input[@aria-label='Player Natural Search Bar']");
                playerSearchInput.sendKeys(playerName);

                // keep waiting for only 1 player left in the result table -> if input data does not guarantee it, we're lost, commenting out
//                List<WebElement> players;
//                do { players = driver.findElements(By.cssSelector("[class='players-list'] tr")); } while (players.size() != 2);
                Thread.sleep(2000);

                // find first player element in the table and click it
                WebElement firstPlayer = driver.findElementByXPath("//a[@class='flex items-center t6']");
                firstPlayer.click();

                // find players stats link, click it
                WebElement statsPlayer = driver.findElementByXPath("//a[@class='flex px-2 border-b-4 h-full justify-center items-center hover:no-underline hover:border-black focus:border-cerulean focus:outline-none border-transparent']");
                statsPlayer.click();

                // wait for dirty page reloads again
                Thread.sleep(3000);

                // wait/find selector for period, select 40 min option
                wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector("[name='PerMode']")));
                Select gameMode = new Select(driver.findElementByCssSelector("[name='PerMode']"));
                gameMode.selectByVisibleText("Per 40 Minutes");

                // wait/find selector for season type, select regular season option
                wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector("[name='SeasonType']")));
                Select seasonMode = new Select(driver.findElementByCssSelector("[name='SeasonType']"));
                seasonMode.selectByVisibleText("Regular Season");


                // wait and select players stats table
                wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[template='player/player-traditional']")));
                WebElement table = driver.findElement(By.cssSelector("[template='player/player-traditional']"));

                Thread.sleep(1500);

                // loop table rows, print year and 3PA cell values
                for (WebElement row : table.findElements(By.tagName("tr"))) {
                    List<WebElement> rowElements = row.findElements(By.tagName("td"));
                    String year = rowElements.get(0).getText();
                    String threePA = rowElements.get(9).getText();
                    System.out.println("Year:" + year + " " + "3PA: " + threePA);
                }

                break; // got result, exiting loop

            } catch (Exception e) {
                System.out.println("There was and error try to find the players stats, you can try again."); // on error try again
                e.printStackTrace();
            }
        }
        driver.close(); // done deal, close driver
    }

    // get standard input for player name; if '-1' return false to exit while loop
    private static boolean getPlayer() {
        System.out.println("Enter players name ('-1' for exit): ");
        playerName = scanner.nextLine();
        return !playerName.equals("-1");
    }

}
