package com.taco.suit_lady.util.tools;

import com.taco.suit_lady.util.values.numbers.Num2D;

public class Tester {
    
    public static void main(String[] args) {
        final Num2D center = new Num2D(0, 0);
        final Num2D testPoint = new Num2D(10, 10);
        
        final Num2D limitPoint = new Num2D(20, 20);
        final double coneSize = 10;
        
        
        final double minAngle = 0;
        final double maxAngle = 90;
        final double radius = 20;
        
//        System.out.println(Calc.isInCone(center, testPoint, radius, minAngle, maxAngle));
        System.out.println(Calc.isInCone(center, testPoint, limitPoint, coneSize));
    }
}
