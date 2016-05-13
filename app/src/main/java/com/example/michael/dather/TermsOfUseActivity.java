package com.example.michael.dather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TermsOfUseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_terms_of_use);

        TextView participantInfo = (TextView)findViewById(R.id.fullscreen_content);
        participantInfo.setText(Html.fromHtml(getString(R.string.infoText)));

        final Button confirmBtn = (Button) findViewById(R.id.accept);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmed();
            }
        });
    }

    private void confirmed() {
        SharedPreferences mPrefs = getSharedPreferences("prefs", 0);
        SharedPreferences.Editor mEditor = mPrefs.edit();
        mEditor.putBoolean("acceptedTerms", true).commit();

        Intent myIntent = new Intent(this, MainActivity.class);
        startActivity(myIntent);
    }
}
