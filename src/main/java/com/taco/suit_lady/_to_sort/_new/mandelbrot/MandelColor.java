/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.taco.suit_lady._to_sort._new.mandelbrot;

import java.awt.*;

/**
 *
 * @author 13coxs
 */
public class MandelColor {

    private int N;
    private int n;
    private double x;
    private double y;

    public MandelColor(int n, int N, double x, double y) {
        this.N = N;
        this.n = n;
        this.x = x;
        this.y = y;
    }

    public MandelColor(Color c) {
        if (c.equals(Color.BLACK)) {
            this.N = -1;
            this.n = 0;
            this.x = 0;
            this.y = 0;
        }
    }

    public int getN() {
        return this.N;
    }

    public int getn() {
        return this.n;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    @Override
    public String toString() {
        return "\nn: " + getn()
                + "\nN: " + getN()
                + "\nX: " + getX()
                + "\nY: " + getY();
    }
}
