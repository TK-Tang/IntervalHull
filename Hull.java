import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.Map;
import java.util.HashMap;

public class Hull {
	
	Comparator<Point> comparator = new PointComparator();
	public PriorityQueue<Point> eventPoints = new PriorityQueue<Point>(30, comparator);
	public HashMap<String, Interval> intervalMap = new HashMap<String, Interval>();

    // Called when a new line of numbers is read from stdin.
    public void update(int key, String s) {
        String[] split = s.split("\\s+");
		
		Interval i = new Interval(Integer.parseInt(split[2]));
		this.addInterval(key, i);
		
		StartPoint start = new StartPoint( i, Integer.parseInt(split[0]) );
		EndPoint end = new EndPoint( i, Integer.parseInt(split[1]) );
		
		addPoint(end);
		addPoint(start);
		
		i.setStartPoint(start);
		i.setEndPoint(end);
    }
	
	public PriorityQueue<Point> getEventPoints(){
		return eventPoints;
	}
	
	public Interval getInterval(int key){
		return this.intervalMap.get(String.valueOf(key));
	}
	
	public HashMap<String, Interval> getIntervalMap(){
		return intervalMap;
	}
	
	public void addInterval(int key, Interval i){
		intervalMap.put(String.valueOf(key), i);
	}
	
	public void addInterval(int key, Point s, Point e, int h){
		intervalMap.put(String.valueOf(key), new Interval(s, e, h));
	}
						
	public void addPoint(Point s){
		eventPoints.add(s);
	}
	
	public void mergeIntervals(Interval first, Interval second){
		Interval result = new Interval();
		
		if (first.getHeight() > second.getHeight()){
			result.setHeight(first.getHeight());
		} else {
			result.setHeight(second.getHeight());
		}
		
		if(first.getStartPoint().getColumn() < second.getStartPoint().getColumn()){
			result.setStartPoint(first.getStartPoint());
		} else {
			result.setStartPoint(second.getStartPoint());
		}
		
		if(first.getEndPoint().getColumn() > second.getEndPoint().getColumn()){
			result.setEndPoint(first.getEndPoint());
		} else {
			result.setEndPoint(second.getEndPoint());
		}
		this.intervalMap.remove(String.valueOf(this.intervalMap.size() -1));
		this.intervalMap.remove(String.valueOf(this.intervalMap.size() -1));
		this.addInterval(this.intervalMap.size() , result);

	}
	
	public void printInfo(){
		for (Map.Entry<String, Interval> entry : this.intervalMap.entrySet()) {
				String key = entry.getKey();
				Interval value = entry.getValue();
				System.out.println( "Key: " + key + " | Start Point: " + value.getStartPoint().getColumn() + " | End Point: " + value.getEndPoint().getColumn() + " | Height: " + value.getHeight());
		}
		/*
		for (Map.Entry<String, Interval> entry : this.intervalMap.entrySet()) {
				System.out.println(entry);
		}
		
		for (Map.Entry<String, Interval> entry : this.intervalMap.entrySet()) {
			System.out.println(entry.getValue().getStartPoint());
			System.out.println(entry.getValue().getEndPoint());
		}
		
		while(eventPoints.size() > 0){
			System.out.println(eventPoints.peek().getInterval());
			System.out.println(eventPoints.peek() + " | Height: " + eventPoints.peek().getInterval().getHeight() + " | Point: " + eventPoints.peek().getColumn());
			eventPoints.remove(eventPoints.peek());
		}
		*/
	}

}
