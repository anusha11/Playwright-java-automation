package com.anusha.qa;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Map;
import java.util.HashMap;
import com.microsoft.playwright.options.RequestOptions;

public class FirstTest {
    @Test
    void hasTitle() {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
            Page page = browser.newPage();
            page.navigate("https://playwright.dev");
            assertTrue(page.locator("text=Get started").isVisible());
            assertTrue(page.title().contains("Playwright"));
            browser.close();
        }
    }

    @Test
    void loginTest() {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
            Page page = browser.newPage();
            page.navigate("https://the-internet.herokuapp.com/login");
            page.fill("#username", "tomsmith");
            page.fill("#password", "SuperSecretPassword!");
            page.click("button[type='submit']");
            assertTrue(page.locator(".flash.success").isVisible());
            browser.close();
        }
    }

    @Test
    void loginFailureTest() {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
            Page page = browser.newPage();
            page.navigate("https://the-internet.herokuapp.com/login");
            page.fill("#username", "wronguser");
            page.fill("#password", "wrongpassword");
            page.click("button[type='submit']");
            assertTrue(page.locator(".flash.error").isVisible());
            browser.close();
        }
    }

    @Test
    void dropdownAndCheckboxTest() {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
            Page page = browser.newPage();

            // Dropdown
            page.navigate("https://the-internet.herokuapp.com/dropdown");
            page.selectOption("#dropdown", "2");
            assertTrue(page.locator("#dropdown").inputValue().equals("2"));

            // Checkbox
            page.navigate("https://the-internet.herokuapp.com/checkboxes");
            page.locator("input[type='checkbox']").first().check();
            assertTrue(page.locator("input[type='checkbox']").first().isChecked());

            page.waitForTimeout(2000);
            browser.close();
        }
    }

    @Test
    void loginTestWithPOM() {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
            Page page = browser.newPage();
            LoginPage loginPage = new LoginPage(page);

            loginPage.navigate();
            loginPage.login("tomsmith", "SuperSecretPassword!");
            assertTrue(loginPage.isSuccessMessageVisible());

            page.waitForTimeout(2000);
            browser.close();
        }
    }

    @Test
    void apiTest() {
        try (Playwright playwright = Playwright.create()) {
            APIRequestContext request = playwright.request().newContext();
            APIResponse response = request.get("https://reqres.in/api/users/2");
            assertEquals(200, response.status());
            assertTrue(response.text().contains("Janet"));
            request.dispose();
        }
    }

    @Test
    void apiPostTest() {
        try (Playwright playwright = Playwright.create()) {
            APIRequestContext request = playwright.request().newContext();
            Map<String, String> data = new HashMap<>();
            data.put("name", "Anusha");
            data.put("job", "QA Automation Engineer");

            APIResponse response = request.post("https://reqres.in/api/users",
            RequestOptions.create().setData(data));
            assertEquals(201, response.status());
            assertTrue(response.text().contains("Anusha"));
            request.dispose();
        }
    }

    @Test
    void putTest(){
        try(Playwright playwright = Playwright.create()){
            APIRequestContext request = playwright.request().newContext();
            Map <String, String> data = new HashMap<>();
            data.put("name", "Anusha");
            data.put("job", "Senior QA Automation Engineer");
            APIResponse response = request.put("https://reqres.in/api/users/2", RequestOptions.create().setData(data));
            assertEquals(response.status(),200);
            assertTrue(response.text().contains("Senior"));
            request.dispose();
        }
    }

    @Test
    void explicitWaitTest() {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
            Page page = browser.newPage();
            page.navigate("https://the-internet.herokuapp.com/dynamic_loading/1");
            page.click("button");
            page.waitForSelector("#finish", new Page.WaitForSelectorOptions().setTimeout(10000));
            assertTrue(page.locator("#finish").isVisible());
            page.waitForTimeout(1000);
            browser.close();
        }
    }
}