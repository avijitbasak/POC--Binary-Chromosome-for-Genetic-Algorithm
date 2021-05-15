package org.genetic.simulation.model.crossover;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ChromosomePair;
import org.apache.commons.math3.genetics.CrossoverPolicy;
import org.genetic.simulation.model.BinaryChromosome;

public class OnePointBinaryCrossoverPolicy implements CrossoverPolicy {

	@Override
	public ChromosomePair crossover(Chromosome first, Chromosome second) throws MathIllegalArgumentException {

		System.out.println("Inside crossover");
		BinaryChromosome chromosome1 = (BinaryChromosome) first;
		BinaryChromosome chromosome2 = (BinaryChromosome) second;
		int[] representation1 = chromosome1.getRepresentation();
		int[] representation2 = chromosome2.getRepresentation();

		if (chromosome1.getLength() != chromosome2.getLength()) {
			throw new RuntimeException("");
		}

		int alleleCount = chromosome1.getLength();
		int crossoverIndex = 1 + (int) Math.floor((Math.random() * (alleleCount - 2)));
		System.out.println("crossoverIndex : " + crossoverIndex);

		int offset = (int) (BinaryChromosome.BLOCKSIZE - (alleleCount % BinaryChromosome.BLOCKSIZE));
		int offsettedCrossoverIndex = crossoverIndex + offset;
		System.out.println("offset : " + offset);
		System.out.println("offsettedCrossoverIndex : " + offsettedCrossoverIndex);

		int blockIndex = offsettedCrossoverIndex / BinaryChromosome.BLOCKSIZE;
		int blockElementIndex = (blockIndex != 0) ? (offsettedCrossoverIndex % BinaryChromosome.BLOCKSIZE)
				: (offsettedCrossoverIndex % BinaryChromosome.BLOCKSIZE - offset);
		System.out.println("blockIndex : " + blockIndex);
		System.out.println("blockElementIndex : " + blockElementIndex);

		int allelesBlock1 = representation1[blockIndex];
		StringBuilder allelesBlockStr1 = new StringBuilder(Integer.toBinaryString(allelesBlock1));
		int leadingZeroCount = blockIndex == 0 ? (BinaryChromosome.BLOCKSIZE - offset - allelesBlockStr1.length())
				: (BinaryChromosome.BLOCKSIZE - allelesBlockStr1.length());
		allelesBlockStr1 = prependZero(allelesBlockStr1.toString(), leadingZeroCount);

		int allelesBlock2 = representation2[blockIndex];
		StringBuilder allelesBlockStr2 = new StringBuilder(Integer.toBinaryString(allelesBlock2));
		leadingZeroCount = (blockIndex == 0) ? (BinaryChromosome.BLOCKSIZE - offset - allelesBlockStr2.length())
				: (BinaryChromosome.BLOCKSIZE - allelesBlockStr2.length());
		allelesBlockStr2 = prependZero(allelesBlockStr2.toString(), leadingZeroCount);

		System.out.println("allelesBlockStr1 : " + allelesBlockStr1);
		System.out.println("allelesBlockStr2 : " + allelesBlockStr2);

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
		System.out.println("allelesBlockStr1C : " + allelesBlockStr1C);
		System.out.println("allelesBlockStr2C : " + allelesBlockStr2C);

		int[] representation1C = new int[representation1.length];
		int[] representation2C = new int[representation2.length];
		if (blockIndex > 0) {
			System.arraycopy(representation1, 0, representation1C, 0, blockIndex);
			System.arraycopy(representation2, 0, representation2C, 0, blockIndex);
		}
		representation1C[blockIndex] = Integer.parseInt(allelesBlockStr1C, 2);
		representation2C[blockIndex] = Integer.parseInt(allelesBlockStr2C, 2);
		if (blockIndex + 1 < representation2.length) {
			System.arraycopy(representation2, blockIndex + 1, representation1C, blockIndex + 1,
					representation2.length - 1 - blockIndex);
			System.arraycopy(representation1, blockIndex + 1, allelesBlockStr2, blockIndex + 1,
					representation1.length - 1 - blockIndex);
		}

		BinaryChromosome chromosome1C = new BinaryChromosome(alleleCount, representation1C,
				chromosome1.getFitnessCalculator());
		BinaryChromosome chromosome2C = new BinaryChromosome(alleleCount, representation2C,
				chromosome2.getFitnessCalculator());

		System.out.println("After crossover");
		System.out.println(chromosome1C.toString());
		System.out.println(chromosome2C.toString());

		ChromosomePair pair = new ChromosomePair(chromosome1C, chromosome2C);

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
