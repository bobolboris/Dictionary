package com.boris.bobol.laba22bobol;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onButton0Click(View view) {
        startActivity(new Intent(this, AddWordActivity.class));
    }

    public void onButton3Click(View view) {
        startActivity(new Intent(this, LoadWordActivity.class));
    }

    public void onButton4Click(View view) {
        startActivity(new Intent(this, TranslateActivity.class));
    }

    public void onButton2Click(View view) {
        startActivity(new Intent(this, GameActivity.class));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_VOLUME_DOWN){
            event.startTracking();
            startActivity(new Intent(this, SetAddressActivity.class));
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}