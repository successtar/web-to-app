package com.myexampoint.webtoapp;


import android.content.DialogInterface;
import android.net.http.SslError;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.os.Handler;
import android.webkit.WebSettings;


public class MainActivity extends AppCompatActivity {

    String ShowOrHideWebViewInitialUse = "show";
    private WebView webview ;
    private ProgressBar spinner;
    String myurl = "https://successtar.github.io"; //Change this  to your website hostname

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webview =(WebView)findViewById(R.id.webView);
        spinner = (ProgressBar)findViewById(R.id.progressBar1);
        webview.setWebViewClient(new CustomWebViewClient());

        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setDomStorageEnabled(true);
        webview.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        webview.setOverScrollMode(WebView.OVER_SCROLL_NEVER);
        webview.loadUrl(myurl);
    }

    /**
    * This allows for a splash screen
    * Hide elements once the page loads
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

            // only make it invisible the FIRST time the app is run
            if (ShowOrHideWebViewInitialUse.equals("show")) {
                webview.setVisibility(webview.INVISIBLE);
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {

            ShowOrHideWebViewInitialUse = "hide";
            spinner.setVisibility(View.GONE);

            view.setVisibility(webview.VISIBLE);
            super.onPageFinished(view, url);

        }

        // Show custom error page
        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            myurl = view.getUrl();
            setContentView(R.layout.error);
            super.onReceivedError(view, errorCode, description, failingUrl);
        }


    }

    private boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (webview.canGoBack()) {
            webview.goBack();
        } else {
            if (doubleBackToExitPressedOnce) {
                finish();
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, R.string.exit_app, Toast.LENGTH_SHORT).show();
    
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce=false;
                }
            }, 2000);
        }
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
        webview.loadUrl(myurl);
    }
}

