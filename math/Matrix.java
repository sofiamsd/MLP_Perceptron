package math;

import java.util.Random;

public class Matrix {
	
	private double[][] data;
	
	public Matrix(int dimension1, int dimension2) {
		this.data = new double[dimension1][dimension2];
	}
	
	public Matrix(double[][] data) {
		this.data = data;
	}
	
	public static Matrix generateRandom(int dimension1, int dimension2, double min, double max, Random randomnessSource) {
		Matrix result = new Matrix(dimension1, dimension2);
		
		for (int i=0; i<dimension1; i++)
			result.data[i] = Vector.generateRandom(dimension2, min, max, randomnessSource).getCoordinates();
		
		return result;
	}
	
	public Vector getRow(int i) {
		return new Vector(data[i]);
	}
	
	public Vector getColumn(int j) {
		Vector result = new Vector(dimension1());
		for (int i=0; i<dimension1(); i++)
			result.setCoordinate(i, data[i][j]);
		return result;
	}
	
	public double getElement(int i, int j) {
		return getRow(i).getCoordinate(j);
	}
	
	public int dimension1() {
		return data.length;
	}
	
	public int dimension2() {
		return data[0].length;
	}
	
	public Matrix plus(double d, Matrix matrix) {
		if (this.dimension1() != matrix.dimension1() || this.dimension2() != matrix.dimension2())
			throw new IllegalArgumentException(this + ", " + matrix + "have different dimensions");
		
		Matrix result = new Matrix(dimension1(), dimension2());
		
		for (int i=0; i<dimension1(); i++)
			result.data[i] = getRow(i).plus(d, matrix.getRow(i)).getCoordinates();
		
		return result;
	}
	
	public Matrix plus(Matrix matrix) {
		return this.plus(1, matrix);
	}
	
	public Matrix minus(Matrix matrix) {
		return this.plus(-1, matrix);
	}
	
	public Vector dot(Vector vector) {
		if (this.dimension2() != vector.dimension())
			throw new IllegalArgumentException(this + ", " + vector + "have different dimensions");
		
		Vector result = new Vector(dimension1());
		
		for (int i=0; i<result.dimension(); i++)
			result.setCoordinate(i, getRow(i).dot(vector));
		
		return result;
	}
	
	@Override
	public String toString() {
		String s = getRow(0).toString();
		if (dimension2()>1)
			for (int i=1; i<dimension1(); i++)
				s += ",\n " + getRow(i).toString();
		return "[" + s + "]";
	}
}
