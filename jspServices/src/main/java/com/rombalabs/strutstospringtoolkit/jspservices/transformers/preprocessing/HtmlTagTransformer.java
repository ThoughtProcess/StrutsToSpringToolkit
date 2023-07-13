package com.rombalabs.strutstospringtoolkit.jspservices.transformers.preprocessing;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlTagTransformer extends BasePreprocessTransformer {

    final Pattern htmlTagPattern;

    public HtmlTagTransformer() {
        htmlTagPattern = Pattern.compile("html:html");
    }

    @Override
    public String processText(String inputText) {
        String result = inputText;
        Matcher m = htmlTagPattern.matcher(inputText);
        result = m.replaceAll("html");

        return result;
    }
}
