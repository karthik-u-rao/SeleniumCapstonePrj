package com.orangehrm;

import com.utils.EnglishBaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

public class OrangeHRMTests extends EnglishBaseTest {

    @Test(priority = 1, description = "Test successful login to OrangeHRM")
    public void testLogin() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("Admin", "admin123");
        Assert.assertTrue(loginPage.isDashboardDisplayed(), "Login failed - Dashboard not displayed");
        System.out.println("✓ Login test passed successfully");
    }

    @Test(priority = 2, description = "Test applying for leave")
    public void testApplyLeave() {
        // Login first
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("Admin", "admin123");

        // Apply for leave
        LeavePage leavePage = new LeavePage(driver);
        leavePage.applyLeave("2025-12-20", "2025-12-22", "Vacation leave for year end");
        System.out.println("✓ Apply Leave test passed successfully");
    }

    @Test(priority = 3, description = "Test viewing leave list")
    public void testViewLeaveList() {
        // Login first
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("Admin", "admin123");

        // Navigate to My Leave
        LeavePage leavePage = new LeavePage(driver);
        leavePage.navigateToMyLeave();
        Assert.assertTrue(leavePage.isLeaveListDisplayed(), "Leave list not displayed");
        System.out.println("✓ View Leave List test passed successfully");
    }

    @Test(priority = 4, description = "Test adding a candidate in recruitment")
    public void testAddCandidate() {
        // Login first
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("Admin", "admin123");

        // Add candidate
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
        // Login first
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("Admin", "admin123");

        // Navigate to Candidates
        RecruitmentPage recruitmentPage = new RecruitmentPage(driver);
        recruitmentPage.navigateToCandidates();
        Assert.assertTrue(recruitmentPage.isCandidateTableDisplayed(), "Candidates table not displayed");
        System.out.println("✓ View Candidates test passed successfully");
    }
}
