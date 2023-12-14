package dev.kai.mgdg008;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class GlblWebview<OnPageStartedListener> extends WebView {

    private OnPageStartedListener onPageStartedListener;

    public GlblWebview(Context context) {
        super(context);
        initWebViewSettings();
    }

    public GlblWebview(Context context, AttributeSet attrs) {
        super(context, attrs);
        initWebViewSettings();
    }

    public GlblWebview(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initWebViewSettings();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initWebViewSettings() {
        WebSettings webSettings = getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webSettings.setDomStorageEnabled(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setSupportMultipleWindows(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        setWebViewClient(new CustomWebClient());
        addJavascriptInterface(new JSInterface(), "jsBridge");
    }

    public void setOnPageStartedListener(OnPageStartedListener listener) {
        this.onPageStartedListener = listener;
    }

    private class CustomWebClient extends WebViewClient {

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            view.postDelayed(() -> view.evaluateJavascript(
                    "(function() { " +
                            "   if(document.getElementById('pngPreloaderWrapper')) {" +
                            "       document.getElementById('pngPreloaderWrapper').removeChild(document.getElementById('pngLogoWrapper')); " +
                            "   }" +
                            "})();",
                    html -> {
                    }), 600);

            view.postDelayed(() -> view.evaluateJavascript(
                    "(function() { " +
                            "   var myHome = document.getElementById('lobbyButtonWrapper'); " +
                            "   if(document.getElementById('lobbyButtonWrapper')) {" +
                            "       document.getElementById('lobbyButtonWrapper').style.display = 'none';" +
                            "   }" +
                            "})();",
                    html -> {
                    }), 3020);
        }
    }

    private class JSInterface {
        @JavascriptInterface
        public void postMessage(String name, String data) {
            Map<String, Object> eventValue = new HashMap<>();

            if ("openWindow".equals(name)) {
                try {
                    JSONObject extLink = new JSONObject(data);
                    Intent newWindow = new Intent(Intent.ACTION_VIEW);
                    newWindow.setData(Uri.parse(extLink.getString("url")));
                    newWindow.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getContext().startActivity(newWindow);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                eventValue.put(name, data);
            }
        }
    }
}
