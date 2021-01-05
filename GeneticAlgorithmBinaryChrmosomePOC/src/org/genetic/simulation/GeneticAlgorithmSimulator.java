package org.genetic.simulation;

import org.apache.commons.math3.genetics.GeneticAlgorithm;
import org.apache.commons.math3.genetics.Population;
import org.genetic.simulation.model.SimulationPopulation;
import org.genetic.simulation.model.SimulationStoppingCondition;

public class GeneticAlgorithmSimulator {

	private double bestFitness;

	private int evolutionCount;

	public void simulate(GeneticAlgorithm algorithm, SimulationStoppingCondition stopCond,
			SimulationPopulation initial) {
		Population finalPopulation = algorithm.evolve(initial, stopCond);
		this.evolutionCount = stopCond.getEvolutionCount();
		this.bestFitness = finalPopulation.getFittestChromosome().getFitness();
	}

	public int getEvolutionCount() {
		return evolutionCount;
	}

	public double getBestFitness() {
		return bestFitness;
	}

}
