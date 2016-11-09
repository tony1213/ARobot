package com.robot.et.entity;

/**
 * 家庭位置信息
 */
public class FamilyLocationInfo {
    private String robotNum;
    private String positionName;
    private String positionX;
    private String positionY;
    private String spareContent;
    private String spareContent2;
    private String spareContent3;
    private int spareType;

    public String getRobotNum() {
        return robotNum;
    }

    public void setRobotNum(String robotNum) {
        this.robotNum = robotNum;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public String getPositionX() {
        return positionX;
    }

    public void setPositionX(String positionX) {
        this.positionX = positionX;
    }

    public String getPositionY() {
        return positionY;
    }

    public void setPositionY(String positionY) {
        this.positionY = positionY;
    }

    public String getSpareContent() {
        return spareContent;
    }

    public void setSpareContent(String spareContent) {
        this.spareContent = spareContent;
    }

    public String getSpareContent2() {
        return spareContent2;
    }

    public void setSpareContent2(String spareContent2) {
        this.spareContent2 = spareContent2;
    }

    public String getSpareContent3() {
        return spareContent3;
    }

    public void setSpareContent3(String spareContent3) {
        this.spareContent3 = spareContent3;
    }

    public int getSpareType() {
        return spareType;
    }

    public void setSpareType(int spareType) {
        this.spareType = spareType;
    }

    public FamilyLocationInfo() {
        super();
    }
}
