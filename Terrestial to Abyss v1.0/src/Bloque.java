public class Bloque {
    float x, y;
    String Textura;
    public int rotacion;
    
    public Bloque(float x, float y, String Textura) {
        this.x = x;
        this.y = y;
        this.Textura = Textura;
        this.rotacion = 0;
    }

	public Object getX() {
		return this.x;
	}

	public Object getY() {
		return this.y;
	}
}
