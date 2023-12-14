package dev.kai.mgdg008;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;

import android.os.Bundle;
import android.widget.TextView;

public class About extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        TextView textViewPrivacyPolicy = findViewById(R.id.ViewAbout);
        String privacyPolicyText = getString(R.string.About);
        textViewPrivacyPolicy.setText(HtmlCompat.fromHtml(privacyPolicyText, HtmlCompat.FROM_HTML_MODE_LEGACY));
    }
}