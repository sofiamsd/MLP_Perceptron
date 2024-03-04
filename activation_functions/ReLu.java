package activation_functions;

import math.Vector;

final public class ReLu implements ActivationFunction {

	@Override
	public Vector call(Vector x) {
		double[] result = new double[x.dimension()];
		
		for (int i=0; i<x.dimension(); i++)
			result[i] = Math.max(x.getCoordinate(i), 0);
		
		return new Vector(result);
	}
	
	@Override
	public Vector computeDerivativeUsingCallResult(Vector reluX) {
		double[] result = new double[reluX.dimension()];
		
		for (int i=0; i<reluX.dimension(); i++)
			result[i] = (reluX.getCoordinate(i)>=0)? 1: 0;
		
		return new Vector(result);
	}
}