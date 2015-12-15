/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fireflysemantics.math.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.junit.Assert;

/**
 */
public class TestUtils {
	/**
	 * Collection of static methods used in math unit tests.
	 */
	private TestUtils() {
		super();
	}

	/**
	 * Verifies that expected and actual are within delta, or are both NaN or
	 * infinities of the same sign.
	 */
	public static void assertEquals(double expected, double actual, double delta) {
		Assert.assertEquals(null, expected, actual, delta);
	}

	/**
	 * Verifies that expected and actual are within delta, or are both NaN or
	 * infinities of the same sign.
	 */
	public static void assertEquals(String msg, double expected, double actual, double delta) {
		// check for NaN
		if (Double.isNaN(expected)) {
			Assert.assertTrue(""
					+ actual
					+ " is not NaN.", Double.isNaN(actual));
		} else {
			Assert.assertEquals(msg, expected, actual, delta);
		}
	}

	/**
	 * Verifies that the two arguments are exactly the same, either both NaN or
	 * infinities of same sign, or identical floating point values.
	 */
	public static void assertSame(double expected, double actual) {
		Assert.assertEquals(expected, actual, 0);
	}

	/**
	 * Serializes an object to a bytes array and then recovers the object from
	 * the bytes array. Returns the deserialized object.
	 *
	 * @param o
	 *            object to serialize and recover
	 * @return the recovered, deserialized object
	 */
	public static Object serializeAndRecover(Object o) {
		try {
			// serialize the Object
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream so = new ObjectOutputStream(bos);
			so.writeObject(o);

			// deserialize the Object
			ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
			ObjectInputStream si = new ObjectInputStream(bis);
			return si.readObject();
		} catch (IOException ioe) {
			return null;
		} catch (ClassNotFoundException cnfe) {
			return null;
		}
	}

	/**
	 * Verifies that serialization preserves equals and hashCode. Serializes the
	 * object, then recovers it and checks equals and hash code.
	 *
	 * @param object
	 *            the object to serialize and recover
	 */
	public static void checkSerializedEquality(Object object) {
		Object object2 = serializeAndRecover(object);
		Assert.assertEquals("Equals check", object, object2);
		Assert.assertEquals("HashCode check", object.hashCode(), object2.hashCode());
	}

	/**
	 * Computes the sum of squared deviations of <values> from <target>
	 * 
	 * @param values
	 *            array of deviates
	 * @param target
	 *            value to compute deviations from
	 *
	 * @return sum of squared deviations
	 */
	public static double sumSquareDev(double[] values, double target) {
		double sumsq = 0d;
		for (int i = 0; i < values.length; i++) {
			final double dev = values[i]
					- target;
			sumsq += (dev
					* dev);
		}
		return sumsq;
	}

	/**
	 * Updates observed counts of values in quartiles. counts[0] <-> 1st
	 * quartile ... counts[3] <-> top quartile
	 */
	public static void updateCounts(double value, long[] counts, double[] quartiles) {
		if (value < quartiles[0]) {
			counts[0]++;
		} else if (value > quartiles[2]) {
			counts[3]++;
		} else if (value > quartiles[1]) {
			counts[2]++;
		} else {
			counts[1]++;
		}
	}

	/**
	 * Eliminates points with zero mass from densityPoints and densityValues
	 * parallel arrays. Returns the number of positive mass points and collapses
	 * the arrays so that the first <returned value> elements of the input
	 * arrays represent the positive mass points.
	 */
	public static int eliminateZeroMassPoints(int[] densityPoints, double[] densityValues) {
		int positiveMassCount = 0;
		for (int i = 0; i < densityValues.length; i++) {
			if (densityValues[i] > 0) {
				positiveMassCount++;
			}
		}
		if (positiveMassCount < densityValues.length) {
			int[] newPoints = new int[positiveMassCount];
			double[] newValues = new double[positiveMassCount];
			int j = 0;
			for (int i = 0; i < densityValues.length; i++) {
				if (densityValues[i] > 0) {
					newPoints[j] = densityPoints[i];
					newValues[j] = densityValues[i];
					j++;
				}
			}
			System.arraycopy(newPoints, 0, densityPoints, 0, positiveMassCount);
			System.arraycopy(newValues, 0, densityValues, 0, positiveMassCount);
		}
		return positiveMassCount;
	}
}
