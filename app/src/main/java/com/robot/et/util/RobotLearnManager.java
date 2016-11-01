package com.robot.et.util;

import android.text.TextUtils;
import android.util.Log;

import com.robot.et.db.RobotDB;
import com.robot.et.entity.LearnAnswerInfo;
import com.robot.et.entity.LearnQuestionInfo;

/**
 * 机器学习的管理
 */
public class RobotLearnManager {

    private static final String LEARN_TAG = "learn";

    /**
     * 增加智能学习的问题与答案
     * @param question 问题
     * @param answer 答案
     * @param action 动作
     * @param learnType 类型
     * @return
     */
    public static boolean insertLeanInfo(String robotNum, String question, String answer, String action, int learnType) {
        LearnQuestionInfo info = new LearnQuestionInfo();
        info.setRobotNum(robotNum);
        info.setQuestion(question);
        info.setLearnType(learnType);
        LearnAnswerInfo aInfo = new LearnAnswerInfo();
        aInfo.setRobotNum(robotNum);
        aInfo.setLearnType(learnType);
        if (!TextUtils.isEmpty(answer)) {
            aInfo.setAnswer(answer);
        }
        if (!TextUtils.isEmpty(action)) {
            aInfo.setAction(action);
        }
        return addLearnInfoToDB(info, aInfo);
    }

    /**
     * 把学习内容加入到数据库
     * @param info 问题信息
     * @param aInfo 答案信息
     * @return
     */
    private static boolean addLearnInfoToDB(LearnQuestionInfo info, LearnAnswerInfo aInfo) {
        if (info != null && aInfo != null) {
            RobotDB mDb = RobotDB.getInstance();
            String question = info.getQuestion();
            int questionId = mDb.getQuestionId(question);
            if (questionId != -1) {//已存在
                info.setId(questionId);
                mDb.updateLearnQuestion(info);
                aInfo.setQuestionId(questionId);
                mDb.updateLearnAnswer(aInfo);
            } else {//不存在
                mDb.addLearnQuestion(info);
                aInfo.setQuestionId(mDb.getQuestionId(question));
                mDb.addLearnAnswer(aInfo);
            }
            return true;
        }
        return false;
    }

    /**
     * 机器人学习
     * @param robotNum 机器编号
     * @param learnType 类型
     * @param questionAndAnswer 内容
     * @return
     */
    public static boolean learn(String robotNum, int learnType, String questionAndAnswer) {
        if (!TextUtils.isEmpty(questionAndAnswer)) {
            String question = MatchStringUtil.getQuestion(questionAndAnswer);
            String answer = MatchStringUtil.getAnswer(questionAndAnswer);
            Log.i(LEARN_TAG, "question===" + question);
            Log.i(LEARN_TAG, "answer===" + answer);
            if (!TextUtils.isEmpty(question) && !TextUtils.isEmpty(answer)) {
                return insertLeanInfo(robotNum, question, answer, "", learnType);
            }
        }
        return false;
    }

    /**
     * 获取机器人学习的信息
     * @param result 内容
     * @return
     */
    public static LearnAnswerInfo getLearnAnswerInfo(String result) {
        LearnAnswerInfo info = null;
        if (!TextUtils.isEmpty(result)) {
            RobotDB mDb = RobotDB.getInstance();
            int questionId = mDb.getQuestionId(result);
            Log.i(LEARN_TAG, "questionId====" + questionId);
            if (questionId != -1) {
                info = mDb.getLearnAnswer(questionId);
            }
        }
        return info;
    }
}
