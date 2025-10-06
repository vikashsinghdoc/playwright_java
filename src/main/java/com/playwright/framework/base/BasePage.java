package com.playwright.framework.base;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.SelectOption;
import com.microsoft.playwright.options.WaitForSelectorState;
import org.junit.Assert;
import com.playwright.framework.log.Log;
import com.playwright.framework.utils.PlaywrightDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BasePage {
    @Autowired
    private PlaywrightDriver playwrightDriver;

    public void click(String locatorKey){
        try {
            this.playwrightDriver.getPage().locator(PlaywrightDriver.OR.getProperty(locatorKey)).click();
            Log.info("Successfully clicked on:"+locatorKey);
        } catch (Throwable tx){
            Log.error("Failed to click on:"+locatorKey);
            Assert.fail(tx.getMessage());
        }
    }
    public void mouseHover(String locatorKey){
        try {
            this.playwrightDriver.getPage().hover(PlaywrightDriver.OR.getProperty(locatorKey));
            Log.info("Successfully Mouse Hovered on:"+locatorKey);
        } catch (Throwable tx){
            Log.error("Failed to Mouse Hovered on:"+locatorKey);
            Assert.fail(tx.getMessage());
        }
    }
    public boolean isElementVisible(String locatorKey){
        //Default Time
        return isElementVisible(locatorKey, 2000);
    }
    public boolean isElementVisible(String locatorKey, int timeoutMillis) {
        try {
            this.playwrightDriver.getPage().waitForSelector(
                    PlaywrightDriver.OR.getProperty(locatorKey),
                    new Page.WaitForSelectorOptions()
                            .setTimeout(timeoutMillis)
                            .setState(WaitForSelectorState.VISIBLE)
            );
            Log.pass("Element " + locatorKey + " is visible within " + timeoutMillis + " ms.");
            return true;
        } catch (Exception e) {
            Log.error("Element " + locatorKey + " not visible within " + timeoutMillis + " ms. " + e.getMessage());
            return false;
        }
    }

    public void type(String locatorKey, String value){
        try {
            this.playwrightDriver.getPage().locator(PlaywrightDriver.OR.getProperty(locatorKey)).fill(value);
            Log.info("Successfully typed in:"+locatorKey);
        } catch (Throwable tx){
            Log.error("failed to typed in:"+locatorKey);
            Assert.fail(tx.getMessage());
        }
    }
    public void select(String locatorKey, String value){
        try {
            this.playwrightDriver.getPage().selectOption(PlaywrightDriver.OR.getProperty(locatorKey), new SelectOption().setLabel(value));
            Log.info("Successfully selected option:"+locatorKey+" in Dropdown");
        } catch (Throwable tx){
            Log.error("Failed to select option:"+locatorKey+" in Dropdown");
            Assert.fail(tx.getMessage());
        }
    }
    public String getInnerText(String locatorKey) {
        try {
            Locator element = this.playwrightDriver.getPage()
                    .locator(PlaywrightDriver.OR.getProperty(locatorKey));
            element.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(2000));

            String text = element.innerText();
            Log.info("Successfully fetched text from '" + locatorKey + "': " + text);
            return text;

        } catch (Exception e) {
            Log.error("Failed to fetch text from '" + locatorKey + "'. " + e.getMessage());
            Assert.fail("Failed to fetch text from '" + locatorKey + "': " + e.getMessage());
            return "";
        }
    }
    public void navigateToUrl(String url){
        try {
            this.playwrightDriver.getPage().navigate(url);
            Log.info("Successfully navigated to url:"+url);
        } catch (Throwable tx){
            Log.error("Failed to navigate to url:"+url);
            Assert.fail(tx.getMessage());
        }
    }
    public void highlightElement(String locatorKey){
        try {
            this.playwrightDriver.getPage().locator(PlaywrightDriver.OR.getProperty(locatorKey))
                    .evaluate("element => element.style.border = '3px solid red'");
            Log.info("Successfully highlighted element:"+locatorKey);
        } catch (Throwable tx){
            Log.error("Failed to highlight element:"+locatorKey);
            Assert.fail(tx.getMessage());
        }
    }
    public void selectOptionFromDropdown(String locatorKeyDD, String locatorKeyOptions, String optionText) {
        click(locatorKeyDD);
        try {
            Locator options = this.playwrightDriver.getPage().locator(PlaywrightDriver.OR.getProperty(locatorKeyOptions));
            options.first().waitFor();
            Locator matchingOption = options.filter(new Locator.FilterOptions().setHasText(optionText)).first();

            if (matchingOption.count() > 0) {
                matchingOption.click();
                Log.info("Selected option '" + optionText + "' from dropdown: " + locatorKeyDD);
            } else {
                Log.error("Option '" + optionText + "' not found in dropdown: " + locatorKeyDD);
                Assert.fail("Option not found: " + optionText);
            }
        } catch (Exception e) {
            Log.error("Failed to select option '" + optionText + "' from dropdown: " + locatorKeyDD);
            Assert.fail("Error selecting option: " + e.getMessage());
        }
    }
    public void clearText(String locatorKey){
        try {
            this.playwrightDriver.getPage().locator(PlaywrightDriver.OR.getProperty(locatorKey)).clear();
            Log.info("Successfully cleared text in:"+locatorKey);
        } catch (Throwable tx){
            Log.error("Failed to clear text in:"+locatorKey);
            Assert.fail(tx.getMessage());
        }
    }
    public boolean isTextMatching(String locatorKey, String expectedText, int timeoutMillis) {
        Locator element = playwrightDriver.getPage()
                .locator(PlaywrightDriver.OR.getProperty(locatorKey));
        element.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(timeoutMillis));
        String actualText = element.innerText().trim();
        boolean matches = actualText.equals(expectedText);
        if (matches) {
            Log.pass("Text matches for '" + locatorKey + "': '" + actualText + "'");
        } else {
            Log.error("Text mismatch for '" + locatorKey + "'. Expected: '"
                    + expectedText + "', Actual: '" + actualText + "'");
        }

        return matches;
    }

}
