package dev.kai.mgdg008;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;

public class CabanaContent extends AppCompatActivity {

    private SharedPreferences appSharedPref;
    private Context context;
    private PopupMenu subMenu;
    private BottomNavigationView verticalView;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mgdg_content);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        context = this;
        appSharedPref = getSharedPreferences(CabanaConfig.appCode, MODE_PRIVATE);

        WebView game = findViewById(R.id.game_view);
        game.setWebViewClient(new WebViewClient());
        WebSettings webSettings = game.getSettings();
        webSettings.setJavaScriptEnabled(true);
        game.loadUrl(CabanaConfig.gameURL);
        BottomNavigationView verticalView = findViewById(R.id.vertical_view);

        verticalView.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_UNLABELED);


        if (CabanaConfig.success.equals("success"))
        {
            verticalView.setVisibility(View.VISIBLE);

            verticalView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    if (item.getItemId() == R.id.menu_notification) {
                        Intent notificationIntent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                        notificationIntent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
                        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(notificationIntent);
                        return true;
                    } else if (item.getItemId() == R.id.menu_terms_policy) {
                        startActivity(new Intent(CabanaContent.this, Termspolicy.class));
                        return true;
                    } else if (item.getItemId() == R.id.menu_about) {
                        startActivity(new Intent(CabanaContent.this, About.class));
                        return true;
                    }
                    return false;
                }
            });
        } else {
            verticalView.setVisibility(View.GONE);
        }
    }
}