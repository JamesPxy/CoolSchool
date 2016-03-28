package com.pxy.studyhelper.entity;

import java.io.Serializable;

public class Question implements Serializable{
	public int id;
	public String question;
	public String answerA;
	public String answerB;
	public String answerC;
	public String answerD;
	public String answerE;
	public int rightAnswer;
	public int selectedAnswer=-1;
	public String explaination;
	public int  isWrong;
	public int  isFavorite;
	

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
	public int getSelectedAnswer() {
		return selectedAnswer;
	}
	public void setSelectedAnswer(int selectedAnswer) {
		this.selectedAnswer = selectedAnswer;
	}
	public String getExplaination() {
		return explaination;
	}
	public void setExplaination(String explaination) {
		this.explaination = explaination;
	}

	@Override
	public String toString() {
		return "Question{" +
				"id=" + id +
				", question='" + question + '\'' +
				", answerA='" + answerA + '\'' +
				", answerB='" + answerB + '\'' +
				", answerC='" + answerC + '\'' +
				", answerD='" + answerD + '\'' +
				", answerE='" + answerE + '\'' +
				", rightAnswer=" + rightAnswer +
				", selectedAnswer=" + selectedAnswer +
				", explaination='" + explaination + '\'' +
				", isWrong=" + isWrong +
				", isFavorite=" + isFavorite +
				'}';
	}
}
