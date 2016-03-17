package com.pxy.studyhelper.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.pxy.studyhelper.R;
import com.pxy.studyhelper.activity.ChatActivity;
import com.pxy.studyhelper.adapter.MessageRecentAdapter;
import com.pxy.studyhelper.utils.DialogTips;

import org.xutils.common.util.LogUtil;

import java.util.List;

import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.im.bean.BmobRecent;
import cn.bmob.im.db.BmobDB;


/** 最近会话
 * @ClassName: ConversationFragment
 * @Description: TODO
 * @author smile
 * @date 2014-6-7 下午1:01:37
 */
public class RecentFragment extends Fragment implements OnItemClickListener,OnItemLongClickListener{

	private View  rootView;
	private ListView listview;
	private TextView tvAlert;
	private List<BmobRecent> mBmobRecentList;

	private MessageRecentAdapter adapter;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (null == rootView) {
			rootView = inflater.inflate(R.layout.fragment_recent, null);
		}
		ViewGroup parent = (ViewGroup) rootView.getParent();
		if (parent != null) {
			parent.removeAllViewsInLayout();
		}
		initView();
		return rootView;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

	}

	private void initView(){
		tvAlert= (TextView) rootView.findViewById(R.id.tv_alert);
		listview = (ListView)rootView.findViewById(R.id.list);
		listview.setOnItemClickListener(this);
		listview.setOnItemLongClickListener(this);
		mBmobRecentList=BmobDB.create(getActivity()).queryRecents();
		if(mBmobRecentList.size()<1){
			tvAlert.setVisibility(View.VISIBLE);
		}
		adapter = new MessageRecentAdapter(getActivity(), R.layout.item_conversation,mBmobRecentList);
		listview.setAdapter(adapter);

	}

	/** 删除会话
	 * deleteRecent
	 * @param @param recent
	 * @return void
	 * @throws
	 */
	private void deleteRecent(BmobRecent recent){
		if(adapter!=null&&recent!=null) {
			mBmobRecentList.remove(recent);
			adapter.notifyDataSetChanged();
//			adapter.remove(recent);
			BmobDB.create(getActivity()).deleteRecent(recent.getTargetid());
			BmobDB.create(getActivity()).deleteMessages(recent.getTargetid());
		}else{
			LogUtil.e("delete Recent   error");
		}
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int position,
								   long arg3) {
		// TODO Auto-generated method stub
		BmobRecent recent = adapter.getItem(position);
		showDeleteDialog(recent);
		return true;
	}

	public void showDeleteDialog(final BmobRecent recent) {
		DialogTips dialog = new DialogTips(getActivity(),recent.getUserName(),"删除会话", "确定",true,true);
		// 设置成功事件
		dialog.SetOnSuccessListener(new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialogInterface, int userId) {
				deleteRecent(recent);
			}
		});
		// 显示确认对话框
		dialog.show();
		dialog = null;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		// TODO Auto-generated method stub
		BmobRecent recent = adapter.getItem(position);
		//重置未读消息
		BmobDB.create(getActivity()).resetUnread(recent.getTargetid());
		//组装聊天对象
		BmobChatUser user = new BmobChatUser();
		user.setAvatar(recent.getAvatar());
		user.setNick(recent.getNick());
		user.setUsername(recent.getUserName());
		user.setObjectId(recent.getTargetid());
		// TODO: 2016-02-26   启动聊天框
		Intent intent = new Intent(getActivity(), ChatActivity.class);
		intent.putExtra("user", user);
		startActivity(intent);
	}

	private boolean hidden;
	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		this.hidden = hidden;
		LogUtil.i("onHiddenChanged"+hidden);
		if(!hidden){
			refresh();
		}
	}

	public void refresh(){
		try {
			LogUtil.i("refresh()"+hidden);
			getActivity().runOnUiThread(new Runnable() {
				public void run() {
					mBmobRecentList= BmobDB.create(getActivity()).queryRecents();
					if(mBmobRecentList.size()<1){
						tvAlert.setVisibility(View.VISIBLE);
					}else {
						tvAlert.setVisibility(View.GONE);
					}
					adapter = new MessageRecentAdapter(getActivity(), R.layout.item_conversation,mBmobRecentList);
					listview.setAdapter(adapter);
//					adapter.notifyDataSetChanged();
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		LogUtil.i("onResume()" + hidden);
		if(!hidden){
			refresh();
		}
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		LogUtil.i("isVisibleToUser--"+isVisibleToUser);
		if(isVisibleToUser){
			refresh();
		}
	}
}
