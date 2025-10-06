package com.playwright.framework.pages;

import com.playwright.framework.base.BasePage;
import org.springframework.stereotype.Component;

@Component
public class NewCarsPage extends BasePage {

    public void goToToyota(){
        highlightElement("toyota_xpath");
        click("toyota_xpath");
    }
    public void goToHonda(){
        highlightElement("honda_xpath");
        click("honda_xpath");
    }
    public void goToKia(){
        highlightElement("kia_xpath");
        click("kia_xpath");
    }
}
