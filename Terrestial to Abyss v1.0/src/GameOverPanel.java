import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.plaf.basic.BasicButtonUI;

public class GameOverPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private Image backgroundImage;

    public GameOverPanel(Ventana ventana) {
        setLayout(null);
        setOpaque(true); 
        

        try {
            backgroundImage = ImageIO.read(getClass().getResource("/Texturas/slain.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

 

     
        JButton restartButton = new JButton(); 
        restartButton.setBounds(ventana.getAncho() / 2 - 160, 360, 320, 50);
        restartButton.setOpaque(true); 
        restartButton.setContentAreaFilled(false); 
        restartButton.setBorder(BorderFactory.createEmptyBorder()); 
        restartButton.setUI(new BasicButtonUI()); 
        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ventana.reiniciarGame(); 
            }
        });
 
        restartButton.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) { }
            @Override
            public void mousePressed(MouseEvent e) { }
            @Override
            public void mouseReleased(MouseEvent e) { }
            @Override
            public void mouseEntered(MouseEvent e) { }
            @Override
            public void mouseExited(MouseEvent e) { }
        });
        add(restartButton);


        JButton exitButton = new JButton();
        exitButton.setBounds(ventana.getAncho() / 2 - 100, 460, 200, 50);
        exitButton.setOpaque(false); 
        exitButton.setContentAreaFilled(false);
        exitButton.setBorder(BorderFactory.createEmptyBorder());
        exitButton.setUI(new BasicButtonUI()); 
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
 
        exitButton.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) { }
            @Override
            public void mousePressed(MouseEvent e) { }
            @Override
            public void mouseReleased(MouseEvent e) { }
            @Override
            public void mouseEntered(MouseEvent e) { }
            @Override
            public void mouseExited(MouseEvent e) { }
        });
        add(exitButton);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);


        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
