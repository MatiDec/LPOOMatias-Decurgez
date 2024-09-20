import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.util.Map;

import javax.swing.ImageIcon;

public class Lava {
    float x, y;
    String textura;
    
    public Lava(float x, float y, String textura) {
        this.x = x;
        this.y = y;
        this.textura = textura;
    }
    
    public void dibujar(Graphics g, Camara camara, Map<String, ImageIcon> texturas) {
        ImageIcon icono = texturas.get(textura);
        if (icono != null) {
            g.drawImage(icono.getImage(), (int)((x * 32) - camara.getX()), (int)((y * 32) - camara.getY()), null);
        }
    }
    
    public Rectangle2D.Float getAreaColision() {
        return new Rectangle2D.Float(x * 32, y * 32, 20, 32); 
    }
}
