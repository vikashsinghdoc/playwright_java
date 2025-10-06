package com.playwright.framework.pages;

import com.playwright.framework.base.BasePage;
import com.playwright.framework.utils.PlaywrightDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HomePage extends BasePage {

    public void mouseOverNewCar(){
        highlightElement("new_cars");
        mouseHover("new_cars");
    }
    public void clickFindNewCars(){
        highlightElement("find_new_cars");
        click("find_new_cars");
    }

    public void openHomePage(String url){
        navigateToUrl(url);
    }

}
