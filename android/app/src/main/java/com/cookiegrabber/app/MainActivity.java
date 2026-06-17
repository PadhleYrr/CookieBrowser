package com.cookiegrabber.app;

import android.os.Bundle;
import android.webkit.CookieManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
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

        // Strip headers that block WebView loading (X-Frame-Options, CSP)
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                WebResourceResponse response = super.shouldInterceptRequest(view, request);
                if (response != null && response.getResponseHeaders() != null) {
                    java.util.Map<String, String> headers = new java.util.HashMap<>(response.getResponseHeaders());
                    headers.remove("X-Frame-Options");
                    headers.remove("x-frame-options");
                    headers.remove("Content-Security-Policy");
                    headers.remove("content-security-policy");
                    response.setResponseHeaders(headers);
                }
                return response;
            }
        });

        // Enable JS, DOM storage, mixed content
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setMixedContentMode(
            android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        );
    }
}
