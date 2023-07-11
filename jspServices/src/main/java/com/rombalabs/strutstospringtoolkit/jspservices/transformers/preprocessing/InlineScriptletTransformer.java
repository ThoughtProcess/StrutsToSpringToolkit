package com.rombalabs.strutstospringtoolkit.jspservices.transformers.preprocessing;

import com.rombalabs.strutstospringtoolkit.jspservices.transformers.PreprocessTransformer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InlineScriptletTransformer implements PreprocessTransformer {

    protected static final Logger logger = LogManager.getLogger();

    Pattern initialRequestPattern;
    Pattern requestAttributePattern;
    Pattern requestParameterPattern;
    Pattern requestParameterValuesPattern;
    Pattern requestHeaderPattern;
    Pattern requestHeaderValuesPattern;
    Pattern requestContextPathPattern;
    Pattern sessionAttributePattern;

    public InlineScriptletTransformer() {
        initialRequestPattern = Pattern.compile("<%=[\\s+]?request\\.get\\S+\\((\\S+)?\\)[\\s+]?%>");
        requestAttributePattern = Pattern.compile("<%=[\\s+]?request\\.getAttribute\\(\"(\\S+)\"\\)[\\s+]?%>");
        requestParameterPattern = Pattern.compile("<%=[\\s+]?request\\.getParameter\\(\"(\\S+)\"\\)[\\s+]?%>");
        requestParameterValuesPattern = Pattern.compile("<%=[\\s+]?request\\.getParameterValues\\(\"(\\S+)\"\\)[\\s+]?%>");
        requestHeaderPattern = Pattern.compile("<%=[\\s+]?request\\.getHeader\\(\"(\\S+)\"\\)[\\s+]?%>");
        requestHeaderValuesPattern = Pattern.compile("<%=[\\s+]?request\\.getHeaderValues\\(\"(\\S+)\"\\)[\\s+]?%>");
        requestContextPathPattern = Pattern.compile("<%=[\\s+]?request\\.getContextPath\\(\\)[\\s+]?%>");

        sessionAttributePattern = Pattern.compile("<%=[\\s+]?request\\.getSession\\(\\)\\.getAttribute\\(\"(\\S+)\"\\)[\\s+]?%>");
    }
    @Override
    public String processText(String inputText) {
        String result = inputText;
        Matcher m = initialRequestPattern.matcher(inputText);
        if (m.find()) {
            /*
            https://balusc.omnifaces.org/2011/09/communication-in-jsf-20.html#ImplicitELObjects
            #{requestScope}: the current request attribute map
            #{viewScope}: the current view attribute map
            #{sessionScope}: the current session attribute map
            #{param}: the current request parameter map
            #{paramValues}: the current request parameter values map
            #{header}: the current request header map
            #{headerValues}: the current request header values map
            #{cookie}: the current request cookie map
            */

            m = requestAttributePattern.matcher(inputText);
            result = m.replaceAll("\\${requestScope.$1}");

            m = requestParameterPattern.matcher(result);
            result = m.replaceAll("\\${param.$1}");

            m = requestParameterValuesPattern.matcher(result);
            result = m.replaceAll("\\${paramValues.$1}");

            m = requestHeaderPattern.matcher(result);
            result = m.replaceAll("\\${header[$1]}");

            m = requestHeaderValuesPattern.matcher(result);
            result = m.replaceAll("\\${headerValues[$1]}");

            m = requestContextPathPattern.matcher(result);
            result = m.replaceAll("\\${pageContext.request.contextPath}");

            m = sessionAttributePattern.matcher(result);
            result = m.replaceAll("\\${sessionScope[$1]}");
        }

        return result;
    }
}
