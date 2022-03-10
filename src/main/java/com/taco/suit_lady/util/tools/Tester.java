package com.taco.suit_lady.util.tools;

import com.taco.suit_lady.util.values.numbers.Num2D;

public class Tester {
    
    public static void main(String[] args) {
        final Num2D center = new Num2D(0, 0);
        final Num2D[] testPoints = new Num2D[]{
                new Num2D(0, -10),
                new Num2D(0, 10),
                new Num2D(-10, 0),
                new Num2D(10, 0),
                new Num2D(10, 10),
                new Num2D(-10, -10)
        };
        
        final Num2D limitPoint = new Num2D(20, 20);
        final double coneSize = 10;
        
        
        final double minAngle = 350;
        final double maxAngle = 10;
        final double radius = 20;
        
        System.out.println("Center: " + center);
//        System.out.println("Limit Point: " + limitPoint);
//        System.out.println("Cone Size: " + coneSize);
        System.out.println("Angle Bounds: " + new Num2D(minAngle, maxAngle));
//        System.out.println("Radius: " + radius);
        
        System.out.println();
        
        for (int i = 0; i < testPoints.length; i++) {
            final Num2D testPoint = testPoints[i];
            final double angle = Calc.angle(center, testPoint);
            final boolean inCone = Calc.isInCone(center, testPoint, radius, minAngle, maxAngle);
            System.out.println("" + (i + 1) + ". " + testPoint + ":  " + "Angle: " + angle + "  " + "In Cone: " + inCone);
            System.out.println();
//            System.out.println("[" + i + "] — " + testPoint + ": " + Calc.isInCone(center, testPoint, limitPoint, coneSize));
        }
    }
}
