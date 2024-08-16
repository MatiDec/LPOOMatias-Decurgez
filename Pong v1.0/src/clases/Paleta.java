package clases;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

public class Paleta {

    private double x, y;
    private double dy;
    private final int width = 20;
    private final int height = 100;

    public Paleta(int x, int y) {
        this.x = x;
        this.y = y;
        this.dy = 0;
    }

    public Rectangle2D getPaleta()
    {
        return new Rectangle2D.Double(x, y, width, height);
    }


    public void mover(Rectangle limites) {
        if (y + dy >= limites.getMinY() && y + dy + height <= limites.getMaxY())
        {
            y += dy;
        }
    }

    public void dibujar(Graphics2D g) {
        g.setColor(Color.WHITE);
        g.fill(new Rectangle2D.Double(x, y, width, height));
    }

    public void dibujar_en_y(double dibuja_y) {
        this.dy = dibuja_y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
