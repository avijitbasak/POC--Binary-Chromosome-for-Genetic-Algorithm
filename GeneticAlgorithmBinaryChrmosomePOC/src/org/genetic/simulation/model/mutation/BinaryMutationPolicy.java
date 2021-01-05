package org.genetic.simulation.model.mutation;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.MutationPolicy;
import org.genetic.simulation.model.SimulationBinaryChromosome;

public class BinaryMutationPolicy implements MutationPolicy {

	private double mutationProbability;

	private final long BLOCK_SIZE = 63;

	public BinaryMutationPolicy(double mutationProbability) {
		this.mutationProbability = mutationProbability;
	}

	public void setMutationProbability(double mutationProbability) {
		this.mutationProbability = mutationProbability;
	}

	@Override
	public Chromosome mutate(Chromosome original) throws MathIllegalArgumentException {

		if (!(original instanceof SimulationBinaryChromosome)) {
			throw new MathIllegalArgumentException(LocalizedFormats.INVALID_BINARY_CHROMOSOME, new Object[0]);
		}

		SimulationBinaryChromosome origChrom = (SimulationBinaryChromosome) original;
		long noOfAlleleChanges = (long) Math.floor(mutationProbability * (double) origChrom.getChromosomeLength());

		List<Long> oldRep = origChrom.getRepresentation();
		List<Long> newRep = new ArrayList<Long>(oldRep);

		long alleleCount = newRep.get(0);
		for (long i = 0; i < noOfAlleleChanges; i++) {

			long geneIndex = (long) (Math.random() * alleleCount);
			long offset = BLOCK_SIZE - (alleleCount % BLOCK_SIZE);
			geneIndex += offset;

			/*
			 * The long is converted to int as there are limitation on number of
			 * elements ArrayList can accommodate.
			 */
			int blockIndex = (int) (geneIndex % BLOCK_SIZE == 0 ? geneIndex / BLOCK_SIZE : geneIndex / BLOCK_SIZE) + 1;
			int blockElementIndex = (int) (geneIndex % BLOCK_SIZE);

			if (blockIndex == 1) {
				blockElementIndex -= offset;
			}

			long allelesBlock = newRep.get(blockIndex);
			StringBuilder allelesBlockStr = new StringBuilder(Long.toBinaryString(allelesBlock));
			int leadingZeroCount = (int) (blockIndex == 1 ? (BLOCK_SIZE - offset - allelesBlockStr.length())
					: (BLOCK_SIZE - allelesBlockStr.length()));
			allelesBlockStr = prependZero(allelesBlockStr.toString(), leadingZeroCount);

			allelesBlockStr.insert(blockElementIndex, allelesBlockStr.charAt(blockElementIndex) == '0' ? '1' : '0');

			long mutatedBlock = Long.parseLong(allelesBlockStr.toString(), 2);

			newRep.set(blockIndex, mutatedBlock);

		}

		SimulationBinaryChromosome newChrom = new SimulationBinaryChromosome(newRep, origChrom.getFitnessCalculator());

		return newChrom;

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