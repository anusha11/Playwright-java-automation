package com.anusha.qa;

import com.microsoft.playwright.Page;

public class LoginPage {
    private final Page page;

    public LoginPage(Page page) {
        this.page = page;
    }

    public void navigate() {
        page.navigate("https://the-internet.herokuapp.com/login");
    }

    public void login(String username, String password) {
        page.fill("#username", username);
        page.fill("#password", password);
        page.click("button[type='submit']");
    }

    public boolean isSuccessMessageVisible() {
        return page.locator(".flash.success").isVisible();
    }

    public boolean isErrorMessageVisible() {
        return page.locator(".flash.error").isVisible();
    }
}