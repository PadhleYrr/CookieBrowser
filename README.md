# 🍪 Cookie Grabber

Minimal Android WebView browser that grabs cookies in one tap.

## What it does
- Full browser WebView — login to any site normally
- Tap **"Get Cookies"** → see all cookies for that page  
- Tap any cookie to copy `name=value`
- **Copy All** → copies all as `name=val; name2=val2` (ready to paste)
- **JSON** → copies as `{ "name": "value" }` format
- Reads **HttpOnly cookies** too (via native Android CookieManager bridge)

---

## Setup: First time only

You need a Capacitor Android project initialized. If you've already done this, skip to step 3.

### 1. Init Capacitor Android (one time)
```bash
npm install
npx cap add android
```

### 2. Copy the native Java files
Copy these into your android project:
- `android/app/src/main/java/com/cookiegrabber/app/MainActivity.java`
- `android/app/src/main/java/com/cookiegrabber/app/CookieBridge.java`

### 3. Push to GitHub → APK builds automatically
```bash
git add .
git commit -m "Initial build"
git push
```

Then go to **Actions** tab on GitHub → download the APK from **Artifacts**.

---

## How the cookie reading works

```
WebView (chromium)
    └─ Android CookieManager  ← stores ALL cookies including HttpOnly
            └─ CookieBridge.java (JavascriptInterface)
                    └─ window.AndroidCookies.getCookies(url)  ← JS calls this
                            └─ Your UI shows + copies them
```

The key is `addJavascriptInterface` in `MainActivity.java` — this is what lets the web page talk to Android's native cookie store, which has access to cookies that `document.cookie` can't see (like HttpOnly ones).

---

## Project structure
```
cookie-app/
├── src/
│   └── index.html              ← The entire app UI
├── android/
│   └── app/src/main/java/com/cookiegrabber/app/
│       ├── MainActivity.java   ← Injects cookie bridge
│       └── CookieBridge.java   ← @JavascriptInterface methods
├── capacitor.config.json
├── package.json
└── .github/workflows/build.yml ← GitHub Actions APK builder
```
