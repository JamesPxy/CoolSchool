package com.pxy.studyhelper.entity;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * User: Pxy(15602269883@163.com)
 * Date: 2016-02-04
 * Time: 23:38
 * FIXME
 */
public class Test extends BmobObject  implements Serializable{

    private String  name;
    private BmobFile TestFile;
    private int sorts1;
    private int sorts2;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BmobFile getTestFile() {
        return TestFile;
    }

    public void setTestFile(BmobFile testFile) {
        TestFile = testFile;
    }

    public int getSorts1() {
        return sorts1;
    }

    public void setSorts1(int sort1) {
        this.sorts1 = sort1;
    }

    public int getSorts2() {
        return sorts2;
    }

    public void setSorts2(int sort2) {
        this.sorts2 = sort2;
    }

    @Override
    public String toString() {
        return "Test{" +
                "name='" + name + '\'' +
                ", TestFile=" + TestFile +
                ", sorts1=" + sorts1 +
                ", sorts2=" + sorts2 +
                '}';
    }
}
