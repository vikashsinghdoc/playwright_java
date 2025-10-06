package com.playwright.framework.pages;

import com.playwright.framework.base.BasePage;
import com.playwright.framework.utils.PlaywrightDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HomePage extends BasePage {

    @Autowired
    private PlaywrightDriver playwrightDriver;
    public void mouseOverNewCar(){
        mouseHover("new_cars");
    }
    public void clickFindNewCars(){
        click("find_new_cars");
    }

    public void openHomePage(String url) {
        this.playwrightDriver.getPage().navigate(url);
    }


}
