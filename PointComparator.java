import java.util.Comparator;

// Class needed for comparing weights in Edges. 
// An instantation of WeightComparator is put into PriorityQ's constructor.

public class PointComparator implements Comparator<Point>{
	
	@Override
	public int compare(Point x, Point y){
		
		int a = x.getColumn();
		int b = y.getColumn();
		
		if ( a < b ){
			return -1;
		} else if( a > b){
			return 1;
		}
		return 0;
	}
}
