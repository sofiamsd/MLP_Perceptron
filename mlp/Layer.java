package mlp;


import java.util.Random;

import activation_functions.ActivationFunction;

import math.Vector;
import math.Matrix;


class Layer {
	Matrix W;
	Vector b;
	private Matrix partialErrorDerWithRespectToW;
	private Vector partialErrorDerWithRespectToB;
	private ActivationFunction g;
	
	Layer(int numberOfNodes, int previousLayerNodes, ActivationFunction activation, Random randomnessSource) {
		W = Matrix.generateRandom(numberOfNodes, previousLayerNodes, -1, 1, randomnessSource);
		b = Vector.generateRandom(numberOfNodes, -1, 1, randomnessSource);
		g = activation;
	}
	
	int size() {
		return b.dimension();
	}
	
	Vector feedForward(Vector y) {
		Vector u = b.plus(W.dot(y));
		return g.call(u);
	}
	
	Vector computePreviousLayerDelta(Vector previousLayerOutput, Vector layerDelta) {
		Vector previousGDeriv = g.computeDerivativeUsingCallResult(previousLayerOutput);
		return layerDelta.dot(W).dotElementwise(previousGDeriv);
	}
	
	Matrix computePartialWithRespectToLayerWeights(Vector previousLayerOutput, Vector layerDelta) {
		return layerDelta.dotToMatrix(previousLayerOutput);
	}
	
	Vector computePartialWithRespectToLayerBiases(Vector layerDelta) {
		return layerDelta;
	}
	
	void initializePartialDerivatives() {
		partialErrorDerWithRespectToW = new Matrix(W.dimension1(), W.dimension2());
		partialErrorDerWithRespectToB = new Vector(b.dimension());
	}
	
	void updatePartialDerivatives(Vector previousLayerOutput, Vector layerDelta) {
		Matrix dErrorDW = computePartialWithRespectToLayerWeights(previousLayerOutput, layerDelta);
		partialErrorDerWithRespectToW = partialErrorDerWithRespectToW.plus(dErrorDW);
		
		Vector dErrorDb = computePartialWithRespectToLayerBiases(layerDelta);
		partialErrorDerWithRespectToB = partialErrorDerWithRespectToB.plus(dErrorDb);
	}
	
	void performGradientDescent(double learningRate) {
		this.W = W.plus(-learningRate, partialErrorDerWithRespectToW);
		this.b = b.plus(-learningRate, partialErrorDerWithRespectToB);
	}
}
