package com.example.bottomnavigationdemo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.bottomnavigationdemo.Poetry_Show.Poem_Adapter;
import com.example.bottomnavigationdemo.Poetry_Show.Poem_Content;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.SynthesizerListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
public class Show extends AppCompatActivity {
    private String poem_info=null;
    private List<Poem_Content> poem_list=new ArrayList<>();
    private String TAG="myLog";
    private JSONArray jsonArray;
    private JSONObject jsonObject_poem;
    private String poem_content;
    SwipeRefreshLayout swipeRefreshLayout;
    private String  serach_content;
    private int i=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.poem_show_listview);
        Bundle receive=getIntent().getExtras();
        poem_content=receive.getString("content");
        serach_content=receive.getString("search");
        initpoem();//初始化数据
        Poem_Adapter adapter=new Poem_Adapter(Show.this,R.layout.listview_show,poem_list);
        ListView listView=findViewById(R.id.list_item);
        listView.setAdapter(adapter);
        swipeRefreshLayout=findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //这里获取数据的逻辑
                swipeRefreshLayout.setRefreshing(false);
                //设置进度View样式的大小，只有两个值DEFAULT和LARGE，表示默认和较大
                swipeRefreshLayout.setSize(SwipeRefreshLayout.DEFAULT);
                //分割字符与数字
                String[] serach_content_text=serach_content.split("&");
                serach_content=serach_content_text[0];
                i=i+Integer.valueOf(serach_content_text[1]);
                String v=Integer.toString(i);

                serach_content=serach_content+"&"+v;
                Log.d(TAG,serach_content);
                if(!serach_content.equals("")){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            tcpClient();
                        }
                    }).start();
                }
                initpoem();//初始化数据
                Poem_Adapter adapter=new Poem_Adapter(Show.this,R.layout.listview_show,poem_list);
                ListView listView=findViewById(R.id.list_item);
                listView.setAdapter(adapter);
            }
        });
    }
    public  void initpoem() {
        Log.d(TAG,"进入List");
        try {
            String poemname,dynasty,author,content;
            jsonArray=new JSONArray(poem_content);
            for(int i=0;i<jsonArray.length();i++){
                Log.d(TAG,"进入循环");
                try {
                    jsonObject_poem=jsonArray.getJSONObject(i);
                    poemname=jsonObject_poem.getString("poemname");
                    dynasty=jsonObject_poem.getString("dynasty");
                    author=jsonObject_poem.getString("author");
                    content=jsonObject_poem.getString("content");
                    content= content.replaceAll("。","。\n");
                    content= content.replaceAll("？","？\n");
                    content= content.replaceAll("；","；\n");
                    content= content.replaceAll("！","！\n");
                    Poem_Content poem=new Poem_Content(poemname,dynasty,author,content);
                    poem_list.add(0,poem);
                    Log.d(TAG,"添加成功");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //刷新
    private void tcpClient(){
        int port=5000;
        String hostip="10.133.129.55";   //39.97.250.70   //在同一局域网下才能使用   A1005 192.168.123.2//10.133.129.55
        try{
            Socket socketClient = new Socket(hostip, port);     	//1.bind
            InputStream in=socketClient.getInputStream();			//2.获得IO流
            OutputStream out = socketClient.getOutputStream();

            out.write(serach_content.getBytes());					//3.发送
            Log.d(TAG,serach_content);
            out.flush();


            byte[] bytes = new byte[9216];
            in.read(bytes);
            //4.接收
            poem_content=new String(bytes, StandardCharsets.UTF_8).trim();
            Log.i(TAG, poem_content);

            socketClient.close();
            //5.关闭
        } catch (IOException e){e.printStackTrace();}
    }

    //语音播放
        private void initSpeech() {
        // 将“12345678”替换成您申请的 APPID，申请地址： http://www.xfyun.cn
        // 请勿在 “ =”与 appid 之间添加任务空字符或者转义符
        SpeechUtility. createUtility( this, SpeechConstant.APPID + "=5ddd0498" );
    }
    private void speekText() {
        //1. 创建 SpeechSynthesizer 对象 , 第二个参数： 本地合成时传 InitListener
        SpeechSynthesizer mTts = SpeechSynthesizer.createSynthesizer( this, null);
        //2.合成参数设置，详见《 MSC Reference Manual》 SpeechSynthesizer 类
        //设置发音人（更多在线发音人，用户可参见 附录 13.2
        mTts.setParameter(SpeechConstant. VOICE_NAME, "xiaoyan" ); // 设置发音人
        mTts.setParameter(SpeechConstant. SPEED, "50" );// 设置语速
        mTts.setParameter(SpeechConstant. VOLUME, "80" );// 设置音量，范围 0~100
        mTts.setParameter(SpeechConstant. ENGINE_TYPE, SpeechConstant. TYPE_CLOUD); //设置云端
        //设置合成音频保存位置（可自定义保存位置），保存在 “./sdcard/iflytek.pcm”
        //保存在 SD 卡需要在 AndroidManifest.xml 添加写 SD 卡权限
        //仅支持保存为 pcm 和 wav 格式， 如果不需要保存合成音频，注释该行代码
        mTts.setParameter(SpeechConstant. TTS_AUDIO_PATH, "./sdcard/iflytek.pcm" );
        //3.开始合成
        mTts.startSpeaking(poem_info, new MySynthesizerListener()) ;

    }


    class MySynthesizerListener implements SynthesizerListener {

        @Override
        public void onSpeakBegin() {
            showTip(" 开始播放 ");
        }

        @Override
        public void onSpeakPaused() {
            showTip(" 暂停播放 ");
        }

        @Override
        public void onSpeakResumed() {
            showTip(" 继续播放 ");
        }

        @Override
        public void onBufferProgress(int percent, int beginPos, int endPos , String info) {
            // 合成进度
        }

        @Override
        public void onSpeakProgress(int percent, int beginPos, int endPos) {
            // 播放进度
        }

        @Override
        public void onCompleted(SpeechError error) {
            if (error == null) {
                showTip("播放完成 ");
            } else if (error != null ) {
                showTip(error.getPlainDescription( true));
            }
        }

        @Override
        public void onEvent(int eventType, int arg1 , int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话 id，当业务出错时将会话 id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话 id为null
            //if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            //     String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            //     Log.d(TAG, "session id =" + sid);
            //}
        }
    }
    private void showTip (String data) {
        Toast.makeText( this, data, Toast.LENGTH_SHORT).show() ;
    }
}