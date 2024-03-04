package main;


import java.util.Random;
import java.util.List;
import java.util.ArrayList;

import java.nio.file.Files;
import java.nio.file.Path;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;



final public class SetGenerator {
	
	public static Point[] generateRandomPoints(int size, Random randomnessSource) {
		
		Point[] points = new Point[size];
		
        double min = -1;
        double max = Math.nextUp(1);
        
		for (int i = 0; i < size; i++) {
			double x = randomnessSource.nextDouble(min, max);
			double y = randomnessSource.nextDouble(min, max);
			int label = generateSDTLabel(x, y);
			points[i] = new Point(x, y, label);
		}
		
		return points;
	}
	
	private static Integer generateSDTLabel(final double x, final double y) {
		if (Math.pow(x - 0.5, 2) + Math.pow(y - 0.5, 2) < 0.2 && x >= 0.5)  return 0;
		if (Math.pow(x - 0.5, 2) + Math.pow(y - 0.5, 2) < 0.2 && x < 0.5)   return 1;
		if (Math.pow(x + 0.5, 2) + Math.pow(y + 0.5, 2) < 0.2 && x >= -0.5) return 0;
        if (Math.pow(x + 0.5, 2) + Math.pow(y + 0.5, 2) < 0.2 && x < -0.5)  return 1;
        if (Math.pow(x - 0.5, 2) + Math.pow(y + 0.5, 2) < 0.2 && x >= 0.5)  return 0;
        if (Math.pow(x - 0.5, 2) + Math.pow(y + 0.5, 2) < 0.2 && x < 0.5)   return 1;
        if (Math.pow(x + 0.5, 2) + Math.pow(y - 0.5, 2) < 0.2 && x >= -0.5) return 0;
        if (Math.pow(x + 0.5, 2) + Math.pow(y - 0.5, 2) < 0.2 && x < -0.5)  return 1;
        if (x >= 0) return 2;
        if (x < 0) return 3;
        
        return null;
	}
	
	public static void write(String filePath, Point[] points) throws IOException {
		
		Path path = Path.of(filePath);
		
		BufferedWriter writer = Files.newBufferedWriter(path);
		
		for (Point point: points) {
			writer.write(point.toString());
			writer.newLine();
		}
		
		writer.close();
	}
	
	public static Point[] parse(String filePath) throws IOException {
		
		List<Point> points = new ArrayList<>();
		
		Path path = Path.of(filePath);
		
		BufferedReader reader = Files.newBufferedReader(path);
		
		String line;
		
		while ((line = reader.readLine()) != null) {
			String[] fields = line.split(" ");
			
			try {
				Double x = Double.parseDouble(fields[0]);
				Double y = Double.parseDouble(fields[1]);
				Integer label = Integer.parseInt(fields[2]);
				
				points.add(new Point(x, y, label));
			} catch (NumberFormatException e) {
				throw new IOException("Invalid point format");
			}
		}
		
		reader.close();
		
		return points.toArray(new Point[points.size()]);
	}

}