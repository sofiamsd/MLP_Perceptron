package encoding;

import math.Vector;

public class OneOutOfPEncoder {
	final private Vector[] encodedCategoriesAsVectors;
	
	public OneOutOfPEncoder(int numberOfCategories) {
		encodedCategoriesAsVectors = new Vector[numberOfCategories];
		
		for (int i=0; i<numberOfCategories; i++) {
			double[] oneOutOfP = new double[numberOfCategories];
			oneOutOfP[i] = 1;
			encodedCategoriesAsVectors[i] = new Vector(oneOutOfP);
		}	
	}
	
	public Vector encode(int category) {
		return encodedCategoriesAsVectors[category];
	}
	
	public int decodeSoftMax(Vector vector) {
		int maxIndex = 0;
		double max   = vector.getCoordinate(0);
		
		for (int i=0; i<vector.dimension(); i++)
			if (max < vector.getCoordinate(i)) {
				maxIndex = i;
				max = vector.getCoordinate(i);
			} 
		
		return maxIndex;
	}
}