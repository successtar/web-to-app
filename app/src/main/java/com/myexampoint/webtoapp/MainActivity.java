package com.myexampoint.webtoapp;


import android.content.DialogInterface;
import android.net.http.SslError;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    /**
     * If set to true, the web view will be hidden on app launch until the target web page has
     * finished loading.
     */
    Boolean hideWebViewOnInitialLoad = true;

    /**
     * The URL the app will open on.
     */
    String defaultUrl = "https://www.bbc.co.uk/cbbc";

    /**
     * An optional list of URL prefixes which the user is allowed to navigate to. If the user
     * attempts to navigate (eg: tapping a link) to a URL which does not start with one of the
     * values in this list, the navigation will be prevented and a toast message displayed. This
     * is useful if you want to keep the user inside a specfic part of the internet (eg: your
     * domain).
     *
     * If this list is left empty, the user will be allowed to navigate to any URL.
     */
    List<String> allowedUrlPrefixes = Arrays.asList(
            "https://www.bbc.co.uk/cbbc",
            "https://www.bbc.co.uk/games/embed"
    );

    private Boolean initialLoadComplete = false;
    private WebView webview ;
    private ProgressBar spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webview =(WebView)findViewById(R.id.webView);
        spinner = (ProgressBar)findViewById(R.id.progressBar1);
        webview.setWebViewClient(new CustomWebViewClient());

        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setDomStorageEnabled(true);
        webview.setOverScrollMode(WebView.OVER_SCROLL_NEVER);
        webview.loadUrl(defaultUrl);
    }


    /**
    * This allows for a splash screen
    *  Hide elements once the page loads
    * Show custom error page
    * Resolve issue with SSL certificate
    **/

    private class CustomWebViewClient extends WebViewClient {

        // Handle SSL issue
        @Override
        public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {


            final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

            builder.setMessage(R.string.notification_error_ssl_cert_invalid);

            builder.setPositiveButton("continue", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    handler.proceed();
                }
            });

            builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    handler.cancel();
                }
            });

            final AlertDialog dialog = builder.create();

            dialog.show();
        }

        @Override
        public void onPageStarted(WebView webview, String url, Bitmap favicon) {
            if (!initialLoadComplete) {
                if (hideWebViewOnInitialLoad) {
                    webview.setVisibility(View.INVISIBLE);
                }
                initialLoadComplete = true;
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            spinner.setVisibility(View.GONE);
            view.setVisibility(View.VISIBLE);
            super.onPageFinished(view, url);

        }

        // Show custom error page
        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            defaultUrl = view.getUrl();
            Log.w("web-to-app", "onReceivedError: description: " + description + " ,view.getUrl(): " + view.getUrl());
            setContentView(R.layout.error);
            super.onReceivedError(view, errorCode, description, failingUrl);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (allowedUrlPrefixes.isEmpty() || url.equals(defaultUrl)) return false;

            for (String s : allowedUrlPrefixes) {
                if (url.startsWith(s)) {
                    return false; // URL is allowed, don't override navigation.
                }
            }

            // URL is not allowed, override (prevent) navigation.
            Toast.makeText(MainActivity.this,
                    R.string.toast_navigation_prevented, Toast.LENGTH_SHORT).show();
            return true;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (webview.canGoBack()) {
                        webview.goBack();
                    }
                    else {

                        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                        builder.setMessage(R.string.exit_app);

                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                finish();
                            }
                        });

                        builder.setNegativeButton("No", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                // Do nothing
                            }
                        });

                        final AlertDialog dialog = builder.create();

                        dialog.show();
                    }
                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }

    /* Retry Loading the page */
    public void tryAgain(View v){
        setContentView(R.layout.activity_main);
        webview =(WebView)findViewById(R.id.webView);
        spinner = (ProgressBar)findViewById(R.id.progressBar1);
        webview.setWebViewClient(new CustomWebViewClient());

        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setDomStorageEnabled(true);
        webview.setOverScrollMode(WebView.OVER_SCROLL_NEVER);
        webview.loadUrl(defaultUrl);
    }
}

