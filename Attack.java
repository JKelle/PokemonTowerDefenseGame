import java.awt.Point;

public abstract class Attack extends GridObject{
	
	public int strength;
	public int range;
	public double accel;
	public double topSpeed;
	public double initialSpeed;
	
	private Enemy target;
	private Point center;
	private long lastTime;
	private double vx;
	private double vy;
	private double ax;
	private double ay;
	
	public Attack( Tower t ) {
		super(t.gridx, t.gridy, t.world);
		lastTime = System.currentTimeMillis();
		priority = 4;
		
		imageWidth /= 2;
		imageHeight /= 2;
		center = new Point((int)(x+imageWidth/2), (int)(y + imageHeight/2));
		
		
		basics();
		target = world.getTarget(this);
		if(hasTarget()) {
			double[] curDir = getUnitVector( getVectorToward(target) );
			vx = curDir[0]*initialSpeed;
			vy = curDir[1]*initialSpeed;
		}
	}
	
	public abstract void basics();

	public boolean update(long curTime) {
		target = world.getTarget(this);
		move(curTime);
		
		return !hit() && world.isInBounds(this);
	}
	private void move(long curTime) {
		double dt = (curTime - lastTime)/1000.0;
		lastTime = curTime;

		if( hasTarget() ) {
			double[] curDir = getUnitVector( getVectorToward(target) );
			ax = curDir[0]*accel;
			ay = curDir[1]*accel;
			
			double dvx = ax*dt;
			double dvy = ay*dt;
			vx += dvx;
			vy += dvy;
		}
		
		double speed = Math.sqrt(vx*vx+vy*vy);
		if( speed > topSpeed ) {
			vx = vx/speed*topSpeed;
			vy = vy/speed*topSpeed;
		}
				
		double dx = vx*dt;
		double dy = vy*dt;

		x += dx;
		y += dy;
		
		center.setLocation(x,y);
		
		gridx = (int)(x/(Demo.getWidth()/world.nx));
		gridy = (int)(y/(Demo.getHeight()/world.ny));
	}
	private boolean hit() {
		for(int i = world.objectsList.size()-1; i >= 0; i--)
			if( world.objectsList.get(i) instanceof Enemy ) {
				Enemy e = (Enemy)(world.objectsList.get(i));
				if( e.rect.contains(center) ) {
					e.hp -= strength;
					return true;
				}
			}
		return false;
	}
	
	public boolean isInRange(GridObject o) {
		double[] vector = getVectorToward(o);
		double mag = Math.sqrt(vector[0]*vector[0] + vector[1]*vector[1]);
		return Math.abs(mag) <= range? true : false; 
	}
	
	public boolean hasTarget() {
		return target != null;
	}
	
	public String toString() {
		return super.toString() +" Target = "+ target;
	}
}
