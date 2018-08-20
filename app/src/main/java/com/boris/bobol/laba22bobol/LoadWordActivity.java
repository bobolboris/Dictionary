package com.boris.bobol.laba22bobol;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.boris.bobol.laba22bobol.bd.BDController;
import com.boris.bobol.laba22bobol.untils.Files;
import com.boris.bobol.laba22bobol.untils.PathUtil;

import java.net.URISyntaxException;

public class LoadWordActivity extends AppCompatActivity implements BDController.BDListener {

    final int ACTIVITY_CHOOSE_FILE = 22;
    TextView textView;
    BDController bdController;
    Intent chooseFile;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_word);
        textView = findViewById(R.id.textView);
        bdController = new BDController(this, this);
        chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFile.addCategory(Intent.CATEGORY_OPENABLE);
        chooseFile.setType("application/json");
        intent = Intent.createChooser(chooseFile, getString(R.string.chooseJSONFile));
    }

    public void onButton6Click(View view) {
        startActivityForResult(intent, ACTIVITY_CHOOSE_FILE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != RESULT_OK) return;
        if(requestCode == ACTIVITY_CHOOSE_FILE){
            Uri uri = data.getData();
            try {
                textView.setText(PathUtil.getPath(this, uri));
                textView.setVisibility(View.VISIBLE);
            } catch (URISyntaxException e) {
                Toast.makeText(this, getString(R.string.errorFileOpen),
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    public void onButton7Click(View view) {
        if(textView.getVisibility() != View.VISIBLE){
            Toast.makeText(this, getString(R.string.errorFile), Toast.LENGTH_LONG).show();
            return;
        }
        String text = "";

        try {
            text = Files.readAllText(textView.getText().toString());
        } catch (Exception e) {
            Toast.makeText(this, getString(R.string.errorFileStruct), Toast.LENGTH_LONG).show();
        }
        bdController.addAllWords(text, 0);
    }

    @Override
    public void onResponse(Intent intent, int code) {
        Toast.makeText(this,
                getString(R.string.successfulAddWords), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onError(Exception e, int code) {
        Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
    }
}