package org.genetic.simulation.model.fitness;

import org.apache.commons.math3.genetics.Chromosome;
import org.genetic.simulation.model.FitnessCalculator;
import org.genetic.simulation.model.SimulationBinaryChromosome;

public class Function7FitnessCalculator implements FitnessCalculator {

	@Override
	public double calculate(Chromosome chromosome) {
		SimulationBinaryChromosome binaryChromosome = (SimulationBinaryChromosome) chromosome;
		String valueStr = binaryChromosome.getStringRepresentation();

		double x = Integer.parseInt(valueStr.substring(0, 12), 2) / 100.0;
		double y = Integer.parseInt(valueStr.substring(12, 24), 2) / 100.0;
		double computedValue = Math.pow((Math.pow(x, 2) + Math.pow(y, 2)), .25)
				* (Math.pow(Math.sin(50 * Math.pow((Math.pow(x, 2) + Math.pow(y, 2)), .1)), 2) + 1);
		// System.out.println("(" + x + ", " + y + ") --> " + computedValue);

		return computedValue * (-1.0);
	}

}
