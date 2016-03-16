package com.pxy.studyhelper.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.pxy.studyhelper.R;
import com.pxy.studyhelper.adapter.NewFriendAdapter;
import com.pxy.studyhelper.utils.DialogTips;

import cn.bmob.im.bean.BmobInvitation;
import cn.bmob.im.db.BmobDB;

/** 新朋友
 * @ClassName: NewFriendActivity
 * @Description: TODO
 */
public class NewFriendActivity extends ActivityBase implements OnItemLongClickListener{

	ListView listview;

	NewFriendAdapter adapter;
	ImageView  ivBack;

	String from="";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_friend);
		from = getIntent().getStringExtra("from");
		initView();
	}

	private void initView(){
		ivBack= (ImageView) findViewById(R.id.iv_back);
		ivBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		listview = (ListView)findViewById(R.id.list_newfriend);
		listview.setOnItemLongClickListener(this);
		adapter = new NewFriendAdapter(this,BmobDB.create(this).queryBmobInviteList());
		listview.setAdapter(adapter);
		if(from==null){//若来自通知栏的点击，则定位到最后一条
			listview.setSelection(adapter.getCount());
		}
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int position,
								   long arg3) {
		// TODO Auto-generated method stub
		BmobInvitation invite = (BmobInvitation) adapter.getItem(position);
		showDeleteDialog(position,invite);
		return true;
	}

	public void showDeleteDialog(final int position,final BmobInvitation invite) {
		DialogTips dialog = new DialogTips(this,invite.getFromname(),"删除好友请求", "确定",true,true);
		// 设置成功事件
		dialog.SetOnSuccessListener(new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialogInterface, int userId) {
				deleteInvite(position,invite);
			}
		});
		// 显示确认对话框
		dialog.show();
		dialog = null;
	}

	/**
	 * 删除请求
	 * deleteRecent
	 * @param @param recent
	 * @return void
	 * @throws
	 */
	private void deleteInvite(int position, BmobInvitation invite){
		adapter.remove(position);
		BmobDB.create(this).deleteInviteMsg(invite.getFromid(), Long.toString(invite.getTime()));
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(from==null){
			startActivity(new Intent(NewFriendActivity.this,MainActivity.class));
		}
	}


}
