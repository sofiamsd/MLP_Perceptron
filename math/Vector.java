package math;

import java.util.Random;

public class Vector {
	
	private double[] coordinates;
	
	public Vector(int dimension) {
		this.coordinates = new double[dimension];
	}
	
	public Vector(double[] coordinates) {
		this.coordinates = coordinates;
	}
	
	public static Vector generateRandom(int dimension, double min, double max, Random randomnessSource) {
		Vector result = new Vector(dimension);
		result.coordinates = randomnessSource.doubles(dimension, Math.nextUp(min), max).toArray();
		return result;
	}
	
	public double[] getCoordinates() {
		return coordinates;
	}
	
	public double getCoordinate(int i) {
		return coordinates[i];
	}
	
	void setCoordinate(int i, double value) {
		coordinates[i] = value;
	}
	
	public int dimension() {
		return coordinates.length;
	}
	
	void enforceEqualDimensions(Vector vector) {
		if (this.dimension() == vector.dimension()) return;
		throw new IllegalArgumentException(this + ", " + vector + "have different dimensions");
	}
	
	public Vector plus(double d, Vector vector) {
		enforceEqualDimensions(vector);
		
		Vector result = new Vector(dimension());
		
		for (int i=0; i<dimension(); i++)
			result.setCoordinate(i, this.getCoordinate(i) + d*vector.getCoordinate(i));
			
		return result;
	}
	
	public Vector plus(Vector vector) {
		return this.plus(1, vector);
	}
	
	public Vector minus(Vector vector) {
		return this.plus(-1, vector);
	}
	
	public double dot(Vector vector) {
		enforceEqualDimensions(vector);
		
		double result = 0;  
		
		for (int i=0; i<dimension(); i++)
			result += this.getCoordinate(i) * vector.getCoordinate(i);
			
		return result;
	}
	
	public Vector dot(Matrix matrix) {
		if (this.dimension() != matrix.dimension1())
			throw new IllegalArgumentException(this + ", " + matrix + "have different dimensions");
		
		Vector result = new Vector(matrix.dimension2());
		
		for (int i=0; i<result.dimension(); i++)
			result.setCoordinate(i, this.dot(matrix.getColumn(i)));
		
		return result;
	}
	
	public Vector dotElementwise(Vector vector) {
		enforceEqualDimensions(vector);
		
		Vector result = new Vector(dimension());
		
		for (int i=0; i<dimension(); i++)
			result.setCoordinate(i, this.getCoordinate(i) * vector.getCoordinate(i));
		
		return result;
	}
	
	public Matrix dotToMatrix(Vector vector) {
		double[][] data = new double[this.dimension()][vector.dimension()]; 
		
		for (int i=0; i<this.dimension(); i++)
			for (int j=0; j<vector.dimension(); j++)
				data[i][j] = this.getCoordinate(i) * vector.getCoordinate(j);
		
		return new Matrix(data);
	}
	
	@Override
	public String toString() {
		String s = String.valueOf(getCoordinate(0));
		
		if (dimension() > 1)
			for (int i=1; i<dimension(); i++)
				s += ", " + getCoordinate(i);
			
		return "[" + s + "]";
	}
}
