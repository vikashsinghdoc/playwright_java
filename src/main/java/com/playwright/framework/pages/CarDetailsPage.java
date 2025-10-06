package com.playwright.framework.pages;

import com.playwright.framework.utils.PlaywrightDriver;
import org.springframework.stereotype.Component;

@Component
public class CarDetailsPage {

    public String getCarTitle(){
        return PlaywrightDriver.getPage().locator(PlaywrightDriver.OR.getProperty("car_title_css")).innerText();
    }
}
