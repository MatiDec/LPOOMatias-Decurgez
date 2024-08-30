package clases;

import java.awt.Graphics2D;
import java.awt.TexturePaint;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class Boss {
    private double x , y;
    private BufferedImage texturaBoss;
    private Rectangle2D bossRect;
    private boolean visible;
    private int direccion; // Nueva variable para controlar la dirección

    public Boss(double x, double y, BufferedImage textura) {
        this.x = x;
        this.y = y;
        this.texturaBoss = textura;
        this.bossRect = new Rectangle2D.Double(x, y, 640, 256);
        this.visible = true;
        this.direccion = 1; // Inicialmente se mueve a la derecha
    }

    public void dibujar(Graphics2D g) {
        if (visible) {
            Rectangle2D rect = new Rectangle2D.Double(x, y, 640, 256);
            TexturePaint paint = new TexturePaint(texturaBoss, rect);
            g.setPaint(paint);
            g.fill(rect);
        }
    }

    public void mover() {
        // Movimiento del Boss
        x += 5 * direccion;

        // Verifica si se debe cambiar la dirección
        if (x + 640 > 1280) { // Considera el ancho del Boss para el borde derecho
            x = 1280 - 640; // Ajusta la posición para que no se salga del borde
            direccion = -1; // Cambia la dirección a la izquierda
        } else if (x < 0) {
            x = 0; // Ajusta la posición para que no se salga del borde
            direccion = 1; // Cambia la dirección a la derecha
        }
    }

    public Rectangle2D getBoss() {
        return new Rectangle2D.Double(x, y, 640, 256);
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean esVisible() {
        return visible;
    }
}
