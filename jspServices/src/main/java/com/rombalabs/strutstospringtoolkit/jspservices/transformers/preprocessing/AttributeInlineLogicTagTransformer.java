package com.rombalabs.strutstospringtoolkit.jspservices.transformers.preprocessing;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AttributeInlineLogicTagTransformer extends BasePreprocessTransformer {

    Pattern attributeLogicTagPattern;

    public AttributeInlineLogicTagTransformer() {
        // <logic:equal (\S+=["|']\S+['|"] ?)+>.*</logic:equal>
        attributeLogicTagPattern = Pattern.compile("<logic:equal (\\S+=[\"|']\\S+[\"|'](\\s+)?)+>.*</logic:equal>");
    }

    @Override
    public String processText(String inputText) {
        String result = inputText;
        Matcher m = attributeLogicTagPattern.matcher(inputText);
        if (m.find()) {
            logger.info("Preprocessing a line that contains a <logic:[not]equal> element: " + inputText);

            Parser parser = Parser.xmlParser();
            parser.settings(new ParseSettings(true, true)); // tag, attribute preserve case
            Document doc = Jsoup.parse(m.toMatchResult().group(),"", parser);

            var elString = convertElement(doc.root().child(0), "");

            result = m.replaceAll(elString);

            logger.info("Converted to: " + result);
        }

        return result;
    }

    protected String convertElement(Element element, String newTagName) {
        var name = element.attr("name");
        var property = element.attr("property");
        var value = element.attr("value");
        var content = element.text();

        return createExpressionLanguageString(name, property, value, content);
    }

    private static String createExpressionLanguageString(String name, String property, String value, String content) {
        var elString = name +
                (!name.isEmpty() && !property.isEmpty() ? "." : "") +
                property;

        if (!value.contentEquals("true")) {
            elString = elString + " eq " + value;
        }

        return " \\${" + elString + " ? '" + content + "' : ''}";
    }
}
