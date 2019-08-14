/*
 * Tanaguru - Automated webpage assessment
 * Copyright (C) 2008-2015  Tanaguru.org
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contact us by mail: tanaguru AT tanaguru DOT org
 */
package org.tanaguru.rules.rgaa42019;

import org.tanaguru.entity.audit.TestSolution;
import org.tanaguru.entity.audit.ProcessResult;
import org.tanaguru.rules.rgaa42019.test.Rgaa42019RuleImplementationTestCase;

/**
 * Unit test class for the implementation of the rule 9-2-1 of the referential Rgaa 4-2019.
 *
 * @author edaconceicao
 */
public class Rgaa42019Rule090201Test extends Rgaa42019RuleImplementationTestCase {

    /**
     * Default constructor
     */
    public Rgaa42019Rule090201Test (String testName){
        super(testName);
    }

    @Override
    protected void setUpRuleImplementationClassName() {
        setRuleImplementationClassName(
                "org.tanaguru.rules.rgaa42019.Rgaa42019Rule090201");
    }

    @Override
    protected void setUpWebResourceMap() {
//        addWebResource("Rgaa42019.Test.09.02.01-1Passed-01");
        addWebResource("Rgaa42019.Test.09.02.01-2Failed-01");
        addWebResource("Rgaa42019.Test.09.02.01-3NMI-01");
//        addWebResource("Rgaa42019.Test.09.02.01-4NA-01");
    }

    @Override
    protected void setProcess() {
        //----------------------------------------------------------------------
        //------------------------------1Passed-01------------------------------
        //----------------------------------------------------------------------
//        checkResultIsPassed(processPageTest("Rgaa42019.Test.09.02.01-1Passed-01"), 1);

        //----------------------------------------------------------------------
        //------------------------------2Failed-01------------------------------
        //----------------------------------------------------------------------
        ProcessResult processResult = processPageTest("Rgaa42019.Test.09.02.01-2Failed-01");
        checkResultIsFailed(processResult, 43, 6);

        //----------------------------------------------------------------------
        //------------------------------3NMI-01---------------------------------
        //----------------------------------------------------------------------
        processResult = processPageTest("Rgaa42019.Test.09.02.01-3NMI-01");
        checkResultIsPreQualified(processResult, 42,6);


        //----------------------------------------------------------------------
        //------------------------------4NA-01------------------------------
        //----------------------------------------------------------------------
//        checkResultIsNotApplicable(processPageTest("Rgaa42019.Test.09.02.01-4NA-01"));
    }
}
