import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.util.Map;

import javax.swing.ImageIcon;

public class Spike extends Bloque {

    public Spike(float x, float y, String textura) {
        super(x, y, textura);
    }


    public Rectangle2D.Float getAreaColision() {
        float spikeX = (x * 32) + (32 - 20) / 2;  
        float spikeY = (y * 32) + (32 - 20) / 2; 
        return new Rectangle2D.Float(spikeX, spikeY, 20, 20);
    }


    public void dibujar(Graphics g, Camara camara, Map<String, ImageIcon> texturas) {
        ImageIcon icono = texturas.get(this.Textura);
        if (icono != null) {
            g.drawImage(icono.getImage(), (int)((this.x * 32) - camara.getX()), (int)((this.y * 32) - camara.getY()), null);
        }
    }
}
