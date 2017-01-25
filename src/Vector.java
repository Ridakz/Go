import java.io.Serializable;

/**
 * Defines a Vector : (x,y)
 * @author KadirF
 */
public class Vector implements Serializable {
	private static final long serialVersionUID = 1L;
	private int x;
	private int y;

	public Vector() {
		this.x=0;
		this.y=0;
	}
	public Vector(int x, int y) {
		this.x=x;
		this.y=y;
	}
	public Vector(Vector V) {
		this.x=V.x;
		this.y=V.y;
	}
	 public void setX (int x) {
		this.x=x;
	}
	public boolean equals(Vector V) {
		return(this.x==V.x && this.y==V.y);
	}
	public void setY (int y) {
		this.y=y;
	}
	public String toString () {
		return "Vector: " + this.x+" " +this.y;
	}
	public int getX() {
		return(this.x);	
	}
	public int getY() {
		return(this.y);
	}
}
	
	

	








