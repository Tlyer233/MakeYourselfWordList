package pojo;

import java.awt.*;
import java.util.Objects;

public class RGB {
    private int Red;
    private int Green;
    private int Blue;

    public RGB() {
    }

    public RGB(int red, int green, int blue) {
        this.Red = red;
        this.Green = green;
        this.Blue = blue;
    }
    public RGB(Color color) {
        this.Red = color.getRed();
        this.Green = color.getGreen();
        this.Blue = color.getBlue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RGB rgb = (RGB) o;
        return Red == rgb.Red &&
                Green == rgb.Green &&
                Blue == rgb.Blue;
    }

    @Override
    public int hashCode() {
        return Objects.hash(Red, Green, Blue);
    }


    @Override
    public String toString() {
        return "RGB[" +
                "Red=" + Red +
                ", Green=" + Green +
                ", Blue=" + Blue +
                ']';
    }

    public int getRed() {
        return Red;
    }

    public void setRed(int red) {
        Red = red;
    }

    public int getGreen() {
        return Green;
    }

    public void setGreen(int green) {
        Green = green;
    }

    public int getBlue() {
        return Blue;
    }

    public void setBlue(int blue) {
        Blue = blue;
    }

    /**
     * 两个颜色取平均
     *
     * @param a: 颜色A
     * @param b: 颜色B
     * @return 返回取平均后的颜色RGB
     */
    public static RGB averageRGB(RGB a, RGB b) {
        int red = (a.getRed() + b.getRed()) / 2;
        int green = (a.getGreen() + b.getGreen()) / 2;
        int blue = (a.getBlue() + b.getBlue()) / 2;
        return new RGB(red, green, blue);
    }
}
