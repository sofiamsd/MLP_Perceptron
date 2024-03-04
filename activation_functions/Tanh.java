package activation_functions;

import math.Vector;

final public class Tanh implements ActivationFunction{

	@Override
	public Vector call(Vector x) {
		double[] result = new double[x.dimension()];
		
		for (int i=0; i<x.dimension(); i++)
			result[i] = Math.tanh(x.getCoordinate(i));
		
		return new Vector(result);
	}
	
	@Override
	public Vector computeDerivativeUsingCallResult(Vector tanhX) {
		double[] result = new double[tanhX.dimension()];
		
		for (int i=0; i<tanhX.dimension(); i++) {
			double tanhXi = tanhX.getCoordinate(i);
			result[i] = 1-Math.pow(tanhXi, 2);
		}
		
		return new Vector(result);
	}
}
