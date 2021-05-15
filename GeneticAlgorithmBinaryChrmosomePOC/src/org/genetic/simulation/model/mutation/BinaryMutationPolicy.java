package org.genetic.simulation.model.mutation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.MutationPolicy;
import org.genetic.simulation.model.BinaryChromosome;

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

		if (!(original instanceof BinaryChromosome)) {
			throw new MathIllegalArgumentException(LocalizedFormats.INVALID_BINARY_CHROMOSOME, new Object[0]);
		}

		BinaryChromosome origChrom = (BinaryChromosome) original;
		int alleleCount = origChrom.getLength();
		int offset = (int) (BinaryChromosome.BLOCKSIZE - (alleleCount % BinaryChromosome.BLOCKSIZE));
		int[] oldRep = origChrom.getRepresentation();
		int[] newRep = new int[oldRep.length];
		System.arraycopy(oldRep, 0, newRep, 0, oldRep.length);
		Map<Integer, List<Integer>> blockIntdexes = findMutableBlockIndexes(alleleCount, offset, mutationProbability);

		for (Integer blockIndex : blockIntdexes.keySet()) {
			StringBuilder allelesBlockStr = extractChromosomePart(newRep, offset, blockIndex);
			System.out.println("Before Mutation: " + allelesBlockStr.toString());
			System.out.println("Mutation Indexes : " + blockIntdexes.get(blockIndex));
			for (Integer blockElementIndex : blockIntdexes.get(blockIndex)) {
				String replacementCharacter = allelesBlockStr.charAt(blockElementIndex) == '0' ? "1" : "0";
				allelesBlockStr.replace(blockElementIndex, blockElementIndex + 1, replacementCharacter);
			}
			System.out.println("After Mutation: " + allelesBlockStr.toString());
			int mutatedBlock = Integer.parseInt(allelesBlockStr.toString(), 2);
			newRep[blockIndex] = mutatedBlock;
		}
		BinaryChromosome newChrom = new BinaryChromosome(alleleCount, newRep, origChrom.getFitnessCalculator());

		return newChrom;
	}

	private Map<Integer, List<Integer>> findMutableBlockIndexes(int alleleCount, int offset,
			double mutationProbability) {
		Map<Integer, List<Integer>> blockIndexes = new HashMap<>();
		int noOfAlleleChanges = (int) Math.floor(mutationProbability * (double) alleleCount);
		for (int i = 0; i < noOfAlleleChanges; i++) {
			int geneIndex = (int) (Math.random() * alleleCount);
			int offsettedGeneIndex = geneIndex + offset;

			Integer blockIndex = offsettedGeneIndex / BinaryChromosome.BLOCKSIZE;
			int blockElementIndex = (blockIndex != 0) ? (offsettedGeneIndex % BinaryChromosome.BLOCKSIZE)
					: (offsettedGeneIndex % BinaryChromosome.BLOCKSIZE - offset);
			if (!blockIndexes.containsKey(blockIndex)) {
				blockIndexes.put(blockIndex, new ArrayList<>());
			}
			blockIndexes.get(blockIndex).add(blockElementIndex);
		}
		return blockIndexes;
	}

	private StringBuilder extractChromosomePart(int[] newRep, int offset, int blockIndex) {
		StringBuilder allelesBlockStr = new StringBuilder(Integer.toBinaryString(newRep[blockIndex]));
		int leadingZeroCount = (int) (blockIndex == 0 ? (BinaryChromosome.BLOCKSIZE - offset - allelesBlockStr.length())
				: (BinaryChromosome.BLOCKSIZE - allelesBlockStr.length()));
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