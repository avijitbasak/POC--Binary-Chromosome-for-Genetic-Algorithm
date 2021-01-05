package org.genetic.simulation.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.genetics.AbstractListChromosome;
import org.apache.commons.math3.genetics.InvalidRepresentationException;

/**
 * This class represents a metadata based optimum implementation of Binary
 * chromosome.
 * 
 * @author AvijitBasak
 *
 */
public class SimulationBinaryChromosome extends AbstractListChromosome<Long> {

	private FitnessCalculator fitnessCalculator;

	private static final long BLOCKSIZE = 63;

	private int rank;

	public SimulationBinaryChromosome(int length, FitnessCalculator fitnessCalculator, double probabilityOfSet)
			throws InvalidRepresentationException {
		super(randomBinaryRepresentation(length, probabilityOfSet));
		this.fitnessCalculator = fitnessCalculator;
	}

	public SimulationBinaryChromosome(List<Long> alleles, FitnessCalculator fitnessCalculator)
			throws InvalidRepresentationException {
		super(alleles);
		this.fitnessCalculator = fitnessCalculator;
	}

	public static List<Long> randomBinaryRepresentation(int length, double setProbability) {
		List<Long> rList = new ArrayList<>();
		rList.add(Long.valueOf(length));

		StringBuilder chromosomeTmp = new StringBuilder();
		StringBuilder test = new StringBuilder();

		int bitCount = (int) (length < BLOCKSIZE ? BLOCKSIZE - length : BLOCKSIZE - length % BLOCKSIZE);

		/*
		 * This block of code stores the generated string representation of
		 * chromosome to the list of longs in big endian order.
		 */
		for (int j = 0; j < length; j++) {
			double value = Math.random();
			if (value < setProbability) {
				chromosomeTmp.append(1);
			} else {
				chromosomeTmp.append(0);
			}

			if (++bitCount == BLOCKSIZE) {
				test.append(chromosomeTmp);
				rList.add(Long.parseLong(chromosomeTmp.toString(), 2));
				bitCount = 0;
				chromosomeTmp.delete(0, chromosomeTmp.length());
			}
		}

		return rList;
	}

	@Override
	public List<Long> getRepresentation() {
		return super.getRepresentation();
	}

	@Override
	public double fitness() {
		return fitnessCalculator.calculate(this);
	}

	@Override
	protected void checkValidity(List<Long> chromosomeRepresentation) throws InvalidRepresentationException {
		// Not Required.
	}

	@Override
	public AbstractListChromosome<Long> newFixedLengthChromosome(List<Long> chromosomeRepresentation) {
		SimulationBinaryChromosome simulationBinaryChromosome = new SimulationBinaryChromosome(chromosomeRepresentation,
				fitnessCalculator);
		return simulationBinaryChromosome;
	}

	public String getStringRepresentation() {
		List<Long> alleles = getRepresentation();
		long length = alleles.get(0);
		if ((alleles.size() - 1) * BLOCKSIZE > Integer.MAX_VALUE) {
			return "Chromosome too Large to represent as string";
		}

		StringBuilder bitString = new StringBuilder();

		long start = length < BLOCKSIZE ? (BLOCKSIZE - length) : BLOCKSIZE - (length % BLOCKSIZE);
		String first = Long.toBinaryString(alleles.get(1));
		if (start < (BLOCKSIZE - first.length())) {
			appendZero(bitString, (int) (BLOCKSIZE - first.length() - start));
		}
		bitString.append(first);
		String data = null;

		for (long i = 2; i < alleles.size(); i++) {
			data = Long.toBinaryString(alleles.get((int) i));
			appendZero(bitString, (int) (BLOCKSIZE - data.length()));
			bitString.append(data);
		}

		return bitString.toString();
	}

	private void appendZero(StringBuilder chromosomeStr, int count) {
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

	@Override
	public int getLength() {
		throw new RuntimeException("Not supported");
	}

	public long getChromosomeLength() {
		return getRepresentation().get(0);
	}

}