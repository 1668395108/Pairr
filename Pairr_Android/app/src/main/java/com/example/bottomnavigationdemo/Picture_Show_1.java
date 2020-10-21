package com.example.bottomnavigationdemo;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import pub.devrel.easypermissions.EasyPermissions;

public class Picture_Show_1 extends AppCompatActivity implements EasyPermissions.PermissionCallbacks{
    private ImageView imageView_show=null;
    private String TAG="myLog";
    private Uri uri;
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/*");
    private final OkHttpClient client = new OkHttpClient();
    private Button button_up;
    String photoPath;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // requestWindowFeature(Window.FEATURE_NO_TITLE);//去除标题栏那个框框
        setContentView(R.layout.picture_show_1);
        if (getSupportActionBar() != null){     //这个也是哦（去除标题栏的框框）
            getSupportActionBar().hide();
        }
        imageView_show=findViewById(R.id.image_showpicture);
        button_up=findViewById(R.id.button_up);
        goPhotoAlbum();
   //   startActivityForResult(selectPicture(),1);
        button_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                up(photoPath);
            }
        });
    }
          /*获取图片方法一*/
//    public Intent selectPicture()
//    {
//        Intent intent = new Intent(Intent.ACTION_PICK,
//                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        return intent;
//    }
    //回调函数进行图片地址的获取和显示
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        // TODO 自动生成的方法存根
//        if (requestCode == 1 && resultCode == RESULT_OK && null != data) {
//            Uri selectedImage = data.getData();
//            String[] filePathColumn = { MediaStore.Images.Media.DATA };
//            //查询我们需要的数据
//            Cursor cursor = getContentResolver().query(selectedImage,
//                    filePathColumn, null, null, null);
//            cursor.moveToFirst();
//
//            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//            String picturePath = cursor.getString(columnIndex);
//            cursor.close();
//
//            //拿到了图片的路径picturePath可以自行使用
//            imageView_show.setImageBitmap(BitmapFactory.decodeFile(picturePath));
//
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }



/*获取图片方法二*/
    //激活相册操作
    private void goPhotoAlbum() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,2);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

      if (requestCode == 2 && resultCode == RESULT_OK) {
            photoPath = getPhotoFromPhotoAlbum.getRealPathFromUri(this, data.getData());
            Log.d(TAG,photoPath);
            Glide.with(Picture_Show_1.this).load(photoPath).into(imageView_show);
        }

    }

    public void up(String path){
        String url="http://39.97.250.70:5000/image";
        MultipartBody.Builder builder=new MultipartBody.Builder().setType(MultipartBody.FORM);
        File file=new File(path);
        if(file!=null){
            builder.addFormDataPart("img",file.getName(), RequestBody.create(MEDIA_TYPE_PNG,file));
        }
        MultipartBody requestBody=builder.build();
        Log.d(TAG,path);
        Request request=new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d(TAG,"上传失败！");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                Log.d(TAG,"上传成功！");
            }
        });
    }



    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        Toast.makeText(this, "相关权限获取成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        Toast.makeText(this, "请同意相关权限，否则功能无法使用", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //框架要求必须这么写
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
}
