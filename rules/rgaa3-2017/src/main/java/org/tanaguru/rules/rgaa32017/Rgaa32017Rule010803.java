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
import static org.tanaguru.entity.audit.TestSolution.NEED_MORE_INFO;
import static org.tanaguru.entity.audit.TestSolution.NOT_APPLICABLE;
import org.tanaguru.ruleimplementation.AbstractMarkerPageRuleImplementation;
import org.tanaguru.rules.elementchecker.element.ElementPresenceChecker;
import org.tanaguru.rules.elementselector.ImageElementSelector;
import static org.tanaguru.rules.keystore.AttributeStore.DATA_ATTR;
import static org.tanaguru.rules.keystore.CssLikeQueryStore.OBJECT_TYPE_IMG_CSS_LIKE_QUERY;
import static org.tanaguru.rules.keystore.MarkerStore.DECORATIVE_IMAGE_MARKER;
import static org.tanaguru.rules.keystore.MarkerStore.INFORMATIVE_IMAGE_MARKER;
import static org.tanaguru.rules.keystore.RemarkMessageStore.CHECK_NATURE_OF_IMAGE_AND_TEXT_STYLED_PRESENCE_MSG;
import static org.tanaguru.rules.keystore.RemarkMessageStore.CHECK_TEXT_STYLED_PRESENCE_OF_INFORMATIVE_IMG_MSG;

/**
 * Implementation of the rule 1.8.3 of the referential Rgaa 3-2017.
 * <br>
 * For more details about the implementation, refer to <a href="http://tanaguru-rules-rgaa3.readthedocs.org/en/latest/Rule-1-8-4">the rule 1.8.3 design page.</a>
 * @see <a href="http://references.modernisation.gouv.fr/referentiel-technique-0#test-1-8-3"> 1.8.3 rule specification</a>
 *
 */
public class Rgaa32017Rule010803 extends AbstractMarkerPageRuleImplementation {

    /**
     * Default constructor
     */
    public Rgaa32017Rule010803 () {
        super(
                new ImageElementSelector(OBJECT_TYPE_IMG_CSS_LIKE_QUERY),
                INFORMATIVE_IMAGE_MARKER,
                DECORATIVE_IMAGE_MARKER,
                new ElementPresenceChecker(
                        new ImmutablePair(NEED_MORE_INFO, CHECK_TEXT_STYLED_PRESENCE_OF_INFORMATIVE_IMG_MSG),
                        new ImmutablePair(NOT_APPLICABLE, ""),
                        // evidence elements
                        DATA_ATTR
                ),
                new ElementPresenceChecker(
                        new ImmutablePair(NEED_MORE_INFO, CHECK_NATURE_OF_IMAGE_AND_TEXT_STYLED_PRESENCE_MSG),
                        new ImmutablePair(NOT_APPLICABLE, ""),
                        // evidence elements
                        DATA_ATTR
                )
            );
    }

}
