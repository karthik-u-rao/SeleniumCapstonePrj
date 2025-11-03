package com.orangehrm;

import com.utils.SmartWait;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.time.Duration;

public class LoginPage {
    private WebDriver driver;
    private SmartWait sw;

    // Locators
    private By usernameField = By.name("username");
    private By passwordField = By.name("password");
    private By loginButton = By.cssSelector("button[type='submit']");
    private By dashboardHeader = By.xpath("//h6[text()='Dashboard']");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.sw = new SmartWait(driver);
    }

    public void navigateToLoginPage() {
        driver.get("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");
        sw.visible(usernameField, Duration.ofSeconds(10));
    }

    public void enterUsername(String username) {
        sw.visible(usernameField, Duration.ofSeconds(10));
        driver.findElement(usernameField).sendKeys(username);
    }

    public void enterPassword(String password) {
        sw.visible(passwordField, Duration.ofSeconds(10));
        driver.findElement(passwordField).sendKeys(password);
    }

    public void clickLoginButton() {
        sw.safeClick(loginButton);
    }

    public boolean isDashboardDisplayed() {
        try {
            // Fast path: check header or URL, with short waits
            sw.visible(dashboardHeader, Duration.ofSeconds(5));
            return true;
        } catch (Exception ignored) {
            return driver.getCurrentUrl().contains("/dashboard");
        }
    }

    public void login(String username, String password) {
        navigateToLoginPage();
        enterUsername(username);
        enterPassword(password);
        clickLoginButton();
    }
}
