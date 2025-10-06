package com.playwright.framework.steps;

import com.playwright.framework.base.CarBase;
import com.playwright.framework.log.Log;
import com.playwright.framework.pages.HomePage;
import com.playwright.framework.pages.NewCarsPage;
import com.playwright.framework.utils.PlaywrightDriver;
import io.cucumber.java.en.Given;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;


public class CarwaleSteps {
    @Autowired
    private HomePage homePage;
    @Autowired
    private NewCarsPage newCarsPage;
    @Autowired
    private CarBase carBase;
   ;

   @Value("${base.url}")
   private String appUrl;

    @Given("user navigates to carlwale website")
    public void user_navigates_to_carlwale_website() {
        PlaywrightDriver.openPage(appUrl);
        Log.info(PlaywrightDriver.getPage().locator("[data-skin='title-black']").innerText());
    }
    @Given("user mouseHovers on newcars")
    public void user_mouse_hovers_on_newcars() {
        homePage.mouseOverNewCar();
    }
    @Given("user clicks on findnewcars link")
    public void user_clicks_on_findnewcars_link() throws InterruptedException {
        homePage.clickFindNewCars();
    }
    @Given("user clicks on {string}")
    public void user_clicks_on(String carBrand) {
        PlaywrightDriver.getPage().waitForResponse("**/makepagedata/?maskingName**", () -> {
            switch (carBrand){
                case "Toyota":
                    newCarsPage.goToToyota();
                    break;
                case "Kia":
                    newCarsPage.goToKia();
                    break;
                case "Honda":
                    newCarsPage.goToHonda();
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        });
    }
    @Given("User validates car title as {string}")
    public void user_validates_car_title_as(String carTitle) {
        Assert.assertEquals(carTitle, carBase.getCarTitle());
    }


}
