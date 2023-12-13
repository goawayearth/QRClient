package com.example.qrclient;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bean.Courier;
import com.example.bean.MyUserInfo;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class MeFragment extends Fragment  implements View.OnClickListener{
    private static final String ip = "192.168.117.235";
    private static final String TAG = "MeFragment";
    private TextView mMeId = null;
    private TextView mMeName = null;
    private TextView task_num = null;
    private Button button = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new FetchBlogTask().execute();
        setHasOptionsMenu(true);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_me, container, false);
        getActivity().setTitle("我的");
        Courier me = MyUserInfo.get().getMyUser();

        mMeId = view.findViewById(R.id.me_id);
        mMeId.setText(me.getCourier_id());
        mMeName = view.findViewById(R.id.me_name);
        task_num = view.findViewById(R.id.task_num);
        button = view.findViewById(R.id.task_btn);

        button.setOnClickListener(this);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.logout,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){   //注销
            case R.id.logout:
                Intent intent = MainActivity.newIntent(getActivity());
                //清除当前这个栈，再从新建一个栈
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = HomeActivity.newIntent(getActivity());
        startActivity(intent);
    }


    private class FetchBlogTask extends AsyncTask<Void,Void,Courier> {
        @Override
        protected Courier doInBackground(Void... voids) {
            Courier user = new Courier();
            try {
                String id = MyUserInfo.get().getMyUser().getCourier_id();
                String path = "http://"+ip+":8080/server_war_exploded/getUser?id="+id;
                URL url = new URL(path);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");//获取服务器数据
                connection.setReadTimeout(10000);//设置读取超时的毫秒数
                connection.setConnectTimeout(10000);//设置连接超时的毫秒数

                if(connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    String result = reader.readLine();//读取服务器进行逻辑处理后页面显示的数据
                    Gson gson = new Gson();
                    user = gson.fromJson(result,Courier.class);
                    // 将json数据解析成为User类型付给User对象

                }else{
                    Log.i(TAG,"访问服务器失败");
                }
            } catch (Exception e) {
                System.out.println(e);
            };
            return user;
        }

        @Override
        protected void onPostExecute(Courier user) {
            // 给界面各个元素赋值
            String name = user.getName();
            mMeName.setText(name);
            String num = user.getMobile();
            task_num.setText(num);
        }
    }
}
