package com.orangehrm;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.utils.EnglishBaseTest;

public class OrangeHRMTests extends EnglishBaseTest {

    @Test(priority = 1, description = "Test successful login to OrangeHRM")
    public void testLogin() {
        loginAsAdmin();
        Assert.assertTrue(new LoginPage(driver).isDashboardDisplayed(), "Login failed - Dashboard not displayed");
        System.out.println("✓ Login test passed successfully");
    }

    @Test(priority = 2, description = "Test opening the Buzz module")
    public void testBuzzPageLoads() {
        loginAsAdmin();
        clickMainMenu("Buzz");
        wait.until(ExpectedConditions.urlContains("/buzz"));
        WebElement buzzInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("textarea.oxd-buzz-post-input")));
        Assert.assertTrue(buzzInput.isDisplayed(), "Buzz post text area not displayed");
        System.out.println("✓ Buzz Page test passed successfully");
    }

    @Test(priority = 3, description = "Test viewing leave list")
    public void testViewLeaveList() {
        loginAsAdmin();
        LeavePage leavePage = new LeavePage(driver);
        leavePage.navigateToMyLeave();
        Assert.assertTrue(leavePage.isLeaveListDisplayed(), "Leave list not displayed");
        System.out.println("✓ View Leave List test passed successfully");
    }

    @Test(priority = 4, description = "Test adding a candidate in recruitment")
    public void testAddCandidate() {
        loginAsAdmin();
        RecruitmentPage recruitmentPage = new RecruitmentPage(driver);
        String timestamp = String.valueOf(System.currentTimeMillis());
        recruitmentPage.addCandidate(
                "John" + timestamp.substring(timestamp.length() - 4),
                "Robert",
                "Doe",
                "john.doe" + timestamp.substring(timestamp.length() - 6) + "@test.com"
        );
        System.out.println("✓ Add Candidate test passed successfully");
    }

    @Test(priority = 5, description = "Test viewing candidates list")
    public void testViewCandidates() {
        loginAsAdmin();
        RecruitmentPage recruitmentPage = new RecruitmentPage(driver);
        recruitmentPage.navigateToCandidates();
        Assert.assertTrue(recruitmentPage.isCandidateTableDisplayed(), "Candidates table not displayed");
        System.out.println("✓ View Candidates test passed successfully");
    }

    @Test(priority = 6, description = "Verify dashboard quick launch cards are visible")
    public void testDashboardQuickLaunchVisible() {
        loginAsAdmin();
        List<WebElement> quickLaunchCards = driver.findElements(By.cssSelector("div.orangehrm-quick-launch-card"));
        Assert.assertTrue(quickLaunchCards.size() >= 1, "Quick Launch cards not found on dashboard");
        System.out.println("✓ Dashboard quick launch cards visible");
    }

    @Test(priority = 7, dataProvider = "mainMenuSmokeData", description = "Verify key main menu modules load")
    public void testMainMenuModuleLoads(String menuText, String expectedUrlFragment, By[] mustSeeElements) {
        loginAsAdmin();
        clickMainMenu(menuText);
        if (expectedUrlFragment != null && !expectedUrlFragment.isBlank()) {
            wait.until(ExpectedConditions.urlContains(expectedUrlFragment));
        }
        for (By locator : mustSeeElements) {
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            Assert.assertTrue(element.isDisplayed(), "Expected element not visible for module " + menuText);
        }
        System.out.println("✓ " + menuText + " module loads");
    }

    @Test(priority = 8, description = "Verify Maintenance password prompt appears")
    public void testMaintenancePromptAppears() {
        loginAsAdmin();
        clickMainMenu("Maintenance");
        wait.until(ExpectedConditions.urlContains("/maintenance"));
        WebElement passwordInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[type='password']")));
        Assert.assertTrue(passwordInput.isDisplayed(), "Maintenance password form not displayed");
        System.out.println("✓ Maintenance access screen displayed");
    }

    @Test(priority = 9, description = "Verify dashboard widgets render")
    public void testDashboardWidgetsVisible() {
        loginAsAdmin();
        List<WebElement> widgets = driver.findElements(By.cssSelector("div.orangehrm-dashboard-widget"));
        Assert.assertTrue(widgets.size() >= 1, "Dashboard widgets not visible");
        System.out.println("✓ Dashboard widgets visible");
    }

    @DataProvider(name = "mainMenuSmokeData")
    public Object[][] mainMenuSmokeData() {
        return new Object[][]{
            {"Admin", "/admin", new By[]{By.xpath("//h6[text()='Admin']")}},
            {"PIM", "/pim", new By[]{By.xpath("//h6[text()='PIM']"), By.cssSelector("div.oxd-table")}},
            {"Time", "/time", new By[]{By.xpath("//h6[text()='Time']")}},
            {"My Info", "/viewPersonalDetails", new By[]{By.xpath("//h6[text()='Personal Details']")}},
            {"Directory", "/directory", new By[]{By.xpath("//h6[text()='Directory']"), By.cssSelector("input[placeholder='Type for hints...']")}},
            {"Performance", "/performance", new By[]{By.xpath("//h6[text()='Performance']")}},
            {"Claim", "/claim", new By[]{By.xpath("//h6[contains(normalize-space(),'Claim')]")}}
        };
    }

    private void loginAsAdmin() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("Admin", "admin123");
        wait.until(ExpectedConditions.urlContains("/dashboard"));
    }

    private void clickMainMenu(String menuText) {
        String xpath = String.format("//span[text()='%s']", menuText);
        WebElement menu = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
        menu.click();
    }
}
