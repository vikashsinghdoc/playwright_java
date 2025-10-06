package com.playwright.framework.base;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.SelectOption;
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
    public void isElementVisible(String locatorKey){
        try {
            this.playwrightDriver.getPage().waitForSelector(PlaywrightDriver.OR.getProperty(locatorKey),
                    new Page.WaitForSelectorOptions().setTimeout(2000));
            Log.info("Element "+ locatorKey+" is Visible.");
        } catch (Throwable tx){
            Log.error("Element "+ locatorKey+" is not Visible.");
            Assert.fail(tx.getMessage());
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

}
