package com.example.bottomnavigationdemo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Poem_Show extends Activity {
    private TextView textView_content;
    private TextView textView_poemname;
    private TextView textView_dynasty;
    private TextView textView_author;
    private String TAG="myLog";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去除标题栏那个框框
        setContentView(R.layout.poem_show);
        textView_content=findViewById(R.id.textView_content);
        textView_poemname=findViewById(R.id.textView_poemname);
        textView_dynasty=findViewById(R.id.textView_dynasty);
        textView_author=findViewById(R.id.textView_author);

        Bundle receive=getIntent().getExtras();
        String poem_content=receive.getString("content");
        //textView_content.setText(poem_content);
        JSONArray jsonArray;
        JSONObject jsonObject_poem;
        String poemname;
        String dynasty;
        String author;
        String content;

        try {
             jsonArray=new JSONArray(poem_content);
             jsonObject_poem=jsonArray.getJSONObject(0);
             poemname=jsonObject_poem.getString("poemname");
             textView_poemname.setText(poemname);

             dynasty=jsonObject_poem.getString("dynasty");
             textView_dynasty.setText(dynasty);

             author=jsonObject_poem.getString("author");
             textView_author.setText(author);

             content=jsonObject_poem.getString("content");
             textView_content.setText(content);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
