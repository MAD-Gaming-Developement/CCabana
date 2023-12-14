package dev.kai.mgdg008;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.WindowManager;

public class CabanaSplash extends AppCompatActivity {

    private static final String CHANNEL_ID = "my_channel";
    private boolean hasUserConsent;
    @SuppressLint("SuspiciousIndentation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent webApp = new Intent(this, CabanaContent.class);
            webApp.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(webApp);
            finish();
        }, 1800);
    }
}