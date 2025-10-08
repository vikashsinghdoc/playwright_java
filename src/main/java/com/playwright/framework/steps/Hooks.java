package com.playwright.framework.steps;

import com.playwright.framework.log.FrameLoggingConfig;
import com.playwright.framework.log.Log;
import com.playwright.framework.driver_manager.PlaywrightDriver;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.springframework.beans.factory.annotation.Autowired;

public class Hooks {

    @Autowired
    private PlaywrightDriver playwrightDriver;

    private static boolean frameFlagLogged = false;

    @Before(order = 0)
    public void logFrameFlag() {
        if (!frameFlagLogged) {
            frameFlagLogged = true;
            Log.bannerInfo("FrameSearch & Highlight flags=" + FrameLoggingConfig.resolve());
        }
    }
    @Before(order = 1)
    public void setupDriver(){
        this.playwrightDriver.playwrightDriverCreation();
        Log.bannerInfo("Created Playwright Driver Successfully");
    }
    @After
    public void tearDown(Scenario scenario) {
        if (scenario.isFailed()) {
            Log.bannerInfo("Scenario Failed, Capturing Screenshot");
            byte[] screenshot = this.playwrightDriver.getPage().screenshot();
            scenario.attach(screenshot, "image/png", "Failed_Screenshot");
            Log.bannerInfo("Screenshot Captured & attached Successfully");
        }
        this.playwrightDriver.saveVideoForScenarioWithScenarioNames(scenario.getName());
        this.playwrightDriver.closeBrowser();
        this.playwrightDriver.quitPlaywright();
        Log.bannerInfo("Closed Browser & Playwright driver Successfully");
    }
}
