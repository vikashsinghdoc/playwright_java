package com.playwright.framework.pages;

import com.playwright.framework.base.BasePage;
import com.playwright.framework.driver_manager.PlaywrightDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CarDetailsPage extends BasePage {
    @Autowired
    private PlaywrightDriver playwrightDriver;

    public String getCarTitle(){
        return this.playwrightDriver.getPage().locator(PlaywrightDriver.OR.getProperty("car_title_css")).innerText();
    }
}
