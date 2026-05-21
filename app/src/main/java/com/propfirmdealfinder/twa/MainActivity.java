package com.propfirmdealfinder.twa;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class MainActivity extends AppCompatActivity {

    private static final String HOME_URL = "https://propfirmdealfinder.com";
    private static final String HOST = "propfirmdealfinder.com";

    private WebView webView;
    private SwipeRefreshLayout swipeRefresh;
    private FrameLayout splashScreen;
    private FrameLayout errorScreen;
    private boolean hasLoaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Immersive dark status bar and nav bar
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#060810"));
        window.setNavigationBarColor(Color.parseColor("#060810"));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            window.getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }

        setContentView(R.layout.activity_main);

        splashScreen = findViewById(R.id.splash_screen);
        errorScreen = findViewById(R.id.error_screen);
        swipeRefresh = findViewById(R.id.swipe_refresh);
        webView = findViewById(R.id.webview);

        // SwipeRefreshLayout
        swipeRefresh.setColorSchemeColors(
            Color.parseColor("#22C55E"),
            Color.parseColor("#3B82F6")
        );
        swipeRefresh.setProgressBackgroundColorSchemeColor(Color.parseColor("#060810"));
        swipeRefresh.setOnRefreshListener(() -> webView.reload());

        // Retry button
        findViewById(R.id.retry_button).setOnClickListener(v -> {
            if (isNetworkAvailable()) {
                errorScreen.setVisibility(View.GONE);
                splashScreen.setVisibility(View.VISIBLE);
                webView.loadUrl(HOME_URL);
            }
        });

        setupWebView();

        // Load URL
        if (isNetworkAvailable()) {
            webView.loadUrl(HOME_URL);
        } else {
            splashScreen.setVisibility(View.GONE);
            errorScreen.setVisibility(View.VISIBLE);
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setupWebView() {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setAllowFileAccess(false);
        settings.setAllowContentAccess(false);
        settings.setMediaPlaybackRequiresUserGesture(true);
        settings.setMixedContentMode(WebSettings.MIXED_CONTENT_NEVER_ALLOW);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setSupportZoom(false);
        settings.setBuiltInZoomControls(false);

        // Enable cookies
        CookieManager.getInstance().setAcceptCookie(true);
        CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true);

        webView.setBackgroundColor(Color.parseColor("#060810"));
        webView.setOverScrollMode(View.OVER_SCROLL_NEVER);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String host = request.getUrl().getHost();
                if (host != null && (host.equals(HOST) || host.endsWith("." + HOST))) {
                    return false; // Load in app
                }
                // External links open in browser
                Intent intent = new Intent(Intent.ACTION_VIEW, request.getUrl());
                startActivity(intent);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (!hasLoaded) {
                    hasLoaded = true;
                    splashScreen.setVisibility(View.GONE);
                }
                swipeRefresh.setRefreshing(false);
                errorScreen.setVisibility(View.GONE);
                webView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request,
                                        WebResourceError error) {
                if (request.isForMainFrame() && !hasLoaded) {
                    splashScreen.setVisibility(View.GONE);
                    errorScreen.setVisibility(View.VISIBLE);
                    webView.setVisibility(View.GONE);
                }
                swipeRefresh.setRefreshing(false);
            }
        });

        webView.setWebChromeClient(new WebChromeClient());
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        webView.onResume();
    }

    @Override
    protected void onPause() {
        webView.onPause();
        super.onPause();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return info != null && info.isConnected();
    }
}
