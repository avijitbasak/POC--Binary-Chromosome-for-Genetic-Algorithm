package org.genetic.simulation.model.crossover;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ChromosomePair;
import org.apache.commons.math3.genetics.CrossoverPolicy;
import org.genetic.simulation.model.SimulationBinaryChromosome;

public class OnePointBinaryCrossoverPolicy implements CrossoverPolicy {

	@Override
	public ChromosomePair crossover(Chromosome first, Chromosome second) throws MathIllegalArgumentException {

		SimulationBinaryChromosome chromosome1 = (SimulationBinaryChromosome) first;
		SimulationBinaryChromosome chromosome2 = (SimulationBinaryChromosome) second;
		List<Long> representation1 = chromosome1.getRepresentation();
		List<Long> representation2 = chromosome2.getRepresentation();

		if (representation1.get(0) != representation2.get(0)) {
			throw new RuntimeException("");
		}

		long alleleCount = representation1.get(0);
		long crossoverIndex = (long) Math.floor((Math.random() * alleleCount));

		int offset = (int) (SimulationBinaryChromosome.BLOCKSIZE
				- (alleleCount % SimulationBinaryChromosome.BLOCKSIZE));
		long offsettedCrossoverIndex = crossoverIndex + offset;

		/*
		 * The long is converted to int as there are limitation on number of
		 * elements ArrayList can accommodate.
		 */
		int blockIndex = (int) (crossoverIndex % SimulationBinaryChromosome.BLOCKSIZE == 0
				? crossoverIndex / SimulationBinaryChromosome.BLOCKSIZE
				: crossoverIndex / SimulationBinaryChromosome.BLOCKSIZE) + 1;
		int blockElementIndex = (int) ((blockIndex != 1)
				? (offsettedCrossoverIndex % SimulationBinaryChromosome.BLOCKSIZE)
				: (offsettedCrossoverIndex % SimulationBinaryChromosome.BLOCKSIZE - offset));

		long allelesBlock1 = representation1.get(blockIndex);
		StringBuilder allelesBlockStr1 = new StringBuilder(Long.toBinaryString(allelesBlock1));
		int leadingZeroCount = (int) (blockIndex == 1
				? (SimulationBinaryChromosome.BLOCKSIZE - offset - allelesBlockStr1.length())
				: (SimulationBinaryChromosome.BLOCKSIZE - allelesBlockStr1.length()));
		allelesBlockStr1 = prependZero(allelesBlockStr1.toString(), leadingZeroCount);

		long allelesBlock2 = representation2.get(blockIndex);
		StringBuilder allelesBlockStr2 = new StringBuilder(Long.toBinaryString(allelesBlock2));
		leadingZeroCount = (int) (blockIndex == 1
				? (SimulationBinaryChromosome.BLOCKSIZE - offset - allelesBlockStr2.length())
				: (SimulationBinaryChromosome.BLOCKSIZE - allelesBlockStr2.length()));
		allelesBlockStr2 = prependZero(allelesBlockStr2.toString(), leadingZeroCount);

		String allelesBlockStr1C = null;
		String allelesBlockStr2C = null;

		if (blockElementIndex < allelesBlockStr1.length()) {
			allelesBlockStr1C = allelesBlockStr1.substring(0, blockElementIndex)
					+ allelesBlockStr2.substring(blockElementIndex);
			allelesBlockStr2C = allelesBlockStr2.substring(0, blockElementIndex)
					+ allelesBlockStr1.substring(blockElementIndex);
		} else {
			allelesBlockStr1C = allelesBlockStr1.toString();
			allelesBlockStr2C = allelesBlockStr2.toString();
		}

		List<Long> representation1C = new ArrayList<>(representation1.subList(0, blockIndex));
		representation1C.add(Long.parseLong(allelesBlockStr1C.toString(), 2));
		representation1C.addAll(representation2.subList(blockIndex + 1, representation2.size()));

		List<Long> representation2C = new ArrayList<>(representation2.subList(0, blockIndex));
		representation2C.add(Long.parseLong(allelesBlockStr2C.toString(), 2));
		representation2C.addAll(representation1.subList(blockIndex + 1, representation1.size()));

		ChromosomePair pair = new ChromosomePair(
				new SimulationBinaryChromosome(representation1C, chromosome1.getFitnessCalculator()),
				new SimulationBinaryChromosome(representation2C, chromosome2.getFitnessCalculator()));

		return pair;

	}

	private StringBuilder prependZero(String chromosomeStr, int count) {
		StringBuilder modChromosomeStr = new StringBuilder();
		for (int i = 0; i < count; i++) {
			modChromosomeStr.append('0');
		}
		modChromosomeStr.append(chromosomeStr);

		return modChromosomeStr;
	}
}
