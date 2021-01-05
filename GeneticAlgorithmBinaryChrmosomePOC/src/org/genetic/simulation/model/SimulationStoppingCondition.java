package org.genetic.simulation.model;

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

	private boolean enableGraphPlotting;

	public SimulationStoppingCondition() {

	}

	public SimulationStoppingCondition(GraphPlotter fitnessSimulationGraphPlotter, String plotName) {
		this.fitnessSimulationGraphPlotter = fitnessSimulationGraphPlotter;
		this.plotName = plotName;
		this.enableGraphPlotting = true;
	}

	@Override
	public boolean isSatisfied(Population population) {

		SimulationPopulation simulationPopulation = (SimulationPopulation) population;
		double currentBestFitness = simulationPopulation.getFittestChromosome().fitness();

		if (enableGraphPlotting) {
			fitnessSimulationGraphPlotter.addDataPoint(plotName, evolutionCount,
					Math.abs(simulationPopulation.getAverageFitness()));
		}
		evolutionCount++;

		if (lastBestFitness == currentBestFitness) {
			if (noOfGenerationsHavingUnchangedFitness == MAX_NO_OF_GENERATION_WITH_SAME_BEST_FITNESS - 1) {
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

	public int getEvolutionCount() {
		return evolutionCount;
	}
}
