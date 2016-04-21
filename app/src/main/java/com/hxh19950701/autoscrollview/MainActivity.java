package com.hxh19950701.autoscrollview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AutoScrollView autoScrollView = (AutoScrollView) findViewById(R.id.asv);
        autoScrollView.setScrolled(true);
    }
}