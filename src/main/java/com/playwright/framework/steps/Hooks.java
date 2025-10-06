package com.playwright.framework.steps;

import com.playwright.framework.log.Log;
import com.playwright.framework.utils.PlaywrightDriver;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.springframework.beans.factory.annotation.Autowired;

public class Hooks {

    @Autowired
    private PlaywrightDriver playwrightDriver;

    @Before
    public void setupDriver(){
        playwrightDriver.playwrightDriverCreation();
        Log.info("SKIPPED");
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
