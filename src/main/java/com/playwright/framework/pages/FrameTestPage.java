package com.playwright.framework.pages;

import com.playwright.framework.base.BasePage;
import org.junit.Assert;
import org.springframework.stereotype.Component;

@Component
public class FrameTestPage extends BasePage {

    public void navigateToFrameTestPage(String url) {
       navigateToUrl(url);
    }

    public void validateTextInFrame() {
        isElementVisible("text=Who Are We?");
        isTextMatching("text=Who Are We?","Who Are We", 2000);
    }
}
