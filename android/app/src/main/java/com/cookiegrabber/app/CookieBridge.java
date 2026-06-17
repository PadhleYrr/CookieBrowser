package com.cookiegrabber.app;

import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;

/**
 * Exposes Android's CookieManager to JavaScript via addJavascriptInterface.
 * In JS: window.AndroidCookies.getCookies(url) → "name=value; name2=value2"
 */
public class CookieBridge {

    private final CookieManager cookieManager;

    public CookieBridge(CookieManager cookieManager) {
        this.cookieManager = cookieManager;
    }

    /**
     * Returns the raw cookie string for the given URL.
     * Android's CookieManager returns cookies for the exact URL's domain —
     * this includes HttpOnly cookies that JS can't see via document.cookie.
     *
     * @param url  full URL of the page (e.g. "https://example.com/path")
     * @return     cookie string "name=val; name2=val2" or "" if none
     */
    @JavascriptInterface
    public String getCookies(String url) {
        if (url == null || url.isEmpty()) return "";
        String cookies = cookieManager.getCookie(url);
        return cookies != null ? cookies : "";
    }

    /**
     * Set a cookie for a URL.
     * @param url    target URL
     * @param cookie "name=value; path=/; domain=.example.com"
     */
    @JavascriptInterface
    public void setCookie(String url, String cookie) {
        if (url != null && cookie != null) {
            cookieManager.setCookie(url, cookie);
            cookieManager.flush();
        }
    }

    /**
     * Remove all cookies.
     */
    @JavascriptInterface
    public void clearAllCookies() {
        cookieManager.removeAllCookies(null);
        cookieManager.flush();
    }
}
