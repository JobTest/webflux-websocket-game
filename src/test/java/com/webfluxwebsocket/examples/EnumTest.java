package com.webfluxwebsocket.examples;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EnumTest {

    enum ColorTitle {
        A("one"), B("two"), C("three"),
        NONE("");

        private String title;

        ColorTitle(String title) {
            this.title = title;
        }
    }

    enum ColorRGB {
        A(1), B(2), C(3),
        NONE(0);

        private int value;

        ColorRGB(int value) {
            this.value = value;
        }
    }

    public static void main(String[] args) {
        ColorTitle title;
        ColorRGB rgb;
        boolean c;

        title = ColorTitle.A;
        rgb = ColorRGB.A;
        System.out.println("title = " + title + "; rgb = " + rgb + ";"); //todo: title = A; value = A;
        System.out.println("title = " + title.name() + "; rgb = " + rgb.name() + ";"); //todo: title = A; value = A;
        System.out.println("title = " + title.toString() + "; rgb = " + rgb.toString() + ";"); //todo: title = A; value = A;
        c = title.name()==rgb.name();
        System.out.println(c); //todo: true

        System.out.println("--------------------------------");

        title = ColorTitle.B;
        rgb = ColorRGB.C;
        c = title.name()==rgb.name();
        System.out.println(c); //todo: false

        System.out.println("--------------------------------");

        title = ColorTitle.A;
        rgb = toRGB(title);
        System.out.println("title = " + title.title + "; rgb = " + rgb.value); //todo: title = one; rgb = 1

        rgb = ColorRGB.B;
        title = toTitle(rgb);
        System.out.println("rgb = " + rgb.value + "; title = " + title.title + ";"); //todo: rgb = 2; title = two;

        System.out.println("--------------------------------");

        List<ColorTitle> titles = new ArrayList<>();
        titles.add(ColorTitle.A);
        titles.add(ColorTitle.B);
        titles.add(ColorTitle.C);

        List<ColorRGB> rgbs = titles.stream()
                .map(t -> toRGB(t))
                .collect(Collectors.toList());
        for (ColorRGB _rgb : rgbs) {
            System.out.print("rgb = " + _rgb.value + "; ");
        }

        System.out.println("\n================================");

        System.out.println( ColorTitle.valueOf("A") ); //todo: A
        System.out.println( Arrays.asList(ColorTitle.values()).contains(ColorTitle.A) ); //todo: true
        System.out.println("'one' = " + getTitle("one")); //todo: 'one' = A
    }

    static ColorTitle getTitle(String title) {
        return Arrays.stream(ColorTitle.values())
                .filter(x -> x.title.equals(title))
                .findFirst()
                .orElse(ColorTitle.NONE);

//        for (ColorTitle x: ColorTitle.values()) {
//            if (x.title.equals("one")) return x;
//        }
//        return ColorTitle.NONE;
    }

    static ColorRGB toRGB(ColorTitle title) {
        return ColorRGB.valueOf(title.name());
    }

    static ColorTitle toTitle(ColorRGB rgb) {
        return ColorTitle.valueOf(rgb.name());
    }
}
