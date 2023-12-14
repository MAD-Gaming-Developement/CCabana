package dev.kai.mgdg008;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;

import android.os.Bundle;
import android.widget.TextView;

public class Termspolicy extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_termspolicy);

        TextView textViewPrivacyPolicy = findViewById(R.id.ViewPrivacyPolicy);
        String privacyPolicyText = getString(R.string.TermsandCondition);
        textViewPrivacyPolicy.setText(HtmlCompat.fromHtml(privacyPolicyText, HtmlCompat.FROM_HTML_MODE_LEGACY));
    }
}