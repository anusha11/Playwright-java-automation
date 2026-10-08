package com.anusha.qa;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitUntilState;

public class LoginPage {
    private final Page page;

    public LoginPage(Page page) {
        this.page = page;
    }

    public void navigate() {
        page.navigate("https://practice.expandtesting.com/login", new Page.NavigateOptions().setTimeout(60000).setWaitUntil(WaitUntilState.DOMCONTENTLOADED));
    }

    public void login(String username, String password) {
        page.fill("#username", username);
        page.fill("#password", password);
        page.click("button[type='submit']");
        page.waitForSelector(".alert.alert-success", new Page.WaitForSelectorOptions().setTimeout(2000));
    }

    public boolean isSuccessMessageVisible() {
        return page.locator(".alert.alert-success").isVisible();
    }

    public boolean isErrorMessageVisible() {
        return page.locator(".alert.alert-danger").isVisible();
    }
}