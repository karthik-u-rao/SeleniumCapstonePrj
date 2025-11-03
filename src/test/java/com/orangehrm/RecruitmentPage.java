package com.orangehrm;

import com.utils.SmartWait;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import java.time.Duration;

public class RecruitmentPage {
    private WebDriver driver;
    private SmartWait sw;

    // Locators
    private By recruitmentMenu = By.xpath("//span[text()='Recruitment']");
    private By addButton = By.xpath("//button[normalize-space()='Add']");
    private By firstNameField = By.name("firstName");
    private By middleNameField = By.name("middleName");
    private By lastNameField = By.name("lastName");
    private By emailField = By.xpath("//label[text()='Email']/parent::div/following-sibling::div//input");
    private By saveButton = By.cssSelector("button[type='submit']");
    private By candidatesLink = By.linkText("Candidates");
    private By candidateTable = By.cssSelector(".oxd-table");
    private By searchButton = By.cssSelector("button[type='submit']");
    private By deleteButton = By.xpath("//i[contains(@class,'bi-trash')]");
    private By confirmDeleteButton = By.xpath("//button[normalize-space()='Yes, Delete']");
    private By editButton = By.xpath("//i[contains(@class,'bi-pencil')]");

    public RecruitmentPage(WebDriver driver) {
        this.driver = driver;
        this.sw = new SmartWait(driver);
    }

    public void navigateToRecruitment() {
        sw.safeClick(recruitmentMenu);
    }

    public void clickAddButton() {
        sw.safeClick(addButton);
    }

    public void enterFirstName(String firstName) {
        WebElement el = sw.visible(firstNameField, Duration.ofSeconds(10));
        el.sendKeys(firstName);
    }

    public void enterMiddleName(String middleName) {
        WebElement el = sw.visible(middleNameField, Duration.ofSeconds(10));
        el.sendKeys(middleName);
    }

    public void enterLastName(String lastName) {
        WebElement el = sw.visible(lastNameField, Duration.ofSeconds(10));
        el.sendKeys(lastName);
    }

    public void enterEmail(String email) {
        WebElement el = sw.visible(emailField, Duration.ofSeconds(10));
        el.sendKeys(email);
    }

    public void clickSaveButton() {
        sw.safeClick(saveButton);
    }

    public void addCandidate(String firstName, String middleName, String lastName, String email) {
        navigateToRecruitment();
        clickAddButton();
        enterFirstName(firstName);
        enterMiddleName(middleName);
        enterLastName(lastName);
        enterEmail(email);
        clickSaveButton();
    }

    public void navigateToCandidates() {
        navigateToRecruitment();
        sw.safeClick(candidatesLink);
    }

    public boolean isCandidateTableDisplayed() {
        try {
            sw.visible(candidateTable, Duration.ofSeconds(10));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void deleteFirstCandidate() {
        sw.safeClick(deleteButton);
        sw.safeClick(confirmDeleteButton);
    }

    public void editFirstCandidate() {
        sw.safeClick(editButton);
    }

    public void updateCandidateMiddleName(String newMiddleName) {
        WebElement middleName = sw.visible(middleNameField, Duration.ofSeconds(10));
        middleName.clear();
        middleName.sendKeys(newMiddleName);
        clickSaveButton();
    }
}
