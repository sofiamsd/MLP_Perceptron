package activation_functions;

import math.Vector;

final public class Logistic implements ActivationFunction {
	
	@Override
	public Vector call(Vector x) {
		double[] result = new double[x.dimension()];
		
		for (int i=0; i<x.dimension(); i++)
			result[i] = 1/(1+Math.exp(-x.getCoordinate(i)));
		
		return new Vector(result);
	}
	
	@Override
	public Vector computeDerivativeUsingCallResult(Vector logisticX) {
		double[] result = new double[logisticX.dimension()];
		
		for (int i=0; i<logisticX.dimension(); i++) {
			double logisticXi = logisticX.getCoordinate(i);
			result[i] = logisticXi*(1-logisticXi);
		}
		
		return new Vector(result);
	}	
}