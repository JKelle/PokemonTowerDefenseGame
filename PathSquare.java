import java.io.File;
import javax.imageio.ImageIO;

public class PathSquare extends GridObject {
	
	public PathSquare(int x, int y, Grid g) {
		super(x,y,g);
		setImage();
		priority = 1;
	}
	
	public void setImage() {
		try{
			image = ImageIO.read( new File("Images\\Path1.png") );
		} catch( Exception e ) {
			System.out.println(e);
			System.exit(1);
		}
	}

	public String toString() {
		return "pathSquare at ("+gridx+", "+gridy+")";
	}
}
