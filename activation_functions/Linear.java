package activation_functions;

import math.Vector;

public class Linear implements ActivationFunction {
	
	@Override
	public Vector call(Vector x) {
		return x;
	}
	
	@Override
	public Vector computeDerivativeUsingCallResult(Vector linearX) {
		double[] coordinates = new double[linearX.dimension()];
		
		for (int i=0; i<linearX.dimension(); i++)
			coordinates[i] = 1;
		
		return new Vector(coordinates);
	}
}
