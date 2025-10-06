package com.playwright.framework.utils.shutdown_utils;

import com.playwright.framework.utils.report_utils.ReportDirectoryMergerAndCleaner;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ShutdownListener {

    @EventListener
    public void handleContextClosedEvent(ContextClosedEvent event) {
        System.out.println("🛑 Spring context is closing. Running file cleanup...");
        try {
            ReportDirectoryMergerAndCleaner.main(new String[]{});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
