package com.metagards.metagames.Utils;

public class FloatToLongConverter {
    private void ConvertFloat(){
        Float PIE = 3.14f;
        long fromFloat = PIE.longValue();
        System.out.printf("float value %f, long value %d %n", PIE, fromFloat); // Simple cast to get rid of decimals

         float number = 444.33f;
         long aValue = (long) number;
         System.out.printf("float value %f, after casting into long %d %n", number, aValue); // Using Math.round() and cast back to long

        float points = 333.322f;
        long rounded = Math.round(points);
        System.out.printf("float : %f, long : %d %n", points, rounded);

    }
}
