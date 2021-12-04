/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.taco.suit_lady._to_sort._new.mandelbrot;

import com.taco.suit_lady.view.ui.ui_internal.contents_sl.mandelbrot.SLMandelbrotContentData;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * @author Spencer
 */
public class Mandelbrot
{
    
    private double startX = -2;
    private double startY = -1;
    private double endX = 1;
    private double endY = 1;
    private double tempStartX;
    private double tempStartY;
    private double tempEndX;
    private double tempEndY;
    private final int SIZE_X = 300 * 2;
    private final int SIZE_Y = 200 * 2;
    private final int MAX_ITERATION = 1000;
    private final int PRECISION = 1;
    private BufferedImage image;
    private JFrame frame;
    private JLabel label;
    private Color[] colors;
    private boolean selectingFirst = true;
    private JLabel selectionBox;
    private MouseMotionAdapter mouseMovedAdapter;
    private ArrayList<MandelColor>[][] aliasing;
    
    public Mandelbrot()
    {
        aliasing = new ArrayList[SIZE_X][SIZE_Y];
        setupAliasArray();
        frame = new JFrame();
        image = new BufferedImage(
                SIZE_X, SIZE_Y, BufferedImage.TYPE_INT_ARGB);
        label = new JLabel(new ImageIcon(image));
        frame.add(label);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setResizable(false);
        label.setFocusable(true);
        label.requestFocusInWindow();
        mouseMovedAdapter = new MouseMotionAdapter()
        {
            @Override
            public void mouseMoved(MouseEvent evt)
            {
                mouseMovedListener(evt);
            }
        };
        MouseAdapter mouseClickedAdapter = new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent evt)
            {
                mouseClickedListener(evt);
            }
        };
        label.addMouseListener(mouseClickedAdapter);
    }
    
    private void setupAliasArray()
    {
        for (int i = 0; i < SIZE_X; i++) {
            for (int j = 0; j < SIZE_Y; j++) {
                aliasing[i][j] = new ArrayList<>();
            }
        }
    }
    
    private void mouseClickedListener(MouseEvent evt)
    {
        if (selectingFirst) {
            System.out.println("Selected First");
            tempStartX = getX(evt.getX());
            tempStartY = getY(evt.getY());
            selectingFirst = false;
            selectionBox = new JLabel();
            selectionBox.setBorder(new LineBorder(Color.BLACK, 2));
            label.add(selectionBox);
            selectionBox.setLocation(evt.getX(), evt.getY());
            label.addMouseMotionListener(mouseMovedAdapter);
            label.add(selectionBox);
        } else {
            System.out.println("Selected Second");
            label.removeMouseMotionListener(mouseMovedAdapter);
            label.remove(selectionBox);
            tempEndX = getX(evt.getX());
            double temp = tempEndX - tempStartX;
            temp *= (double) SIZE_Y / SIZE_X;
            tempEndY = temp + tempStartY;
            selectingFirst = true;
            startX = tempStartX;
            startY = tempStartY;
            endX = tempEndX;
            endY = tempEndY;
            drawSet();
        }
    }
    
    private void mouseMovedListener(MouseEvent evt)
    {
        int xSize = Math.abs(selectionBox.getX() - evt.getX());
        selectionBox.setSize(xSize, (int) (xSize * ((double) SIZE_Y / SIZE_X)));
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
//        Mandelbrot brot = new Mandelbrot();
//        brot.drawSet();
    
        final SLMandelbrotContentData mandel = new SLMandelbrotContentData();
        mandel.regenerate();
        
        final Mandelbrot brot = new Mandelbrot();
        for (int i = 0; i < brot.SIZE_X; i++) {
            for (int j = 0; j < brot.SIZE_Y; j++) {
                brot.image.setRGB(i, j, mandel.getColors()[i][j].getAwtColor().getRGB());
            }
        }
    }
    
    public void drawSet()
    {
        setupAliasArray();
        frame.remove(label);
        colors = initColors();
        for (int i = 0; i < SIZE_X * PRECISION; i++) {
            for (int j = 0; j < SIZE_Y * PRECISION; j++) {
                double i2 = i;
                double j2 = j;
                i2 /= PRECISION;
                j2 /= PRECISION;
                double xScaled = getX(i2);
                double yScaled = getY(j2);
                double x = 0;
                double y = 0;
                int n = 0;
                int N = (int) Math.pow(10, 100);
                if (escapes(i2, j2)) {
                    while ((x * x) + (y * y) < N) {
                        double xTemp = (x * x) - (y * y) + xScaled;
                        y = 2 * x * y + yScaled;
                        x = xTemp;
                        n++;
                    }
                    aliasing[i / PRECISION][j / PRECISION]
                            .add(new MandelColor(n, N, x, y));
                } else {
                    aliasing[i / PRECISION][j / PRECISION]
                            .add(new MandelColor(Color.BLACK));
                }
            }
        }
//        paintColors();
        paintColorsReg();
        frame.add(label);
        label.repaint();
    }
    
    private boolean escapes(double i, double j)
    {
        double xScaled = getX(i);
        double yScaled = getY(j);
        double x = 0;
        double y = 0;
        int iteration = 0;
        while ((x * x) + (y * y) < (2 * 2)
               && iteration < MAX_ITERATION) {
            double xTemp = (x * x) - (y * y) + xScaled;
            y = 2 * x * y + yScaled;
            x = xTemp;
            iteration++;
            if ((x * x) + (y * y) > (2 * 2)) {
                return true;
            }
        }
        return false;
    }
    
    private double getX(double x)
    {
        double xDiff = endX - startX;
        double xPerc = x / SIZE_X;
        return (xDiff * xPerc) + startX;
    }
    
    private double getY(double y)
    {
        double yDiff = endY - startY;
        double yPerc = y / SIZE_Y;
        return (yDiff * yPerc) + startY;
    }
    
    private Color getColor(int n, int N, double x, double y)
    {
        if (N == -1) {
            return Color.BLACK;
        }
        double z = Math.sqrt((x * x) + (y * y));
        double part1 = Math.log10(z);
        double part2 = Math.log10(N);
        double partWhole = Math.log(part1 / part2) / Math.log(2);
        double whole = n - partWhole;
        int total = colors.length;
        whole *= 20;
        int intWhole = (int) whole;
        int value = intWhole % total;
        return colors[value];
    }
    
    private int getColorValue(MandelColor mC)
    {
        if (mC.getN() == -1) {
            return 0;
        }
        int n = mC.getn();
        int N = mC.getN();
        double x = mC.getX();
        double y = mC.getY();
        double z = Math.sqrt((x * x) + (y * y));
        double part1 = Math.log10(z);
        double part2 = Math.log10(N);
        double partWhole = Math.log(part1 / part2) / Math.log(2);
        double whole = n - partWhole;
        int total = 255 * 6;
        whole *= 20;
        int intWhole = (int) whole;
        return intWhole % total;
    }
    
    private void paintColorsReg()
    {
        for (int i = 0; i < SIZE_X; i++) {
            for (int j = 0; j < SIZE_Y; j++) {
                int n = aliasing[i][j].get(0).getn();
                int N = aliasing[i][j].get(0).getN();
                double x = aliasing[i][j].get(0).getX();
                double y = aliasing[i][j].get(0).getY();
                image.setRGB(i, j, getColor(n, N, x, y).getRGB());
            }
        }
    }
    
    private void externalPrecision()
    {
        for (int i = 0; i < SIZE_X; i += 2) {
            for (int j = 0; j < SIZE_Y; j += 2) {
                
            }
        }
    }
    
    private void paintColors()
    {
        ArrayList<MandelColor>[] groups = new ArrayList[(PRECISION * PRECISION)];
        //i and j represent x and y point on screen
        for (int i = 0; i < SIZE_X; i++) {
            for (int j = 0; j < SIZE_Y; j++) {
                ArrayList<MandelColor> tempColors = new ArrayList<>();
                for (int q = 0; q < groups.length; q++) {
                    groups[q] = new ArrayList<>();
                }
                for (int q = 0; q < aliasing[i][j].size(); q++) {
                    if (tempColors.isEmpty()) {
                        tempColors.add(aliasing[i][j].get(q));
                    }
                    if (getColorValue(tempColors.get(0))
                        > getColorValue(aliasing[i][j].get(q))) {
                        tempColors.add(0, aliasing[i][j].get(q));
                    }
                }
                aliasing[i][j] = tempColors;
                //q represents a color stored in a point on the screen
                for (int q = 0; q < aliasing[i][j].size(); q++) {
                    double total = 255 * 6;
                    double fractal = getColorValue(aliasing[i][j].get(q))
                                     / total;
                    int groupNum = groups.length;
                    double actual = groupNum * fractal;
                    int actualInt = (int) actual;
                    groups[actualInt].add(aliasing[i][j].get(q));
                }
                int biggestGroup = 0;
                for (int q = 0; q < groups.length; q++) {
                    if (groups[q].size() > groups[biggestGroup].size()) {
                        biggestGroup = q;
                    }
                }
                MandelColor color = groups[biggestGroup].get(0);
                
                int n = color.getn();
                int N = color.getN();
                double x = color.getX();
                double y = color.getY();
                Color c = getColor(n, N, x, y);
                image.setRGB(i, j, c.getRGB());
            }
        }
    }
    
    private Color[] initColors()
    {
        Color[] colorsTemp = new Color[255 * 2];
        for (int i = 0; i < 255; i++) {
            colorsTemp[i] = new Color(0, 0, i);
            colorsTemp[i + 255] = new Color(0, 0, 255 - i);
        }
        //        int increment = 0;
        //        int red = 0;
        //        int green = 0;
        //        int blue = 0;
        //        for (int a = 1; a <= 255; a++) {
        //            colorsTemp[increment] = new Color(green, red, blue);
        //            red++;
        //            increment++;
        //        }
        //        for (int b = 1; b <= 255; b++) {
        //            colorsTemp[increment] = new Color(green, red, blue);
        //            green++;
        //            increment++;
        //        }
        //        for (int c = 1; c <= 255; c++) {
        //            colorsTemp[increment] = new Color(green, red, blue);
        //            blue++;
        //            increment++;
        //        }
        //        for (int a = 1; a <= 255; a++) {
        //            colorsTemp[increment] = new Color(green, red, blue);
        //            red--;
        //            increment++;
        //        }
        //        for (int b = 1; b <= 255; b++) {
        //            colorsTemp[increment] = new Color(green, red, blue);
        //            green--;
        //            increment++;
        //        }
        //        for (int c = 1; c <= 255; c++) {
        //            colorsTemp[increment] = new Color(green, red, blue);
        //            blue--;
        //            increment++;
        //        }
        return colorsTemp;
    }
    
    private void antiAliasing()
    {
        Color[][] colors = new Color[SIZE_X][SIZE_Y];
        for (int i = 1; i < SIZE_X - 1; i++) {
            for (int j = 1; j < SIZE_Y - 1; j++) {
                int red;
                int green;
                int blue;
                red = (new Color(image.getRGB(i - 1, j)).getRed()
                       + new Color(image.getRGB(i + 1, j)).getRed()
                       + new Color(image.getRGB(i, j + 1)).getRed()
                       + new Color(image.getRGB(i, j - 1)).getRed()
                       + new Color(image.getRGB(i, j)).getRed()) / 5;
                green = (new Color(image.getRGB(i - 1, j)).getGreen()
                         + new Color(image.getRGB(i + 1, j)).getGreen()
                         + new Color(image.getRGB(i, j + 1)).getGreen()
                         + new Color(image.getRGB(i, j - 1)).getGreen()
                         + new Color(image.getRGB(i, j)).getGreen()) / 5;
                blue = (new Color(image.getRGB(i - 1, j)).getBlue()
                        + new Color(image.getRGB(i + 1, j)).getBlue()
                        + new Color(image.getRGB(i, j + 1)).getBlue()
                        + new Color(image.getRGB(i, j - 1)).getBlue()
                        + new Color(image.getRGB(i, j)).getBlue()) / 5;
                colors[i][j] = new Color(red, green, blue);
            }
        }
        for (int i = 1; i < SIZE_X - 1; i++) {
            for (int j = 1; j < SIZE_Y - 1; j++) {
                image.setRGB(i, j, colors[i][j].getRGB());
            }
        }
    }
}
