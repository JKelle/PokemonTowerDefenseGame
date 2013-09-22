import java.awt.Image;

public abstract class GridObject implements Comparable<GridObject>{
	Grid world;
	
	public Image image;
	public int imageWidth;
	public int imageHeight;
	public int gridx;
	public int gridy;
	public int priority;
	
	public double x;
	public double y;
	
	public GridObject(){}
	
	public GridObject(int a, int b, Grid g) {
		world = g;
		gridx = a;
		gridy = b;
		x = gridx*Demo.getWidth()/world.nx;
		y = gridy*Demo.getHeight()/world.ny;
		imageWidth = Demo.getWidth()/world.nx;
		imageHeight = Demo.getHeight()/world.ny;
	}
	
	public int compareTo(GridObject o) {
		return priority - o.priority;
	}
	
	public abstract void setImage();
	
	public int[] getLoc() {
		return new int[]{gridx,gridy};
	}
	
	public double[] getVectorToward(GridObject o) {
		return new double[]{o.x-x, o.y-y};
	}
	public double[] getUnitVector(double[] vector) {
		if( vector.length != 2  )
			throw new IllegalStateException("Must be a 2D vector!");
		if( vector[0] == 0 && vector[1] == 0 )
			return new double[]{0,0};
		double[] vectorHat = new double[2];
		double mag = Math.sqrt(vector[0]*vector[0] + vector[1]*vector[1]);
		vectorHat[0] = vector[0]/mag;
		vectorHat[1] = vector[1]/mag;
		return vectorHat;
	}
	
	public boolean update(long curTime) {
		return true;
	}
	
	public boolean equals(Object o) {
		return this == o;
	}
	public String toString() {
		return "("+gridx+", "+gridy+")";
	}
}