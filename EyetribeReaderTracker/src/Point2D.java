
public class Point2D {
	private double x;
	private double y;

	public Point2D(double x, double y){
		this.x = x;
		this.y = y;
	}

	public void setX(double x){
		this.x = x;
	}

	public void setY(double y){
		this.y = y;
	}

	public double getX(){
		return this.x;
	}

	public double getY(){
		return this.y;
	}
	
	@Override
	public String toString(){
		return "(" + x + "," + y + ")";
	}
	
	public Point2D add(Point2D other){
		x += other.getX();
		y += other.getY();
		return this;
	}
	
	public Point2D div(double d){
		if (d == 0){
			return new Point2D(-1,-1);
		}
		
		x = x/d;
		y = y/d;
		
		return this;
	}
	
	@Override
	public boolean equals(Object o){
		if (o == null)
			return false;
		if (((Point2D) o).getX() == x && ((Point2D) o).getY() == y)
			return true;
		return false;
	}
}