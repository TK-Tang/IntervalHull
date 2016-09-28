public class StartPoint implements Point{
	private Interval i;
	private int column;
	
	public StartPoint(Interval i, int column){
		this.i = i;
		this.column = column;
	}
	
	public int getColumn(){
		return column;
	}
	
	public Interval getInterval(){
		return i;
	}
}
