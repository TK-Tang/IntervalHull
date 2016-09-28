public  class Interval{
	
	public Interval(){
		
	}
	
	public Interval (int h){
		this.s = null;
		this.e = null;
		this.height = h;
	}
	
	public Interval(Point s, Point e, int h){
		this.s = s;
		this.e = e;
		this.height = h;
	}
	
	Point s;
	Point e;
	int height;
	
	public Point getStartPoint(){
		return s;
	}
	
	public Point getEndPoint(){
		return e;
	}
	
	public int getHeight(){
		return height;
	}
	
	public void setHeight(int h){
		this.height = h;
	}
	
	public void setStartPoint(Point s){
		this.s = s;
	}
	
	public void setEndPoint(Point e){
		this.e = e;
	}
}
