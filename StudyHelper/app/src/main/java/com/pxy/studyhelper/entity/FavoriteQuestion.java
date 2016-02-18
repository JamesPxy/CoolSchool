package com.pxy.studyhelper.entity;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

@Table(name = "FavoriteQuestion")
public class FavoriteQuestion {
	@Column(name = "id",isId = true,autoGen = true)
	private int id;
	@Column(name = "question")
	private String question;
	@Column(name = "answerA")
	private String answerA;
	@Column(name = "answerB")
	private String answerB;
	@Column(name = "answerC")
	private String answerC;
	@Column(name = "answerD")
	private String answerD;
	@Column(name = "answerE")
	private String answerE;
	@Column(name = "rightAnswer")
	public int rightAnswer;
	@Column(name = "explaination")
	private String explaination;
	@Column(name = "isWrong")
	private int  isWrong;
	@Column(name = "isFavorite")
	private boolean  isFavorite;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getAnswerA() {
		return answerA;
	}

	public void setAnswerA(String answerA) {
		this.answerA = answerA;
	}

	public String getAnswerB() {
		return answerB;
	}

	public void setAnswerB(String answerB) {
		this.answerB = answerB;
	}

	public String getAnswerC() {
		return answerC;
	}

	public void setAnswerC(String answerC) {
		this.answerC = answerC;
	}

	public String getAnswerD() {
		return answerD;
	}

	public void setAnswerD(String answerD) {
		this.answerD = answerD;
	}

	public String getAnswerE() {
		return answerE;
	}

	public void setAnswerE(String answerE) {
		this.answerE = answerE;
	}

	public int getRightAnswer() {
		return rightAnswer;
	}

	public void setRightAnswer(int rightAnswer) {
		this.rightAnswer = rightAnswer;
	}

	public String getExplaination() {
		return explaination;
	}

	public void setExplaination(String explaination) {
		this.explaination = explaination;
	}

	public int getIsWrong() {
		return isWrong;
	}

	public void setIsWrong(int isWrong) {
		this.isWrong = isWrong;
	}

	public boolean isFavorite() {
		return isFavorite;
	}

	public void setIsFavorite(boolean isFavorite) {
		this.isFavorite = isFavorite;
	}

	@Override
	public String toString() {
		return "FavoriteQuestion{" +
				"id=" + id +
				", question='" + question + '\'' +
				", answerA='" + answerA + '\'' +
				", answerB='" + answerB + '\'' +
				", answerC='" + answerC + '\'' +
				", answerD='" + answerD + '\'' +
				", answerE='" + answerE + '\'' +
				", rightAnswer=" + rightAnswer +
				", explaination='" + explaination + '\'' +
				", isWrong=" + isWrong +
				", isFavorite=" + isFavorite +
				'}';
	}
}
