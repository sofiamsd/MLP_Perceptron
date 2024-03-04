package mlp;

import java.util.Arrays;
import java.util.Random;

import math.Vector;

import encoding.*;  //TODO remove this
import activation_functions.*; //TODO remove this

public class MLPClassifier {
	final private InputLayer inputLayer;
	final private Layer[] hiddenLayers;
	final private SoftMaxLayer outputLayer;
	final private OneOutOfPEncoder oneOutOfPEncoder;
	
	final private Random randomnessSource;
	
	public MLPClassifier(
		int numberOfInputs,
		int numberOfClasses,
		int[] numbersOfNodesPerHiddenLayer,
		ActivationFunction[] activationsPerHiddenLayer
	) throws IllegalArgumentException {
		
		int numberOfHiddenLayers = numbersOfNodesPerHiddenLayer.length;
		
		if (numberOfHiddenLayers != activationsPerHiddenLayer.length)
			throw new IllegalArgumentException("The number of activation functions doesn't match the number of layers specified by numbersOfNodesPerLayer");
		
		randomnessSource = new Random(System.currentTimeMillis());
		hiddenLayers     = new Layer[numberOfHiddenLayers];
		
		inputLayer = new InputLayer(numberOfInputs);
		
		hiddenLayers[0] = new Layer(numbersOfNodesPerHiddenLayer[0], inputLayer.size(), activationsPerHiddenLayer[0], randomnessSource);
		for (int i=1; i<numberOfHiddenLayers; i++)
			hiddenLayers[i] = new Layer(numbersOfNodesPerHiddenLayer[i], hiddenLayers[i-1].size(), activationsPerHiddenLayer[i], randomnessSource);
		
		int lastLayerSize = hiddenLayers[numberOfHiddenLayers-1].size();
		
		if (lastLayerSize != numberOfClasses)
			throw new IllegalArgumentException("The last layer must have exactly as many nodes as the number of classes");
		
		oneOutOfPEncoder = new OneOutOfPEncoder(numberOfClasses);
		
		outputLayer = new SoftMaxLayer(lastLayerSize);
	}
	
	public int size() {
		return hiddenLayers.length + 2;
	}
	
	public int numberOfCategories() {
		return outputLayer.size();
	}
	
	public double computeAccuracy(int[] expectedCategories, Vector[] predictedOutputs) {
		double correctCategoryPredictions = 0;
		for (int i=0; i<predictedOutputs.length; i++)
			if (expectedCategories[i] == oneOutOfPEncoder.decodeSoftMax(predictedOutputs[i]))
				correctCategoryPredictions += 1;
		return correctCategoryPredictions / predictedOutputs.length;
	}
	
	public double computeLoss(int expectedCategory, Vector predictedOutput) {
	    return -Math.log(predictedOutput.getCoordinate(expectedCategory));
	}

	public double computeLoss(int[] expectedCategories, Vector[] predictedOutputs) {
		double crossEntropy = 0;
		double m = expectedCategories.length;
		
		for (int i=0; i<m; i++)
			crossEntropy += computeLoss(expectedCategories[i], predictedOutputs[i]);
			
		return 1/m*crossEntropy;
	}
	
	public Vector predict(Vector input) {
		Vector output = inputLayer.feedForward(input);
		
		for (Layer layer: hiddenLayers)
			output = layer.feedForward(output);
		
		return outputLayer.feedForward(output);
	}
	
	public Vector[] predictAll(Vector[] inputs) {
		Vector[] predictions = new Vector[inputs.length];
		
		for (int i=0; i<inputs.length; i++)
			predictions[i] = predict(inputs[i]);
		
		return predictions;
	}
	
	private Vector[] computeOutputsDuringForwardPass(Vector input) {
		Vector[] layerOutputs = new Vector[size()];
	
		layerOutputs[0] = inputLayer.feedForward(input);
		
		for (int i=0; i<hiddenLayers.length; i++)
			layerOutputs[i+1] = hiddenLayers[i].feedForward(layerOutputs[i]);
		
		layerOutputs[size()-1] = outputLayer.feedForward(layerOutputs[size()-2]); 
		
		return layerOutputs;
	}
	
	private void computeDerivativesWithBackpropagation(Vector input, Vector expectedOutput) {
		Vector[] layerOutputs = computeOutputsDuringForwardPass(input);
		
		Vector prediction = layerOutputs[size()-1];
		
		Vector layerDelta = outputLayer.computeLayerDelta(prediction, expectedOutput);
		
		for (int i=hiddenLayers.length-1; i>=0; i--) {
			Layer layer = hiddenLayers[i];
			
			Vector previousLayerOutput = layerOutputs[i];
			
			layer.updatePartialDerivatives(previousLayerOutput, layerDelta);
			
			layerDelta = layer.computePreviousLayerDelta(previousLayerOutput, layerDelta);
		}
	}
	
	public void trainOneBatch(Vector[] data, Vector[] expectedOutputs, double learningRate) {
        for (Layer layer: hiddenLayers)
            layer.initializePartialDerivatives();
		
		for (int i=0; i<data.length; i++) {
			Vector input 	      = data[i];
			Vector expectedOutput = expectedOutputs[i]; 
			computeDerivativesWithBackpropagation(input, expectedOutput);
		}
		
		for (Layer layer: hiddenLayers)
			layer.performGradientDescent(learningRate);
	}
	
	public void train(Vector[] data, int[] categoryLabels, double learningRate, int batchSize, int minEpochs, double threshold) {
	    int numBatches = (int) Math.ceil((double) data.length / batchSize);
	    int epoch = 1;
	    double prevLoss = Double.MAX_VALUE;
	    double currLoss = 0;

	    while (epoch<3000) {
	    	
	        for (int i = 0; i < numBatches; i++) {
	            int start = i * batchSize;
	            int end = Math.min(start + batchSize, data.length);

	            Vector[] batchData = Arrays.copyOfRange(data, start, end);
	            Vector[] batchExpected = new Vector[batchData.length];
	            for (int j=0; j<batchData.length; j++) 
	            	batchExpected[j] = oneOutOfPEncoder.encode(categoryLabels[start + j]);
	            
	            trainOneBatch(batchData, batchExpected, learningRate);
	        }

	        Vector[] predictions = predictAll(data);
	        currLoss = computeLoss(categoryLabels, predictions);
	        double accuracy = computeAccuracy(categoryLabels, predictions);
	        
	        System.out.println("Epoch: " + epoch + ", loss: " + currLoss + ", accuracy: " + accuracy*100 + "%");
	        
	        if (epoch >= minEpochs && Math.abs(currLoss - prevLoss) < threshold) break;
	        
	        if (Double.isNaN(prevLoss)) return;

	        epoch++;
	        prevLoss = currLoss;
	    }
	}

	
}
