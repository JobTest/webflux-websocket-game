package com.webfluxwebsocket.examples;

import java.util.Arrays;

/*
   Есть массив элементов.
   Нужно реализовать некий интерфейс для обхода этого массива чтобы получить все элементы массива.

   Методы 'next' и 'prev' - это не атомарная операция - они делают две операции:
   1. сдвигают индекс массива на одну позицию в право/лево
   2. возвращают элемент найденный по этому индексу
   Но проблема в том, что тяжело получить первый и последний элемент массива,
        потому-что отсчет для найденного элемента всегда начинается НЕ с текущего, а со следующего/предыдущего элемента
        (то есть, пропускается текущий элемент...)

   Чтобы исправить проблему можно:
   - сделать чтобы первый и последний элемент массива возвращали какой-то NULL (начало/конец массива)
   - а все остальное тело массива (полезные элементы) находились внутри между началом и концом массива

   Метод 'has***' говорит о наличии или отсутствии очередного (полезного) элемента в масиве
   А методы 'next' и 'prev' чтобы могли адекватно работать в случаях когда перемещаеся на крайние позиции массива:
   - то есть, эти методы выводят индекс из тела полезной области и прибивают его к всегда фиксированному крайнему положению!
 */
public class AlgorithmTest3 {

    public static void main(String[] args) {

        String arg = "Hello";

        char[] charArray = arg.toCharArray();

//        ArrayIterator iterator = new ArrayIterator(charArray);
        AlgorithmTest3 test3 = new AlgorithmTest3();
        ArrayIterator iterator = test3.new ArrayIterator(charArray);

        while (iterator.hasNext())
            System.out.print( iterator.next() );
        iterator.next();

        while (iterator.hasPrev())
            System.out.print( iterator.prev() );
        iterator.prev();
    }


    class ArrayIterator<T> {
        private final char[] charArray;
        private final int beginningIndex;
        private final int endingIndex;
        private final int minCharArray;
        private final int maxCharArray;

        private int index;

        final int lengthCharArray;
        final static char EMPTY = '\n';

        public ArrayIterator(char[] charArray) {
            lengthCharArray = charArray.length;
            this.charArray = Arrays.copyOf(charArray, lengthCharArray); // this.charArray = charArray;
            beginningIndex = -1;
            endingIndex = lengthCharArray;

            minCharArray = 0;
            maxCharArray = endingIndex - 1;
            index = beginningIndex;
        }

        public boolean hasNext() {
            int nextIndex = index + 1;
            return nextIndex <= maxCharArray
                    ? true
                    : false;
        }

        public boolean hasPrev() {
            int prevIndex = index - 1;
            return minCharArray <= prevIndex
                    ? true
                    : false;
        }

        public char next() {
            char next = EMPTY;

            if (index < maxCharArray) {
                index++;
                next = charArray[index];
            } else if (index < endingIndex) {
                index++;
            }

            return next;
        }

        public char prev() {
            char prev = EMPTY;

            if (minCharArray < index) {
                index--;
                prev = charArray[index];
            } else if (index < beginningIndex) {
                index--;
            }

            return prev;
        }
    }
}
