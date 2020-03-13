package com.key.rocket_chat_webview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends AppCompatActivity {

    private WebView webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        webview = findViewById(R.id.webview);

        webview.setWebViewClient(new WebViewClient());
        webview.getSettings().setJavaScriptEnabled(true);
        // web.getSettings().setDomStorageEnabled(true);

        webview.getSettings().setAllowContentAccess(true);
        WebSettings webSettings = webview.getSettings();
        webSettings.setPluginState(WebSettings.PluginState.ON);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webview.canGoBack();
        webview.getSettings().setDomStorageEnabled(true);
        // webview.setWebChromeClient(new WebChromeClient());
        webview.getSettings().setLoadsImagesAutomatically(true);

        webview.getSettings().setAllowFileAccess(true);
        webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        //  webview.getSettings().setPluginState(WebSettings.PluginState.ON);
        webview.getSettings().setMediaPlaybackRequiresUserGesture(false);

        webview.loadUrl("https://chatkarloyaar.tk");

        webview.setWebChromeClient(new WebChromeClient() {
        });

        webview.setWebViewClient(new WebViewClient() {

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request,
                                        WebResourceError error) {
                super.onReceivedError(view, request, error);
                // Do something
                //  no_internet.setVisibility(View.VISIBLE);


                // webview.setBackgroundColor(getResources().getColor(R.color.white));
            }
        });
    }
}
