package com.playwright.framework.base;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.Response;
import com.microsoft.playwright.options.LoadState;
import com.playwright.framework.utils.PlaywrightDriver;

public class CarBase {
    Page page;
    public CarBase(Page page){
        this.page=page;
    }

    public String getCarTitle(){
        return page.locator(PlaywrightDriver.OR.getProperty("car_title_css")).innerText();
    }
}
