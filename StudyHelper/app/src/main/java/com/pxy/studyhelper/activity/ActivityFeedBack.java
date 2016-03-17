package com.pxy.studyhelper.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.pxy.studyhelper.MyApplication;
import com.pxy.studyhelper.R;
import com.pxy.studyhelper.entity.Feedback;
import com.pxy.studyhelper.utils.DialogUtil;
import com.pxy.studyhelper.utils.Tools;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import cn.bmob.v3.listener.SaveListener;

/**
 * Created by:Pxy
 * Date: 2016-03-17
 * Time: 14:43
 */
@ContentView(value = R.layout.activity_feedback)
public class ActivityFeedBack  extends ActivityBase {

    @ViewInject(value = R.id.edt_feedback)
    private EditText edtFeedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        x.view().inject(this);
    }


    @Event(value={R.id.iv_back,R.id.tv_send},type = View.OnClickListener.class)
    private   void  doClick(View view){
        switch (view.getId()){
            case R.id.iv_back:finish();break;
            case R.id.tv_send:doSendFeedBack();break;
        }

    }

    private void doSendFeedBack() {
        String  msg=edtFeedback.getText().toString().trim();
        if(TextUtils.isEmpty(msg)){
            Tools.ToastShort("反馈不行不能为空...");
            return;
        }
        DialogUtil.showProgressDialog(this,"发送ing...");
        saveFeedbackMsg(msg);
    }


    /**
     * 保存反馈信息到Bmob云数据库中
     * @param msg 反馈信息
     */
    private void saveFeedbackMsg(String msg){
        Feedback feedback = new Feedback();
        feedback.setContent(msg);
        feedback.setUserId(MyApplication.mCurrentUser.getObjectId());
        feedback.setContact(MyApplication.mCurrentUser.getMobilePhoneNumber());
        feedback.save(this, new SaveListener() {
            @Override
            public void onSuccess() {
                Log.i("bmob", "反馈信息已保存到服务器");
                //发送推送信息
                Tools.ToastShort("反馈信息发送成功");
                DialogUtil.closeProgressDialog();
                finish();
            }
            @Override
            public void onFailure(int code, String arg0) {
                // TODO Auto-generated method stub
                Log.e("bmob", "保存反馈信息失败："+arg0);
                Tools.ToastShort("反馈信息发送失败，请稍后再试");
            }
        });
    }

}
