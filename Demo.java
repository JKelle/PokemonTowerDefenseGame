// borrowed from http://www.java-gaming.org

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Image;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferStrategy;
import java.io.File;

import java.util.PriorityQueue;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Demo implements Runnable
{
	private Grid world;
	private boolean debug = false;
	Image grass;

	private static final int WIDTH = 600;
	private static final int HEIGHT = 600;

	JFrame frame;
	Canvas canvas;
	BufferStrategy bufferStrategy;
	MouseControl mouse;

	private int timer = 0;

	public Demo()
	{
		frame = new JFrame("Game1");

		JPanel panel = (JPanel) frame.getContentPane();
		panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		panel.setLayout(null);

		canvas = new Canvas();
		canvas.setBounds(0, 0, WIDTH, HEIGHT);
		canvas.setIgnoreRepaint(true);

		panel.add(canvas);

		mouse = new MouseControl();
		canvas.addMouseListener(mouse);
		canvas.addMouseMotionListener(mouse);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setResizable(false);
		frame.setVisible(true);

		canvas.createBufferStrategy(2);
		bufferStrategy = canvas.getBufferStrategy();

		canvas.requestFocus();
	}

	private void init()
	{
		world = new Grid();
		
		try {
			grass = ImageIO.read(new File("Images\\grass.png"));			
		}catch(Exception e){
			System.out.println(e);
			System.exit(1);
		}
		
		world.add( new PathSquare(0,0,world) );
		world.add( new PathSquare(1,0,world) );
		world.add( new PathSquare(2,0,world) );
		world.add( new PathSquare(2,1,world) );
		
		world.add( new PathSquare(3,1,world) );
		world.add( new PathSquare(3,2,world) );
		world.add( new PathSquare(3,3,world) );
		world.add( new PathSquare(3,4,world) );
		world.add( new PathSquare(3,5,world) );
		world.add( new PathSquare(3,6,world) );
		world.add( new PathSquare(3,7,world) );
		world.add( new PathSquare(3,8,world) );
		world.add( new PathSquare(3,9,world) );
		

		world.add( new PathSquare(4,6,world) );
		world.add( new PathSquare(5,6,world) );
		world.add( new PathSquare(6,6,world) );
		world.add( new PathSquare(7,6,world) );
		world.add( new PathSquare(8,6,world) );
		world.add( new PathSquare(9,6,world) );

		world.add( new Charmander(1,2,world) );
		world.add( new Charmander(5,4,world) );
		world.add( new RocketGrunt(world) );
	}

	protected void update(int deltaTime)
	{		
		for(int i = world.objectsList.size()-1; i >= 0; i--) {
			GridObject o = world.objectsList.get(i);
			if( !o.update(System.currentTimeMillis()) )
				world.objectsList.remove(o);
		}

		timer += deltaTime;
		if( timer > 4000 ){
			world.add( new RocketGrunt(world) );
			timer = 0;
		}

	}

	protected void render(Graphics2D g)
	{
		drawGrass(g);
		
		drawGridObjects(g);
		if(debug)
			drawGridLines(g);
	}
	private void drawGrass(Graphics2D g) {
		for(int i = 0; i < WIDTH; i+= WIDTH/world.nx)
			for(int j = 0; j < HEIGHT; j += HEIGHT/world.ny)
				g.drawImage(grass, i, j, WIDTH/world.nx, HEIGHT/world.ny, canvas);
	}
	private void drawGridLines(Graphics2D g) {
		g.setColor(Color.gray);
		for(int i = 0; i < world.nx; i++)
			g.drawLine(WIDTH/world.nx*i, 0, WIDTH/world.nx*i, HEIGHT);
		for(int i = 0; i < world.ny; i++)
			g.drawLine(0, HEIGHT/world.ny*i, WIDTH, HEIGHT/world.ny*i);
	}
	private void drawGridObjects(Graphics2D g) {		
		PriorityQueue<GridObject> queue = new PriorityQueue<GridObject>();
		queue.addAll(world.objectsList);
			
		while(!queue.isEmpty()) {
			GridObject o = queue.poll();
			g.drawImage(o.image, (int)o.x, (int)o.y, o.imageWidth, o.imageHeight, canvas);
			if( o instanceof Enemy )
				drawHealthBars(g, (Enemy)o);
			
		}			
	}
	private void drawHealthBars(Graphics2D g, Enemy e) {
		Rectangle[] r = e.getHealthBar();
		g.setColor(Color.gray);
		g.fill( r[0] );
		g.setColor(Color.green);
		g.fill( r[1] );
		g.setColor(Color.red);
		g.fill(r[2]);
	}
	
	private class MouseControl extends MouseAdapter
	{
/*		
		public boolean left = false;
		public boolean right = false;
		public int xDelta = 0;
		public int lastX = 0;
		public int curX = 500;

		public void mouseClicked(MouseEvent e) {}
		public void mouseDragged(MouseEvent e)
		{
			lastX = curX;
			curX = e.getX();
			xDelta += curX - lastX;
		}

		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}

		public void mouseMoved(MouseEvent e)
		{
			lastX = curX;
			curX = e.getX();
			xDelta += curX - lastX;
		}

		public void mousePressed(MouseEvent e)
		{
			if (e.getButton() == MouseEvent.BUTTON1)
				left = true;
		}

		public void mouseReleased(MouseEvent e)
		{
			if (e.getButton() == MouseEvent.BUTTON1)
				left = false;
		}

		public void mouseWheelMoved(MouseEvent e) {}
*/
	}

	long desiredFPS = 60;
	long desiredDeltaLoop = (1000*1000*1000)/desiredFPS;
	boolean running = true;

	public void run()
	{
		long beginLoopTime;
		long endLoopTime;
		long currentUpdateTime = System.nanoTime();
		long lastUpdateTime;
		long deltaLoop;
		int deltaTime;

		init();

		while(running)
		{
			beginLoopTime = System.nanoTime();

			render();

			lastUpdateTime = currentUpdateTime;
			currentUpdateTime = System.nanoTime();
			deltaTime = (int) ((currentUpdateTime - lastUpdateTime)/(1000*1000));
			update(deltaTime);

			endLoopTime = System.nanoTime();
			deltaLoop = endLoopTime - beginLoopTime;

			if(deltaLoop <= desiredDeltaLoop)
			{
				try
				{
					Thread.sleep((desiredDeltaLoop - deltaLoop)/(1000*1000));
				} catch(InterruptedException e) { /* Do nothing */ }
			}
		}
	}

	private void render() {
		Graphics2D g = (Graphics2D)bufferStrategy.getDrawGraphics();
		g.clearRect(0, 0, WIDTH, HEIGHT);
		render(g);
		g.dispose();
		bufferStrategy.show();
	}

	public static int getWidth() {
		return WIDTH;
	}
	public static int getHeight() {
		return HEIGHT;
	}
	
	public static void main(String [] args)
	{
		Demo ex = new Demo();
		new Thread(ex).start();
	}
}