package com.playwright.framework.steps;

import com.playwright.framework.log.Log;
import com.playwright.framework.pages.CarDetailsPage;
import com.playwright.framework.pages.HomePage;
import com.playwright.framework.pages.NewCarsPage;
import com.playwright.framework.driver_manager.PlaywrightDriver;
import io.cucumber.java.en.Given;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;


public class CarwaleSteps {
    @Value("${base.url}")
    private String appUrl;
    @Autowired
    private PlaywrightDriver playwrightDriver;
    @Autowired
    private HomePage homePage;
    @Autowired
    private NewCarsPage newCarsPage;
    @Autowired
    private CarDetailsPage carDetailsPage;

    @Given("user navigates to carlwale website")
    public void user_navigates_to_carlwale_website() {
        this.homePage.openHomePage(appUrl);
        Log.info(this.playwrightDriver.getPage().locator("[data-skin='title-black']").innerText());
    }
    @Given("user mouseHovers on newcars")
    public void user_mouse_hovers_on_newcars() {
        this.homePage.mouseOverNewCar();
    }
    @Given("user clicks on findnewcars link")
    public void user_clicks_on_findnewcars_link() {
        this.homePage.clickFindNewCars();
    }
    @Given("user clicks on {string}")
    public void user_clicks_on(String carBrand) {
        this.playwrightDriver.getPage().waitForResponse("**/makepagedata/?maskingName**", () -> {
            switch (carBrand){
                case "Toyota":
                    this.newCarsPage.goToToyota();
                    break;
                case "Kia":
                    this.newCarsPage.goToKia();
                    break;
                case "Honda":
                    this.newCarsPage.goToHonda();
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        });
    }
    @Given("User validates car title as {string}")
    public void user_validates_car_title_as(String carTitle) {
        Assert.assertTrue(this.carDetailsPage.isTextMatching("car_title_css", carTitle,2000));
    }

}
