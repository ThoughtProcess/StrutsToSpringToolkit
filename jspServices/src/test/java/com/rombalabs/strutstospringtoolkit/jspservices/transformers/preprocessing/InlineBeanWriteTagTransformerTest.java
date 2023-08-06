package com.rombalabs.strutstospringtoolkit.jspservices.transformers.preprocessing;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InlineBeanWriteTagTransformerTest {

    private InlineBeanWriteTagTransformer beanWriteTagTransformer;

    @BeforeEach
    void setUp() {
        beanWriteTagTransformer = new InlineBeanWriteTagTransformer();
    }

    @Test
    void testNameProperty() {
        String testCase = "<bean:write name=\"employee\" property=\"empId\"/>";
        String expected = "${employee.empId}";
        String result = beanWriteTagTransformer.processText(testCase);
        Assertions.assertEquals(expected, result);
    }

    @Test
    void testNameScopeProperty() {
        String testCase = "<bean:write name=\"employee\" scope=\"request\" property=\"empId\"/>";
        String expected = "${employee.empId}";
        String result = beanWriteTagTransformer.processText(testCase);
        Assertions.assertEquals(expected, result);
    }

    @Test
    void testTrailingWhitespace() {
        String testCase = "<bean:write name=\"employee\" scope=\"request\" property=\"empId\" />";
        String expected = "${employee.empId}";
        String result = beanWriteTagTransformer.processText(testCase);
        Assertions.assertEquals(expected, result);
    }

    @Test
    void testMixedExample() {
        String testCase = "<td><input type=\"checkbox\" name=\"foo\" value=\"<bean:write name=\"employee\" scope=\"request\" property=\"empId\"/>\"></td>";
        String expected = "<td><input type=\"checkbox\" name=\"foo\" value=\"${employee.empId}\"></td>";
        String result = beanWriteTagTransformer.processText(testCase);
        Assertions.assertEquals(expected, result);
    }
}