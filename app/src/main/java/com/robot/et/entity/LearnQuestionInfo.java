package com.robot.et.entity;

/**
 * 机器学习的问题
 */
public class LearnQuestionInfo {

    private int id;// 问题ID
    private String robotNum;// 机器编号
    private String question;// 问题
    private int learnType;// 问题类型

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRobotNum() {
        return robotNum;
    }

    public void setRobotNum(String robotNum) {
        this.robotNum = robotNum;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getLearnType() {
        return learnType;
    }

    public void setLearnType(int learnType) {
        this.learnType = learnType;
    }

    public LearnQuestionInfo() {
        super();
    }
}
