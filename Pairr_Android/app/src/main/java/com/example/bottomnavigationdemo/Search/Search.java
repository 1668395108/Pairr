package com.example.bottomnavigationdemo.Search;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bottomnavigationdemo.R;
import com.example.bottomnavigationdemo.Show;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class Search extends AppCompatActivity implements View.OnClickListener{
    private EditText editText_serach;
    private ImageButton imageButton_text,imageButton_voice;
    private  String TAG="myLog";
    private HashMap<String, String> mIatResults = new LinkedHashMap<String , String>();
    private String search=null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去除标题栏那个框框
        if (getSupportActionBar() != null){     //这个也是哦（去除标题栏的框框）
            getSupportActionBar().hide();
        }
        initView() ;
        initSpeech();
    }
    private void initView(){
        setContentView(R.layout.serach);
        editText_serach=findViewById(R.id.editText_search);
        imageButton_text=findViewById(R.id.imageButton_text);
        imageButton_voice=findViewById(R.id.imageButton_voice);
        imageButton_text.setOnClickListener(this);
        imageButton_voice.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageButton_text: //文本搜索
                search=editText_serach.getText().toString().trim()+"&0";
                if(!search.equals("")){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            tcpClient();
                        }
                    }).start();
                } else
                {
                    Toast.makeText(this, "请输入要查找的内容", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.imageButton_voice:// 语音合成（把声音转文字）
                startSpeechDialog();
                break;
        }
    }
    private void initSpeech() {
        // 将“12345678”替换成您申请的 APPID，申请地址： http://www.xfyun.cn
        // 请勿在 “ =”与 appid 之间添加任务空字符或者转义符
        SpeechUtility. createUtility( this, SpeechConstant.APPID + "=5ddd0498" );
    }
    private void startSpeechDialog() {
        //1. 创建RecognizerDialog对象
        RecognizerDialog mDialog = new RecognizerDialog(this, new MyInitListener()) ;
        //2. 设置accent、 language等参数
        mDialog.setParameter(SpeechConstant.LANGUAGE, "zh_cn" );// 设置中文
        mDialog.setParameter(SpeechConstant.ACCENT, "mandarin" );
        // 若要将UI控件用于语义理解，必须添加以下参数设置，设置之后 onResult回调返回将是语义理解
        // 结果
        // mDialog.setParameter("asr_sch", "1");
        // mDialog.setParameter("nlp_version", "2.0");
        //3.设置回调接口
        mDialog.setListener( new MyRecognizerDialogListener()) ;
        //4. 显示dialog，接收语音输入
        mDialog.show() ;
    }
    class MyInitListener implements InitListener {

        @Override
        public void onInit(int code) {
            if (code != ErrorCode.SUCCESS) {
                showTip("初始化失败 ");
            }

        }
    }
    private void showTip (String data) {
        Toast.makeText( this, data, Toast.LENGTH_SHORT).show() ;
    }
    class MyRecognizerDialogListener implements RecognizerDialogListener {

        /**
         * @param results
         * @param isLast  是否说完了
         */
        @Override
        public void onResult(RecognizerResult results, boolean isLast) {
            String result = results.getResultString(); //为解析的
            showTip(result) ;
            System. out.println(" 没有解析的 :" + result);

            String text = JsonParser.parseIatResult(result) ;//解析过后的

            System. out.println(" 解析后的 :" + text);

            String sn = null;
            // 读取json结果中的 sn字段
            try {
                JSONObject resultJson = new JSONObject(results.getResultString()) ;
                sn = resultJson.optString("sn" );
            } catch (JSONException e) {
                e.printStackTrace();
            }

            mIatResults .put(sn, text) ;//没有得到一句，添加到

            StringBuilder resultBuffer = new StringBuilder();
            for (String key : mIatResults.keySet()) {
                resultBuffer.append(mIatResults .get(key));
            }
            Toast.makeText(Search.this, resultBuffer.toString(), Toast.LENGTH_SHORT).show();
            editText_serach.setText(resultBuffer.toString());// 设置输入框的文本
            editText_serach .setSelection(editText_serach.length()) ;//把光标定位末尾
        }

        @Override
        public void onError(SpeechError speechError) {
        }
    }
    private void tcpClient(){
        int port=5000;
        String hostip="10.133.129.55";   //39.97.250.70   //在同一局域网下才能使用   A1005 192.168.123.2//10.133.129.55
        try{
            Socket socketClient = new Socket(hostip, port);     	//1.bind
            InputStream in=socketClient.getInputStream();			//2.获得IO流
            OutputStream out = socketClient.getOutputStream();

            out.write(search.getBytes());					//3.发送
            Log.d(TAG,search);
            out.flush();


            byte[] bytes = new byte[9216];
            in.read(bytes);
            //4.接收
            String s=new String(bytes, StandardCharsets.UTF_8).trim();
            Log.i(TAG, s);

            Bundle bundle=new Bundle();
            bundle.putString("content",s);
            bundle.putString("search",search);
            Intent intent=new Intent(getApplicationContext(), Show.class);   //显示到Show中
            intent.putExtras(bundle);
            startActivity(intent);

            socketClient.close();
            							//5.关闭
        } catch (IOException e){e.printStackTrace();}
    }}
