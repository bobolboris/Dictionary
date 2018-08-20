package com.boris.bobol.laba22bobol;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.boris.bobol.laba22bobol.bd.BDController;


public class AddWordActivity extends AppCompatActivity implements BDController.BDListener {
    EditText editText4;
    EditText editText5;
    BDController bdController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_word);
        bdController = new BDController(this, this);
        editText4 = findViewById(R.id.editText4);
        editText5 = findViewById(R.id.editText5);
    }

    public void onButton5Click(View view) {
        Editable tmp = editText4.getText();
        if(tmp == null || tmp.toString().trim().length() == 0){
            Toast.makeText(this, getString(R.string.errorEmptyWord),
                    Toast.LENGTH_LONG).show();
            return;
        }
        tmp = editText5.getText();
        if(tmp == null || tmp.toString().trim().length() == 0){
            Toast.makeText(this, getString(R.string.errorRepeat), Toast.LENGTH_LONG).show();
            return;
        }
        bdController.addWord(editText4.getText().toString().trim(),
                editText5.getText().toString().trim(), 0);
    }

    @Override
    public void onResponse(Intent intent, int code) {
        Toast.makeText(this, String.format(getString(R.string.successfulAdd),
                intent.getLongExtra("result", -1)), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onError(Exception e, int code) {
        Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
    }
}