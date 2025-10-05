package com.playwright.framework.utils;

import com.playwright.framework.reporting.FileMoverAndDeleteFolder;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ShutdownListener {

    @EventListener
    public void handleContextClosedEvent(ContextClosedEvent event) {
        System.out.println("🛑 Spring context is closing. Running file cleanup...");
        try {
            FileMoverAndDeleteFolder.main(new String[]{}); // call your existing main method
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
