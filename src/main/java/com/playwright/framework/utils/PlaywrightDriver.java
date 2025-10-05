package com.playwright.framework.utils;

import com.microsoft.playwright.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

public class PlaywrightDriver {

    public static PlaywrightDriver playwrightDriver;
    public static Properties OR = new Properties();
    public static Properties config = new Properties();
    private static FileInputStream fis;

    // ThreadLocal instances for parallel execution
    private static ThreadLocal<Playwright> pw = new ThreadLocal<>();
    private static ThreadLocal<Browser> br = new ThreadLocal<>();
    private static ThreadLocal<BrowserContext> bc = new ThreadLocal<>();
    private static ThreadLocal<Page> pg = new ThreadLocal<>();

    // Global execution timestamp
    public static String executionStartTime;

    // Getters
    public static Playwright getPlaywright() { return pw.get(); }
    public static Browser getBrowser() { return br.get(); }
    public static BrowserContext getBrowserContext() { return bc.get(); }
    public static Page getPage() { return pg.get(); }

    // Private constructor
    private PlaywrightDriver() {
        try {
            // Load OR.properties
            fis = new FileInputStream("src/main/resources/properties/OR.properties");
            OR.load(fis);

            // Load config.properties
            fis = new FileInputStream("src/main/resources/properties/config.properties");
            config.load(fis);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load properties", e);
        }

        // Initialize global execution timestamp if not already set
        if (executionStartTime == null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d-MMM-yy_HH-mm");
            executionStartTime = LocalDateTime.now().format(formatter);
        }

        // Initialize Playwright
        pw.set(Playwright.create());

        // Launch browser based on config
        String browserName = config.getProperty("browser", "chrome").toLowerCase();
        Browser browser;
        switch (browserName) {
            case "chrome" -> browser = getPlaywright().chromium()
                    .launch(new BrowserType.LaunchOptions()
                            .setChannel("chrome")
                            .setHeadless(Boolean.parseBoolean(config.getProperty("headless", "false"))));
            case "firefox" -> browser = getPlaywright().firefox()
                    .launch(new BrowserType.LaunchOptions()
                            .setChannel("firefox")
                            .setHeadless(Boolean.parseBoolean(config.getProperty("headless", "false"))));
            case "webkit" -> browser = getPlaywright().webkit()
                    .launch(new BrowserType.LaunchOptions()
                            .setChannel("webkit")
                            .setHeadless(Boolean.parseBoolean(config.getProperty("headless", "false"))));
            default -> throw new IllegalArgumentException("Browser name is incorrect or not supported");
        }
        br.set(browser);

        // Create execution folder + videos folder
        Path executionFolder = Paths.get("target", "Execution_" + executionStartTime);
        Path videosFolder = executionFolder.resolve("videos");
        try {
            Files.createDirectories(videosFolder);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create execution/videos folder", e);
        }

        // Set Extent report path system property (so Index.html will go into this folder)
        System.setProperty("extent.reporter.spark.out", executionFolder.resolve("Index.html").toString());

        // Create BrowserContext with optional video recording
        Browser.NewContextOptions contextOptions = new Browser.NewContextOptions();
        if (config.getProperty("setVideoRecording", "false").equalsIgnoreCase("true")) {
            contextOptions.setRecordVideoDir(videosFolder);
        }
        BrowserContext browserContext = browser.newContext(contextOptions);
        bc.set(browserContext);

        // Create page
        pg.set(browserContext.newPage());
    }

    // Setup driver
    public static void setUpDriver() {
        playwrightDriver = new PlaywrightDriver();
    }

    // Open page
    public static void openPage(String url) {
        getPage().navigate(url);
    }

    // Close page, context, and browser safely
    public static void closeBrowser() {
        if (getPage() != null) getPage().close();
        if (getBrowserContext() != null) getBrowserContext().close();
        if (getBrowser() != null) getBrowser().close();
    }

    // Quit Playwright
    public static void quitPlaywright() {
        if (getPlaywright() != null) getPlaywright().close();
    }

    // Save video per scenario
    public static void saveVideoForScenario(String scenarioName) {
        try {
            Page page = getPage();
            if (page == null || page.video() == null) return;

            // Sanitize scenario name
            String sanitizedName = scenarioName.replaceAll("[^a-zA-Z0-9_]", "_") + ".webm";

            Path originalVideoPath = page.video().path();
            Path targetPath = Paths.get("target", "Execution_" + executionStartTime, "videos", sanitizedName);

            Files.move(originalVideoPath, targetPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
