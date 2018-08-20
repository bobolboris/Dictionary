package com.boris.bobol.laba22bobol;

import android.content.Intent;
import android.media.MediaPlayer;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.boris.bobol.laba22bobol.bd.BDController;
import com.boris.bobol.laba22bobol.untils.Files;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;

public class TranslateActivity extends AppCompatActivity implements BDController.BDListener {

    boolean state;
    TextView textView2;
    TextView textView3;
    TextView textView5;
    EditText editText2;
    Button button10;
    Button button11;
    Button button14;
    Button button15;
    TextToSpeech textToSpeech;
    BDController bdController;
    MediaPlayer mediaPlayer;
    String nameFile;
    public static String fileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate);
        fileName = getCacheDir().getPath() + "temp.temp";
        mediaPlayer = new MediaPlayer();
        textView2 = findViewById(R.id.textView2);
        textView3 = findViewById(R.id.textView3);
        textView5 = findViewById(R.id.textView5);
        editText2 = findViewById(R.id.editText2);
        button10 = findViewById(R.id.button10);
        button11 = findViewById(R.id.button11);
        button14 = findViewById(R.id.button14);
        button15 = findViewById(R.id.button15);
        bdController = new BDController(this, this);
        state = true;
        checkState();

        editText2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(state){
                    bdController.getTranslate(s.toString(), 0);
                }else{
                    bdController.getWord(s.toString(), 1);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.UK);
                }
            }
        });

    }

    void checkSound(){
        String text = getWord();
        if(text.length() == 0){
            button14.setVisibility(View.GONE);
            button15.setVisibility(View.GONE);
            return;
        }
        bdController.soundExist(text, 2);
    }

    void checkState(){
        if(state){
            textView2.setText(R.string.word);
            textView3.setText(R.string.translate);
            button10.setVisibility(View.GONE);
            button11.setVisibility(View.VISIBLE);
        }else{
            textView2.setText(R.string.translate);
            textView3.setText(R.string.word);
            button10.setVisibility(View.VISIBLE);
            button11.setVisibility(View.GONE);
        }
        textView5.setText("");
    }

    public String join(String[] text){
        StringBuilder aa = new StringBuilder();
        for(String t: text){
            aa.append(t + ' ');
        }
        return aa.toString();
    }

    public void onButton8Click(View view) {
        state = !state;
        checkState();
    }

    public String getWord(){
        String word = "";
        if(state){
            if(editText2.length() > 0)
                word = editText2.getText().toString();
        }else{
            if(textView5.length() > 0)
                word = textView5.getText().toString().trim();
        }
        return word;
    }

    public void onSpeech(View view) {
        textToSpeech.speak(getWord(), TextToSpeech.QUEUE_FLUSH, null);
    }

    public void onButton13Click(View view) {
        Intent intent = new Intent(this, RecordAudioActivity.class);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String word = getWord();
        if(word.length() == 0) {
            Toast.makeText(this, getString(R.string.errorEmptyWord),
                    Toast.LENGTH_LONG).show();
            return;
        }
        if(resultCode == RecordAudioActivity.OKEY && requestCode == 1){
            nameFile = RecordAudioActivity.FILE_NAME;
            try {
                bdController.insertSound(word, RecordAudioActivity.FILE_NAME, 3);
            } catch (FileNotFoundException e) {
                Toast.makeText(this, getString(R.string.errorAddSound),
                        Toast.LENGTH_LONG).show();
            }

        }
    }

    public void onPlaySound(View view) {
        bdController.getSound(getWord(), 4);
    }

    private void startPlaying(byte[] data) throws IOException {
        File file = new File(fileName);
        if(file.exists())
            file.delete();
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(fileName));
        bos.write(data);
        bos.flush();
        bos.close();
        mediaPlayer.reset();
        mediaPlayer.setDataSource(fileName);
        mediaPlayer.prepare();
        mediaPlayer.start();
    }

    @Override
    public void onResponse(Intent intent, int code) {
        switch (code){
            case 0: case 1:{
                String[] tmp = intent.getStringArrayExtra("result");
                if(tmp != null) {
                    textView5.setText(join(tmp));
                    checkSound();
                }else
                    textView5.setText("");
                break;
            }
            case 2:{
                if(intent.getBooleanExtra("result", false)){
                    if(state){
                        button14.setVisibility(View.GONE);
                        button15.setVisibility(View.VISIBLE);
                    }else{
                        button14.setVisibility(View.VISIBLE);
                        button15.setVisibility(View.GONE);
                    }
                }
                break;
            }
            case 3:{
                Files.delete(nameFile);
                Toast.makeText(this, getString(R.string.successfulAddSound),
                        Toast.LENGTH_LONG).show();
            }
            case 4:{
                if(intent == null) return;
                byte[] bytes = intent.getByteArrayExtra("result");
                try {
                    if(bytes != null)
                        startPlaying(bytes);
                } catch (IOException e) {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

        }
    }

    @Override
    public void onError(Exception e, int code) {
        if(code != 0 && code != 1)
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
    }
}
