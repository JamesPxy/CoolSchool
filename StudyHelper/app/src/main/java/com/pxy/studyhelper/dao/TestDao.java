package com.pxy.studyhelper.dao;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.pxy.studyhelper.entity.Question;

import org.xutils.common.util.LogUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * 查询指定数据库获得所有问题，返回问题List集合
 * @author pxy
 *
 */
public class TestDao {
	private SQLiteDatabase db;
	private String dataName;
	private Context context;

	public TestDao(Context  context,String  dataName)
	{
		db = SQLiteDatabase.openDatabase(context.getFilesDir().getAbsolutePath()+"/"+dataName,null, SQLiteDatabase.OPEN_READONLY);
		//db = SQLiteDatabase.openDatabase("/data/data/com.pxy.exam/databases/question.db",null, SQLiteDatabase.OPEN_READONLY);
		this.dataName =dataName;
		this.context=context;
	}

	public List<Question> getQuestions()
	{
		String  data= dataName.replace(".db", "");
		String sql=("select * from "+data);

		List<Question> list = new ArrayList<Question>();
		Cursor cursor = db.rawQuery(sql, null);
		LogUtil.i("cursor get question data--" + cursor.getCount());
		if(cursor.getCount() > 0)
		{
			cursor.moveToFirst();
			for(int i = 0; i < cursor.getCount(); i++)
			{
				cursor.moveToPosition(i);
				Question question = new Question();
				question.question = cursor.getString(cursor.getColumnIndex("question"));
				question.answerA = cursor.getString(cursor.getColumnIndex("answerA"));
				question.answerB = cursor.getString(cursor.getColumnIndex("answerB"));
				question.answerC = cursor.getString(cursor.getColumnIndex("answerC"));
				question.answerD = cursor.getString(cursor.getColumnIndex("answerD"));
				question.answerE = cursor.getString(cursor.getColumnIndex("answerE"));
				question.rightAnswer = cursor.getInt(cursor.getColumnIndex("answer"));
				question.isWrong = cursor.getInt(cursor.getColumnIndex("isWrong"));
				question.explaination = cursor.getString(cursor.getColumnIndex("explaination"));
				question.setSelectedAnswer(-1);
				question.id = cursor.getInt(cursor.getColumnIndex("ID"));
				list.add(question);
			}
		}
		db.close();
		return list;
	}
	public List<Question> getWrongQuestion()
	{
		db = SQLiteDatabase.openDatabase(context.getFilesDir().getAbsolutePath()+"/"+dataName,null, SQLiteDatabase.OPEN_READONLY);

		String  data= dataName.replace(".db", "");
		Cursor cursor = db.rawQuery("select * from  " + data + "  where isWrong=?",new String[]{"1"});
		List<Question> wrongList = new ArrayList<Question>();
		LogUtil.i("cursor-getWrongQuestions--"+cursor.getCount());
		if(cursor.getCount() > 0) {
			cursor.moveToFirst();
			for(int i = 0; i < cursor.getCount(); i++)
			{
				cursor.moveToPosition(i);
				Question question = new Question();
				question.question = cursor.getString(cursor.getColumnIndex("question"));
				question.answerA = cursor.getString(cursor.getColumnIndex("answerA"));
				question.answerB = cursor.getString(cursor.getColumnIndex("answerB"));
				question.answerC = cursor.getString(cursor.getColumnIndex("answerC"));
				question.answerD = cursor.getString(cursor.getColumnIndex("answerD"));
				question.answerE = cursor.getString(cursor.getColumnIndex("answerE"));
				question.rightAnswer = cursor.getInt(cursor.getColumnIndex("answer"));
				question.isWrong = cursor.getInt(cursor.getColumnIndex("isWrong"));
				question.explaination = cursor.getString(cursor.getColumnIndex("explaination"));
				question.setSelectedAnswer(-1);
				question.id = cursor.getInt(cursor.getColumnIndex("ID"));
				wrongList.add(question);
			}
		}
		db.close();
		LogUtil.i("wrong Quesion size--"+wrongList.size());
		return wrongList;
	}

	//更新错题记录
	public boolean updateQuestion(String  where,int wrongNum){
		db = SQLiteDatabase.openDatabase(context.getFilesDir().getAbsolutePath()+"/"+dataName,null, SQLiteDatabase.OPEN_READWRITE);
		String  data= dataName.replace(".db", "");
		ContentValues contentValues=new ContentValues();
		contentValues.put("isWrong", wrongNum);
		int rows=db.update(data, contentValues,"answerA=?",new String[]{where});
		db.close();//释放资源
		LogUtil.i("update wrong  question  success...");
		if(rows!=-1) return   true;
		return false;
	}
//	//更新错题记录
//	public boolean getIsFavorite(String  where){
//		db = SQLiteDatabase.openDatabase(context.getFilesDir().getAbsolutePath()+"/"+dataName,null, SQLiteDatabase.OPEN_READWRITE);
//		String  data= dataName.replace(".db", "");
//		ContentValues contentValues=new ContentValues();
//		contentValues.put("isWrong", wrongNum);
//		int rows=db.update(data, contentValues,"answerA=?",new String[]{where});
//		db.rawQuery()
//		db.close();//释放资源
//		LogUtil.i("update wrong  question  success...");
//		if(rows!=-1) return   true;
//		return false;
//	}


}
