package com.example.chatgpt_api;

/**
 * @author xiaoyun
 * @Description: (用一句话描述)
 * @date 2023/2/15 14:29
 */
public class Car {
    private String carname;
    private int carimage;

    //    创建这个类的构造函数
//    构造函数的函数名就是当前类的名字
//    构造函数没有返回值
    public Car(String name) {
        this.carname = name;
    }

    public String getCarname() {
        return carname;
    }

    public int getCarimage(){
        return carimage;
    }
}
