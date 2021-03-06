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
package org.aksw.palmetto.calculations.direct;

import org.aksw.palmetto.data.SubsetProbabilities;

/**
 * This confirmation measure calculates the ratio between the joint probability
 * of W' and W* and the product of the two marginal probabilities. result =
 * P(W',W*)/(P(W')*P(W*))
 * 
 * @author Michael Röder
 * 
 */
public class RatioConfirmationMeasure extends AbstractUndefinedResultHandlingConfirmationMeasure {

    public RatioConfirmationMeasure() {
        super();
    }

    public RatioConfirmationMeasure(double resultIfCalcUndefined) {
        super(resultIfCalcUndefined);
    }

    @Override
    public double[] calculateConfirmationValues(SubsetProbabilities subsetProbabilities) {
        int pos = 0;
        for (int i = 0; i < subsetProbabilities.segments.length; ++i) {
            pos += subsetProbabilities.conditions[i].length;
        }
        double values[] = new double[pos];

        double segmentProbability, conditionProbability, intersectionProbability;
        pos = 0;
        for (int i = 0; i < subsetProbabilities.segments.length; ++i) {
            segmentProbability = subsetProbabilities.probabilities[subsetProbabilities.segments[i]];
            if (segmentProbability > 0) {
                for (int j = 0; j < subsetProbabilities.conditions[i].length; ++j) {
                    conditionProbability = subsetProbabilities.probabilities[subsetProbabilities.conditions[i][j]];
                    intersectionProbability = subsetProbabilities.probabilities[subsetProbabilities.segments[i]
                            | subsetProbabilities.conditions[i][j]];
                    if (conditionProbability > 0) {
                        values[pos] = intersectionProbability / (segmentProbability * conditionProbability);
                    } else {
                        values[pos] = resultIfCalcUndefined;
                    }
                    ++pos;
                }
            } else {
                for (int j = 0; j < subsetProbabilities.conditions[i].length; ++j) {
                    values[pos] = resultIfCalcUndefined;
                    ++pos;
                }
            }
        }
        return values;
    }

    @Override
    public String getName() {
        return "m_r";
    }
}
