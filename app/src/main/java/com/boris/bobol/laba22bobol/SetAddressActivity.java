package com.boris.bobol.laba22bobol;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.boris.bobol.laba22bobol.untils.URL;

public class SetAddressActivity extends AppCompatActivity {
    EditText editText;
    EditText editText3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_address);
        editText = findViewById(R.id.editText);
        editText3 = findViewById(R.id.editText3);
    }

    public void onButton9Click(View view) {
        if(editText.length() > 0){
            String address = "http://" + editText.getText().toString();
            if(editText3.length() > 0){
                address += ":" + editText3.getText().toString();
            }
            URL.getInstance().setUrl(address);
            finish();
        }
    }
}
