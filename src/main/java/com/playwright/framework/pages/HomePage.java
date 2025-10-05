package com.playwright.framework.pages;

import com.playwright.framework.base.BasePage;
import org.springframework.stereotype.Component;

@Component
public class HomePage extends BasePage {
    public void mouseOverNewCar(){
        mouseHover("new_cars");
    }
    public void clickFindNewCars(){
        click("find_new_cars");
    }


}
