import java.io.File;

import javax.imageio.ImageIO;

public class Ember extends Attack {
	
	public Ember( Tower t ) {
		super(t);
	}
	public void basics() {
		strength = 5;
		range = 200;
		accel = 200;
		topSpeed = 80;
		initialSpeed = 30;
		setImage();
	}
	
	public void setImage() {
		try{
			image = ImageIO.read( new File("Images\\flame.png") );
		} catch( Exception e ) {
			System.out.println(e);
			System.exit(1);
		}
	}

	public String toString() {
		return "Ember at ("+(int)x+", "+(int)y+")";
	}
}
