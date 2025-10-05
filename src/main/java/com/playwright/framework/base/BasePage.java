package com.playwright.framework.base;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.SelectOption;
import org.junit.Assert;
import com.playwright.framework.log.Log;
import com.playwright.framework.utils.PlaywrightDriver;
import org.springframework.stereotype.Component;

@Component
public class BasePage {

    public static Page page;
    public static CarBase carBase;

    public BasePage() {
        this.page= PlaywrightDriver.getPage();
        carBase=new CarBase(this.page);
    }
    public void click(String locatorKey){
        try {
            page.locator(PlaywrightDriver.OR.getProperty(locatorKey)).click();
            Log.info("Successfully clicked on:"+locatorKey);
        } catch (Throwable tx){
            Log.error("Failed to click on:"+locatorKey);
            Assert.fail(tx.getMessage());
        }
    }
    public void mouseHover(String locatorKey){
        try {
            page.hover(PlaywrightDriver.OR.getProperty(locatorKey));
            Log.info("Successfully Mouse Hovered on:"+locatorKey);
        } catch (Throwable tx){
            Log.error("Failed to Mouse Hovered on:"+locatorKey);
            Assert.fail(tx.getMessage());
        }
    }
    public void isElementVisible(String locatorKey){
        try {
            page.waitForSelector(PlaywrightDriver.OR.getProperty(locatorKey),
                    new Page.WaitForSelectorOptions().setTimeout(2000));
            Log.info("Element "+ locatorKey+" is Visible.");
        } catch (Throwable tx){
            Log.error("Element "+ locatorKey+" is not Visible.");
            Assert.fail(tx.getMessage());
        }
    }
    public void type(String locatorKey, String value){
        try {
            page.locator(PlaywrightDriver.OR.getProperty(locatorKey)).fill(value);
            Log.info("Successfully typed in:"+locatorKey);
        } catch (Throwable tx){
            Log.error("failed to typed in:"+locatorKey);
            Assert.fail(tx.getMessage());
        }
    }
    public void select(String locatorKey, String value){
        try {
            page.selectOption(PlaywrightDriver.OR.getProperty(locatorKey), new SelectOption().setLabel(value));
            Log.info("Successfully selected option:"+locatorKey+" in Dropdown");
        } catch (Throwable tx){
            Log.error("Failed to select option:"+locatorKey+" in Dropdown");
            Assert.fail(tx.getMessage());
        }
    }

}
