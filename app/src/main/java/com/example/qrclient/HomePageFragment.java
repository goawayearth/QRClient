package com.example.qrclient;


import android.annotation.SuppressLint;
import android.app.AppComponentFactory;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.bean.MyUserInfo;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class HomePageFragment extends Fragment {
    static String ip = "192.168.117.235";
    private static final String TAG = "HomePageFragment";
    /*
    类似于滚动窗口
    */
    private RecyclerView mRecyclerView;
    /*
    存储微博的列表
    */
    private List<String> express_num;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);
        getActivity().setTitle("主页");
        //承载微博的容器
        mRecyclerView = (RecyclerView) view.findViewById(R.id.homepage);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

    //程序正常启动：onCreate()->onStart()->onResume();
    //正常退出：onPause()->onStop()->onDestory()
    @Override
    public void onResume() {
        super.onResume();
        new FetchBlogTask().execute();
    }



    //异步的从微博的数据库里取出所有的微博数据，并且将它转化为Microblog的形式
    private class FetchBlogTask extends AsyncTask<Void,Void,List<String>> {
        @Override
        protected List<String> doInBackground(Void... voids) {

            List<String> list = new ArrayList<String>();
            try {
                String path = "http://"+ip+":8080/server_war_exploded/getQR?courier_id="+ MyUserInfo.get().getMyUser().getCourier_id();
                URL url = new URL(path);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");//获取服务器数据
                connection.setReadTimeout(10000);//设置读取超时的毫秒数
                connection.setConnectTimeout(10000);//设置连接超时的毫秒数

                if(connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    String result = reader.readLine();//读取服务器进行逻辑处理后页面显示的数据
                    JSONArray array = JSON.parseArray(result);
                    Log.i(TAG,""+array);

                    //将JSON数组转
                    for(int i=0;i<array.size();i++){
                        list.add(array.getString(i));
                        Log.i(TAG,"%%%%%%%%%%"+list.get(i));
                    }

                }else{
                    Log.i(TAG,"访问服务器失败");
                }
            } catch (Exception e) {
                System.out.println(e);
            };
            return list;
        }

        // 后台加载完成后，执行函数将数据传递给MicroblogAdapter
        @Override
        protected void onPostExecute(List<String> list) {
            express_num = list;
            /*
            Adapter用于将数据转化成视图并显示在控件上
             */
            mRecyclerView.setAdapter(new MicroblogAdapter(express_num));
        }
    }



    /*
    适配器类
    */
    private class MicroblogAdapter extends RecyclerView.Adapter<MicroblogHolder>{

        public MicroblogAdapter(List<String> microblogs){
            express_num = microblogs;
        }

        @NonNull
        @Override
        public MicroblogHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

            return new MicroblogHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull MicroblogHolder holder, int position) {
            String num = express_num.get(position);
            holder.bind(num);
        }

        @Override
        public int getItemCount() {
            return express_num.size();
        }
    }



    // holder用户微博内容
    private class MicroblogHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        String num = null;
        private TextView textView = null;
        /*
        每个微博内容的结构类
        */
        public MicroblogHolder(LayoutInflater inflater, ViewGroup parent){
            // 微博列表
            super(inflater.inflate(R.layout.list_tast,parent,false));
            itemView.setOnClickListener(this);
            textView = itemView.findViewById(R.id.btn_task);
        }

        public void bind(String num1) {
            //首先下载图片，然后将图片放在view上
            num = num1;
            String text = "任务："+num1;
            textView.setText(text);
        }


        @Override
        public void onClick(View v) {
            Log.i(TAG, "点击了");
            Intent intent = null;
            intent = ViewQR.newIntent(getActivity(), num);
            startActivity(intent);
        }

    }
}
