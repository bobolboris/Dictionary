package com.boris.bobol.laba22bobol;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.boris.bobol.laba22bobol.bd.BDController;

import java.util.ArrayList;
import java.util.List;

public class GameActivity extends AppCompatActivity implements BDController.BDListener {
    BDController bdController;
    ArrayList<String> list;
    TextView textView7;
    ListView listView;
    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        textView7 = findViewById(R.id.textView7);
        listView = findViewById(R.id.listView);
        bdController = new BDController(this, this);
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);

        Intent intent = getIntent();
        try{
            if(intent != null)
                list = intent.getStringArrayListExtra("previousWords");
            if(list == null) list = new ArrayList<>();

            bdController.getGame(list, 0);
        }catch (Exception e){
            finish();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String translation = arrayAdapter.getItem(position);
                String word = textView7.getText().toString();
                bdController.checkTranslate(word, translation, 1);
            }
        });
    }

    public void perestart(){
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("previousWords", list);
        finish();
        startActivity(intent);
    }

    public void onButton16Click(View view) {
        perestart();
    }

    @Override
    public void onResponse(Intent intent, int code) {
        if(code == 0){
            Intent intent1 = intent.getParcelableExtra("result");
            String word = intent1.getStringExtra("word");
            String[] translations = intent1.getStringArrayExtra("translations");
            textView7.setText(word);
            list.add(word);
            for(String translation: translations){
                arrayAdapter.add(translation);
            }
        }else if(code == 1){
            if(intent.getBooleanExtra("result", false)){
                Toast.makeText(this, getString(R.string.victory), Toast.LENGTH_LONG).show();
                bdController.checkStatic(textView7.getText().toString(), 2);
                perestart();
            }else{
                Toast.makeText(this, getString(R.string.loss), Toast.LENGTH_LONG).show();
                bdController.setStatic(textView7.getText().toString(), 0, 3);
            }
        }
    }

    @Override
    public void onError(Exception e, int code) {
        Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        finish();
    }
}
