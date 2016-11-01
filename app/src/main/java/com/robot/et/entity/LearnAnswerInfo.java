package com.robot.et.entity;

/**
 * 机器学习的答案
 */
public class LearnAnswerInfo {

	private int questionId;// 问题ID
	private String robotNum;// 机器编号
	private String answer;// 答案
	private String action;// 动作
	private int learnType;// 类型

	public int getQuestionId() {
		return questionId;
	}
	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}
	public String getRobotNum() {
		return robotNum;
	}
	public void setRobotNum(String robotNum) {
		this.robotNum = robotNum;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public int getLearnType() {
		return learnType;
	}
	public void setLearnType(int learnType) {
		this.learnType = learnType;
	}
	public LearnAnswerInfo() {
		super();
	}
}
