package org.genetic.simulation.model;

import org.apache.commons.math3.genetics.Chromosome;

@FunctionalInterface
public interface FitnessCalculator {

	public double calculate(Chromosome chromosome);

}
