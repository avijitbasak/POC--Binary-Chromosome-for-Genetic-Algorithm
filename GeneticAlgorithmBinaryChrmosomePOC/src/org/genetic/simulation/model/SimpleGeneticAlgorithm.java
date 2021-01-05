package org.genetic.simulation.model;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.genetics.ChromosomePair;
import org.apache.commons.math3.genetics.CrossoverPolicy;
import org.apache.commons.math3.genetics.GeneticAlgorithm;
import org.apache.commons.math3.genetics.MutationPolicy;
import org.apache.commons.math3.genetics.Population;
import org.apache.commons.math3.genetics.TournamentSelection;
import org.apache.commons.math3.random.RandomGenerator;

public class SimpleGeneticAlgorithm extends GeneticAlgorithm {

	public SimpleGeneticAlgorithm(CrossoverPolicy crossoverPolicy, double crossoverRate, MutationPolicy mutationPolicy,
			double mutationRate, int tournamentSize) throws OutOfRangeException {
		super(crossoverPolicy, crossoverRate, mutationPolicy, mutationRate, new TournamentSelection(tournamentSize));
	}

	public Population nextGeneration(Population current) {
		SimulationPopulation nextGeneration = (SimulationPopulation) current.nextGeneration();

		RandomGenerator randGen = getRandomGenerator();

		while (nextGeneration.getPopulationSize() < nextGeneration.getPopulationLimit()) {
			ChromosomePair pair = getSelectionPolicy().select(current);

			if (randGen.nextDouble() < getCrossoverRate()) {
				pair = getCrossoverPolicy().crossover(pair.getFirst(), pair.getSecond());
			}

			if (randGen.nextDouble() < getMutationRate()) {
				pair = new ChromosomePair(getMutationPolicy().mutate(pair.getFirst()),
						getMutationPolicy().mutate(pair.getSecond()));
			}

			nextGeneration.addChromosome(pair.getFirst());

			if (nextGeneration.getPopulationSize() < nextGeneration.getPopulationLimit()) {
				nextGeneration.addChromosome(pair.getSecond());
			}
		}

		return nextGeneration;
	}

}