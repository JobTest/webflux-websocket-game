package com.webfluxwebsocket.examples;

import java.util.Arrays;

/**
 * http://java-master.com/алгоритмы-сортировки-массива/
 * Bubble-sort-example-300px.gif
 *
 * https://habr.com/post/274493/ >> https://github.com/sfkulyk/SortingVizualization/tree/Habra
 */

public class AlgorithmTest4 {

    public static void main(String[] args) {
        int arr[] = {62, 84, 32, 5, 0, 14, 52, 82, 58, 71};

        int switchCount = 0;
        int compareCount = 0;
        long beginTime = System.nanoTime();

        // ///////////////////////// еще один алгоритм пузырьковой сортировки
//        for (int j = 1; j < arr.length; j++) {
//            for (int index = 0; index < arr.length - j; index++) {
//                if (arr[index] > arr[index + 1]) {
//                    int min = arr[index + 1];
//                    int max = arr[index];
//                    arr[index] = min;
//                    arr[index + 1] = max;
//                    compareCount++;
//                }
//                switchCount++;
//            }
//        }

       // 0 5 14 32 52 58 62 71 82 84  // = 100 (22)
       // 0 5 14 32 52 58 62 71 82 84  // = 90 (20)
        /*
            [0, 5, 14, 32, 52, 58, 62, 71, 82, 84]
            Compares:  20,
            Switches:  45,
            Time:   6 528
         */


        // ///////////////////////// алгоритм сортировки вставками
        for (int minIndex = 1; minIndex < arr.length; minIndex++) {
            int min = arr[minIndex];
            int index = minIndex - 1;
            while (0 <= index && arr[index] > min) {
                int max = arr[index];
                arr[index + 1] = max;
                index = index - 1;
                compareCount++;
                switchCount++;
            }
            switchCount++;
            arr[index + 1] = min;
        }

        /*
            [0, 5, 14, 32, 52, 58, 62, 71, 82, 84]
            Compares:  20,
            Switches:  29,
            Time:  4 196
         */

        long endTime = System.nanoTime();
        System.out.println(String.format("%-40s \nCompares: %, 2d, \nSwitches: %, 2d, \nTime: %, 5d", Arrays.toString(arr), compareCount, switchCount, (endTime-beginTime)));

    }

}
