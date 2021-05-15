package org.genetic.simulation.model;

import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.InvalidRepresentationException;

public class BinaryChromosome extends Chromosome {

	public static final int BLOCKSIZE = 31;

	private FitnessCalculator fitnessCalculator;

	private int rank;

	private int[] alleles;

	private int length;

	private static final long MAX_LENGTH = Integer.MAX_VALUE;

	public BinaryChromosome(int length, FitnessCalculator fitnessCalculator) throws InvalidRepresentationException {
		checkLength();
		this.length = length;
		this.alleles = new int[calculateArraySize()];
		this.fitnessCalculator = fitnessCalculator;
		initRandomBinaryRepresentation();
	}

	public BinaryChromosome(int length, int[] alleles, FitnessCalculator fitnessCalculator)
			throws InvalidRepresentationException {
		checkLength();
		this.length = length;
		this.alleles = alleles;
		this.fitnessCalculator = fitnessCalculator;
	}

	private void checkLength() {
		if (this.length > MAX_LENGTH) {
			throw new IllegalArgumentException("Maximum length of a binary chromosome can be " + MAX_LENGTH);
		}
	}

	private int calculateArraySize() {
		if (length % BLOCKSIZE == 0) {
			return length / BLOCKSIZE;
		} else {
			return length / BLOCKSIZE + 1;
		}
	}

	public void initRandomBinaryRepresentation() {

		if (length > Integer.MAX_VALUE * BLOCKSIZE) {
			throw new IllegalArgumentException(
					"Length of chromosome cannot be more than " + Integer.MAX_VALUE * BLOCKSIZE);
		}

		long firstElementAlleleCount = length % BLOCKSIZE;

		if (firstElementAlleleCount != 0) {
			alleles[0] = (int) (Math.random() * (Math.pow(2, firstElementAlleleCount) - 1));
		}

		for (int i = 1; i < alleles.length; i++) {
			alleles[i] = (int) (Math.random() * Integer.MAX_VALUE);
		}
	}

	public int[] getRepresentation() {
		return alleles;
	}

	@Override
	public double fitness() {
		return fitnessCalculator.calculate(this);
	}

	public String getStringRepresentation() {

		StringBuilder bitString = new StringBuilder();

		long start = length < BLOCKSIZE ? (BLOCKSIZE - length) : BLOCKSIZE - (length % BLOCKSIZE);
		String first = Integer.toBinaryString(alleles[0]);
		if (start < (BLOCKSIZE - first.length())) {
			prependZero(bitString, (int) (BLOCKSIZE - first.length() - start));
		}
		bitString.append(first);
		String data = null;

		for (int i = 1; i < alleles.length; i++) {
			data = Integer.toBinaryString(alleles[i]);
			prependZero(bitString, (int) (BLOCKSIZE - data.length()));
			bitString.append(data);
		}

		return bitString.toString();
	}

	private void prependZero(StringBuilder chromosomeStr, int count) {
		for (int i = 0; i < count; i++) {
			chromosomeStr.append(0);
		}
	}

	@Override
	public String toString() {
		return getStringRepresentation() + " - " + String.format("%.6f", fitness());
	}

	public void setFitnessCalculator(FitnessCalculator fitnessCalculator) {
		this.fitnessCalculator = fitnessCalculator;
	}

	public FitnessCalculator getFitnessCalculator() {
		return fitnessCalculator;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public int getLength() {
		return length;
	}

}
