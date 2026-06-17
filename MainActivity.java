package com.cookiegrabber.app;

import android.os.Bundle;
import android.webkit.CookieManager;
import android.webkit.WebView;
import com.getcapacitor.BridgeActivity;

public class MainActivity extends BridgeActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WebView webView = getBridge().getWebView();

        // Enable cookies & 3rd party cookies
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.setAcceptThirdPartyCookies(webView, true);

        // Inject native cookie bridge into the JS context
        webView.addJavascriptInterface(
            new CookieBridge(cookieManager),
            "AndroidCookies"
        );

        // Enable JS, DOM storage, mixed content
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setMixedContentMode(
            android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        );

        // Do NOT override WebViewClient — Capacitor handles routing src/index.html
        // X-Frame-Options / CSP blocking is handled in index.html via JS navigation
    }
}
