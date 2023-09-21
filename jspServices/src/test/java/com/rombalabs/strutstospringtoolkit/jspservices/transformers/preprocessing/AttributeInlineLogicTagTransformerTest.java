package com.rombalabs.strutstospringtoolkit.jspservices.transformers.preprocessing;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AttributeInlineLogicTagTransformerTest {

    private AttributeInlineLogicTagTransformer inlineLogicTagTransformer;

    @BeforeEach
    void setUp() {
        inlineLogicTagTransformer = new AttributeInlineLogicTagTransformer();
    }

    @Test
    void processText() {
        String testCase = "<div class=\"some-default-class<logic:equal name=\"Bean123\" property=\"prop\" value=\"blahblah\"> specialPropClass</logic:equal>\">\n" +
                "          <div class=\"foo\">Foo</div>\n" +
                "          <div class=\"bar\">Bar</div>\n" +
                "        </div>";

        String expected = "<div class=\"some-default-class ${Bean123.prop eq blahblah ? 'specialPropClass' : ''}\">\n" +
                "          <div class=\"foo\">Foo</div>\n" +
                "          <div class=\"bar\">Bar</div>\n" +
                "        </div>";

        String result = inlineLogicTagTransformer.processText(testCase);
        Assertions.assertEquals(expected, result);
    }
}