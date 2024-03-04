package main;

import java.util.Arrays;
import java.util.Random;
import java.io.IOException;

import activation_functions.ActivationFunction;
import activation_functions.Logistic;
import activation_functions.ReLu;
import activation_functions.Tanh;

import math.Vector;

import mlp.MLPClassifier;

import encoding.OneOutOfPEncoder;

public class Main {

	public static void main(String[] args) throws IOException {
		Point[] trainPoints = new Point[4000];
		Point[] testPoints  = new Point[4000];
		Random random = new Random(System.currentTimeMillis());
		
		try {
			trainPoints = SetGenerator.parse("./TrainingSet.txt");
		}
		catch (IOException e) {
			trainPoints = SetGenerator.generateRandomPoints(trainPoints.length, random);
			SetGenerator.write("./TrainingSet.txt", trainPoints);
		}
	
		Vector[] trainVectors = new Vector[trainPoints.length];
		int[] trainLabels     = new int[trainPoints.length];
		
		for (int i=0; i<trainPoints.length; i++) {
			Point point     = trainPoints[i];
			trainVectors[i] = new Vector(new double[] {point.getX(), point.getY()});
			trainLabels[i]  = point.getLabel();
		}
		
		try {
			testPoints = SetGenerator.parse("./TestSet.txt");
		}
		catch (IOException e) {
			testPoints = SetGenerator.generateRandomPoints(testPoints.length, random);
			SetGenerator.write("./TestSet.txt", testPoints);
		}
		
		Vector[] testVectors = new Vector[testPoints.length];
		int[] testLabels     = new int[testPoints.length];
		
		for (int i=0; i<testPoints.length; i++) {
			Point point    = testPoints[i];
			testVectors[i] = new Vector(new double[] {point.getX(), point.getY()});
			testLabels[i]  = point.getLabel();
		}

		ActivationFunction[][] functions={
				{new Tanh(),new Tanh(),new Tanh()}
		}; 
		
		int[][] nodesperLayer = {
				{3,4,4},
				{3,5,4},
				{2,3,4},
		};
		int[] b_values = {40,400};
        double bestAccuracy = 0.0;
        int bestb=0;
        int[] bestNodesPerLayer = null;
        ActivationFunction[] bestActivations = null;
        Vector[] bestPredictions = null;
      
        for (int[] nodes: nodesperLayer) {
            for(int b: b_values) {
            	for(ActivationFunction[] activations : functions) {
            		System.out.println("\n\nArchitecture: "+Arrays.toString(nodes));
            		System.out.println("Activation functions: "+activations[0].getClass());
            		System.out.println("Batch size: "+b);
                    MLPClassifier mlp = new MLPClassifier(2, 4, nodes, activations);
                    mlp.train(trainVectors, trainLabels, 0.0001, b, 700, 0.0001);
                    Vector[] testPredictions = mlp.predictAll(testVectors);
                    double accuracy = mlp.computeAccuracy(testLabels, testPredictions);
            		System.out.println("Accuracy: " + accuracy * 100 + "%");

                    if (accuracy > bestAccuracy) {
                        bestAccuracy = accuracy;
                        bestNodesPerLayer = nodes;
                        bestActivations = activations;
                        bestb=b;
                        bestPredictions = testPredictions;
                    }
            	}
            }
        }
        
        Point[] predictedPoints = new Point[bestPredictions.length]; 
        
        OneOutOfPEncoder encoder = new OneOutOfPEncoder(4);

        for(int i=0; i<bestPredictions.length;i++) {
        	Vector prediction  = bestPredictions[i];
        	int predictedLabel = encoder.decodeSoftMax(prediction);
        	if(testLabels[i] == predictedLabel) {
        		System.out.println(testVectors[i]+" + ");
        	} else {
        		System.out.println(testVectors[i]+" - ");
    		}
        	predictedPoints[i] = new Point(
        			testVectors[i].getCoordinate(0), 
        			testVectors[i].getCoordinate(1),
        			predictedLabel
        	);
        }
        
        SetGenerator.write("./PredictedSet.txt", predictedPoints);
        
        System.out.println("Best Nodes Per Layer: " + Arrays.toString(bestNodesPerLayer));
        System.out.println("Best Accuracy: " + bestAccuracy * 100 + "%");
        System.out.println("Best Batch size: "+ bestb);
	}
}
