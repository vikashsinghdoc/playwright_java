package com.playwright.framework.steps;

import com.playwright.framework.utils.PlaywrightDriver;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

public class Hooks {

    @Before
    public void setupDriver(){
        PlaywrightDriver.setUpDriver();
    }
    @After
    public void tearDown(Scenario scenario) {
        if (scenario.isFailed()) {
            byte[] screenshot = PlaywrightDriver.getPage().screenshot();
            scenario.attach(screenshot, "image/png", "Failed_Screenshot");
        }
        PlaywrightDriver.saveVideoForScenario(scenario.getName());
        PlaywrightDriver.closeBrowser();
        PlaywrightDriver.quitPlaywright();

    }
}
