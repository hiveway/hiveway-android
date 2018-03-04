package org.hiveway;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

public class AboutActivity extends BaseActivity {
    private Button appAccountButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        setTitle(R.string.about_title_activity);

        TextView versionTextView = findViewById(R.id.versionTV);
        String versionName = BuildConfig.VERSION_NAME;
        String versionFormat = getString(R.string.about_tusky_version);
        versionTextView.setText(String.format(versionFormat, versionName));
    }

    private void viewAccount(String id) {
        Intent intent = new Intent(this, AccountActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }

    private void onSearchFailed() {
        Snackbar.make(appAccountButton, R.string.error_generic, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                onBackPressed();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
