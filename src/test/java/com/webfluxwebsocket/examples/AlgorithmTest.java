package com.webfluxwebsocket.examples;

/**
 * @see http://www.quizful.net/interview/java/string-stringbuffer-difference
 *      ********************************************************************
 * String - является immutable, нельзя модифицировать объект String, но можна заменить его созданием нового экземпляра (и это обходится дороговато)
 * StringBuffer и StringBuilder - является mutable, использовать StringBuffer или StringBuilder следует тогда когда нужно модифицировать содержимое
 * StringBuilder - не синхронизирован что делает его значительно шустрее (но цена скорости - небезопасное поведение в мультипоточной среде)
 */


public class AlgorithmTest {

    public static void main(String[] args) {
        String in = "1000000000000000000000000";

        char[] charArray = in.toCharArray(); // можно перевести строку в массив чтобы было проще манипулировать строкой

        char[] reverseCharArray = toReverse(charArray); // точка отсчитывается с конца числа (строки) - поэтому строка переворачивается

        String result = toSplit(reverseCharArray);

        System.out.println( result );
//        System.out.println( in );
//        System.out.println( result.replace(".", "") );
//  http://easy-code.ru/lesson/formatting-numeric-output-java
//  http://qaru.site/questions/606025/split-a-string-at-every-3rd-comma-in-java
//  https://ru.stackoverflow.com/questions/37463/Разбиение-числа-на-цифры-независимо-от-длины
    }

    static char[] toReverse(char[] charArray) {
        int lengthCharArray = charArray.length;
        int   halfCharArray = charArray.length / 2;

        for (int beginningIndex = 0; beginningIndex < halfCharArray; beginningIndex++) {
            int endingIndex = lengthCharArray - beginningIndex - 1;
            char beginningVal = charArray[beginningIndex];
            char    endingVal = charArray[endingIndex];

            charArray[beginningIndex] = endingVal;
            charArray[endingIndex] = beginningVal;
        }

        return charArray;
    }

    static String toSplit(char[] charArray) {
        int lengthCharArray = charArray.length; // длина строки это количество всех элементов в строке
        StringBuilder sb = new StringBuilder();

        for (int index = lengthCharArray; 0 < index; index--) {
            int arrayIndex = index - 1; // как правило массив начинается (не с первого) с нулевого элемента
            if (isInsertPoint(index, lengthCharArray)) sb.append('.');
            sb.append( charArray[arrayIndex] );
        }

        return sb.toString();
    }

    static boolean isInsertPoint(int index, int lengthCharArray) {
        int divisionByModule = index % 3;
        boolean isLastPoint = index == lengthCharArray;

        return (!isLastPoint && divisionByModule == 0) // при условии что: (1) элемент не является последним; (2) и элемент без остатка делится на 3;   >> тогда добавляем точку-разделения
                ? true
                : false;
    }
}
