/**
 * This file is part of Palmetto.
 *
 * Palmetto is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Palmetto is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Palmetto.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.aksw.palmetto.weight;

import java.util.Arrays;
import java.util.Collection;

import org.aksw.palmetto.data.SegmentationDefinition;
import org.aksw.palmetto.data.SubsetProbabilities;
import org.aksw.palmetto.subsets.OneOne;
import org.aksw.palmetto.subsets.Segmentator;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@Deprecated
@RunWith(Parameterized.class)
public class MarginalProbabilityBasedWeighterTest {

    private static final double DOUBLE_PRECISION_DELTA = 0.00000001;

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                /*
                 * word1 1 1 1
                 * 
                 * word2 0 1 1
                 * 
                 * word3 0 1 1
                 */
                { new OneOne(), 3,
                        new double[] { 0, 1.0, 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0 },
                        new double[] { 1.0, 1.0, 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0 } },

                /*
                 * word1 0 1 1
                 * 
                 * word2 1 0 1
                 * 
                 * word3 1 1 0
                 */{ new OneOne(), 3,
                        new double[] { 0, 2.0 / 3.0, 2.0 / 3.0, 1.0 / 3.0, 2.0 / 3.0, 1.0 / 3.0, 1.0 / 3.0, 0 },
                        new double[] { 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0 } },
                /*
                 * word1 0 0 0 1
                 * 
                 * word2 0 1 0 1
                 * 
                 * word3 0 0 1 1
                 */
                { new OneOne(), 3, new double[] { 0, 0.25, 0.5, 0.25, 0.5, 0.25, 0.25, 0.25 },
                        new double[] { 0.25, 0.25, 0.5, 0.5, 0.5, 0.5 } } });
    }

    private Segmentator subsetCreator;
    private int wordsetSize;
    private double probabilities[];
    private double expectedWeightings[];
    
    public MarginalProbabilityBasedWeighterTest(Segmentator subsetCreator, int wordsetSize, double[] probabilities,
            double[] expectedWeightings) {
        this.subsetCreator = subsetCreator;
        this.wordsetSize = wordsetSize;
        this.probabilities = probabilities;
        this.expectedWeightings = expectedWeightings;
    }

    @Test
    public void test() {
        SegmentationDefinition subsets = subsetCreator.getSubsetDefinition(wordsetSize);
        SubsetProbabilities subProbs = new SubsetProbabilities(subsets.segments, subsets.conditions, probabilities);
        Weighter weighter = new MarginalProbabilityBasedWeighter();
        Assert.assertArrayEquals(expectedWeightings, weighter.createWeights(subProbs), DOUBLE_PRECISION_DELTA);
    }
}
