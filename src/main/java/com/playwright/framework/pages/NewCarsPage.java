package com.playwright.framework.pages;

import com.playwright.framework.base.BasePage;
import org.springframework.stereotype.Component;

@Component
public class NewCarsPage extends BasePage {

    public void goToToyota(){
        click("toyota_xpath");
    }
    public void goToHonda(){
        click("honda_xpath");
    }
    public void goToKia(){
        click("kia_xpath");
    }
}
