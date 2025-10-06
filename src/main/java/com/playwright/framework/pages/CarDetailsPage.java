package com.playwright.framework.pages;

import com.playwright.framework.utils.PlaywrightDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CarDetailsPage {
    @Autowired
    private PlaywrightDriver playwrightDriver;

    public String getCarTitle(){
        return this.playwrightDriver.getPage().locator(PlaywrightDriver.OR.getProperty("car_title_css")).innerText();
    }
}
