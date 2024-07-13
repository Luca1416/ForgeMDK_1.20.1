package net.superlucamon.luero.util;

import org.joml.Random;

public class UtilMethods {
    public static double getRandomInRange(double min, double max) {
        Random random = new Random();
        return min + (max - min) * random.nextFloat();
    }
}
