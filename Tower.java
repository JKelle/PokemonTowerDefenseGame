import java.awt.Point;
import java.awt.*;

public abstract class Tower extends GridObject {
	
	public double strength;
	public long timeLastAttacked;
	public String name;
	public double attackDelay;
	public long startTime;
	
	private Point center;
	private Image rangeCircle;
	
	public Tower(int gridx, int gridy, Grid g) {
		super(gridx, gridy, g);
		priority = 2;
		center = new Point( (int)x, (int)y );
		basics();
	}
	
	public abstract void basics();
	
	public abstract void tryAttack();
	
	public boolean update(long curTime) {
		if(canAttack())
			tryAttack();
		return true;
	}
	
	public boolean canAttack() {
		return (System.currentTimeMillis()-timeLastAttacked)/1000.0 > attackDelay;
	}
	
	public String toString() {
		return name +": "+ super.toString();
	}
}
