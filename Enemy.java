import java.awt.Rectangle;

public abstract class Enemy extends GridObject {
	
	public boolean isNew = true;
	public double maxHp;
	public double hp;
	public double speed;
	public PathSquare prevPath;
	public PathSquare curPath;
	public PathSquare nextPath;
	public double distanceToNextPath;
	
	public Rectangle rect;
	public HealthBar healthBar;
	
	public Enemy(Grid g) {
		super(g.startPath.gridx, g.startPath.gridy, g);
		priority = 3;
		rect = new Rectangle((int)x,(int)y,imageWidth,imageHeight);
		healthBar = new HealthBar();
	}
	
	public boolean update(long curTime) {
		if( hp <= 0)
			return false;
		
		if( distanceToNextPath < 1 ) {
			prevPath = curPath;
			curPath = nextPath;
			gridx = curPath.gridx;
			gridy = curPath.gridy;
			PathSquare p = world.getNextPathSquare(this);
			if( p == null )
				return false;
			nextPath = p;
		}
		
		move();
		distanceToNextPath = Math.sqrt( Math.pow(nextPath.x-x,2) + Math.pow(nextPath.y-y,2) );
		
		rect.setLocation( (int)x, (int)y );
		healthBar.update();
			
		return true;
	}
	
	private void move() {
		double[] dir = getUnitVector( getVectorToward(nextPath) );
		double dx = dir[0]*speed;
		double dy = dir[1]*speed;
		x += dx;
		y += dy;
	}
	
	public Rectangle[] getHealthBar() {
		return healthBar.getRects();
	}
	
	private class HealthBar {
		private double widthPercent = .3;
		private double heightPercent = .05;
		private int grayBoarder = 2;
		
		private Rectangle green = new Rectangle();
		private Rectangle red = new Rectangle();
		private Rectangle grey = new Rectangle();
		
		public HealthBar() {
			update();
		}
		
		public void update() {
			green.setBounds( (int)(x+imageWidth*widthPercent/2),
					(int)(y+imageHeight*heightPercent),
					(int)(imageWidth*(1-widthPercent)/maxHp*hp),
					2);

			red.setBounds(green.x+green.width,
					green.y,
					(int)(imageWidth*(1-widthPercent))-green.width,
					green.height);

			grey.setBounds(green.x-grayBoarder,
					green.y-grayBoarder,
					green.width+red.width+2*grayBoarder,
					green.height+2*grayBoarder);
		}
		
		public Rectangle[] getRects() {
			return new Rectangle[]{grey,green,red};
		}
	}
}
