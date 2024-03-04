package activation_functions;

import math.Vector;

public interface ActivationFunction {
	
	public Vector call(Vector x);
	
	public Vector computeDerivativeUsingCallResult(Vector activationForX);
}