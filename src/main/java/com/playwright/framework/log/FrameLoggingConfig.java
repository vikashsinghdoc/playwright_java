package com.playwright.framework.log;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class FrameLoggingConfig {
    private static volatile Boolean cached;
    private FrameLoggingConfig() {}

    public static boolean resolve() {
        if (cached != null) return cached;
        synchronized (FrameLoggingConfig.class) {
            if (cached != null) return cached;
            String v = System.getenv("FRAME_SEARCH_LOGGING");
            if (v != null) return cache(parseBool(v));
            Properties props = loadProps();
            if (props != null) {
                v = props.getProperty("frame.search.logging");
                if (v != null) return cache(parseBool(v));
            }
            return cache(false);
        }
    }
    public static void clearCache() { cached = null; }

    private static boolean parseBool(String raw) { return Boolean.parseBoolean(raw.trim()); }

    private static Properties loadProps() {
        try (InputStream in = FrameLoggingConfig.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (in == null) return null;
            Properties p = new Properties();
            p.load(in);
            return p;
        } catch (IOException e) {
            return null; // silent fallback
        }
    }

    private static boolean cache(boolean val) { cached = val; return val; }
}