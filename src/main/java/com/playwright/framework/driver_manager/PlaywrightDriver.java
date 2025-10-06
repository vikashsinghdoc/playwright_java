package com.playwright.framework.driver_manager;

import com.microsoft.playwright.*;
import com.playwright.framework.log.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
@Component
public class PlaywrightDriver {

    public static Properties OR = new Properties();
    @Value("${headless:false}")
    private String headless;
    @Value("${browser:chrome}")
    private String browserName;
    @Value("${video.recording:false}")
    private String isVideoRecording;
    private FileInputStream fis;

    private ThreadLocal<Playwright> pw = new ThreadLocal<>();
    private static ThreadLocal<Browser> br = new ThreadLocal<>();
    private static ThreadLocal<BrowserContext> bc = new ThreadLocal<>();
    private static ThreadLocal<Page> pg = new ThreadLocal<>();

    // Global execution timestamp
    public String executionStartTime;

    // Getters
    public Playwright getPlaywright() { return pw.get(); }
    public Browser getBrowser() { return br.get(); }
    public BrowserContext getBrowserContext() { return bc.get(); }
    public Page getPage() { return pg.get(); }


    public void playwrightDriverCreation() {
        try {
            // Load OR.properties
            this.fis = new FileInputStream("src/main/resources/repositories/OR.properties");
            OR.load(fis);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load properties", e);
        }

        // Initialize global execution timestamp if not already set
        if (this.executionStartTime == null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d-MMM-yy_HH-mm");
            this.executionStartTime = LocalDateTime.now().format(formatter);
        }

        // Initialize Playwright
        this.pw.set(Playwright.create());

        // Launch browser based on config
        Browser browser;
        switch (this.browserName) {
            case "chrome" -> browser = getPlaywright().chromium()
                    .launch(new BrowserType.LaunchOptions()
                            .setChannel("chrome")
                            .setHeadless(Boolean.parseBoolean(this.headless)));
            case "firefox" -> browser = getPlaywright().firefox()
                    .launch(new BrowserType.LaunchOptions()
                            .setChannel("firefox")
                            .setHeadless(Boolean.parseBoolean(this.headless)));
            case "webkit" -> browser = getPlaywright().webkit()
                    .launch(new BrowserType.LaunchOptions()
                            .setChannel("webkit")
                            .setHeadless(Boolean.parseBoolean(this.headless)));
            default -> throw new IllegalArgumentException("Browser name is incorrect or not supported");
        }
        br.set(browser);

        // Create execution folder + videos folder
        Path executionFolder = Paths.get("target", "Execution_" + this.executionStartTime);
        Path videosFolder = executionFolder.resolve("videos");
        try {
            Files.createDirectories(videosFolder);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create execution/videos folder", e);
        }

        // Create BrowserContext with optional video recording
        Browser.NewContextOptions contextOptions = new Browser.NewContextOptions();
        if (this.isVideoRecording.equalsIgnoreCase("true")) {
            contextOptions.setRecordVideoDir(videosFolder);
        }
        BrowserContext browserContext = browser.newContext(contextOptions);
        bc.set(browserContext);

        pg.set(browserContext.newPage());
    }

    // Close page, context, and browser safely
    public void closeBrowser() {
        if (getPage() != null) getPage().close();
        if (getBrowserContext() != null) getBrowserContext().close();
        if (getBrowser() != null) getBrowser().close();
    }

    // Quit Playwright
    public void quitPlaywright() {
        if (getPlaywright() != null) getPlaywright().close();
    }

    // Save video per scenario
    public void saveVideoForScenarioWithScenarioNames(String scenarioName) {
        try {
            Page page = getPage();
            if (page == null || page.video() == null) return;

            // Sanitize scenario name
            String sanitizedName = scenarioName.replaceAll("[^a-zA-Z0-9_]", "_") + ".webm";

            Path originalVideoPath = page.video().path();
            Path targetPath = Paths.get("target", "Execution_" + this.executionStartTime, "videos", sanitizedName);

            Files.move(originalVideoPath, targetPath);
            Log.info("Videos saved to: " + targetPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
