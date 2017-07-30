package com.github.quartzweb.job;

import com.github.quartzweb.utils.DateUtils;

import java.util.Date;

/**
 * @author quxiucheng [quxiuchengdev@gmail.com]
 */
public class Test {

    private String a;

    private int b;

    public Test() {
    }

    public Test(String a) {
        this.a = a;
    }

    public Test(int b) {
        this.b = b;
    }

    public Test(String a, int b) {
        this.a = a;
        this.b = b;
    }

    public void test(){
        System.out.println(DateUtils.formart(new Date()) + ":test:" + toString());
    }

    public void test(String str){
        System.out.println(DateUtils.formart(new Date()) + ":"+str
                +":"+":test:" + toString());
    }
    @Override
    public String toString() {
        return "Test{" +
                "a='" + a + '\'' +
                ", b=" + b +
                '}';
    }
}
