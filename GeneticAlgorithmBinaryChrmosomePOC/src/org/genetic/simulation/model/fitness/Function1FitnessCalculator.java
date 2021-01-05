package org.genetic.simulation.model.fitness;

import org.apache.commons.math3.genetics.Chromosome;
import org.genetic.simulation.model.FitnessCalculator;
import org.genetic.simulation.model.SimulationBinaryChromosome;

public class Function1FitnessCalculator implements FitnessCalculator {

	@Override
	public double calculate(Chromosome chromosome) {
		SimulationBinaryChromosome binaryChromosome = (SimulationBinaryChromosome) chromosome;
		double value = getValue(binaryChromosome.getStringRepresentation().substring(0, 10))
				+ getValue(binaryChromosome.getStringRepresentation().substring(10, 20))
				+ getValue(binaryChromosome.getStringRepresentation().substring(20, 30));
		return value * (-1.0);
	}

	private double getValue(String valueStr) {
		return Math.pow(Integer.parseInt(valueStr.substring(1, 10), 2) / 100.0, 2);
	}

}
