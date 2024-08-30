package clases;

import java.awt.Graphics2D;
import java.awt.TexturePaint;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class Enemigos {
    private double x, y;
    private double dx;
    private final int width = 32;
    private final int height = 32;
    private boolean visible = true;

   // gracias chatty por explicarme como usar static
    private static BufferedImage texturaEnemigos;
    private static BufferedImage texturaEnemigosIndex;


    public Enemigos(int x, int y, double velocidad) {
        this.x = x;
        this.y = y;
        this.dx = velocidad;
    }

    // para que la textura no se multiplique y se quede fija en el rectangulo de la hitbox
    public static void aplicarTexturaEnemigos(BufferedImage textura) {
        texturaEnemigos = textura;
    }
    
    public static void aplicarTexturaEnemigosIndex(BufferedImage textura) {
        texturaEnemigosIndex = textura;
    }

    public Rectangle2D getEnemigo() {
        return new Rectangle2D.Double(x, y, width, height);
    }

    public void mover(boolean direccionderecha) {
        if (direccionderecha) {
            x = x + dx;
        } 
        else {
            x = x - dx;
        }
    }

    public void bajarFila() {
        y = y + height;
    }

    public void dibujar(Graphics2D g) {
        if (visible && texturaEnemigos != null) {
            Rectangle2D rect = new Rectangle2D.Double(x, y, width, height);
            TexturePaint paint = new TexturePaint(texturaEnemigos, rect);
            g.setPaint(paint);
            g.fill(rect);
        }
    }

    public void dibujarIndex(Graphics2D g) {
    	if(visible && texturaEnemigosIndex != null) {
    		Rectangle2D rect = new Rectangle2D.Double(x, y, width, height);
            TexturePaint paint = new TexturePaint(texturaEnemigosIndex, rect);
            g.setPaint(paint);
            g.fill(rect);
    	}
    }
    
    public void Visibilizar(boolean visible) {
        this.visible = visible;
    }

    public boolean esVisible() {
        return visible;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }
}
