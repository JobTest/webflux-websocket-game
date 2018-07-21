package com.webfluxwebsocket.examples;

public class AbstractTest {

    public static void main(String[] args) {

        MyClazz myClazz = new MyClazz();

        myClazz.func1();
        myClazz.func2();
    }

}


class My {

    public void func1() {
        System.out.println("My.func1()");
    }
}

abstract class AMy extends My {

    public abstract void func1();

    public abstract void func2();
}

class MyClazz extends AMy {

    @Override
    public void func1() {
//        super.func1();
        System.out.println("MyClazz.func1()");
    }

    @Override
    public void func2() {
        System.out.println("MyClazz.func2()");
    }
}