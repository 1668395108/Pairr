package com.example.bottomnavigationdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;


public class SelectPicPopupWindow extends Activity implements View.OnClickListener {
    private Button btn_take_photo, btn_pick_photo, btn_cancel;
    private LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selectpicpopupwindow);
        btn_take_photo = this.findViewById(R.id.btn_take_photo);
        btn_pick_photo = this.findViewById(R.id.btn_pick_photo);
        btn_cancel =  this.findViewById(R.id.btn_cancel);
        layout=findViewById(R.id.pop_layout);
        //添加选择窗口范围监听可以优先获取触点，即不再执行onTouchEvent()函数，点击其他地方时执行onTouchEvent()函数销毁Activity
        layout.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(getApplicationContext(), "提示：点击窗口外部关闭窗口！",
                        Toast.LENGTH_SHORT).show();
            }
        });
        //添加按钮监听
        btn_cancel.setOnClickListener(this);
        btn_pick_photo.setOnClickListener(this);
        btn_take_photo.setOnClickListener(this);
    }



    public void onClick(View v) {
        this.overridePendingTransition(0, R.anim.push_buttom_out);
        switch (v.getId()) {
            case R.id.btn_take_photo:
                Intent intent=new Intent(getApplicationContext(), Picture_Take_show.class);
                startActivity(intent);
                break;
            case R.id.btn_pick_photo:
                Intent intent1=new Intent(getApplicationContext(),Picture_Show_1.class);
                startActivity(intent1);
                break;
            case R.id.btn_cancel:
                break;
        }
        finish();
    }

}
