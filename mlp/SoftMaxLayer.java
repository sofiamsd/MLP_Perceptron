package mlp;

import math.Vector;

final class SoftMaxLayer {
	final private int numberOfOutputs;

	SoftMaxLayer(int numberOfNodes) {
		this.numberOfOutputs = numberOfNodes;
	}
	
	int size() {
		return numberOfOutputs;
	}

	Vector softmax(Vector x) {
		double[] coordinates  = new double[x.dimension()];
		
		for (int i=0; i<x.dimension(); i++) {
			double sumOfExpsOtherThanI = 0;
			
			for (int j=0; j<x.dimension(); j++)
				if (i!=j)
					sumOfExpsOtherThanI += Math.exp(x.getCoordinate(j)-x.getCoordinate(i));
		
			coordinates[i] = 1 / (1 + sumOfExpsOtherThanI); 
		}
		
		return new Vector(coordinates);
	}
	
	Vector feedForward(Vector input) {
		if (input.dimension() == size()) return softmax(input);
		
		throw new IllegalArgumentException("A vector with wrong dimension number was fed to the softmax layer");
	}
	
	Vector computeLayerDelta(Vector layerOutput, Vector expectedOutput) {
		return layerOutput.minus(expectedOutput);
	}
}
