package com.example.qrclient;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
public class ViewQR extends AppCompatActivity implements View.OnClickListener {

//    private static final String ip = "1192.168.221.235";
    static String ip = GetIP.ip;
    private static final String EXTRA_NUM = "express_num";
    private String content = null;
    private String express_num = null;
    private ImageView imageView = null;
    private TextView textView = null;
    private Button button = null;

    public static Intent newIntent(FragmentActivity activity, String num) {
        Intent intent = new Intent(activity, ViewQR.class);
        intent.putExtra(EXTRA_NUM, num);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_qr);

        imageView = findViewById(R.id.imageView);
        textView = findViewById(R.id.textView);
        button = findViewById(R.id.button_delete);

        express_num = getIntent().getStringExtra(EXTRA_NUM);
        Log.i(TAG, express_num);

        loadImage(express_num);

        button.setOnClickListener(this);

        imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // 处理长按事件的逻辑
                Toast.makeText(ViewQR.this, "二维码扫描成功！", Toast.LENGTH_SHORT).show();
                //现在是得到了所有信息的字符串，要将字符串里面的信息解析出来
                /*
                 * 运单号
                 * 快递员id
                 * 收货人名字
                 * 收货人地址
                 * 收货人电话号
                 * 发送方名字
                 * 发送方地址
                 * 发送方电话
                 * 快递的类型
                 */
                String[] substrings = content.split("&");
                String text = "运单号：" + substrings[0] + "\n" +
                        "收件人姓名：" + substrings[2] + "\n" +
                        "收件人地址：" + substrings[3] + "\n" +
                        "收件人电话：" + substrings[4] + "\n" +
                        "寄件人姓名：" + substrings[5] + "\n" +
                        "寄件人地址：" + substrings[6] + "\n" +
                        "寄件人电话：" + substrings[7] + "\n" +
                        "快递类型：" + substrings[8];
                textView.setText(text);
                return true; // 返回 true 表示事件被消费
            }
        });
    }

    private void loadImage(String express_num) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://" + ip + ":8080/server_war_exploded/" + express_num + ".png");
                    Bitmap bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    showQrCode(bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void showQrCode(Bitmap bitmap) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap);
                    content = QrCodeScanner.scanQrCode(bitmap);
                    // 如果需要，可以处理结果
                } else {
                    Log.e(TAG, "Bitmap 为空");
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        // 点击按钮时，执行异步任务来处理网络请求
        new DeleteTask().execute();
    }

    private class DeleteTask extends AsyncTask<Void, Void, Integer> {

        @Override
        protected Integer doInBackground(Void... voids) {
            try {
                URL url = new URL("http://" + ip + ":8080/server_war_exploded/deleteQR?express_num=" + express_num);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(5000);

                // 发送请求
                int responseCode = connection.getResponseCode();

                // 关闭连接
                connection.disconnect();

                return responseCode;

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer responseCode) {
            if (responseCode != null) {
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    Log.i(TAG, "成功");
                    // 在这里处理成功的逻辑，比如跳转到另一个Activity
                    Intent intent = new Intent(ViewQR.this, HomeActivity.class);
                    startActivity(intent);
                } else {
                    // 在这里处理失败的逻辑
                    Log.i(TAG, "失败");
                    // 可以添加一些UI反馈，比如Toast
                    Toast.makeText(ViewQR.this, "删除失败", Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.e(TAG, "网络请求失败");
            }
        }
    }
}
