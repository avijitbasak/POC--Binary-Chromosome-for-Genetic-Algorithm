package org.genetic.simulation.model;

import org.apache.commons.math3.random.JDKRandomGenerator;

public class Test {

//012345670123456701234567	

	public static void main(String[] args) {

		signedDataType(11);
		//unsignedDataType();

		// String value = "01010101010101010101010101010101";
		// Long value1 = Long.parseLong(value, 2);
		// System.out.println(value);
		// System.out.println(Long.toBinaryString(~value1));
		// test1();
	}

	private static void signedDataType(long len) {
		long geneIndex = 0;
		long blockSize = 7;
		long offset = blockSize - (len % blockSize);
		geneIndex += offset;

		int blockIndex = (int) (geneIndex % blockSize == 0 ? geneIndex / blockSize : geneIndex / blockSize) + 1;
		int blockElementIndex = (int) (geneIndex % blockSize) + 1;

		System.out.println(blockIndex + ", " + blockElementIndex);
	}

	private static void unsignedDataType() {
		long geneIndex = 20;
		long blockSize = 8;

		int blockIndex = (int) (geneIndex % blockSize == 0 ? geneIndex / blockSize : geneIndex / blockSize) + 1;
		int blockElementIndex = (int) (geneIndex % blockSize);

		System.out.println(blockIndex + ", " + blockElementIndex);
	}

	private static void test1() {
		int position = 62;
		for (int i = 0; i < 10; i++) {
			long val = new JDKRandomGenerator().nextLong() >>> 1;
			System.out.println(Long.toBinaryString(val));
			// long val = Long.MAX_VALUE / 2;
			// System.out.println(val);
			// System.out.println(Long.MIN_VALUE / 2);
			// System.out.println(val - Long.MIN_VALUE / 2);
			System.out.println(Long.toBinaryString((val >>> (63 - position))));
			long allele = (val >>> (63 - position)) & 1;
			System.out.println(Long.toBinaryString(allele));
		}
	}

}
