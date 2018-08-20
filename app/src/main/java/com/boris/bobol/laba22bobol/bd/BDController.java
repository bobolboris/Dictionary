package com.boris.bobol.laba22bobol.bd;

import android.content.Context;
import android.content.Intent;

import cz.msebera.android.httpclient.*;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Random;

import com.boris.bobol.laba22bobol.untils.URL;


public class BDController {
    Context context;
    Random rand;
    String url;
    BDListener bdListener;
    AsyncHttpClient client;

    public BDController(Context context, BDListener bdListener){
        this.context = context;
        this.bdListener = bdListener;
        rand = new Random();
        client = new AsyncHttpClient();
        url = URL.getInstance().getUrl();
        url = url + "/api";
    }

    public void addWord(String word, String translation, int code) {
        final int code1 = code;
        RequestParams params = new RequestParams();
        params.put("word", word);
        params.put("translation", translation);
        client.post(url + "/addWord", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                try {
                    JSONObject json = new JSONObject(new String(bytes));
                    if(json.getBoolean("ok")) {
                        Intent intent = new Intent();
                        intent.putExtra("result", json.getLong("id"));
                        bdListener.onResponse(intent, code1);
                    }else{
                        bdListener.onError(new Exception(json.getString("error")), code1);
                    }
                } catch (JSONException e) {
                    bdListener.onError(e, code1);
                }
            }
            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                bdListener.onError((Exception) throwable, code1);
            }
        });
    }

    public void getTranslate(String word, int code) {
        final int code1 = code;
        RequestParams params = new RequestParams();
        params.put("word", word);
        client.post(url + "/getTranslation", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int j, Header[] headers, byte[] bytes) {
                try {
                    JSONObject json = new JSONObject(new String(bytes));
                    if(json.getBoolean("ok")) {
                        JSONArray array = json.getJSONArray("translations");
                        String[] translations = new String[array.length()];
                        for(int i = 0; i < array.length(); i++){
                            translations[i] = array.getString(i);
                        }
                        Intent intent = new Intent();
                        intent.putExtra("result", translations);
                        bdListener.onResponse(intent, code1);
                    }else{
                        bdListener.onError(new Exception(json.getString("error")), code1);
                    }
                } catch (JSONException e) {
                    bdListener.onError(e, code1);
                }
            }
            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                bdListener.onError((Exception) throwable, code1);
            }
        });
    }

    public void getWord(String translation, int code) {
        final int code1 = code;
        RequestParams params = new RequestParams();
        params.put("translation", translation);
        client.post(url + "/getWord", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int j, Header[] headers, byte[] bytes) {
                try {
                    JSONObject json = new JSONObject(new String(bytes));
                    if(json.getBoolean("ok")) {
                        JSONArray array = json.getJSONArray("words");
                        String[] words = new String[array.length()];
                        for(int i = 0; i < array.length(); i++){
                            words[i] = array.getString(i);
                        }
                        Intent intent = new Intent();
                        intent.putExtra("result", words);
                        bdListener.onResponse(intent, code1);
                    }else{
                        bdListener.onError(new Exception(json.getString("error")), code1);
                    }
                } catch (JSONException e) {
                    bdListener.onError(e, code1);
                }
            }
            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                bdListener.onError((Exception) throwable, code1);
            }
        });
    }

    public void getAllWords(int code) {
        final int code1 = code;
        client.post(url + "/getAllWords", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int j, Header[] headers, byte[] bytes) {
                try {
                    JSONObject json = new JSONObject(new String(bytes));
                    if(json.getBoolean("ok")) {
                        JSONArray array = json.getJSONArray("words");
                        String[] words = new String[array.length()];
                        for(int i = 0; i < array.length(); i++){
                            words[i] = array.getString(i);
                        }
                        Intent intent = new Intent();
                        intent.putExtra("result", words);
                        bdListener.onResponse(intent, code1);
                    }else{
                        bdListener.onError(new Exception(json.getString("error")), code1);
                    }
                } catch (JSONException e) {
                    bdListener.onError(e, code1);
                }
            }
            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                bdListener.onError((Exception) throwable, code1);
            }
        });
    }

    public void getRandomWord(List<String> previousWords, int code) {
        StringBuilder params = new StringBuilder();
        for(int i = 0; i < previousWords.size() - 1; i++){
            params.append(previousWords.get(i) + ',');
        }
        params.append(previousWords.get(previousWords.size() - 1));

        final int code1 = code;
        RequestParams params1 = new RequestParams();
        params1.put("previousWords[]", params.toString());

        client.post(url + "/getRandomWord", params1, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int j, Header[] headers, byte[] bytes) {
                try {
                    JSONObject json = new JSONObject(new String(bytes));
                    if(json.getBoolean("ok")) {
                        Intent intent = new Intent();
                        intent.putExtra("result", json.getString("word"));
                        bdListener.onResponse(intent, code1);
                    }else{
                        bdListener.onError(new Exception(json.getString("error")), code1);
                    }
                } catch (JSONException e) {
                    bdListener.onError(e, code1);
                }
            }
            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                bdListener.onError((Exception) throwable, code1);
            }
        });
    }

    public void getRandomTranslateList(String word, int limit, int code) {
        final int code1 = code;
        RequestParams params = new RequestParams();
        params.put("word", word);
        params.put("limit", limit);

        client.post(url + "/getRandomTranslate", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int j, Header[] headers, byte[] bytes) {
                try {
                    JSONObject json = new JSONObject(new String(bytes));
                    if(json.getBoolean("ok")) {
                        JSONArray array = json.getJSONArray("translations");
                        String[] translations = new String[array.length()];
                        for(int i = 0; i < array.length(); i++){
                            translations[i] = array.getString(i);
                        }
                        Intent intent = new Intent();
                        intent.putExtra("result", translations);
                        bdListener.onResponse(intent, code1);
                    }else{
                        bdListener.onError(new Exception(json.getString("error")), code1);
                    }
                } catch (JSONException e) {
                    bdListener.onError(e, code1);
                }
            }
            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                bdListener.onError((Exception) throwable, code1);
            }
        });

    }

    public void checkTranslate(String word, String translation, int code) {
        final int code1 = code;
        RequestParams params = new RequestParams();
        params.put("word", word);
        params.put("translation", translation);

        client.post(url + "/checkTranslation", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int j, Header[] headers, byte[] bytes) {
                try {
                    JSONObject json = new JSONObject(new String(bytes));
                    if(json.getBoolean("ok")) {
                        Intent intent = new Intent();
                        intent.putExtra("result", json.getBoolean("equals"));
                        bdListener.onResponse(intent, code1);
                    }else{
                        bdListener.onError(new Exception(json.getString("error")), code1);
                    }
                } catch (JSONException e) {
                    bdListener.onError(e, code1);
                }
            }
            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                bdListener.onError((Exception) throwable, code1);
            }
        });
    }

    public void insertSound(String word, String path, int code) throws FileNotFoundException {
        final int code1 = code;
        RequestParams params = new RequestParams();
        params.put("word", word);
        params.put("sound", new File(path));
        client.post(url + "/insertSound", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int j, Header[] headers, byte[] bytes) {
                try {
                    JSONObject json = new JSONObject(new String(bytes));
                    if(json.getBoolean("ok")) {
                        bdListener.onResponse(null, code1);
                    }else{
                        bdListener.onError(new Exception(json.getString("error")), code1);
                    }
                } catch (JSONException e) {
                    bdListener.onError(e, code1);
                }
            }
            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                bdListener.onError((Exception) throwable, code1);
            }
        });
    }

    public void getSound(String word, int code){
        final int code1 = code;
        RequestParams params = new RequestParams();
        params.put("word", word);
        client.post(url + "/getSound", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int j, Header[] headers, byte[] bytes) {
                Intent intent = new Intent();
                intent.putExtra("result", bytes);
                bdListener.onResponse(intent, code1);
            }
            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                bdListener.onError((Exception) throwable, code1);
            }
        });
    }

    public void soundExist(String word, int code){
        final int code1 = code;
        RequestParams params = new RequestParams();
        params.put("word", word);

        client.post(url + "/soundExist", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int j, Header[] headers, byte[] bytes) {
                try {
                    JSONObject json = new JSONObject(new String(bytes));
                    if(json.getBoolean("ok")) {
                        Intent intent = new Intent();
                        intent.putExtra("result", json.getBoolean("equals"));
                        bdListener.onResponse(intent, code1);
                    }else{
                        bdListener.onError(new Exception(json.getString("error")), code1);
                    }
                } catch (JSONException e) {
                    bdListener.onError(e, code1);
                }
            }
            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                bdListener.onError((Exception) throwable, code1);
            }
        });
    }

    public void addToArchive(String word, int code) {
        final int code1 = code;
        RequestParams params = new RequestParams();
        params.put("word", word);

        client.post(url + "/addToArchive", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int j, Header[] headers, byte[] bytes) {
                try {
                    JSONObject json = new JSONObject(new String(bytes));
                    if(!json.getBoolean("ok")) {
                        bdListener.onError(new Exception(json.getString("error")), code1);
                    }else{
                        bdListener.onResponse(null, code1);
                    }
                } catch (JSONException e) {
                    bdListener.onError(e, code1);
                }
            }
            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                bdListener.onError((Exception) throwable, code1);
            }
        });
    }

    public void getStatic(String word, int code) {
        final int code1 = code;
        RequestParams params = new RequestParams();
        params.put("word", word);

        client.post(url + "/getStatic", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int j, Header[] headers, byte[] bytes) {
                try {
                    JSONObject json = new JSONObject(new String(bytes));
                    if(json.getBoolean("ok")) {
                        Intent intent = new Intent();
                        intent.putExtra("result", json.getInt("correctly"));
                        bdListener.onResponse(intent, code1);
                    }else{
                        bdListener.onError(new Exception(json.getString("error")), code1);
                    }
                } catch (JSONException e) {
                    bdListener.onError(e, code1);
                }
            }
            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                bdListener.onError((Exception) throwable, code1);
            }
        });

    }

    public void setStatic(String word, int value, int code){
        final int code1 = code;
        RequestParams params = new RequestParams();
        params.put("word", word);
        params.put("value", value);

        client.post(url + "/setStatic", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int j, Header[] headers, byte[] bytes) {
                try {
                    JSONObject json = new JSONObject(new String(bytes));
                    if(!json.getBoolean("ok")) {
                        bdListener.onError(new Exception(json.getString("error")), code1);
                    }else{
                        bdListener.onResponse(null, code1);
                    }
                } catch (JSONException e) {
                    bdListener.onError(e, code1);
                }
            }
            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                bdListener.onError((Exception) throwable, code1);
            }
        });
    }

    public void getGame(List<String> previousWords, int code) {
        final int code1 = code;
        RequestParams params1 = new RequestParams();
        if(previousWords != null && previousWords.size() > 0){
            StringBuilder params = new StringBuilder();
            for(int i = 0; i < previousWords.size() - 1; i++){
                params.append(previousWords.get(i) + ',');
            }
            params.append(previousWords.get(previousWords.size() - 1));
            params1.put("previousWords[]", params.toString());
        }

        client.post(url + "/getGame", params1, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int j, Header[] headers, byte[] bytes) {
                try {
                    JSONObject json = new JSONObject(new String(bytes));
                    if(json.getBoolean("ok")) {
                        Intent intent = new Intent();
                        Intent intent1 = new Intent();
                        intent1.putExtra("word", json.getString("word"));
                        JSONArray array = json.getJSONArray("translations");
                        String[] translations = new String[array.length()];
                        for(int i = 0; i < array.length(); i++){
                            translations[i] = array.getString(i);
                        }
                        intent1.putExtra("translations", translations);
                        intent.putExtra("result", intent1);
                        bdListener.onResponse(intent, code1);
                    }else{
                        bdListener.onError(new Exception(json.getString("error")), code1);
                    }
                } catch (JSONException e) {
                    bdListener.onError(e, code1);
                }
            }
            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                bdListener.onError((Exception) throwable, code1);
            }
        });

    }

    public void checkStatic(String word, int code) {
        final int code1 = code;
        RequestParams params = new RequestParams();
        params.put("word", word);

        client.post(url + "/checkStatic", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int j, Header[] headers, byte[] bytes) {
                try {
                    JSONObject json = new JSONObject(new String(bytes));
                    if(!json.getBoolean("ok")) {
                        bdListener.onError(new Exception(json.getString("error")), code1);
                    }else{
                        bdListener.onResponse(null, code1);
                    }
                } catch (JSONException e) {
                    bdListener.onError(e, code1);
                }
            }
            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                bdListener.onError((Exception) throwable, code1);
            }
        });
    }

    public void addAllWords(String json, int code){
        final int code1 = code;
        RequestParams params = new RequestParams();
        params.put("json", json);
        client.post(url + "/addAllWords", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int j, Header[] headers, byte[] bytes) {
                try {
                    JSONObject json = new JSONObject(new String(bytes));
                    if(!json.getBoolean("ok")) {
                        bdListener.onError(new Exception(json.getString("error")), code1);
                    }else{
                        bdListener.onResponse(null, code1);
                    }
                } catch (JSONException e) {
                    bdListener.onError(e, code1);
                }
            }
            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                bdListener.onError((Exception) throwable, code1);
            }
        });
    }

    public BDListener getBDListener() {
        return bdListener;
    }

    public void setBDListener(BDListener bdListener) {
        this.bdListener = bdListener;
    }

    public interface BDListener{
        void onResponse(Intent intent, int code);
        void onError(Exception e, int code);
    }
}