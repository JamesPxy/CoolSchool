package com.pxy.studyhelper.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.EditText;

import com.pxy.studyhelper.R;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * User: Pxy(15602269883@163.com)
 * Date: 2016-02-17
 * Time: 21:12
 * FIXME
 */
@ContentView(value = R.layout.activity_make_topic)
public class MakeTopicActivity   extends Activity {

    @ViewInject(value = R.id.editText)
    private EditText  mEdtContent;

    private static final int RESULT_LOAD_IMG=1;


    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        x.view().inject(this);
    }

    @Event(value = {R.id.iv_send,R.id.iv_add_img},type = View.OnClickListener.class)
    private void doClick(View  view){
        switch (view.getId()){
            case R.id.iv_send:
                break;

            case R.id.iv_add_img:
                GetImgPic();
                break;
        }
    }

    private void GetImgPic() {
        Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(i, RESULT_LOAD_IMG);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        
        super.onActivityResult(requestCode, resultCode, data);
    }
}
