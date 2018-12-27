/*
 * Tanaguru - Automated webpage assessment
 * Copyright (C) 2008-2017  Tanaguru.org
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
package org.tanaguru.rules.rgaa32017;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.tanaguru.entity.audit.*;
import static org.tanaguru.entity.audit.TestSolution.FAILED;
import static org.tanaguru.entity.audit.TestSolution.NEED_MORE_INFO;
import static org.tanaguru.rules.keystore.AttributeStore.ABSENT_ATTRIBUTE_VALUE;
import org.tanaguru.rules.rgaa32017.test.Rgaa32017RuleImplementationTestCase;
import static org.tanaguru.rules.keystore.AttributeStore.ALT_ATTR;
import static org.tanaguru.rules.keystore.AttributeStore.SRC_ATTR;
import static org.tanaguru.rules.keystore.AttributeStore.TITLE_ATTR;
import static org.tanaguru.rules.keystore.AttributeStore.ARIA_LABEL_ATTR;
import static org.tanaguru.rules.keystore.AttributeStore.ARIA_LABELLEDBY_ATTR;
import static org.tanaguru.rules.keystore.HtmlElementStore.IMG_ELEMENT;
import static org.tanaguru.rules.keystore.MarkerStore.DECORATIVE_IMAGE_MARKER;
import static org.tanaguru.rules.keystore.MarkerStore.INFORMATIVE_IMAGE_MARKER;
import static org.tanaguru.rules.keystore.RemarkMessageStore.CHECK_ALT_PERTINENCE_OF_INFORMATIVE_IMG_MSG;
import static org.tanaguru.rules.keystore.RemarkMessageStore.CHECK_NATURE_OF_IMAGE_AND_ALT_PERTINENCE_MSG;
import static org.tanaguru.rules.keystore.RemarkMessageStore.CHECK_NATURE_OF_IMAGE_WITH_NOT_PERTINENT_ALT_MSG;
import static org.tanaguru.rules.keystore.RemarkMessageStore.IMG_TEXT_NOT_IDENTICAL_TO_ALT_WITH_ARIA_LABELLEDBY_MSG;
import static org.tanaguru.rules.keystore.RemarkMessageStore.INFORMATIVE_IMG_TEXT_NOT_IDENTICAL_TO_ALT_WITH_ARIA_LABELLEDBY_MSG;
import static org.tanaguru.rules.keystore.RemarkMessageStore.NOT_PERTINENT_ALT_MSG;
import static org.tanaguru.rules.keystore.RemarkMessageStore.TITLE_NOT_IDENTICAL_TO_ALT_MSG;
import static org.tanaguru.rules.keystore.RemarkMessageStore.ARIA_LABEL_NOT_IDENTICAL_TO_ALT_MSG;
import static org.tanaguru.rules.keystore.RemarkMessageStore.ARIA_LABELLEDBY_NOT_IDENTICAL_TO_ALT_MSG;

/**
 * Unit test class for the implementation of the rule 1-3-1 of the referential Rgaa 3-2017.
 *
 * @author jkowalczyk
 */
public class Rgaa32017Rule010301Test extends Rgaa32017RuleImplementationTestCase {

    /**
     * Default constructor
     * @param testName
     */
    public Rgaa32017Rule010301Test (String testName){
        super(testName);
    }

    @Override
    protected void setUpRuleImplementationClassName() {
        setRuleImplementationClassName("org.tanaguru.rules.rgaa32017.Rgaa32017Rule010301");
    }

    @Override
    protected void setUpWebResourceMap() {
        addWebResource("Rgaa32017.Test.01.03.01-2Failed-01");
        addWebResource("Rgaa32017.Test.01.03.01-2Failed-02");
        addWebResource("Rgaa32017.Test.01.03.01-2Failed-03");
        addWebResource("Rgaa32017.Test.01.03.01-2Failed-04",
                    createParameter("Rules", INFORMATIVE_IMAGE_MARKER, "class-informative-image"));
        addWebResource("Rgaa32017.Test.01.03.01-2Failed-05",
                    createParameter("Rules", INFORMATIVE_IMAGE_MARKER, "class-informative-image"));
        addWebResource("Rgaa32017.Test.01.03.01-2Failed-06",
                    createParameter("Rules", INFORMATIVE_IMAGE_MARKER, "id-informative-image"));
        addWebResource("Rgaa32017.Test.01.03.01-2Failed-07",
                	createParameter("Rules", INFORMATIVE_IMAGE_MARKER, "class-informative-image"),
                	createParameter("Rules", "DECORATIVE_IMAGE_MARKER", "class-decorative-image"));
        addWebResource("Rgaa32017.Test.01.03.01-2Failed-08",
                	createParameter("Rules", INFORMATIVE_IMAGE_MARKER, "id-informative-image"));
        addWebResource("Rgaa32017.Test.01.03.01-2Failed-09",
                    createParameter("Rules", INFORMATIVE_IMAGE_MARKER, "class-informative-image"),
                    createParameter("Rules", "DECORATIVE_IMAGE_MARKER", "class-decorative-image"));
        addWebResource("Rgaa32017.Test.01.03.01-2Failed-10",
                    createParameter("Rules", INFORMATIVE_IMAGE_MARKER, "id-informative-image"));
        addWebResource("Rgaa32017.Test.01.03.01-2Failed-11",
                    createParameter("Rules", INFORMATIVE_IMAGE_MARKER, "id-informative-image"));
        addWebResource("Rgaa32017.Test.01.03.01-2Failed-12");
        addWebResource("Rgaa32017.Test.01.03.01-2Failed-13",
            	createParameter("Rules", INFORMATIVE_IMAGE_MARKER, "class-informative-image"));
        addWebResource("Rgaa32017.Test.01.03.01-2Failed-14",
            	createParameter("Rules", INFORMATIVE_IMAGE_MARKER, "id-informative-image"));
        addWebResource("Rgaa32017.Test.01.03.01-2Failed-15",
                	createParameter("Rules", INFORMATIVE_IMAGE_MARKER, "id-informative-image"));
        addWebResource("Rgaa32017.Test.01.03.01-2Failed-16",
                	createParameter("Rules", INFORMATIVE_IMAGE_MARKER, "id-informative-image"));
        addWebResource("Rgaa32017.Test.01.03.01-2Failed-17",
            		createParameter("Rules", INFORMATIVE_IMAGE_MARKER, "id-informative-image"));
        addWebResource("Rgaa32017.Test.01.03.01-2Failed-18",
            		createParameter("Rules", INFORMATIVE_IMAGE_MARKER, "id-informative-image"));
        addWebResource("Rgaa32017.Test.01.03.01-2Failed-19",
            		createParameter("Rules", INFORMATIVE_IMAGE_MARKER, "id-informative-image"));
        addWebResource("Rgaa32017.Test.01.03.01-2Failed-20",
            		createParameter("Rules", INFORMATIVE_IMAGE_MARKER, "id-informative-image"));
        addWebResource("Rgaa32017.Test.01.03.01-2Failed-21");
        addWebResource("Rgaa32017.Test.01.03.01-3NMI-01",
                    createParameter("Rules", INFORMATIVE_IMAGE_MARKER, "class-informative-image"));
        addWebResource("Rgaa32017.Test.01.03.01-3NMI-02");
        addWebResource("Rgaa32017.Test.01.03.01-3NMI-03",
                    createParameter("Rules", INFORMATIVE_IMAGE_MARKER, "id-informative-image"));
        addWebResource("Rgaa32017.Test.01.03.01-3NMI-04");
        addWebResource("Rgaa32017.Test.01.03.01-3NMI-05",
                createParameter("Rules", INFORMATIVE_IMAGE_MARKER, "id-informative-image"));
        addWebResource("Rgaa32017.Test.01.03.01-3NMI-06",
                createParameter("Rules", INFORMATIVE_IMAGE_MARKER, "id-informative-image"));
        addWebResource("Rgaa32017.Test.01.03.01-3NMI-07");
        addWebResource("Rgaa32017.Test.01.03.01-3NMI-08");
        addWebResource("Rgaa32017.Test.01.03.01-3NMI-09");
        addWebResource("Rgaa32017.Test.01.03.01-3NMI-10",
                createParameter("Rules", INFORMATIVE_IMAGE_MARKER, "id-informative-image"));
        addWebResource("Rgaa32017.Test.01.03.01-3NMI-11",
                createParameter("Rules", INFORMATIVE_IMAGE_MARKER, "id-informative-image"));
        addWebResource("Rgaa32017.Test.01.03.01-3NMI-12",
                createParameter("Rules", INFORMATIVE_IMAGE_MARKER, "class-informative-image"));
        addWebResource("Rgaa32017.Test.01.03.01-3NMI-13");
        addWebResource("Rgaa32017.Test.01.03.01-3NMI-14",
                createParameter("Rules", INFORMATIVE_IMAGE_MARKER, "id-informative-image"));
        addWebResource("Rgaa32017.Test.01.03.01-3NMI-15");
        addWebResource("Rgaa32017.Test.01.03.01-3NMI-16",
    		  	createParameter("Rules", INFORMATIVE_IMAGE_MARKER, "id-informative-image"));
        addWebResource("Rgaa32017.Test.01.03.01-3NMI-17",
    		  	createParameter("Rules", INFORMATIVE_IMAGE_MARKER, "id-informative-image"));
        addWebResource("Rgaa32017.Test.01.03.01-3NMI-18",
    		  createParameter("Rules", INFORMATIVE_IMAGE_MARKER, "id-informative-image"));
        addWebResource("Rgaa32017.Test.01.03.01-3NMI-19");
        addWebResource("Rgaa32017.Test.01.03.01-3NMI-20");
        addWebResource("Rgaa32017.Test.01.03.01-3NMI-21");
        addWebResource("Rgaa32017.Test.01.03.01-3NMI-22",
        		createParameter("Rules", INFORMATIVE_IMAGE_MARKER, "id-informative-image"));
        addWebResource("Rgaa32017.Test.01.03.01-3NMI-23",
        		createParameter("Rules", INFORMATIVE_IMAGE_MARKER, "id-informative-image"));
        addWebResource("Rgaa32017.Test.01.03.01-3NMI-24",
        		createParameter("Rules", INFORMATIVE_IMAGE_MARKER, "id-informative-image"));
        addWebResource("Rgaa32017.Test.01.03.01-4NA-01");
        addWebResource("Rgaa32017.Test.01.03.01-4NA-02");
        addWebResource("Rgaa32017.Test.01.03.01-4NA-03");
        addWebResource("Rgaa32017.Test.01.03.01-4NA-04");
        addWebResource("Rgaa32017.Test.01.03.01-4NA-05",
                    createParameter("Rules", DECORATIVE_IMAGE_MARKER, "class-decorative-image"));

    }

    @Override
    protected void setProcess() {
        //----------------------------------------------------------------------
        //------------------------------2Failed-01------------------------------
        //----------------------------------------------------------------------
        ProcessResult processResult = processPageTest("Rgaa32017.Test.01.03.01-2Failed-01");
        checkResultIsFailed(processResult,1,1);
        checkRemarkIsPresent(
                processResult,
                FAILED,
                NOT_PERTINENT_ALT_MSG,
                IMG_ELEMENT,
                1,
                new ImmutablePair(ALT_ATTR, "mock-image"),
                new ImmutablePair(SRC_ATTR, "mock-image"));
        
        //----------------------------------------------------------------------
        //------------------------------2Failed-02------------------------------
        //----------------------------------------------------------------------
        processResult = processPageTest("Rgaa32017.Test.01.03.01-2Failed-02");
        checkResultIsFailed(processResult,1,1);
        checkRemarkIsPresent(
                processResult,
                FAILED,
                NOT_PERTINENT_ALT_MSG,
                IMG_ELEMENT,
                1,
                new ImmutablePair(ALT_ATTR, "image.gif"),
                new ImmutablePair(SRC_ATTR, "mock-image"));
        
        //----------------------------------------------------------------------
        //------------------------------2Failed-03------------------------------
        //----------------------------------------------------------------------
        processResult = processPageTest("Rgaa32017.Test.01.03.01-2Failed-03");
        checkResultIsFailed(processResult,1,1);
        checkRemarkIsPresent(
                processResult,
                FAILED,
                NOT_PERTINENT_ALT_MSG,
                IMG_ELEMENT,
                1,
                new ImmutablePair(ALT_ATTR, "--><--"),
                new ImmutablePair(SRC_ATTR, "mock-image.jpg"));
        
        //----------------------------------------------------------------------
        //------------------------------2Failed-04------------------------------
        //----------------------------------------------------------------------
        processResult = processPageTest("Rgaa32017.Test.01.03.01-2Failed-04");
        checkResultIsFailed(processResult,1,1);
        checkRemarkIsPresent(
                processResult,
                FAILED,
                NOT_PERTINENT_ALT_MSG,
                IMG_ELEMENT,
                1,
                new ImmutablePair(ALT_ATTR, "mock-image"),
                new ImmutablePair(SRC_ATTR, "images/mock-image"));
        
        //----------------------------------------------------------------------
        //------------------------------2Failed-05------------------------------
        //----------------------------------------------------------------------
        processResult = processPageTest("Rgaa32017.Test.01.03.01-2Failed-05");
        checkResultIsFailed(processResult,1,1);
        checkRemarkIsPresent(
                processResult,
                FAILED,
                NOT_PERTINENT_ALT_MSG,
                IMG_ELEMENT,
                1,
                new ImmutablePair(ALT_ATTR, "image.bmp"),
                new ImmutablePair(SRC_ATTR, "mock-image"));
        
        //----------------------------------------------------------------------
        //------------------------------2Failed-06------------------------------
        //----------------------------------------------------------------------
        processResult = processPageTest("Rgaa32017.Test.01.03.01-2Failed-06");
        checkResultIsFailed(processResult,1,1);
        checkRemarkIsPresent(
                processResult,
                FAILED,
                NOT_PERTINENT_ALT_MSG,
                IMG_ELEMENT,
                1,
                new ImmutablePair(ALT_ATTR, "#!/;'(|"),
                new ImmutablePair(SRC_ATTR, "mock-image.jpg"));
        
        //----------------------------------------------------------------------
        //------------------------------2Failed-07------------------------------
        //----------------------------------------------------------------------
        processResult = processPageTest("Rgaa32017.Test.01.03.01-2Failed-07");
        checkResultIsFailed(processResult,4,4);
        checkRemarkIsPresent(
                processResult,
                NEED_MORE_INFO,
                CHECK_ALT_PERTINENCE_OF_INFORMATIVE_IMG_MSG,
                IMG_ELEMENT,
                1,
                new ImmutablePair(ALT_ATTR, ""),
                new ImmutablePair(SRC_ATTR, "mock-image1.jpg"));
        checkRemarkIsPresent(
                processResult,
                NEED_MORE_INFO,
                CHECK_ALT_PERTINENCE_OF_INFORMATIVE_IMG_MSG,
                IMG_ELEMENT,
                2,
                new ImmutablePair(ALT_ATTR, "Informative image alternative"),
                new ImmutablePair(SRC_ATTR, "mock-image2.jpg"));
        checkRemarkIsPresent(
                processResult,
                FAILED,
                NOT_PERTINENT_ALT_MSG,
                IMG_ELEMENT,
                3,
                new ImmutablePair(ALT_ATTR, "mock-image3"),
                new ImmutablePair(SRC_ATTR, "mock-image3"));
        checkRemarkIsPresent(
                processResult,
                NEED_MORE_INFO,
                CHECK_NATURE_OF_IMAGE_AND_ALT_PERTINENCE_MSG,
                IMG_ELEMENT,
                4,
                new ImmutablePair(ALT_ATTR, "not identified image alternative"),
                new ImmutablePair(SRC_ATTR, "mock-image4.jpg"));
        

        //----------------------------------------------------------------------
        //------------------------------2Failed-08------------------------------
        //----------------------------------------------------------------------
        processResult = processPageTest("Rgaa32017.Test.01.03.01-2Failed-08");
        checkResultIsFailed(processResult,1,2);
        checkRemarkIsPresent(
                processResult,
                NEED_MORE_INFO,
                CHECK_ALT_PERTINENCE_OF_INFORMATIVE_IMG_MSG,
                IMG_ELEMENT,
                1,
                new ImmutablePair(ALT_ATTR, "Alternative"),
                new ImmutablePair(SRC_ATTR, "mock-image.jpg"));
        checkRemarkIsPresent(
                processResult,
                FAILED,
                TITLE_NOT_IDENTICAL_TO_ALT_MSG,
                IMG_ELEMENT,
                2,
                new ImmutablePair(ALT_ATTR, "Alternative"),
                new ImmutablePair(TITLE_ATTR, "Title"),
                new ImmutablePair(SRC_ATTR, "mock-image.jpg"));
        
        
        //----------------------------------------------------------------------
        //------------------------------2Failed-09------------------------------
        //----------------------------------------------------------------------
        processResult = processPageTest("Rgaa32017.Test.01.03.01-2Failed-09");
        checkResultIsFailed(processResult,1,1);
        checkRemarkIsPresent(
                processResult,
                FAILED,
                NOT_PERTINENT_ALT_MSG,
                IMG_ELEMENT,
                1,
                new ImmutablePair(ALT_ATTR, "+-*/"),
                new ImmutablePair(SRC_ATTR, "mock-image.jpg")); 
        
       
        //----------------------------------------------------------------------
        //------------------------------2Failed-10------------------------------
        //----------------------------------------------------------------------
        processResult = processPageTest("Rgaa32017.Test.01.03.01-2Failed-10");
        checkResultIsFailed(processResult,1,1);
        checkRemarkIsPresent(
                processResult,
                FAILED,
                NOT_PERTINENT_ALT_MSG,
                IMG_ELEMENT,
                1,
                new ImmutablePair(ALT_ATTR, "mock-image"),
                new ImmutablePair(SRC_ATTR, "images/mock-image"));

        	
        //----------------------------------------------------------------------
        //------------------------------2Failed-11------------------------------
        //----------------------------------------------------------------------
        processResult = processPageTest("Rgaa32017.Test.01.03.01-2Failed-11");
        checkResultIsFailed(processResult,1,1);
        checkRemarkIsPresent(
                processResult,
                FAILED,
                NOT_PERTINENT_ALT_MSG,
                IMG_ELEMENT,
                1,
                new ImmutablePair(ALT_ATTR, "image.bmp"),
                new ImmutablePair(SRC_ATTR, "mock-image"));

        
        //----------------------------------------------------------------------
        //------------------------------2Failed-12------------------------------
        //----------------------------------------------------------------------
        processResult = processPageTest("Rgaa32017.Test.01.03.01-2Failed-12");
        checkResultIsFailed(processResult,1,1);
        checkRemarkIsPresent(
                processResult,
                FAILED,
                NOT_PERTINENT_ALT_MSG,
                IMG_ELEMENT,
                1,
                new ImmutablePair(ALT_ATTR, "#!/;'(|"),
                new ImmutablePair(SRC_ATTR, "mock-image.jpg"));
        
        
        //----------------------------------------------------------------------
        //------------------------------2Failed-13------------------------------
        //----------------------------------------------------------------------
        processResult = processPageTest("Rgaa32017.Test.01.03.01-2Failed-13");
        checkResultIsFailed(processResult,3,3);
        checkRemarkIsPresent(
                processResult,
                NEED_MORE_INFO,
                CHECK_ALT_PERTINENCE_OF_INFORMATIVE_IMG_MSG,
                IMG_ELEMENT,
                1,
                new ImmutablePair(ALT_ATTR, "Informative image alternative"),
                new ImmutablePair(SRC_ATTR, "mock-image2.jpg"));
        checkRemarkIsPresent(
                processResult,
                FAILED,
                NOT_PERTINENT_ALT_MSG,
                IMG_ELEMENT,
                2,
                new ImmutablePair(ALT_ATTR, "mock-image3"),
                new ImmutablePair(SRC_ATTR, "mock-image3"));
        checkRemarkIsPresent(
                processResult,
                NEED_MORE_INFO,
                CHECK_NATURE_OF_IMAGE_AND_ALT_PERTINENCE_MSG,
                IMG_ELEMENT,
                3,
                new ImmutablePair(ALT_ATTR, "not identified image alternative"),
                new ImmutablePair(SRC_ATTR, "mock-image4.jpg"));
        
        
        //----------------------------------------------------------------------
        //------------------------------2Failed-14------------------------------
        //----------------------------------------------------------------------
        processResult = processPageTest("Rgaa32017.Test.01.03.01-2Failed-14");
        checkResultIsFailed(processResult,1,2);
        checkRemarkIsPresent(
                processResult,
                NEED_MORE_INFO,
                CHECK_ALT_PERTINENCE_OF_INFORMATIVE_IMG_MSG,
                IMG_ELEMENT,
                1,
                new ImmutablePair(ALT_ATTR, "Alternative"),
                new ImmutablePair(SRC_ATTR, "mock-image.jpg"));
        checkRemarkIsPresent(
                processResult,
                FAILED,
                ARIA_LABEL_NOT_IDENTICAL_TO_ALT_MSG,
                IMG_ELEMENT,
                2,
                new ImmutablePair(ALT_ATTR, "Alternative"),
                new ImmutablePair(ARIA_LABEL_ATTR, "Aria-Label"),
                new ImmutablePair(SRC_ATTR, "mock-image.jpg"));
        
        
        //----------------------------------------------------------------------
        //------------------------------2Failed-15------------------------------
        //----------------------------------------------------------------------
        processResult = processPageTest("Rgaa32017.Test.01.03.01-2Failed-15");
        checkResultIsFailed(processResult,1,2);
        checkRemarkIsPresent(
                processResult,
                NEED_MORE_INFO,
                CHECK_ALT_PERTINENCE_OF_INFORMATIVE_IMG_MSG,
                IMG_ELEMENT,
                1,
                new ImmutablePair(ALT_ATTR, "Alternative"),
                new ImmutablePair(SRC_ATTR, "mock-image.jpg"));
        checkRemarkIsPresent(
                processResult,
                FAILED,
                INFORMATIVE_IMG_TEXT_NOT_IDENTICAL_TO_ALT_WITH_ARIA_LABELLEDBY_MSG,
                IMG_ELEMENT,
                2,
                new ImmutablePair(ALT_ATTR, "Alternative"),
                new ImmutablePair(ARIA_LABELLEDBY_ATTR, "FAILED 18"));
        
        
        //----------------------------------------------------------------------
        //------------------------------2Failed-16------------------------------
        //----------------------------------------------------------------------
        processResult = processPageTest("Rgaa32017.Test.01.03.01-2Failed-16");
        checkResultIsFailed(processResult,1,1);
        checkRemarkIsPresent(
                processResult,
                FAILED,
                NOT_PERTINENT_ALT_MSG,
                IMG_ELEMENT,
                1,
                new ImmutablePair(ALT_ATTR, "mock-image.jpg"),
                new ImmutablePair(SRC_ATTR, "mock-image.jpg"));
        
        
        //----------------------------------------------------------------------
        //------------------------------2Failed-17------------------------------
        //----------------------------------------------------------------------
        processResult = processPageTest("Rgaa32017.Test.01.03.01-2Failed-17");
        checkResultIsFailed(processResult,1,1);
        checkRemarkIsPresent(
                processResult,
                FAILED,
                NOT_PERTINENT_ALT_MSG,
                IMG_ELEMENT,
                1,
                new ImmutablePair(ALT_ATTR, "mock-image.jpg"),
                new ImmutablePair(SRC_ATTR, "mock-image.jpg"));
        
        
        //----------------------------------------------------------------------
        //------------------------------2Failed-18------------------------------
        //----------------------------------------------------------------------
        processResult = processPageTest("Rgaa32017.Test.01.03.01-2Failed-18");
        checkResultIsFailed(processResult,1,1);
        checkRemarkIsPresent(
                processResult,
                FAILED,
                NOT_PERTINENT_ALT_MSG,
                IMG_ELEMENT,
                1,
                new ImmutablePair(ALT_ATTR, "mock-image.jpg"),
                new ImmutablePair(SRC_ATTR, "mock-image.jpg"));   
        
        
        //----------------------------------------------------------------------
        //------------------------------2Failed-19------------------------------
        //----------------------------------------------------------------------
        processResult = processPageTest("Rgaa32017.Test.01.03.01-2Failed-19");
        checkResultIsFailed(processResult,1,1);
        checkRemarkIsPresent(
                processResult,
                FAILED,
                NOT_PERTINENT_ALT_MSG,
                IMG_ELEMENT,
                1,
                new ImmutablePair(ALT_ATTR, "mock-image.jpg"),
                new ImmutablePair(SRC_ATTR, "mock-image.jpg"));
        
        
        //----------------------------------------------------------------------
        //------------------------------2Failed-20------------------------------
        //----------------------------------------------------------------------
        processResult = processPageTest("Rgaa32017.Test.01.03.01-2Failed-20");
        checkResultIsFailed(processResult,1,1);
        checkRemarkIsPresent(
                processResult,
                FAILED,
                NOT_PERTINENT_ALT_MSG,
                IMG_ELEMENT,
                1,
                new ImmutablePair(ALT_ATTR, "mock-image.jpg"),
                new ImmutablePair(SRC_ATTR, "mock-image.jpg"));
        
        
        //----------------------------------------------------------------------
        //------------------------------2Failed-21------------------------------
        //----------------------------------------------------------------------
        processResult = processPageTest("Rgaa32017.Test.01.03.01-2Failed-21");
        checkResultIsFailed(processResult,1,1);
        checkRemarkIsPresent(
                processResult,
                FAILED,
                NOT_PERTINENT_ALT_MSG,
                IMG_ELEMENT,
                1,
                new ImmutablePair(ALT_ATTR, "mock-image.jpg"),
                new ImmutablePair(SRC_ATTR, "mock-image.jpg"));
        
      

        //----------------------------------------------------------------------
        //------------------------------3NMI-01---------------------------------
        //----------------------------------------------------------------------
        processResult = processPageTest("Rgaa32017.Test.01.03.01-3NMI-01");
        checkResultIsPreQualified(processResult,1,1);
        checkRemarkIsPresent(
                processResult,
                NEED_MORE_INFO,
                CHECK_ALT_PERTINENCE_OF_INFORMATIVE_IMG_MSG,
                IMG_ELEMENT,
                1,
                new ImmutablePair(ALT_ATTR, "Informative image alternative"),
                new ImmutablePair(SRC_ATTR, "mock-image.jpg"));
        

        //----------------------------------------------------------------------
        //------------------------------3NMI-02---------------------------------
        //----------------------------------------------------------------------
        processResult = processPageTest("Rgaa32017.Test.01.03.01-3NMI-02");
        checkResultIsPreQualified(processResult,1,1);
        checkRemarkIsPresent(
                processResult,
                NEED_MORE_INFO,
                CHECK_NATURE_OF_IMAGE_AND_ALT_PERTINENCE_MSG,
                IMG_ELEMENT,
                1,
                new ImmutablePair(ALT_ATTR, "Not identified image alternative"),
                new ImmutablePair(SRC_ATTR, "mock-image.jpg"));
       
                
        //----------------------------------------------------------------------
        //------------------------------3NMI-03---------------------------------
        //----------------------------------------------------------------------
        processResult = processPageTest("Rgaa32017.Test.01.03.01-3NMI-03");
        checkResultIsPreQualified(processResult,1,1);
        checkRemarkIsPresent(
                processResult,
                NEED_MORE_INFO,
                CHECK_ALT_PERTINENCE_OF_INFORMATIVE_IMG_MSG,
                IMG_ELEMENT,
                1,
                new ImmutablePair(ALT_ATTR, "Alternative"),
                new ImmutablePair(SRC_ATTR, "mock-image.jpg"));
       
                
        //----------------------------------------------------------------------
        //------------------------------3NMI-04---------------------------------
        //----------------------------------------------------------------------
        processResult = processPageTest("Rgaa32017.Test.01.03.01-3NMI-04");
        checkResultIsPreQualified(processResult,1,1);
        checkRemarkIsPresent(
                processResult,
                NEED_MORE_INFO,
                CHECK_NATURE_OF_IMAGE_AND_ALT_PERTINENCE_MSG,
                IMG_ELEMENT,
                1,
                new ImmutablePair(ALT_ATTR, "Alternative"),
                new ImmutablePair(SRC_ATTR, "mock-image.jpg"));
       
                
        //----------------------------------------------------------------------
        //------------------------------3NMI-05---------------------------------
        //----------------------------------------------------------------------
        processResult = processPageTest("Rgaa32017.Test.01.03.01-3NMI-05");
        checkResultIsPreQualified(processResult,1,1);
        checkRemarkIsPresent(
                processResult,
                NEED_MORE_INFO,
                CHECK_ALT_PERTINENCE_OF_INFORMATIVE_IMG_MSG,
                IMG_ELEMENT,
                1,
                new ImmutablePair(ALT_ATTR, "Alternative"),
                new ImmutablePair(SRC_ATTR, "mock-image.jpg"));
       
                
        //----------------------------------------------------------------------
        //------------------------------3NMI-06---------------------------------
        //----------------------------------------------------------------------
        processResult = processPageTest("Rgaa32017.Test.01.03.01-3NMI-06");
        checkResultIsPreQualified(processResult,1,1);
        checkRemarkIsPresent(
                processResult,
                NEED_MORE_INFO,
                CHECK_ALT_PERTINENCE_OF_INFORMATIVE_IMG_MSG,
                IMG_ELEMENT,
                1,
                new ImmutablePair(ALT_ATTR, "Alternative"),
                new ImmutablePair(SRC_ATTR, "mock-image.jpg"));
       
                
        //----------------------------------------------------------------------
        //------------------------------3NMI-07---------------------------------
        //----------------------------------------------------------------------
        processResult = processPageTest("Rgaa32017.Test.01.03.01-3NMI-07");
        checkResultIsPreQualified(processResult,1,2);
        checkRemarkIsPresent(
                processResult,
                NEED_MORE_INFO,
                CHECK_NATURE_OF_IMAGE_AND_ALT_PERTINENCE_MSG,
                IMG_ELEMENT,
                1,
                new ImmutablePair(ALT_ATTR, "Alternative"),
                new ImmutablePair(SRC_ATTR, "mock-image.jpg"));
        checkRemarkIsPresent(
                processResult,
                NEED_MORE_INFO,
                CHECK_NATURE_OF_IMAGE_WITH_NOT_PERTINENT_ALT_MSG,
                IMG_ELEMENT,
                2,
                new ImmutablePair(ALT_ATTR, "Alternative"),
                new ImmutablePair(TITLE_ATTR, "Title"),
                new ImmutablePair(SRC_ATTR, "mock-image.jpg"));
       
                
        //----------------------------------------------------------------------
        //------------------------------3NMI-08---------------------------------
        //----------------------------------------------------------------------
        processResult = processPageTest("Rgaa32017.Test.01.03.01-3NMI-08");
        checkResultIsPreQualified(processResult,1,2);
        checkRemarkIsPresent(
                processResult,
                NEED_MORE_INFO,
                CHECK_NATURE_OF_IMAGE_AND_ALT_PERTINENCE_MSG,
                IMG_ELEMENT,
                1,
                new ImmutablePair(ALT_ATTR, "Alternative"),
                new ImmutablePair(SRC_ATTR, "mock-image.jpg"));
        checkRemarkIsPresent(
                processResult,
                NEED_MORE_INFO,
                CHECK_NATURE_OF_IMAGE_WITH_NOT_PERTINENT_ALT_MSG,
                IMG_ELEMENT,
                2,
                new ImmutablePair(ALT_ATTR, "Alternative"),
                new ImmutablePair(ARIA_LABEL_ATTR, "Aria-Label"),
                new ImmutablePair(SRC_ATTR, "mock-image.jpg"));
       
                
        //----------------------------------------------------------------------
        //------------------------------3NMI-09---------------------------------
        //----------------------------------------------------------------------
        processResult = processPageTest("Rgaa32017.Test.01.03.01-3NMI-09");
        checkResultIsPreQualified(processResult,1,2);
        checkRemarkIsPresent(
                processResult,
                NEED_MORE_INFO,
                CHECK_NATURE_OF_IMAGE_AND_ALT_PERTINENCE_MSG,
                IMG_ELEMENT,
                1,
                new ImmutablePair(ALT_ATTR, "Alternative"),
                new ImmutablePair(SRC_ATTR, "mock-image.jpg"));
        checkRemarkIsPresent(
                processResult,
                NEED_MORE_INFO,
                IMG_TEXT_NOT_IDENTICAL_TO_ALT_WITH_ARIA_LABELLEDBY_MSG,
                IMG_ELEMENT,
                2,
                new ImmutablePair(ALT_ATTR, "Alternative"),
                new ImmutablePair(ARIA_LABELLEDBY_ATTR, "NMI 09"));
       
                
        //----------------------------------------------------------------------
        //------------------------------3NMI-10---------------------------------
        //----------------------------------------------------------------------
        processResult = processPageTest("Rgaa32017.Test.01.03.01-3NMI-10");
        checkResultIsPreQualified(processResult,1,1);
        checkRemarkIsPresent(
                processResult,
                NEED_MORE_INFO,
                CHECK_ALT_PERTINENCE_OF_INFORMATIVE_IMG_MSG,
                IMG_ELEMENT,
                1,
                new ImmutablePair(ALT_ATTR, "Alternative"),
                new ImmutablePair(SRC_ATTR, "mock-image.jpg"));
       
                
        //----------------------------------------------------------------------
        //------------------------------3NMI-11---------------------------------
        //----------------------------------------------------------------------
        processResult = processPageTest("Rgaa32017.Test.01.03.01-3NMI-11");
        checkResultIsPreQualified(processResult,1,1);
        checkRemarkIsPresent(
                processResult,
                NEED_MORE_INFO,
                CHECK_ALT_PERTINENCE_OF_INFORMATIVE_IMG_MSG,
                IMG_ELEMENT,
                1,
                new ImmutablePair(ALT_ATTR, "Alternative"),
                new ImmutablePair(SRC_ATTR, "mock-image.jpg"));
       
                
        //----------------------------------------------------------------------
        //------------------------------3NMI-12---------------------------------
        //----------------------------------------------------------------------
        processResult = processPageTest("Rgaa32017.Test.01.03.01-3NMI-12");
        checkResultIsPreQualified(processResult,11,14);
        checkRemarkIsPresent(
                processResult,
                NEED_MORE_INFO,
                CHECK_ALT_PERTINENCE_OF_INFORMATIVE_IMG_MSG,
                IMG_ELEMENT,
                1,
                new ImmutablePair(ALT_ATTR, "Informative image alternative"),
                new ImmutablePair(SRC_ATTR, "mock-image2.jpg"));
        checkRemarkIsPresent(
                processResult,
                NEED_MORE_INFO,
                CHECK_ALT_PERTINENCE_OF_INFORMATIVE_IMG_MSG,
                IMG_ELEMENT,
                2,
                new ImmutablePair(ALT_ATTR, "Informative image alternative"),
                new ImmutablePair(SRC_ATTR, "mock-image2.jpg"));
        checkRemarkIsPresent(
                processResult,
                NEED_MORE_INFO,
                CHECK_ALT_PERTINENCE_OF_INFORMATIVE_IMG_MSG,
                IMG_ELEMENT,
                3,
                new ImmutablePair(ALT_ATTR, "Informative image alternative"),
                new ImmutablePair(SRC_ATTR, "mock-image2.jpg"));
        checkRemarkIsPresent(
                processResult,
                NEED_MORE_INFO,
                CHECK_ALT_PERTINENCE_OF_INFORMATIVE_IMG_MSG,
                IMG_ELEMENT,
                4,
                new ImmutablePair(ALT_ATTR, "Informative image alternative"),
                new ImmutablePair(SRC_ATTR, "mock-image2.jpg"));
        checkRemarkIsPresent(
                processResult,
                NEED_MORE_INFO,
                CHECK_NATURE_OF_IMAGE_AND_ALT_PERTINENCE_MSG,
                IMG_ELEMENT,
                5,
                new ImmutablePair(ALT_ATTR, "not identified image alternative"),
                new ImmutablePair(SRC_ATTR, "mock-image4.jpg"));
        checkRemarkIsPresent(
                processResult,
                NEED_MORE_INFO,
                CHECK_NATURE_OF_IMAGE_AND_ALT_PERTINENCE_MSG,
                IMG_ELEMENT,
                6,
                new ImmutablePair(ALT_ATTR, "not identified image alternative"),
                new ImmutablePair(SRC_ATTR, "mock-image4.jpg"));
        checkRemarkIsPresent(
                processResult,
                NEED_MORE_INFO,
                CHECK_NATURE_OF_IMAGE_AND_ALT_PERTINENCE_MSG,
                IMG_ELEMENT,
                7,
                new ImmutablePair(ALT_ATTR, "not identified image alternative"),
                new ImmutablePair(SRC_ATTR, "mock-image4.jpg"));
        checkRemarkIsPresent(
                processResult,
                NEED_MORE_INFO,
                CHECK_NATURE_OF_IMAGE_AND_ALT_PERTINENCE_MSG,
                IMG_ELEMENT,
                8,
                new ImmutablePair(ALT_ATTR, "not identified image alternative"),
                new ImmutablePair(SRC_ATTR, "mock-image4.jpg"));
        checkRemarkIsPresent(
                processResult,
                NEED_MORE_INFO,
                CHECK_NATURE_OF_IMAGE_AND_ALT_PERTINENCE_MSG,
                IMG_ELEMENT,
                9,
                new ImmutablePair(ALT_ATTR, "not identified image alternative"),
                new ImmutablePair(SRC_ATTR, "mock-image4.jpg"));
        checkRemarkIsPresent(
                processResult,
                NEED_MORE_INFO,
                CHECK_NATURE_OF_IMAGE_WITH_NOT_PERTINENT_ALT_MSG,
                IMG_ELEMENT,
                10,
                new ImmutablePair(ALT_ATTR, "not identified image alternative"),
                new ImmutablePair(TITLE_ATTR, "title"),
                new ImmutablePair(SRC_ATTR, "mock-image4.jpg"));
        checkRemarkIsPresent(
                processResult,
                NEED_MORE_INFO,
                CHECK_NATURE_OF_IMAGE_AND_ALT_PERTINENCE_MSG,
                IMG_ELEMENT,
                11,
                new ImmutablePair(ALT_ATTR, "not identified image alternative"),
                new ImmutablePair(SRC_ATTR, "mock-image4.jpg"));
        checkRemarkIsPresent(
                processResult,
                NEED_MORE_INFO,
                CHECK_NATURE_OF_IMAGE_WITH_NOT_PERTINENT_ALT_MSG,
                IMG_ELEMENT,
                12,
                new ImmutablePair(ALT_ATTR, "not identified image alternative"),
                new ImmutablePair(ARIA_LABEL_ATTR, "aria-label"),
                new ImmutablePair(SRC_ATTR, "mock-image4.jpg"));
        checkRemarkIsPresent(
                processResult,
                NEED_MORE_INFO,
                CHECK_NATURE_OF_IMAGE_AND_ALT_PERTINENCE_MSG,
                IMG_ELEMENT,
                13,
                new ImmutablePair(ALT_ATTR, "not identified image alternative"),
                new ImmutablePair(SRC_ATTR, "mock-image4.jpg"));
        checkRemarkIsPresent(
                processResult,
                NEED_MORE_INFO,
                IMG_TEXT_NOT_IDENTICAL_TO_ALT_WITH_ARIA_LABELLEDBY_MSG,
                IMG_ELEMENT,
                14,
                new ImmutablePair(ALT_ATTR, "not identified image alternative"),
                new ImmutablePair(ARIA_LABELLEDBY_ATTR, "NMI 12_03"));
        
       
      //----------------------------------------------------------------------
      //------------------------------3NMI-13------------------------------
      //----------------------------------------------------------------------
      processResult = processPageTest("Rgaa32017.Test.01.03.01-3NMI-13");
      checkResultIsPreQualified(processResult,1,1);
      checkRemarkIsPresent(
              processResult,
              NEED_MORE_INFO,
              CHECK_ALT_PERTINENCE_OF_INFORMATIVE_IMG_MSG,
              IMG_ELEMENT,
              1,
              new ImmutablePair(ALT_ATTR, ""),
              new ImmutablePair(SRC_ATTR, "mock-image.jpg"));
      
      //----------------------------------------------------------------------
      //------------------------------3NMI-14------------------------------
      //----------------------------------------------------------------------
      processResult = processPageTest("Rgaa32017.Test.01.03.01-3NMI-14");
      checkResultIsPreQualified(processResult,1,1);
      checkRemarkIsPresent(
              processResult,
              NEED_MORE_INFO,
              CHECK_ALT_PERTINENCE_OF_INFORMATIVE_IMG_MSG,
              IMG_ELEMENT,
              1,
              new ImmutablePair(ALT_ATTR, ""),
              new ImmutablePair(SRC_ATTR, "mock-image.jpg"));

      //----------------------------------------------------------------------
      //------------------------------3NMI-15------------------------------
      //----------------------------------------------------------------------
      processResult = processPageTest("Rgaa32017.Test.01.03.01-3NMI-15");
      checkResultIsPreQualified(processResult,1,1);
      checkRemarkIsPresent(
              processResult,
              NEED_MORE_INFO,
              CHECK_NATURE_OF_IMAGE_AND_ALT_PERTINENCE_MSG,
              IMG_ELEMENT,
              1,
              new ImmutablePair(ALT_ATTR, ""),
              new ImmutablePair(SRC_ATTR, "mock-image.jpg"));
     
      //----------------------------------------------------------------------
      //------------------------------3NMI-16------------------------------
      //----------------------------------------------------------------------
      processResult = processPageTest("Rgaa32017.Test.01.03.01-3NMI-16");
      checkResultIsPreQualified(processResult,1,1);
      checkRemarkIsPresent(
              processResult,
              NEED_MORE_INFO,
              CHECK_ALT_PERTINENCE_OF_INFORMATIVE_IMG_MSG,
              IMG_ELEMENT,
              1,
              new ImmutablePair(ALT_ATTR, ""),
              new ImmutablePair(SRC_ATTR, "mock-image.jpg"));
      
      
      //----------------------------------------------------------------------
      //------------------------------3NMI-17------------------------------
      //----------------------------------------------------------------------
      processResult = processPageTest("Rgaa32017.Test.01.03.01-3NMI-17");
      checkResultIsPreQualified(processResult,1,1);
      checkRemarkIsPresent(
              processResult,
              NEED_MORE_INFO,
              CHECK_ALT_PERTINENCE_OF_INFORMATIVE_IMG_MSG,
              IMG_ELEMENT,
              1,
              new ImmutablePair(ALT_ATTR, ""),
              new ImmutablePair(SRC_ATTR, "mock-image.jpg"));
      
      
      //----------------------------------------------------------------------
      //------------------------------3NMI-18------------------------------
      //----------------------------------------------------------------------
      processResult = processPageTest("Rgaa32017.Test.01.03.01-3NMI-18");
      checkResultIsPreQualified(processResult,1,1);
      checkRemarkIsPresent(
              processResult,
              NEED_MORE_INFO,
              CHECK_ALT_PERTINENCE_OF_INFORMATIVE_IMG_MSG,
              IMG_ELEMENT,
              1,
              new ImmutablePair(ALT_ATTR, ""),
              new ImmutablePair(SRC_ATTR, "mock-image.jpg"));
        
      //----------------------------------------------------------------------
      //------------------------------3NMI-19------------------------------
      //----------------------------------------------------------------------
      processResult = processPageTest("Rgaa32017.Test.01.03.01-3NMI-19");
      checkResultIsPreQualified(processResult,1,1);
      checkRemarkIsPresent(
              processResult,
              NEED_MORE_INFO,
              CHECK_NATURE_OF_IMAGE_AND_ALT_PERTINENCE_MSG,
              IMG_ELEMENT,
              1,
              new ImmutablePair(ALT_ATTR, ""),
              new ImmutablePair(SRC_ATTR, "mock-image.jpg"));
      
      
      //----------------------------------------------------------------------
      //------------------------------3NMI-20------------------------------
      //----------------------------------------------------------------------
      processResult = processPageTest("Rgaa32017.Test.01.03.01-3NMI-20");
      checkResultIsPreQualified(processResult,1,1);
      checkRemarkIsPresent(
              processResult,
              NEED_MORE_INFO,
              CHECK_NATURE_OF_IMAGE_AND_ALT_PERTINENCE_MSG,
              IMG_ELEMENT,
              1,
              new ImmutablePair(ALT_ATTR, ""),
              new ImmutablePair(SRC_ATTR, "mock-image.jpg"));
      
      
      //----------------------------------------------------------------------
      //------------------------------3NMI-21------------------------------
      //----------------------------------------------------------------------
      processResult = processPageTest("Rgaa32017.Test.01.03.01-3NMI-21");
      checkResultIsPreQualified(processResult,1,1);
      checkRemarkIsPresent(
              processResult,
              NEED_MORE_INFO,
              CHECK_NATURE_OF_IMAGE_AND_ALT_PERTINENCE_MSG,
              IMG_ELEMENT,
              1,
              new ImmutablePair(ALT_ATTR, ""),
              new ImmutablePair(SRC_ATTR, "mock-image.jpg"));
      
      
      //----------------------------------------------------------------------
      //------------------------------3NMI-22------------------------------
      //----------------------------------------------------------------------
      processResult = processPageTest("Rgaa32017.Test.01.03.01-3NMI-22");
      checkResultIsPreQualified(processResult,1,1);
      checkRemarkIsPresent(
              processResult,
              NEED_MORE_INFO,
              CHECK_ALT_PERTINENCE_OF_INFORMATIVE_IMG_MSG,
              IMG_ELEMENT,
              1,
              new ImmutablePair(ALT_ATTR, ""),
              new ImmutablePair(SRC_ATTR, "mock-image.jpg"));
      
      
      //----------------------------------------------------------------------
      //------------------------------3NMI-23------------------------------
      //----------------------------------------------------------------------
      processResult = processPageTest("Rgaa32017.Test.01.03.01-3NMI-23");
      checkResultIsPreQualified(processResult,1,1);
      checkRemarkIsPresent(
              processResult,
              NEED_MORE_INFO,
              CHECK_ALT_PERTINENCE_OF_INFORMATIVE_IMG_MSG,
              IMG_ELEMENT,
              1,
              new ImmutablePair(ALT_ATTR, ""),
              new ImmutablePair(SRC_ATTR, "mock-image.jpg"));
      
      
      //----------------------------------------------------------------------
      //------------------------------3NMI-24------------------------------
      //----------------------------------------------------------------------
      processResult = processPageTest("Rgaa32017.Test.01.03.01-3NMI-24");
      checkResultIsPreQualified(processResult,1,1);
      checkRemarkIsPresent(
              processResult,
              NEED_MORE_INFO,
              CHECK_ALT_PERTINENCE_OF_INFORMATIVE_IMG_MSG,
              IMG_ELEMENT,
              1,
              new ImmutablePair(ALT_ATTR, ""),
              new ImmutablePair(SRC_ATTR, "mock-image.jpg"));
        
        //----------------------------------------------------------------------
        //------------------------------4NA-01----------------------------------
        //----------------------------------------------------------------------
        checkResultIsNotApplicable(processPageTest("Rgaa32017.Test.01.03.01-4NA-01"));
        
        //----------------------------------------------------------------------
        //------------------------------4NA-02----------------------------------
        //----------------------------------------------------------------------
        checkResultIsNotApplicable(processPageTest("Rgaa32017.Test.01.03.01-4NA-02"));
        
        //----------------------------------------------------------------------
        //------------------------------4NA-03----------------------------------
        //----------------------------------------------------------------------
        checkResultIsNotApplicable(processPageTest("Rgaa32017.Test.01.03.01-4NA-03"));
        
        //----------------------------------------------------------------------
        //------------------------------4NA-04----------------------------------
        //----------------------------------------------------------------------
        checkResultIsNotApplicable(processPageTest("Rgaa32017.Test.01.03.01-4NA-04"));
        
        //----------------------------------------------------------------------
        //------------------------------4NA-05----------------------------------
        //----------------------------------------------------------------------
        checkResultIsNotApplicable(processPageTest("Rgaa32017.Test.01.03.01-4NA-05"));
    }

}