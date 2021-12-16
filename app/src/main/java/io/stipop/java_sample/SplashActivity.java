package io.stipop.java_sample;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        findViewById(R.id.startSampleButtonView).setOnClickListener(View->{
            Intent mainIntent = new Intent(this, MainActivity.class);
            startActivity(mainIntent);
        });

        findViewById(R.id.goToDocsTextView).setOnClickListener(View->{
            Intent webViewIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://docs.stipop.io/en/sdk/android/get-started/quick-start"));
            startActivity(webViewIntent);
        });
    }
}
