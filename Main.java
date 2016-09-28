import java.util.Scanner;
import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.Map;
import java.util.ArrayList;

public class Main {

    // Main part of your code - actually merge the hulls.
    public static Hull mergeHulls(Hull a, Hull b) {
		
		Hull returnHull = new Hull();
		
		PriorityQueue<Point> mergedEventPoints = mergePoints(a.getEventPoints(), b.getEventPoints());
		ArrayList<Point> underPoints = new ArrayList<Point>(30);
		
		Point currentColumn = mergedEventPoints.peek();
		Point nextColumn = null;
		mergedEventPoints.remove(mergedEventPoints.peek());
	
		while(mergedEventPoints.peek() != null){
			//System.out.println(getGapHeight(underPoints));
			nextColumn = mergedEventPoints.peek();
			mergedEventPoints.remove(mergedEventPoints.peek());

			if (nextColumn instanceof EndPoint){
				removePoint(underPoints, nextColumn);
			}
			
			if (returnHull.getIntervalMap().size() >= 2){
				if (returnHull.getInterval(returnHull.getIntervalMap().size() -2).getEndPoint().getColumn() == returnHull.getInterval(returnHull.getIntervalMap().size() -1).getStartPoint().getColumn() && returnHull.getInterval(returnHull.getIntervalMap().size() -2).getHeight() == returnHull.getInterval(returnHull.getIntervalMap().size() -1).getHeight() ){
					//System.out.println(getLineNumber() + "]   Merging last two intervals.");
					returnHull.mergeIntervals(returnHull.getInterval(returnHull.getIntervalMap().size() -2), returnHull.getInterval(returnHull.getIntervalMap().size() -1));
				}
			}
			
			if(currentColumn.getInterval().getHeight() < nextColumn.getInterval().getHeight()){
				
				if (currentColumn.getColumn() != nextColumn.getColumn()){
					if (currentColumn instanceof StartPoint && nextColumn instanceof StartPoint){
						if(returnHull.getIntervalMap().size() != 0){
							if (returnHull.getInterval(returnHull.getIntervalMap().size() -1).getHeight() == currentColumn.getInterval().getHeight() && returnHull.getInterval(returnHull.getIntervalMap().size() -1).getEndPoint().getColumn() == currentColumn.getColumn() ){
								//System.out.println(getLineNumber() + "]   CCH < NCH | CC != NC | SP vs SP | Map not empty | PrevInter == CCH | Editing previous Interval" + " | (" + currentColumn.getColumn() + "," + currentColumn.getInterval().getHeight() + ") (" + nextColumn.getColumn() + "," + nextColumn.getInterval().getHeight() + ") ");
								returnHull.getInterval(returnHull.getIntervalMap().size() -1).setEndPoint(nextColumn);
							} else {
								//System.out.println(getLineNumber() + "]   CCH < NCH | CC != NC | SP vs SP | Map not empty | PrevInter != CCH | Adding Interval" + " | (" + currentColumn.getColumn() + "," + currentColumn.getInterval().getHeight() + ") (" + nextColumn.getColumn() + "," + nextColumn.getInterval().getHeight() + ") ");
								returnHull.addInterval(returnHull.getIntervalMap().size(), currentColumn, nextColumn, currentColumn.getInterval().getHeight());
							}
						} else {
							//System.out.println(getLineNumber() + "]   CCH < NCH | CC != NC | SP vs SP | Map empty |  Adding Interval" + " | (" + currentColumn.getColumn() + "," + currentColumn.getInterval().getHeight() + ") (" + nextColumn.getColumn() + "," + nextColumn.getInterval().getHeight() + ") ");
							returnHull.addInterval(returnHull.getIntervalMap().size(), currentColumn, nextColumn, currentColumn.getInterval().getHeight());
						}
						currentColumn = nextColumn;
					} else if (currentColumn instanceof StartPoint && nextColumn instanceof EndPoint){
						//System.out.println(getLineNumber() + "]   CCH < NCH | CC != NC | SP vs SE |  Adding Interval" + " | (" + currentColumn.getColumn() + "," + currentColumn.getInterval().getHeight() + ") (" + nextColumn.getColumn() + "," + nextColumn.getInterval().getHeight() + ") ");
						returnHull.addInterval(returnHull.getIntervalMap().size(), returnHull.getIntervalMap().get(String.valueOf(returnHull.getIntervalMap().size() - 1)).getEndPoint(), nextColumn, nextColumn.getInterval().getHeight());
						currentColumn = nextColumn;
					} else if (currentColumn instanceof EndPoint && nextColumn instanceof StartPoint){
						//System.out.println(getLineNumber() + "]   CCH < NCH | CC != NC | SE vs SP " + " | (" + currentColumn.getColumn() + "," + currentColumn.getInterval().getHeight() + ") (" + nextColumn.getColumn() + "," + nextColumn.getInterval().getHeight() + ") ");
						int height = getGapHeight(underPoints);
						if (height > 0){
							//System.out.println(getLineNumber() + "]   Creating interval from underListaaa");
							returnHull.addInterval(returnHull.getIntervalMap().size(), currentColumn, nextColumn, height);
						}
						currentColumn = nextColumn;
					} else if (currentColumn instanceof EndPoint && nextColumn instanceof EndPoint){
						//System.out.println(getLineNumber() + "]   CCH < NCH | CC != NC | SE vs SE |  Adding Interval" + " | (" + currentColumn.getColumn() + "," + currentColumn.getInterval().getHeight() + ") (" + nextColumn.getColumn() + "," + nextColumn.getInterval().getHeight() + ") ");
						returnHull.addInterval(returnHull.getIntervalMap().size(), returnHull.getIntervalMap().get(String.valueOf(returnHull.getIntervalMap().size() - 1)).getEndPoint(), nextColumn, nextColumn.getInterval().getHeight());
						currentColumn = nextColumn;
					}
				} else {
					/*if (returnHull.getIntervalMap().size() > 2){
						if (returnHull.getInterval(returnHull.getIntervalMap().size() -2).getHeight() == nextColumn.getInterval().getHeight() && nextColumn instanceof EndPoint && returnHull.getInterval(returnHull.getIntervalMap().size() -2).getHeight() == returnHull.getInterval(returnHull.getIntervalMap().size() -1).getHeight()){
							//System.out.println("Merging last two intervals.");
							returnHull.mergeIntervals(returnHull.getInterval(returnHull.getIntervalMap().size() -2), returnHull.getInterval(returnHull.getIntervalMap().size() -1));
						}
					} */
					if (currentColumn instanceof EndPoint && nextColumn instanceof StartPoint && returnHull.getInterval(returnHull.getIntervalMap().size() -1).getHeight() < getGapHeight(underPoints)){
						returnHull.getInterval(returnHull.getIntervalMap().size() -1).setHeight(getGapHeight(underPoints));
					} 
					currentColumn = nextColumn;
				}
				
			} else if(currentColumn.getInterval().getHeight() == nextColumn.getInterval().getHeight()){
				if (currentColumn.getColumn() != nextColumn.getColumn()){
					
					if ( returnHull.getIntervalMap().size() > 0){
						
						if (returnHull.getInterval(returnHull.getIntervalMap().size() -1).getHeight() == nextColumn.getInterval().getHeight()){
							if (currentColumn instanceof StartPoint && nextColumn instanceof StartPoint){
								//Instance of partially overlapping intervals
								//System.out.println(getLineNumber() + "]   CCH == NCH | CC != NC | Map not Empty | PrevIntH == NCH | SP vs SP | Iterate" + " | (" + currentColumn.getColumn() + "," + currentColumn.getInterval().getHeight() + ") (" + nextColumn.getColumn() + "," + nextColumn.getInterval().getHeight() + ") ");
							} else if (currentColumn instanceof StartPoint && nextColumn instanceof EndPoint){
								//System.out.println(getLineNumber() + "]   CCH == NCH | CC != NC | Map not Empty | PrevIntH == NCH | SP vs SE | adding Interval" + " | (" + currentColumn.getColumn() + "," + currentColumn.getInterval().getHeight() + ") (" + nextColumn.getColumn() + "," + nextColumn.getInterval().getHeight() + ") ");
								returnHull.addInterval(returnHull.getIntervalMap().size(), currentColumn, nextColumn, currentColumn.getInterval().getHeight());
								currentColumn = nextColumn;
							} else if (currentColumn instanceof EndPoint && nextColumn instanceof StartPoint){
								//System.out.println(getLineNumber() + "]   CCH == NCH | CC != NC | Map not Empty | PrevIntH == NCH | SE vs SP | Iterate" + " | (" + currentColumn.getColumn() + "," + currentColumn.getInterval().getHeight() + ") (" + nextColumn.getColumn() + "," + nextColumn.getInterval().getHeight() + ") ");
								if (returnHull.getInterval(returnHull.getIntervalMap().size() -1).getEndPoint().getColumn() != nextColumn.getColumn() ){
									int height = getGapHeight(underPoints);
									if (height > 0){
										//System.out.println(getLineNumber() + "]   Creating interval from underList");
										returnHull.addInterval(returnHull.getIntervalMap().size(), currentColumn, nextColumn, height);
									}
								}
								currentColumn = nextColumn;
								// There's a gap between intervals of same height(??)
								currentColumn = nextColumn;
							} else if (currentColumn instanceof EndPoint && nextColumn instanceof EndPoint){
								//System.out.println(getLineNumber() + "]   CCH == NCH | CC != NC | Map not Empty | PrevIntH == NCH | SE vs SE | Editing previous Interval" + " | (" + currentColumn.getColumn() + "," + currentColumn.getInterval().getHeight() + ") (" + nextColumn.getColumn() + "," + nextColumn.getInterval().getHeight() + ") ");
								returnHull.getInterval(returnHull.getIntervalMap().size() -1).setEndPoint(nextColumn);
								currentColumn = nextColumn;
							}
						} else { // If height of previous interval not the same as nextColumn
							if (currentColumn instanceof StartPoint && nextColumn instanceof StartPoint){
								//System.out.println(getLineNumber() + "]   CCH == NCH | CC != NC | Map not Empty | PrevIntH != NCH | SP vs SP | Adding Interval" + " | (" + currentColumn.getColumn() + "," + currentColumn.getInterval().getHeight() + ") (" + nextColumn.getColumn() + "," + nextColumn.getInterval().getHeight() + ") ");
								returnHull.addInterval(returnHull.getIntervalMap().size(), currentColumn, nextColumn, currentColumn.getInterval().getHeight());
								currentColumn = nextColumn;
							} else if (currentColumn instanceof StartPoint && nextColumn instanceof EndPoint){
								//System.out.println(getLineNumber() + "]   CCH == NCH | CC != NC | Map not Empty | PrevIntH != NCH | SP vs SE | Adding Interval" + " | (" + currentColumn.getColumn() + "," + currentColumn.getInterval().getHeight() + ") (" + nextColumn.getColumn() + "," + nextColumn.getInterval().getHeight() + ") ");
								returnHull.addInterval(returnHull.getIntervalMap().size(), currentColumn, nextColumn, currentColumn.getInterval().getHeight());
								currentColumn = nextColumn;
							} else if (currentColumn instanceof EndPoint && nextColumn instanceof StartPoint){
								//System.out.println(getLineNumber() + "]   CCH == NCH | CC != NC | Map not Empty | PrevIntH != NCH | SE vs SP | Iterate" + " | (" + currentColumn.getColumn() + "," + currentColumn.getInterval().getHeight() + ") (" + nextColumn.getColumn() + "," + nextColumn.getInterval().getHeight() + ") ");
								currentColumn = nextColumn;
							} else if (currentColumn instanceof EndPoint && nextColumn instanceof EndPoint){
								//System.out.println(getLineNumber() + "]   CCH == NCH | CC != NC | Map not Empty | PrevIntH != NCH | SE vs SE | Iterate" + " | (" + currentColumn.getColumn() + "," + currentColumn.getInterval().getHeight() + ") (" + nextColumn.getColumn() + "," + nextColumn.getInterval().getHeight() + ") ");
								currentColumn = nextColumn;
							}
						}
						
					} else { // No elements in map but currentColumn and nextColumn are the same.
							if (currentColumn instanceof StartPoint && nextColumn instanceof StartPoint){
								// Instance of partially overlapping intervals
								//System.out.println(getLineNumber() + "]   CCH == NCH | CC != NC | Map empty | SP vs SP | Adding interval" + " | (" + currentColumn.getColumn() + "," + currentColumn.getInterval().getHeight() + ") (" + nextColumn.getColumn() + "," + nextColumn.getInterval().getHeight() + ") ");
								returnHull.addInterval(returnHull.getIntervalMap().size(), currentColumn, nextColumn, nextColumn.getInterval().getHeight());
								currentColumn = nextColumn;
							} else if (currentColumn instanceof StartPoint && nextColumn instanceof EndPoint){
								//System.out.println(getLineNumber() + "]   CCH == NCH | CC != NC | Map empty | SP vs SE | Adding interval" + " | (" + currentColumn.getColumn() + "," + currentColumn.getInterval().getHeight() + ") (" + nextColumn.getColumn() + "," + nextColumn.getInterval().getHeight() + ") ");
								returnHull.addInterval(returnHull.getIntervalMap().size(), currentColumn, nextColumn, nextColumn.getInterval().getHeight());
								currentColumn = nextColumn;
							} else if (currentColumn instanceof EndPoint && nextColumn instanceof StartPoint){
								//System.out.println(getLineNumber() + "]   CCH == NCH | CC != NC | Map empty | SE vs SP | Iterate" + " | (" + currentColumn.getColumn() + "," + currentColumn.getInterval().getHeight() + ") (" + nextColumn.getColumn() + "," + nextColumn.getInterval().getHeight() + ") ");
								// Not possible
								currentColumn = nextColumn;
							} else if (currentColumn instanceof EndPoint && nextColumn instanceof EndPoint){
								//System.out.println(getLineNumber() + "]   CCH == NCH | CC != NC | Map empty | SE vs SE | Iterate" + " | (" + currentColumn.getColumn() + "," + currentColumn.getInterval().getHeight() + ") (" + nextColumn.getColumn() + "," + nextColumn.getInterval().getHeight() + ") ");
								// Not possible
								currentColumn = nextColumn;
							}
					}
					
				} else { // CC == NC
					if (currentColumn instanceof StartPoint && nextColumn instanceof StartPoint){
						//System.out.println(getLineNumber() + "]   CCH == NCH | CC == NC | SP vs SP" + " | (" + currentColumn.getColumn() + "," + currentColumn.getInterval().getHeight() + ") (" + nextColumn.getColumn() + "," + nextColumn.getInterval().getHeight() + ") ") ;
						underPoints.add(nextColumn);
						currentColumn = nextColumn;
					} else if (currentColumn instanceof StartPoint && nextColumn instanceof EndPoint){
						//System.out.println(getLineNumber() + "]   CCH == NCH | CC == NC | SP vs SE" + " | (" + currentColumn.getColumn() + "," + currentColumn.getInterval().getHeight() + ") (" + nextColumn.getColumn() + "," + nextColumn.getInterval().getHeight() + ") ");
						// Instance of an interval ending with a height less than current interval
						if ( returnHull.getInterval(returnHull.getIntervalMap().size() -1).getHeight() < nextColumn.getInterval().getHeight()){
							returnHull.getInterval(returnHull.getIntervalMap().size() -1).setHeight(nextColumn.getInterval().getHeight());
						}
					} else if (currentColumn instanceof EndPoint && nextColumn instanceof StartPoint){
						//System.out.println(getLineNumber() + "]   CCH == NCH | CC == NC | SE vs SP " + " | (" + currentColumn.getColumn() + "," + currentColumn.getInterval().getHeight() + ") (" + nextColumn.getColumn() + "," + nextColumn.getInterval().getHeight() + ") ");
						// Instance of a higher interval ending with a new interval beginning.
						// There may be a gap
						currentColumn = nextColumn;
					} else if (currentColumn instanceof EndPoint && nextColumn instanceof EndPoint){
						//System.out.println(getLineNumber() + "]   CCH == NCH | CC == NC | SE vs SE " + " | (" + currentColumn.getColumn() + "," + currentColumn.getInterval().getHeight() + ") (" + nextColumn.getColumn() + "," + nextColumn.getInterval().getHeight() + ") ");
						currentColumn = nextColumn;
					}

				}
				
			} else if (currentColumn.getInterval().getHeight() > nextColumn.getInterval().getHeight()){
				if (currentColumn.getColumn() != nextColumn.getColumn()){
					if (currentColumn instanceof StartPoint && nextColumn instanceof StartPoint){
						//System.out.println(getLineNumber() + "]   CCH > NCH | CC != NC | SP vs SP" + " | Adding Point to underList(" + currentColumn.getColumn() + "," + currentColumn.getInterval().getHeight() + ") (" + nextColumn.getColumn() + "," + nextColumn.getInterval().getHeight() + ") ");
						underPoints.add(nextColumn);
						// Instance of an interval less than current interval
					} else if (currentColumn instanceof StartPoint && nextColumn instanceof EndPoint){
						//System.out.println(getLineNumber() + "]   CCH > NCH | CC != NC | SP vs SE" + " | Removing point from UnderList(" + currentColumn.getColumn() + "," + currentColumn.getInterval().getHeight() + ") (" + nextColumn.getColumn() + "," + nextColumn.getInterval().getHeight() + ") ");
						// Instance of an interval ending with a height less than current interval
					} else if (currentColumn instanceof EndPoint && nextColumn instanceof StartPoint){
						//System.out.println(getLineNumber() + "]   CCH > NCH | CC != NC | SE vs SP | Iterate" + " | (" + currentColumn.getColumn() + "," + currentColumn.getInterval().getHeight() + ") (" + nextColumn.getColumn() + "," + nextColumn.getInterval().getHeight() + ") ");
						// Instance of a higher interval ending with a new interval beginning.
						// There may be a gap
						int height = getGapHeight(underPoints);
						if (height > 0){
							//System.out.println(getLineNumber() + "]   Creating interval from underList");
							returnHull.addInterval(returnHull.getIntervalMap().size(), currentColumn, nextColumn, height);
						}
						currentColumn = nextColumn;
					} else if (currentColumn instanceof EndPoint && nextColumn instanceof EndPoint){
							int h = getGapHeight(underPoints);
							//System.out.println("h :" + h);
							if (nextColumn.getInterval().getHeight() < h ){
								//System.out.println(getLineNumber() + "]   CCH > NCH | CC != NC | SE vs SE | > h in underPoints found compared to endpoint | adding new interval " + " | (" + currentColumn.getColumn() + "," + currentColumn.getInterval().getHeight() + ") (" + nextColumn.getColumn() + "," + nextColumn.getInterval().getHeight() + ") ");
								returnHull.addInterval(returnHull.getIntervalMap().size(), returnHull.getIntervalMap().get(String.valueOf(returnHull.getIntervalMap().size() - 1)).getEndPoint(), nextColumn, h);
							}
							else{
								//System.out.println(getLineNumber() + "]   CCH > NCH | CC != NC | SE vs SE | Adding interval and removing point from UnderList " + " | (" + currentColumn.getColumn() + "," + currentColumn.getInterval().getHeight() + ") (" + nextColumn.getColumn() + "," + nextColumn.getInterval().getHeight() + ") ");
								returnHull.addInterval(returnHull.getIntervalMap().size(), returnHull.getIntervalMap().get(String.valueOf(returnHull.getIntervalMap().size() - 1)).getEndPoint(), nextColumn, nextColumn.getInterval().getHeight());
							}
						currentColumn = nextColumn;
					}
				} else {
					if (currentColumn instanceof StartPoint && nextColumn instanceof StartPoint){
						//System.out.println(getLineNumber() + "]   CCH > NCH | CC == NC | SP vs SP | Adding point to underList " + " | (" + currentColumn.getColumn() + "," + currentColumn.getInterval().getHeight() + ") (" + nextColumn.getColumn() + "," + nextColumn.getInterval().getHeight() + ") ");
						underPoints.add(nextColumn);
						// Instance of an interval less than current interval
					} else if (currentColumn instanceof StartPoint && nextColumn instanceof EndPoint){
						//System.out.println(getLineNumber() + "]   CCH > NCH | CC == NC | SP vs SE | Removing point from underList " + " | (" + currentColumn.getColumn() + "," + currentColumn.getInterval().getHeight() + ") (" + nextColumn.getColumn() + "," + nextColumn.getInterval().getHeight() + ") ");
						// Instance of an interval ending with a height less than current interval
					} else if (currentColumn instanceof EndPoint && nextColumn instanceof StartPoint){
						//System.out.println(getLineNumber() + "]   CCH > NCH | CC == NC | SE vs SP | Iterate" + " | (" + currentColumn.getColumn() + "," + currentColumn.getInterval().getHeight() + ") (" + nextColumn.getColumn() + "," + nextColumn.getInterval().getHeight() + ") ");
						// Instance of a higher interval ending with a new interval beginning.
						// There may be a gap
						currentColumn = nextColumn;
					} else if (currentColumn instanceof EndPoint && nextColumn instanceof EndPoint){
						//System.out.println(getLineNumber() + "]   CCH > NCH | CC == NC | SE vs SE" + " | (" + currentColumn.getColumn() + "," + currentColumn.getInterval().getHeight() + ") (" + nextColumn.getColumn() + "," + nextColumn.getInterval().getHeight() + ") ");
					}	
				}
			}
			
		}
		
		if (returnHull.getIntervalMap().size() >= 2){
				if (returnHull.getInterval(returnHull.getIntervalMap().size() -2).getEndPoint().getColumn() == returnHull.getInterval(returnHull.getIntervalMap().size() -1).getStartPoint().getColumn() && returnHull.getInterval(returnHull.getIntervalMap().size() -2).getHeight() == returnHull.getInterval(returnHull.getIntervalMap().size() -1).getHeight() ){
					//System.out.println(getLineNumber() + "]   Merging last two intervals.");
					returnHull.mergeIntervals(returnHull.getInterval(returnHull.getIntervalMap().size() -2), returnHull.getInterval(returnHull.getIntervalMap().size() -1));
				}
			}
		return returnHull;
    }
												   
	public static void removePoint(ArrayList<Point> underPoints, Point nextColumn){
		underPoints.remove(nextColumn.getInterval().getStartPoint());
		return;
	}
	
	public static int getGapHeight(ArrayList<Point> underPoints){
		int result = -1 ;
		
		for(Point p : underPoints){
			if (p.getInterval().getHeight() > result){
				result = p.getInterval().getHeight();
			}
		}
		return result;
	}
	
	public static PriorityQueue<Point> mergePoints(PriorityQueue<Point> a, PriorityQueue<Point> b){
		Comparator<Point> comparator = new PointComparator();
		PriorityQueue<Point> result = new PriorityQueue<Point>(30, comparator);
		
		while(a.size() > 0){
			result.add(a.peek() );
			a.remove(a.peek());
		}
		
		while(b.size() > 0){
			result.add(b.peek() );
			b.remove(b.peek());
		}
		
		return result;
	}


    // Formats the merged hull for output. TODO.
    public static String fmt(Hull h) {
		StringBuilder result = new StringBuilder();
		
		for (Map.Entry<String, Interval> entry : h.getIntervalMap().entrySet()) {
				String key = entry.getKey();
				Interval value = entry.getValue();
				result.append(value.getStartPoint().getColumn());
				result.append(" " + value.getEndPoint().getColumn());
				result.append(" " + value.getHeight() + "\n");
		}

		return result.substring(0, result.length() -1);
    }
	
	public static int getLineNumber() {
    	return Thread.currentThread().getStackTrace()[2].getLineNumber();
	}

    public static void main(String[] args) {

        Scanner scan = new Scanner(System.in);
		int key = 0;

        Hull hullOne = new Hull();
		Hull hullTwo = new Hull();
		
        while (true) {
            String s = scan.nextLine();
            if (s.equals("x")) {
                break;
            } else {
                hullOne.update(key, s);
				key = key + 1;
            }
        }
		
		key = 0;
        // Read hull 2 from stdin.
        while (scan.hasNextLine()) {
            hullTwo.update(key, scan.nextLine());
			key = key + 1;
        }

        scan.close();

        // Merge and output the hulls.
        Hull merged = mergeHulls(hullOne, hullTwo);
        String output = fmt(merged);
        System.out.println(output);

    }
}
