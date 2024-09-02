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

    // Textura compartida entre todos los enemigos
    private static BufferedImage texturaEnemigos;

    // Textura individual para cada enemigo, puede ser null
    private BufferedImage texturaIndividual;

    public Enemigos(int x, int y, double velocidad) {
        this.x = x;
        this.y = y;
        this.dx = velocidad;
    }

    // Asigna la textura compartida para todos los enemigos
    public static void aplicarTexturaEnemigos(BufferedImage textura) {
        texturaEnemigos = textura;
    }

    // Asigna una textura individual a este enemigo
    public void aplicarTexturaIndividual(BufferedImage textura) {
        this.texturaIndividual = textura;
    }

    public Rectangle2D getEnemigo() {
        return new Rectangle2D.Double(x, y, width, height);
    }

    public void mover(boolean direccionDerecha) {
        if (direccionDerecha) {
            x = x + dx;
        } else {
            x = x - dx;
        }
    }

    public void bajarFila() {
        y = y + height;
    }

    public void dibujar(Graphics2D g) {
        if (visible) {
            Rectangle2D rect = new Rectangle2D.Double(x, y, width, height);

            // Usar la textura individual si está asignada, de lo contrario usar la textura compartida
            BufferedImage textura = (texturaIndividual != null) ? texturaIndividual : texturaEnemigos;

            if (textura != null) {
                TexturePaint paint = new TexturePaint(textura, rect);
                g.setPaint(paint);
                g.fill(rect);
            }
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
