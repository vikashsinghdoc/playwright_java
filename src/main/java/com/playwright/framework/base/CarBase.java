package com.playwright.framework.base;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.Response;
import com.microsoft.playwright.options.LoadState;
import com.playwright.framework.utils.PlaywrightDriver;
import org.springframework.stereotype.Component;

@Component
public class CarBase {
    public String getCarTitle(){
        return PlaywrightDriver.getPage().locator(PlaywrightDriver.OR.getProperty("car_title_css")).innerText();
    }
}
