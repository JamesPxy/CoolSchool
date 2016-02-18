package com.pxy.studyhelper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pxy.studyhelper.R;
import com.pxy.studyhelper.utils.Constant;

public class TestSmallActivity extends AppCompatActivity {

    private ListView  mListView;
    private ArrayAdapter<String>  adapter;
    private int position1;
    private String[]  data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_list_layout);
        position1=getIntent().getIntExtra("position1",0);
        initView();

        data= Constant.TEST_SORTS[position1];

        adapter=new ArrayAdapter<>(this,R.layout.item_test_list, R.id.tv_testName,data);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(TestSmallActivity.this,TestListActivity.class);
                intent.putExtra("position1",position1);
                intent.putExtra("position2",position);
                startActivity(intent);
            }
        });
    }

    private void initView() {
        mListView= (ListView) findViewById(R.id.listView);
        RelativeLayout rv= (RelativeLayout) findViewById(R.id.include_tabbar);
        ImageView iv_back= (ImageView) rv.findViewById(R.id.iv_back);
        TextView title=(TextView)rv.findViewById(R.id.tv_title);
        title.setText("章节列表");
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
