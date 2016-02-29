package com.pxy.studyhelper.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.pxy.studyhelper.R;
import com.pxy.studyhelper.activity.TestSmallActivity;
import com.pxy.studyhelper.utils.Constant;

/**
 *展示科目详情
 */
public class TestBigFragment extends Fragment {
    private ListView mListView;
    private ArrayAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_testlist,null);

        mListView= (ListView) view.findViewById(R.id.listView);

        setListView();

        return view;
    }

    private void setListView() {
        adapter=new ArrayAdapter<>(getActivity(),R.layout.item_test_list, R.id.tv_testName, Constant.TEST_BIG_SORT);
        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent mIntent = new Intent(getActivity(), TestSmallActivity.class);
                mIntent.putExtra("position1", position);
                startActivity(mIntent);
            }
        });
    }

}
