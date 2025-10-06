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
        this.playwrightDriver.playwrightDriverCreation();
        Log.info("******************************** Created Playwright Driver Successfully **************************************");
    }
    @After
    public void tearDown(Scenario scenario) {
        if (scenario.isFailed()) {
            Log.info("******************************** Scenario Failed, Capturing Screenshot *******************************");
            byte[] screenshot = this.playwrightDriver.getPage().screenshot();
            scenario.attach(screenshot, "image/png", "Failed_Screenshot");
            Log.info("******************************** Screenshot Captured Successfully *******************************");
        }
        this.playwrightDriver.saveVideoForScenarioWithScenarioNames(scenario.getName());
        this.playwrightDriver.closeBrowser();
        this.playwrightDriver.quitPlaywright();
        Log.info("******************************** Closed Browser,Playwright driver Successfully *******************************");
    }
}
