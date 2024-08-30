package clases;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

public class Jugador {

    private double x, y;
    private double dx;
    private final int width = 32;
    private final int height = 32;
    public int disparadas = 0;
    public double cooldown = 0.2; 
    private double cooldownActual = 0; 
    public int[] xDisparos = new int[20];
    public int[] yDisparos = new int[20];
    public Rectangle2D[] disparos = new Rectangle2D[20];

    public Jugador(int x, int y) {
        this.x = x;
        this.y = y;
        this.dx = 0;
    }

    public Rectangle2D getJugador() {
        return new Rectangle2D.Double(x, y, width, height);
    }

    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, width, height);
    }
    
    public void mover(Rectangle limites) {
        if (x + dx >= limites.getMinX() && x + dx + height <= limites.getMaxX()) {
            x = x + dx;
        }
    }

    public void dibujar(Graphics2D g) {
        g.setColor(Color.WHITE);
        g.fill(new Rectangle2D.Double(x, y, width, height));
    }

    public void dibujar_en_x(double dibuja_x) {
        this.dx = dibuja_x;
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

    public void InicializarDisparos() {
        for (int i = 0; i < 20; i++) {
            yDisparos[i] = 600;
        }
    }

    public boolean puedeDisparar() {
        if (cooldownActual <= 0) {
            cooldownActual = cooldown; 
            return true;
        }
        return false;
    }

    public void actualizarCooldown(double tiempo) {
        if (cooldownActual > 0) {
            cooldownActual = cooldownActual-tiempo;
        }
    }
}
