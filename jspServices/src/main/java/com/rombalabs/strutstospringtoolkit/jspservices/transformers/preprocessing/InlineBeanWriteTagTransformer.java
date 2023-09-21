package com.rombalabs.strutstospringtoolkit.jspservices.transformers.preprocessing;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InlineBeanWriteTagTransformer extends BasePreprocessTransformer {

    Pattern beanWritePattern;

    public InlineBeanWriteTagTransformer() {
        beanWritePattern = Pattern.compile("<bean:write\\s+(\\S+=['|\"]\\S+['|\"] ?)+/>");
    }

    @Override
    public String processText(String inputText) {
        String result = inputText;
        Matcher m = beanWritePattern.matcher(inputText);
        if (m.find()) {
            logger.info("Preprocessing a one-line <bean:write> element: " + inputText);

            Parser parser = Parser.xmlParser();
            parser.settings(new ParseSettings(true, true)); // tag, attribute preserve case
            Document doc = Jsoup.parse(m.toMatchResult().group(),"", parser);

            String jspExpression = convertElement(doc.root().child(0));
            result = m.replaceAll(jspExpression);
        }

        return result;
    }

    protected String convertElement(Element element) {
        var name = element.attr("name");
        var property = element.attr("property");
        var filter = ("true".equals(element.attr("filter")));

        return createExpressionLanguageString(name, property, filter);
    }

    private static String createExpressionLanguageString(String name, String property, boolean filter) {
        var elString = name +
                (!name.isEmpty() && !property.isEmpty() ? "." : "") +
                property;

        if (filter) {
            elString = "fn:escapeXml(" + elString + ")";
        }

        return "\\${" + elString + "}";
    }
}
