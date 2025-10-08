package com.playwright.framework.base;

import com.microsoft.playwright.Frame;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.PlaywrightException;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.SelectOption;
import com.microsoft.playwright.options.WaitForSelectorState;
import com.playwright.framework.log.FrameLoggingConfig;
import org.junit.Assert;
import com.playwright.framework.log.Log;
import com.playwright.framework.driver_manager.PlaywrightDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BasePage {
    private static final int DEFAULT_TIMEOUT = 2000;
    @Autowired
    private PlaywrightDriver playwrightDriver;
//  ------------------------------------------- Element Actions --------------------------------------------------
    public void navigateToUrl(String url) {
        try {
            Page page = this.playwrightDriver.getPage();
            page.navigate(url);
            page.waitForLoadState(LoadState.NETWORKIDLE);
            Log.info("Successfully navigated to url:" + url);
        } catch(Exception ex) {
            Log.error("Failed to navigate to url:" + url);
            Assert.fail(ex.getMessage());
        }
    }
    public void click(Object locatorOrStrategy) {
        Locator locator = resolveLocator(locatorOrStrategy);
        if (locator == null || locator.count() == 0) {
            Assert.fail("Element not found: " + locatorOrStrategy);
        }
        try {
            locator.click();
            Log.info("Successfully clicked on: " + locatorOrStrategy);
        } catch (Throwable tx) {
            Log.error("Failed to click on: " + locatorOrStrategy);
            Assert.fail(tx.getMessage());
        }
    }
    public void mouseHover(Object locatorOrStrategy) {
        Locator locator = resolveLocator(locatorOrStrategy);
        if (locator == null) {
            Assert.fail("Element not found for hover: " + locatorOrStrategy);
        }
        try {
            locator.hover();
            Log.info("Successfully Mouse Hovered on: " + locatorOrStrategy);
        } catch (Throwable tx) {
            Log.error("Failed to Mouse Hover on: " + locatorOrStrategy);
            Assert.fail(tx.getMessage());
        }
    }
    public void type(Object locatorOrStrategy, String value) {
        Locator locator = resolveLocator(locatorOrStrategy);
        if (locator == null) {
            Assert.fail("Element not found for type: " + locatorOrStrategy);
        }
        try {
            locator.fill(value);
            Log.info("Successfully typed in: " + locatorOrStrategy);
        } catch (Throwable tx) {
            Log.error("Failed to type in: " + locatorOrStrategy);
            Assert.fail(tx.getMessage());
        }
    }
    public void select(Object locatorOrStrategy, String value) {
        Locator locator = resolveLocator(locatorOrStrategy);
        if (locator == null) {
            Assert.fail("Select element not found: " + locatorOrStrategy);
        }
        try {
            locator.selectOption(new SelectOption().setLabel(value));
            Log.info("Successfully selected option on: " + locatorOrStrategy + " label: " + value);
        } catch (Throwable tx) {
            Log.error("Failed to select option on: " + locatorOrStrategy + " label: " + value);
            Assert.fail(tx.getMessage());
        }
    }
    public String getInnerText(Object locatorOrStrategy) {
        try {
            Locator locator = resolveLocator(locatorOrStrategy);
            if (locator == null) {
                Assert.fail("Element not found for getInnerText: " + locatorOrStrategy);
            }
            locator.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(2000));
            String text = locator.innerText();
            Log.info("Successfully fetched text from " + locatorOrStrategy + ": " + text);
            return text;
        } catch (Exception e) {
            Log.error("Failed to fetch text from " + locatorOrStrategy + ": " + e.getMessage());
            Assert.fail("Failed to fetch text from: " + locatorOrStrategy + " - " + e.getMessage());
            return "";
        }
    }
    public void selectOptionFromDropdown(Object dropdownLocatorOrStrategy, Object optionsLocatorOrStrategy, String optionText) {
        click(dropdownLocatorOrStrategy);
        try {
            Locator options = resolveLocator(optionsLocatorOrStrategy);
            if (options == null || options.count() == 0) {
                Assert.fail("Options not found for: " + optionsLocatorOrStrategy);
            }
            Locator matchingOption = options.filter(new Locator.FilterOptions().setHasText(optionText)).first();
            if (matchingOption != null && matchingOption.count() > 0) {
                matchingOption.click();
                Log.info("Selected option '" + optionText + "' from dropdown: " + dropdownLocatorOrStrategy);
            } else {
                Log.error("Option '" + optionText + "' not found in dropdown: " + dropdownLocatorOrStrategy);
                Assert.fail("Option not found: " + optionText);
            }
        } catch (Exception e) {
            Log.error("Failed to select option '" + optionText + "' from dropdown: " + dropdownLocatorOrStrategy);
            Assert.fail("Error selecting option: " + e.getMessage());
        }
    }
    public void clearText(Object locatorOrStrategy) {
        Locator locator = resolveLocator(locatorOrStrategy);
        if (locator == null) {
            Assert.fail("Element not found for clearText: " + locatorOrStrategy);
        }
        try {
            locator.clear();
            Log.info("Successfully cleared text in: " + locatorOrStrategy);
        } catch (Throwable tx) {
            Log.error("Failed to clear text in: " + locatorOrStrategy);
            Assert.fail(tx.getMessage());
        }
    }

//  ------------------------------------------- Element Verification ---------------------------------------------
    public void isTextMatching(Object locatorOrStrategy, String expectedText, int timeoutMillis) {
        Locator element = resolveLocator(locatorOrStrategy);
        if (element == null) {
            Log.error("Element not found for text match: " + locatorOrStrategy);
            Assert.fail("Element not found for text match: " + locatorOrStrategy);
        }
        try {
            highlightElement(element);
            element.waitFor(new Locator.WaitForOptions()
                    .setState(WaitForSelectorState.VISIBLE)
                    .setTimeout(timeoutMillis));
            Assert.assertEquals("Verification of Text in " +element + ": FAILED",expectedText, element.innerText().trim());
            Log.pass("Text verification passed for element: " + locatorOrStrategy + ". \nActual text: '" + element.innerText().trim() + "\nExpected text: '" + expectedText + "'.");
        }catch (AssertionError e){
            highlightElement(element, "color: red");
            Log.error("Text verification failed for element: " + locatorOrStrategy + ".");
            Assert.fail(e.getMessage());
        }
    }public void isTextMatching(Object locatorOrStrategy, String expectedText) {
        isTextMatching(locatorOrStrategy, expectedText, DEFAULT_TIMEOUT);
    }
    public void isElementVisible(Object locatorOrStrategy, int timeoutMillis) {
        Locator locator = null;
        locator = resolveLocator(locatorOrStrategy);
        if (locator == null) {
            Log.error("Element not found for visibility check: " + locatorOrStrategy);
            Assert.fail("Element not found for visibility check: " + locatorOrStrategy);
        }
        try {
            locator.waitFor(new Locator.WaitForOptions()
                    .setState(WaitForSelectorState.VISIBLE)
                    .setTimeout(timeoutMillis));
            Log.pass("Element is visible: " + locatorOrStrategy);
        } catch (PlaywrightException e) {
            highlightElement(locator, "red");
            Log.error("Element not visible within " + timeoutMillis + " ms: " + locatorOrStrategy);
            Assert.fail(e.getMessage());
        }
    }
    public void isElementVisible(Object locatorOrStrategy) {
        isElementVisible(locatorOrStrategy, DEFAULT_TIMEOUT);
    }

//    ------------------------------------------- Element Highlighting -------------------------------------------
//    **********************Highlights elements for better visibility during test execution***********************
//    ------------------------------------------------------------------------------------------------------------
    public Locator highlightElement(Object locatorOrStrategy, String color) {
        if (locatorOrStrategy instanceof Locator) {
            return highlightElement((Locator) locatorOrStrategy, color);
        }
        try {
            Locator locator = resolveLocator(locatorOrStrategy);
            if (locator == null) {
                Log.warn("Could not highlight element (not found): '" + locatorOrStrategy + "'");
                return null;
            }
            locator.waitFor(new Locator.WaitForOptions()
                    .setState(WaitForSelectorState.ATTACHED)
                    .setTimeout(2000));
            locator.evaluate("el => { el.style.border='3px solid " + color + "'; el.style.backgroundColor='" + color + "33'; }");
            if (FrameLoggingConfig.resolve()) Log.info("Highlighted element '" + locatorOrStrategy + "' with color '" + color + "'");
            return locator;
        } catch (Exception e) {
            Log.warn("Could not highlight element '" + locatorOrStrategy + "': " + e.getMessage());
            return null;
        }
    }
    public Locator highlightElement(Object locatorOrStrategy) {
        return highlightElement(locatorOrStrategy, "blue");
    }
    public Locator highlightElement(Locator locator, String color) {
        if (locator == null) return null;
        try {
            locator.waitFor(new Locator.WaitForOptions()
                    .setState(WaitForSelectorState.ATTACHED)
                    .setTimeout(2000));
            locator.evaluate("el => { el.style.border='3px solid " + color + "'; el.style.backgroundColor='" + color + "33'; }");            if (FrameLoggingConfig.resolve()) Log.info("Highlighted resolved locator with color " + color);
            return locator;
        } catch (Exception e) {
            Log.warn("Could not highlight resolved locator: " + e.getMessage());
            return null;
        }
    }
    public Locator highlightElement(Locator locator) {
        return highlightElement(locator, "blue");
    }
// ------------------------------------------- Element Actions -------------------------------------------
//    Locator resolution supports:
//    1. Direct Locator object
//    2. OR key from OR.properties file
//    3. Locator strategy string (text=, label=, alt=, placeholder=, title=, testid=, role=)
//    4. Searches in main page context first, then in frames if not found
//    5. Logs detailed info if FrameLoggingConfig is enabled
//    6. Returns null if element not found
//    ----------------------------------------------------------------------------------------------------
    private Locator resolveLocator(Object locatorOrStrategy) {
        Page page = this.playwrightDriver.getPage();
        boolean verbose = FrameLoggingConfig.resolve();
        if (locatorOrStrategy instanceof Locator) {
            return (Locator) locatorOrStrategy;
        }
        if (locatorOrStrategy instanceof String) {
            String keyOrType = (String) locatorOrStrategy;
            String orLocator = PlaywrightDriver.OR.getProperty(keyOrType);
            if (orLocator != null && !orLocator.isEmpty()) {
                Locator loc = page.locator(orLocator);
                if (loc.count() > 0) return loc;
                if (verbose) Log.info("Element not present in main context for " + keyOrType + " (OR key). Searching frames...");
                int frameIdx = 0;
                for (Frame frame : page.frames()) {
                    if (frame == page.mainFrame()) continue;
                    Locator frameLoc = frame.locator(orLocator);
                    if (frameLoc.count() > 0) {
                        if (verbose) Log.info("Element for OR key " + keyOrType + " found in frame " + safeFrameName(frame) + " (index=" + frameIdx + ")");
                        return frameLoc;
                    }
                    frameIdx++;
                }
                Log.warn("Element for OR key " + keyOrType + " not found in any frame.");
                return null;
            }
            Locator loc = resolveByType(page, keyOrType);
            if (loc != null && loc.count() > 0) return loc;
            if (verbose) Log.info("Element not present in main context for strategy " + keyOrType + ". Searching frames...");
            int frameIdx = 0;
            for (Frame frame : page.frames()) {
                if (frame == page.mainFrame()) continue;
                Locator frameLoc = resolveByType(frame, keyOrType);
                if (frameLoc != null && frameLoc.count() > 0) {
                    if (verbose) Log.info("Element for strategy " + keyOrType + " found in frame " + safeFrameName(frame) + " (index=" + frameIdx + ")");
                    return frameLoc;
                }
                frameIdx++;
            }
            Log.warn("Element for strategy " + keyOrType + " not found in any frame.");
            return null;
        }
        return null;
    }
    private Locator resolveByType(Object pageOrFrame, String keyOrType) {
        if (keyOrType.startsWith("text=")) {
            String value = keyOrType.substring(5);
            if (pageOrFrame instanceof Page) return ((Page) pageOrFrame).getByText(value);
            return ((Frame) pageOrFrame).locator("text=" + value);
        }
        if (keyOrType.startsWith("label=")) {
            String value = keyOrType.substring(6);
            if (pageOrFrame instanceof Page) return ((Page) pageOrFrame).getByLabel(value);
            return ((Frame) pageOrFrame).locator("label=" + value);
        }
        if (keyOrType.startsWith("alt=")) {
            String value = keyOrType.substring(4);
            if (pageOrFrame instanceof Page) return ((Page) pageOrFrame).getByAltText(value);
            return ((Frame) pageOrFrame).locator("alt=" + value);
        }
        if (keyOrType.startsWith("placeholder=")) {
            String value = keyOrType.substring(12);
            if (pageOrFrame instanceof Page) return ((Page) pageOrFrame).getByPlaceholder(value);
            return ((Frame) pageOrFrame).locator("placeholder=" + value);
        }
        if (keyOrType.startsWith("title=")) {
            String value = keyOrType.substring(6);
            if (pageOrFrame instanceof Page) return ((Page) pageOrFrame).getByTitle(value);
            return ((Frame) pageOrFrame).locator("title=" + value);
        }
        if (keyOrType.startsWith("testid=")) {
            String value = keyOrType.substring(7);
            if (pageOrFrame instanceof Page) return ((Page) pageOrFrame).getByTestId(value);
            return ((Frame) pageOrFrame).locator("[data-testid=\"" + value + "\"]");
        }
        if (keyOrType.startsWith("role=")) {
            String roleQuery = keyOrType.substring(5);
            String roleType;
            String name = null;
            if (roleQuery.contains("[name=")) {
                int roleEnd = roleQuery.indexOf("[");
                roleType = roleQuery.substring(0, roleEnd);
                int nameStart = roleQuery.indexOf("name=") + 6;
                int nameEnd = roleQuery.indexOf("]", nameStart);
                name = roleQuery.substring(nameStart, nameEnd);
            } else {
                roleType = roleQuery;
            }
            try {
                AriaRole ariaRole = AriaRole.valueOf(roleType.toUpperCase());
                if (pageOrFrame instanceof Page) {
                    return name != null ? ((Page) pageOrFrame).getByRole(ariaRole, new Page.GetByRoleOptions().setName(name))
                            : ((Page) pageOrFrame).getByRole(ariaRole);
                } else {
                    return name != null ? ((Frame) pageOrFrame).getByRole(ariaRole, new Frame.GetByRoleOptions().setName(name))
                            : ((Frame) pageOrFrame).getByRole(ariaRole);
                }
            } catch (IllegalArgumentException ex) {
                Log.error("Invalid role type: " + roleType);
                return null;
            }
        }
        return null;
    }
    private String safeFrameName(Frame frame) {
        try {
            String name = frame.name();
            if (name != null && !name.isBlank()) return name;
            String url = frame.url();
            if (url != null && !url.isBlank()) return url;
            return "(unnamed)";
        } catch (Exception e) {
            return "(unavailable)";
        }
    }
}
