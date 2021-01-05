package org.genetic.simulation.model;

import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.Population;
import org.apache.commons.math3.genetics.StoppingCondition;
import org.genetic.simulation.graph.GraphPlotter;

public class SimulationStoppingCondition implements StoppingCondition {

	private int noOfGenerationsHavingUnchangedFitness;

	private double lastBestFitness = Double.MIN_VALUE;

	private final long MAX_NO_OF_GENERATION_WITH_SAME_BEST_FITNESS = 50;

	private int evolutionCount;

	private GraphPlotter fitnessSimulationGraphPlotter;

	private String plotName;

//	private GraphPlotter skewnessPlotter;

	private boolean enableGraphPlotting;

	public SimulationStoppingCondition() {

	}

	public SimulationStoppingCondition(GraphPlotter fitnessSimulationGraphPlotter, String plotName) {
		this.fitnessSimulationGraphPlotter = fitnessSimulationGraphPlotter;
//		this.skewnessPlotter = new GraphPlotter("SkewnessPlot", "generations", "skewness");
		this.plotName = plotName;
		this.enableGraphPlotting = true;
	}

	// public SimulationStoppingCondition(GraphPlotter
	// fitnessSimulationGraphPlotter, GraphPlotter skewnessPlotter,
	// String plotName) {
	// this.fitnessSimulationGraphPlotter = fitnessSimulationGraphPlotter;
	// this.skewnessPlotter = skewnessPlotter;
	// this.plotName = plotName;
	// }

	@Override
	public boolean isSatisfied(Population population) {

		SimulationPopulation simulationPopulation = (SimulationPopulation) population;
		double currentBestFitness = simulationPopulation.getFittestChromosome().fitness();

		if (enableGraphPlotting) {
			// System.out.println("Value1: " +
			// simulationPopulation.getFitnessStats().getSkewness());
			// System.out.println("Value2: " + calculateSkewness(simulationPopulation));
			fitnessSimulationGraphPlotter.addDataPoint(plotName, evolutionCount, Math.abs(currentBestFitness));
//			skewnessPlotter.addDataPoint(plotName + "-skewness", evolutionCount,
//					calculateSkewness(simulationPopulation));
		}
		evolutionCount++;

		if (lastBestFitness == currentBestFitness) {
			if (noOfGenerationsHavingUnchangedFitness == MAX_NO_OF_GENERATION_WITH_SAME_BEST_FITNESS - 1) {
				// System.out.println("Evolution Count : " + evolutionCount);
				return true;
			} else {
				this.noOfGenerationsHavingUnchangedFitness++;
			}
		} else {
			this.noOfGenerationsHavingUnchangedFitness = 0;
			lastBestFitness = currentBestFitness;
		}

		return false;
	}

	private double calculateSkewness(SimulationPopulation population) {
		double avgFitness = population.getFitnessStats().getMean();
		double size = population.getPopulationSize();
		double numerator = 0.0;

		for (Chromosome chromosome : population) {
			numerator += Math.pow((chromosome.fitness() - avgFitness), 3);
		}
		numerator = numerator / size;
		double denominator = Math.pow(population.getFitnessStats().getStandardDeviation(), 3);
		double skewness = numerator / denominator;

		return skewness;
	}

	public int getEvolutionCount() {
		return evolutionCount;
	}
}
