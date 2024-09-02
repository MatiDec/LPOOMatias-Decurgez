package clases;

import javax.swing.JFrame;

public class Ventana extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final int ancho = 1280, alto = 720;
	private TableroGalaga tablero;
	public Ventana() {
		setTitle("Galagaga");
		setSize(ancho, alto);
		setLocationRelativeTo(null);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		tablero = new TableroGalaga(this);
		add(tablero);
	}
}
