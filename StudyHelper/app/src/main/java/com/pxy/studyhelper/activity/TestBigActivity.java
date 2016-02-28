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

public class TestBigActivity extends AppCompatActivity {

    private ListView  mListView;
    private ArrayAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_list_layout);
        initView();

        setListView();

    }

    private void initView() {
        mListView= (ListView) findViewById(R.id.listView);
        RelativeLayout rv= (RelativeLayout) findViewById(R.id.include_tabbar);
        ImageView iv_back= (ImageView) rv.findViewById(R.id.iv_back);
        TextView title=(TextView)rv.findViewById(R.id.tv_title);
        title.setText("科目列表");
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void setListView() {
        adapter=new ArrayAdapter<>(this,R.layout.item_test_list, R.id.tv_testName, Constant.TEST_BIG_SORT);
        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                Intent  mIntent=new Intent(TestBigActivity.this,TestSmallActivity.class);
                mIntent.putExtra("position1",position);
                startActivity(mIntent);
                finish();
            }
        });
    }


}
