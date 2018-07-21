package com.webfluxwebsocket.examples;

/**
 * Объекты - сравниваются по ссылке, а примитивные типы - сравниваются по значению!
 * Статический объект в системе всегда один - поэтому и ссылка на такой статический объект всегда будет единственная!
 * Enum-ерейшен является сингтоном и такой объект всегда единственный - поэтому и ссылка на такой статический объект всегда будет единственная!
 *
 * Все статические классы и enum-ы являются класами верхнего уровня - то есть, Java по умолчанию создает только единственный такой объект (без оператора 'new')
 */
public class IntegerTest {

    static Integer a1;
    static Integer b1;

    enum A { ONE, TWO, THREE }
//    enum B { ONE, TWO, THREE }

    public static void main(String[] args) {
        Integer a;
        Integer b;
        boolean c;

        a = new Integer(100);
        b = new Integer(100);
        c = a==b; //todo: false
        System.out.println(c);

        a = 100;
        b = 100;
        c = a==b; //todo: true
        System.out.println(c);

        a = null;
        b = null;
        c = a==b; //todo: true
        System.out.println(c);

        System.out.println("--------------------------------");

        a1 = new Integer(100);
        b1 = new Integer(100);
        c = a==b; //todo: true
        System.out.println(c);

        A oneA = A.ONE;
//        B oneB = A.ONE;
        A oneB = A.ONE;
        c = a==b; //todo: true
        System.out.println(c);

        System.out.println("--------------------------------");

        IntegerTest.a1 = 200;
        IntegerTest.A a2 = A.TWO;
        System.out.println("a1 = " + a1 + "; a2 = " + a2 + ";");
    }

}
