import java.util.ArrayList;

public class Grid {
	public boolean debug = false;
	public int nx = 10;
	public int ny = 10;
	
	public ArrayList<GridObject> objectsList = new ArrayList<GridObject>();
	public PathSquare startPath;
	
	public Grid() {}
	public Grid(int x, int y) {
		if (x < 1 || y < 1) 
            throw new IllegalArgumentException("Violation of precondition: grid dimensions must be positive.");
		
		nx = x;
		ny = y;
	}
	
	public Grid(ArrayList<GridObject> otherList) {
		add(otherList);
	}
	
	public void removeFromGrid(GridObject o) {
		if( !(o instanceof Attack) )
			objectsList.remove(o);
		o = null;
	}

	public void add(ArrayList<GridObject> otherList) {
		for(GridObject o : otherList)
			add(o);
	}
	public void add(GridObject o) {
		if( !isInBounds(o) || !correctPlacement(o) )
			throw new IllegalStateException("Violation of State: Invalid addition of a GridObject.");
		if( o instanceof PathSquare && isFirstPathSquare(o) )
			startPath = (PathSquare)o;
		if( o instanceof Enemy ) {
			Enemy e = ((Enemy) o);
			e.gridx = startPath.gridx;
			e.gridy = startPath.gridy;
			e.curPath = startPath;
			e.nextPath = getNextPathSquare(e);
		}
		objectsList.add(o);
	}
	public boolean isInBounds(GridObject o) {
		if(o instanceof Attack)
			return !( o.x+o.imageWidth < 0 || o.x > Demo.getWidth() || o.y+o.imageHeight < 0 || o.y > Demo.getHeight() );
		return !( o.x < 0 || o.x > Demo.getWidth() || o.y < 0 || o.y > Demo.getHeight() );
	}
	private boolean correctPlacement(GridObject o) {
		if( o instanceof Enemy )
			return getPathSquareAt(o.getLoc()[0], o.getLoc()[1]) != null? true : false;
		if( o instanceof Attack )
			return true;
		return !isOccupied(o.getLoc()[0], o.getLoc()[1]);
	}
	private PathSquare getPathSquareAt(int x, int y) {
		for(GridObject o : objectsList)
			if( x == o.getLoc()[0] && y == o.getLoc()[1] && o instanceof PathSquare )
				return (PathSquare)o;
		return null;
	}
	private boolean isOccupied(int x, int y) {
		for(GridObject o : objectsList)
			if( x == o.getLoc()[0] && y == o.getLoc()[1] )
				return true;
		return false;
	}
	private boolean isFirstPathSquare(GridObject o) {
		for(GridObject a : objectsList)
			if( a instanceof PathSquare)
				return false;
		return true;
	}
	
	public PathSquare getNextPathSquare(Enemy e) {
		ArrayList<PathSquare> options = getOptions(e);
		
		if(debug){
			System.out.print("for enemy at "+ e +", options are: ");
			for(PathSquare p : options)
				System.out.println(p +", ");
		}
		
		if( options.size() == 0 )
			return null;
		
		return options.get( (int)(Math.random()*options.size()) );
	}
	private ArrayList<PathSquare> getOptions(Enemy e) {
		if( e.curPath.gridx != e.gridx || e.curPath.gridy != e.gridy )
			throw new IllegalStateException("Enemy isn't actually on his current PathSquare");
		
		ArrayList<PathSquare> options = new ArrayList<PathSquare>();
		
		options.add( getPathSquareAt(e.gridx, e.gridy-1) );
		options.add( getPathSquareAt(e.gridx, e.gridy+1) );
		options.add( getPathSquareAt(e.gridx-1, e.gridy) );
		options.add( getPathSquareAt(e.gridx+1, e.gridy) );

		for(int i = options.size()-1; i >= 0; i--)
			if( options.get(i) == null || options.get(i) == e.prevPath )
				options.remove(i);
		
		return options;
	}

	public Enemy getTarget(Attack a) {
		for(GridObject o : objectsList)
			if( o instanceof Enemy && a.isInRange(o) )
				return (Enemy)o;
		return null;
	}
}