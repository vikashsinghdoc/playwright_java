package com.playwright.framework.steps;

import com.playwright.framework.pages.FrameTestPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import org.springframework.beans.factory.annotation.Autowired;

public class FrameTestSteps {

    @Autowired
    private FrameTestPage frameTestPage;

    @Given("I navigate to url with frames")
    public void user_navigates_to_url_with_frames() {
        this.frameTestPage.navigateToFrameTestPage("https://webdriveruniversity.com/IFrame/index.html");
    }
    @And("I validate an elementText in Frame")
    public void iValidateAnElementTextInFrame() {
        this.frameTestPage.validateTextInFrame();
    }
}
