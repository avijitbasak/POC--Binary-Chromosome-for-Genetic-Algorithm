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
		int noOfAlleleChanges = (int) Math.floor(mutationProbability * (double) origChrom.getChromosomeLength());

		List<Long> oldRep = origChrom.getRepresentation();
		List<Long> newRep = new ArrayList<Long>(oldRep);

		long alleleCount = newRep.get(0);
		int offset = (int) (SimulationBinaryChromosome.BLOCKSIZE
				- (alleleCount % SimulationBinaryChromosome.BLOCKSIZE));
		for (int i = 0; i < noOfAlleleChanges; i++) {
			int geneIndex = (int) (Math.random() * alleleCount);
			int offsettedGeneIndex = geneIndex + offset;

			int blockIndex = (offsettedGeneIndex % SimulationBinaryChromosome.BLOCKSIZE == 0
					? offsettedGeneIndex / SimulationBinaryChromosome.BLOCKSIZE
					: offsettedGeneIndex / SimulationBinaryChromosome.BLOCKSIZE) + 1;
			int blockElementIndex = (blockIndex != 1) ? (offsettedGeneIndex % SimulationBinaryChromosome.BLOCKSIZE)
					: (offsettedGeneIndex % SimulationBinaryChromosome.BLOCKSIZE - offset);

			StringBuilder allelesBlockStr = extractChromosomePart(newRep, offset, blockIndex);

			final String replacementCharacter = allelesBlockStr.charAt(blockElementIndex) == '0' ? "1" : "0";
			allelesBlockStr.replace(blockElementIndex, blockElementIndex + 1, replacementCharacter);

			long mutatedBlock = Long.parseLong(allelesBlockStr.toString(), 2);

			newRep.set(blockIndex, mutatedBlock);

		}

		SimulationBinaryChromosome newChrom = new SimulationBinaryChromosome(newRep, origChrom.getFitnessCalculator());

		return newChrom;

	}

	private StringBuilder extractChromosomePart(List<Long> newRep, int offset, int blockIndex) {
		StringBuilder allelesBlockStr = new StringBuilder(Long.toBinaryString(newRep.get(blockIndex)));
		int leadingZeroCount = (int) (blockIndex == 1
				? (SimulationBinaryChromosome.BLOCKSIZE - offset - allelesBlockStr.length())
				: (SimulationBinaryChromosome.BLOCKSIZE - allelesBlockStr.length()));
		allelesBlockStr = prependZero(allelesBlockStr.toString(), leadingZeroCount);
		return allelesBlockStr;
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