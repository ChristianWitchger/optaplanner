/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.optaplanner.persistence.xstream.api.score;

import java.io.Serializable;

import com.thoughtworks.xstream.XStream;
import org.optaplanner.core.api.score.Score;

import static org.junit.Assert.*;

public abstract class AbstractScoreXStreamConverterTest {

    // ************************************************************************
    // Helper methods
    // ************************************************************************

    protected <S extends Score, W extends TestScoreWrapper<S>> void assertSerializeAndDeserialize(S expectedScore, W input) {
        XStream xStream = new XStream();
        xStream.setMode(XStream.ID_REFERENCES);
        xStream.processAnnotations(input.getClass());
        String xmlString = xStream.toXML(input);
        W output = (W) xStream.fromXML(xmlString);

        assertEquals(expectedScore, output.getScore());
        String regex;
        if (expectedScore != null) {
            regex = "<([\\w\\-\\.]+)( id=\"\\d+\")?>" // Start of element
                    + "\\s*<score( id=\"\\d+\")?>"
                    + expectedScore.toString().replaceAll("\\[", "\\\\[").replaceAll("\\]", "\\\\]") // Score
                    + "</score>"
                    + "\\s*</\\1>"; // End of element
        } else {
            regex = "<([\\w\\-\\.]+)( id=\"\\d+\")?/>"; // Start and end of element
        }
        if (!xmlString.matches(regex)) {
            fail("Regular expression match failed.\nExpected regular expression: " + regex + "\nActual string: " + xmlString);
        }
    }

    public static abstract class TestScoreWrapper<S extends Score> implements Serializable {

        public abstract S getScore();

    }

}
