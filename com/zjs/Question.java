package com.zjs;

/**
 * 问题的实体类。
 * @author zjs
 * */
public class Question {
    /**问题文本*/
    public String q=null,
    /**选项A文本*/
    a=null,
    /**选项B文本*/
    b=null,
    /**选项C文本，留空以设为无效选项*/
    c=null,
    /**选项D文本，留空以设为无效选项*/
    d=null;
    /**正确选项的字母（a,b,c,d）*/
    public String answer=null;
}
