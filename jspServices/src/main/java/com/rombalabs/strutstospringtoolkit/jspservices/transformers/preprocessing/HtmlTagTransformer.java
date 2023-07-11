package com.rombalabs.strutstospringtoolkit.jspservices.transformers.preprocessing;

import com.rombalabs.strutstospringtoolkit.jspservices.transformers.PreprocessTransformer;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlTagTransformer implements PreprocessTransformer {

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
