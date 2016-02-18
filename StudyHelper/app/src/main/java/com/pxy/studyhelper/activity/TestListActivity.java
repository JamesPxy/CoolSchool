package com.pxy.studyhelper.activity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pxy.studyhelper.R;
import com.pxy.studyhelper.adapter.DownloadAdapter;
import com.pxy.studyhelper.biz.GetExamDataBiz;
import com.pxy.studyhelper.entity.Test;
import com.pxy.studyhelper.utils.Constant;
import com.pxy.studyhelper.utils.DialogUtil;
import com.pxy.studyhelper.utils.IsDownload;
import com.pxy.studyhelper.utils.LoadingDialog;
import com.pxy.studyhelper.utils.Tools;

import java.util.List;

public class TestListActivity extends AppCompatActivity {

    private ListView  mListView;
    private DownloadAdapter mDownloadAdapter;

    private int sort1,sort2;

    private MyReceiver  myReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_list_layout);
        initView();
        sort1=getIntent().getIntExtra("position1",0);
        sort2=getIntent().getIntExtra("position2",0);
        LoadingDialog.showLoadingDialog(this);
        //请求获取试题内容
        GetExamDataBiz.getExamData(this, sort1, sort2);
        myReceiver=new MyReceiver();
        IntentFilter filter=new IntentFilter(Constant.RECEIVER_DOWNLOAD);
        registerReceiver(myReceiver, filter);

    }

    private void initView() {
        mListView= (ListView) findViewById(R.id.listView);
        RelativeLayout rv= (RelativeLayout) findViewById(R.id.include_tabbar);
        ImageView iv_back= (ImageView) rv.findViewById(R.id.iv_back);
        TextView title=(TextView)rv.findViewById(R.id.tv_title);
        title.setText("试题列表");
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void setListView(List<Test>  mTestArrayList) {
        mDownloadAdapter=new DownloadAdapter(this,mTestArrayList);
        mListView.setAdapter(mDownloadAdapter);
        LoadingDialog.dissmissDialog();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myReceiver);
    }

    class MyReceiver  extends BroadcastReceiver{
        @Override
        public void onReceive(final Context context, Intent intent) {
            String uri=intent.getStringExtra("uri");
            final Test  data= (Test) intent.getSerializableExtra("test");
            DialogUtil.closeProgressDialog();
            if(IsDownload.isDownload(TestListActivity.this,uri)){
                if(data==null)return;
                AlertDialog.Builder  builder=new AlertDialog.Builder(context);
                builder.setIcon(R.drawable.ic_luncher);
                builder.setTitle("选择模式:");
                builder.setItems(new String[]{"练习模式", "测试模式", "错题模式"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       switch (which){
                           case 0:case 2:{
                               Intent intent = new Intent(TestListActivity.this, PracticeActivity.class);
                               //试题类别
                               intent.putExtra("dbName", data.getTestFile().getFilename());
                               //做题模式
                               intent.putExtra("mode",which);
                               context.startActivity(intent);
                           }
                           break;
                           case 1:{
                               Intent intent = new Intent(TestListActivity.this, ExamActivity.class);
                               //试题类别
                               intent.putExtra("dbName", data.getTestFile().getFilename());
                               context.startActivity(intent);
                           }

                       }
                    }
                });
                builder.show();
            }else{
                Tools.ToastShort("下载失败,请检查网络...");
            }
        }
    }
}
