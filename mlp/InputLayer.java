package mlp;

import math.Vector;

final class InputLayer {
	final private int numberOfOutputs;

	InputLayer(int numberOfNodes) {
		this.numberOfOutputs = numberOfNodes;
	}
	
	int size() {
		return numberOfOutputs;
	}
	
	Vector feedForward(Vector input) {
		if (input.dimension() == size()) return input;
		
		throw new IllegalArgumentException("Input has wrong dimension");
	}
}
