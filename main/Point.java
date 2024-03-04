package main;


public class Point {
	final private double x;
	final private double y;
	final private int label;
	
	
	public Point(final double x, final double y, final int label) {
		this.x = x;	
		this.y = y;
		this.label = label;
	}
	
	public double getX() {
		return x;
	} 
	
	public double getY() {
		return y;
	}
	
	public int getLabel() {
		return label;
	}
	
	@Override
	public String toString() {
		return x + " " + y + " " + label;
	}
}