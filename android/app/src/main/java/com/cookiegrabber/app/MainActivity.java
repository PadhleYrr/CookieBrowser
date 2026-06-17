package com.cookiegrabber.app;

import android.os.Bundle;
import android.webkit.CookieManager;
import android.webkit.WebView;
import com.getcapacitor.BridgeActivity;

public class MainActivity extends BridgeActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Enable cookies & 3rd party cookies on the WebView
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.setAcceptThirdPartyCookies(getBridge().getWebView(), true);

        // Inject native cookie bridge into the JS context
        getBridge().getWebView().addJavascriptInterface(
            new CookieBridge(cookieManager),
            "AndroidCookies"   // accessible as window.AndroidCookies in JS
        );
    }
}
