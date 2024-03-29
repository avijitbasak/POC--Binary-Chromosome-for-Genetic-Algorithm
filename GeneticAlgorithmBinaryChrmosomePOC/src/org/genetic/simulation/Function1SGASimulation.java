package org.genetic.simulation;

import java.io.IOException;

import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.GeneticAlgorithm;
import org.apache.commons.math3.genetics.Population;
import org.apache.commons.math3.genetics.StoppingCondition;
import org.genetic.simulation.graph.GraphPlotter;
import org.genetic.simulation.model.BinaryChromosome;
import org.genetic.simulation.model.SimpleGeneticAlgorithm;
import org.genetic.simulation.model.SimulationPopulation;
import org.genetic.simulation.model.SimulationStoppingCondition;
import org.genetic.simulation.model.crossover.OnePointBinaryCrossoverPolicy;
import org.genetic.simulation.model.fitness.Function1FitnessCalculator;
import org.genetic.simulation.model.mutation.BinaryMutationPolicy;

public class Function1SGASimulation {

	private static final int POPULATION_SIZE = 10;

	private static final int TOURNAMENT_SIZE = 2;

	private static final int CHROMOZOME_LENGTH = 30;

	private static final double CROSSOVER_RATE = 1.0;

	private static final double ELITISM_RATE = 0.25;

	private static final double AVERAGE_MUTATION_RATE = 0.05;

	private static final String IMAGE_FILE_PATH = "reports\\F1_Optimization.jpeg";

	public static void main(String[] args) {
		try {
			GraphPlotter fitnessSimulationGraphPlotter = new GraphPlotter("FitnessSimulationGraph", "generations",
					"cost");
			SimulationPopulation initPopulation = getInitialPopulation();
			System.out.println("Initial population:");
			System.out.println(initPopulation.toString());

			Function1SGASimulation simulation = new Function1SGASimulation();

			simulation.optimize(initPopulation, AVERAGE_MUTATION_RATE, fitnessSimulationGraphPlotter,
					"SGAOptimization");

			Thread.sleep(5000);

			fitnessSimulationGraphPlotter.saveAsImage(IMAGE_FILE_PATH);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void optimize(Population initial, double mutationProbability, GraphPlotter graphPlotter, String graphLabel)
			throws IOException {

		// initialize a new genetic algorithm
		GeneticAlgorithm ga = new SimpleGeneticAlgorithm(new OnePointBinaryCrossoverPolicy(), CROSSOVER_RATE,
				new BinaryMutationPolicy(mutationProbability), mutationProbability, TOURNAMENT_SIZE);

		// stopping condition
		StoppingCondition stopCond = new SimulationStoppingCondition(graphPlotter, graphLabel);

		// run the algorithm
		Population finalPopulation = ga.evolve(initial, stopCond);

		// best chromosome from the final population
		Chromosome bestFinal = finalPopulation.getFittestChromosome();

		double fitness = bestFinal.fitness();

		System.out.println("*********************************************");
		System.out.println("***********Optimization Result***************");
		System.out.println("*********************************************");

		System.out.println(bestFinal.toString());
		System.out.printf("Best Fitness: %.6f", fitness);

	}

	private static SimulationPopulation getInitialPopulation() {
		SimulationPopulation simulationPopulation = new SimulationPopulation(POPULATION_SIZE, ELITISM_RATE);
		for (int i = 0; i < POPULATION_SIZE; i++) {
			simulationPopulation
					.addChromosome(new BinaryChromosome(CHROMOZOME_LENGTH, new Function1FitnessCalculator()));
		}
		simulationPopulation.setChromosomeRanks();

		return simulationPopulation;
	}

}