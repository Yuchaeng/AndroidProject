package com.example.graduproject;

import java.util.List;

public class userProfile {
    String uid;
    String name;
    String birth;
    String gender;
    String dept;
    String studentId;

    String profileImageUrl;
    String nickName;
    String emptyTime;
    String interest;
    String introduce;

    int changed;

    //생성자
    public userProfile() {}

    //getter
    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public String getBirth() {
        return birth;
    }

    public String getGender() {
        return gender;
    }

    public String getDept() {
        return dept;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public String getNickName() {
        return nickName;
    }

    public String getEmptyTime() {
        return emptyTime;
    }

    public String getInterest() {
        return interest;
    }

    public String getIntroduce() {
        return introduce;
    }

    //setter
    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setEmptyTime(String emptyTime) {
        this.emptyTime = emptyTime;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }


}
