import java.io.File;
import javax.imageio.ImageIO;

public class RocketGrunt extends Enemy {

	public RocketGrunt(Grid g) {
		super(g);
		maxHp = 100;
		hp = maxHp;
		speed = .7;
		setImage();
	}
	
	public void setImage() {
		try{
			super.image = ImageIO.read( new File("Images\\rocketGrunt.png") );
		} catch( Exception e ) {
			System.out.println(e);
			System.exit(1);
		}
	}

	public String toString() {
		return "RocketGrunt at ("+gridx+", "+gridy+")";
	}
}
