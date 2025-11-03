package com.utils;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Function;

public class SmartWait {
    private final WebDriver driver;

    // Known overlay/loader selectors for OrangeHRM
    private static final By OVERLAY = By.cssSelector(".oxd-form-loader, .oxd-loading-spinner");

    public SmartWait(WebDriver driver) {
        this.driver = driver;
    }

    private FluentWait<WebDriver> wait(Duration timeout) {
        return new FluentWait<>(driver)
                .withTimeout(timeout)
                .pollingEvery(Duration.ofMillis(300))
                .ignoring(NoSuchElementException.class)
                .ignoring(ElementNotInteractableException.class)
                .ignoring(ElementClickInterceptedException.class)
                .ignoring(StaleElementReferenceException.class);
    }

    public WebElement visible(By locator, Duration timeout) {
        return wait(timeout).until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public WebElement clickable(By locator, Duration timeout) {
        return wait(timeout).until(ExpectedConditions.elementToBeClickable(locator));
    }

    public boolean invisibilityIfPresent(By locator, Duration timeout) {
        try {
            List<WebElement> els = driver.findElements(locator);
            if (els.isEmpty()) return true; // nothing to wait for
            return wait(timeout).until(ExpectedConditions.invisibilityOfAllElements(els));
        } catch (TimeoutException te) {
            return false;
        }
    }

    public void waitPageReady(Duration timeout) {
        ExpectedCondition<Boolean> jsReady = drv -> ((JavascriptExecutor) drv)
                .executeScript("return document.readyState").toString().equals("complete");
        wait(timeout).until(jsReady);
    }

    public void waitUrlContains(String fragment, Duration timeout) {
        wait(timeout).until(ExpectedConditions.urlContains(fragment));
    }

    public void scrollIntoView(WebElement el) {
        try {
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", el);
        } catch (Exception ignore) { }
    }

    public void jsClick(WebElement el) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
    }

    public void moveToAndClick(WebElement el) {
        new Actions(driver).moveToElement(el).pause(Duration.ofMillis(100)).click().perform();
    }

    // Overlay-aware, fast path click: normal click -> overlay wait + retry -> JS click fallback
    public void safeClick(By locator) {
        WebElement el = visible(locator, Duration.ofSeconds(10));
        scrollIntoView(el);
        try {
            clickable(locator, Duration.ofSeconds(10));
            el.click();
            return;
        } catch (Exception first) {
            // If overlay present, wait briefly and retry
            invisibilityIfPresent(OVERLAY, Duration.ofSeconds(3));
            try {
                WebElement el2 = visible(locator, Duration.ofSeconds(5));
                scrollIntoView(el2);
                clickable(locator, Duration.ofSeconds(5));
                el2.click();
                return;
            } catch (Exception second) {
                // Final fallback: JS click
                try {
                    WebElement el3 = driver.findElement(locator);
                    scrollIntoView(el3);
                    jsClick(el3);
                } catch (Exception ignore) {
                    // Re-throw the original exception for easier diagnosis
                    throw first;
                }
            }
        }
        // After clicking, if overlay flashes, allow it to disappear without long wait
        invisibilityIfPresent(OVERLAY, Duration.ofSeconds(2));
    }

    // Overlay-aware option select (for OrangeHRM select popovers)
    public void selectFromPopover(By trigger, By optionLocator, int optionIndex) {
        safeClick(trigger);
        // If options load under overlay, this will short-wait only if present
        invisibilityIfPresent(OVERLAY, Duration.ofSeconds(2));
        List<WebElement> options = wait(Duration.ofSeconds(5))
                .until(drv -> {
                    List<WebElement> list = drv.findElements(optionLocator);
                    return list.isEmpty() ? null : list;
                });
        WebElement choice = options.get(Math.min(optionIndex, options.size() - 1));
        scrollIntoView(choice);
        try {
            choice.click();
        } catch (Exception e) {
            jsClick(choice);
        }
        invisibilityIfPresent(OVERLAY, Duration.ofSeconds(2));
    }
}

