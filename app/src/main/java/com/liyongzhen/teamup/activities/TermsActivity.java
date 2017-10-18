package com.liyongzhen.teamup.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.github.barteksc.pdfviewer.PDFView;
import com.liyongzhen.teamup.R;

public class TermsActivity extends AppCompatActivity {

    private PDFView mPdfView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mPdfView = (PDFView) findViewById(R.id.pdfView);
        mPdfView.fromAsset("term_real.pdf")
                .enableDoubletap(true)
                .defaultPage(0)
                .spacing(0)
                .load();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
