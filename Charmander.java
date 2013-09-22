import java.io.File;
import javax.imageio.ImageIO;

public class Charmander extends Tower{
	
	public Charmander(int gridx, int gridy, Grid g) {
		super(gridx, gridy, g);
	}	
	
	public void basics() {
		startTime = System.currentTimeMillis();
		strength = 0;
		name = "Charmander";
		attackDelay = .5;
		setImage();
	}
	
	public void tryAttack() {
		if( !canAttack() )
			throw new IllegalStateException("Can't attack yet!");
		Attack a = new Ember(this);
		if( a.hasTarget() ) {
			world.add( a );
			timeLastAttacked = System.currentTimeMillis();
		}
	}
	
	public void setImage() {
		try{
			super.image = ImageIO.read( new File("Images\\charmander2.png") );
		} catch( Exception e ) {
			System.out.println(e);
			System.exit(1);
		}
	}
	
	public String toString() {
		return "Charmander at ("+gridx+", "+gridy+")";
	}
}