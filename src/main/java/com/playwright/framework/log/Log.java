package com.playwright.framework.log;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter;

public class Log {
    private static final Logger log = LogManager.getLogger(Log.class);
    private static final int BANNER_WIDTH = 100;

    private static void safeExtent(String decoratedMessage){
        try {
            ExtentCucumberAdapter.addTestStepLog(decoratedMessage);
        } catch (Throwable ignored) {
            // Handle exception silently
        }
    }

    public static void info(String message) {
        log.info(message);
        ExtentCucumberAdapter.addTestStepLog("ℹ️ " + message);
    }

    public static void warn(String message) {
        log.warn(message);
        ExtentCucumberAdapter.addTestStepLog("⚠️ " + message);
    }

    public static void error(String message) {
        log.error(message);
        ExtentCucumberAdapter.addTestStepLog("❌ " + message);
    }

    public static void pass(String message) {
        log.info("[PASS] " + message);
        ExtentCucumberAdapter.addTestStepLog("✅ " + message);
    }

    public static void fail(String message) {
        log.error("[FAIL] " + message);
        ExtentCucumberAdapter.addTestStepLog("❌ " + message);
    }

    private static String buildBannerLine(String msg) {
        String core = " " + msg.trim() + " ";
        if (core.length() >= BANNER_WIDTH - 2) {
            return "*" + core + "*";
        }
        int remaining = BANNER_WIDTH - core.length();
        int left = remaining / 2;
        int right = remaining - left;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < left; i++) sb.append('*');
        sb.append(core);
        for (int i = 0; i < right; i++) sb.append('*');
        return sb.toString();
    }
    private static void banner(String message, String type) {
        String line = buildBannerLine(message);
        switch (type) {
            case "PASS": pass(line); break;
            case "FAIL": fail(line); break;
            case "WARN": warn(line); break;
            default: info(line); break;
        }
    }

    public static void bannerInfo(String message) { banner(message, "INFO"); }
    public static void bannerWarn(String message) { banner(message, "WARN"); }
    public static void bannerPass(String message) { banner(message, "PASS"); }
    public static void bannerFail(String message) { banner(message, "FAIL"); }
}
