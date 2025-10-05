package com.playwright.framework.log;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter;

public class Log {
    private static final Logger log = LogManager.getLogger(Log.class);

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
}
