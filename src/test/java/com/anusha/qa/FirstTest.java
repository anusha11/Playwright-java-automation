package com.anusha.qa;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Map;
import java.util.HashMap;
import com.microsoft.playwright.options.RequestOptions;
import com.microsoft.playwright.options.WaitUntilState;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import com.microsoft.playwright.options.AriaRole;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

public class FirstTest {

    static Playwright playwright;
    static Browser browser;
    Page page;

    @BeforeEach
    void setup() {
        if (playwright == null) {
            playwright = Playwright.create();
            browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(Boolean.parseBoolean(System.getProperty("headless", "true"))));
        }
        page = browser.newPage();
    }

    @AfterEach
    void teardown() {
        page.close();
    }

    @AfterAll
    static void tearDownAll() {
        browser.close();
        playwright.close();
    }

    @Test
    void hasTitle() {
        page.navigate("https://practice.expandtesting.com", new Page.NavigateOptions().setTimeout(60000).setWaitUntil(WaitUntilState.DOMCONTENTLOADED));
        assertTrue(page.locator("text=Demos").isVisible());
        assertTrue(page.title().contains("Automation Testing Practice"));
    }

    @Test
    void loginTest() {
        page.navigate("https://practice.expandtesting.com/login", new Page.NavigateOptions().setTimeout(60000).setWaitUntil(WaitUntilState.DOMCONTENTLOADED));
        page.fill("#username", "practice");
        page.fill("#password", "SuperSecretPassword!");
        page.click("button[type='submit']");
        page.waitForSelector(".alert.alert-success", new Page.WaitForSelectorOptions().setTimeout(2000));
        assertTrue(page.locator(".alert.alert-success.alert").isVisible());
    }

    @Test
    void loginFailureTest() {
        page.navigate("https://practice.expandtesting.com/login", new Page.NavigateOptions().setTimeout(60000).setWaitUntil(WaitUntilState.DOMCONTENTLOADED));
        page.fill("#username", "wronguser");
        page.fill("#password", "wrongpassword");
        page.click("button[type='submit']");
        page.waitForSelector(".alert.alert-danger", new Page.WaitForSelectorOptions().setTimeout(2000));
        assertTrue(page.locator(".alert.alert-danger").isVisible());
    }

    @Test
    void dropdownAndCheckboxTest() {
        // Dropdown
        page.navigate("https://the-internet.herokuapp.com/dropdown", new Page.NavigateOptions().setTimeout(60000).setWaitUntil(WaitUntilState.DOMCONTENTLOADED));
        page.selectOption("#dropdown", "2");
        assertTrue(page.locator("#dropdown").inputValue().equals("2"));

        // Checkbox
        page.navigate("https://the-internet.herokuapp.com/checkboxes", new Page.NavigateOptions().setTimeout(60000).setWaitUntil(WaitUntilState.DOMCONTENTLOADED));
        page.locator("input[type='checkbox']").first().check();
        assertTrue(page.locator("input[type='checkbox']").first().isChecked());
    }

    @Test
    void loginTestWithPOM() {
        LoginPage loginPage = new LoginPage(page);

        loginPage.navigate();
        loginPage.login("practice", "SuperSecretPassword!");
        assertTrue(loginPage.isSuccessMessageVisible());
    }

    @Test
    void apiTest() {
        APIRequestContext request = playwright.request().newContext();
        APIResponse response = request.get("https://practice.expandtesting.com/notes/api/health-check");
        assertEquals(200, response.status());
        assertTrue(response.text().contains("Notes API is Running"));
        request.dispose();
    }

    @Test
    void apiPostTest() {
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

    @Test
    void putTest(){
            APIRequestContext request = playwright.request().newContext();
            Map <String, String> data = new HashMap<>();
            data.put("name", "Anusha");
            data.put("job", "Senior QA Automation Engineer");
            APIResponse response = request.put("https://reqres.in/api/users/2", RequestOptions.create().setData(data));
            assertEquals(response.status(),200);
            assertTrue(response.text().contains("Senior"));
            request.dispose();
    }

    @Test
    void exceptionHandlingTest() {
            page.navigate("https://the-internet.herokuapp.com/login", new Page.NavigateOptions().setTimeout(60000).setWaitUntil(WaitUntilState.DOMCONTENTLOADED));

            try {
                page.waitForSelector("#nonexistent-element", new Page.WaitForSelectorOptions().setTimeout(3000));
            } catch (TimeoutError e) {
                System.out.println("Expected failure caught: " + e.getMessage());
            }

            assertTrue(page.locator(".flash").count() == 0); // confirms we're still on a working page
    }

    @Test
    void practiceLoginTest() {
        page.navigate("https://practicetestautomation.com/practice-test-login/");
        page.fill("#username", "student");
        page.fill("#password", "Password123");
        page.click("#submit");
        assertTrue(page.locator(".post-title").isVisible());
    }

    @Test
    void codegenLoginTest() {
        page.navigate("https://practicetestautomation.com/practice-test-login/");
        page.getByLabel("Username").fill("student");
        page.getByLabel("Password").fill("Password123");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Submit")).click();
        assertThat(page.locator(".post-title")).isVisible();
    }
}