package com.company;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Mojta on 2/28/2016.
 */
public class number {

    //110 - 260

    public static void main(String[] args) {

        for (int y=0;y<150;y++) {

            System.out.println(ThreadLocalRandom.current().nextInt(110, 260 + 1));
        }

    }

}
