package com.rombalabs.strutstospringtoolkit.jspservices.transformers.struts.bean;

import com.rombalabs.strutstospringtoolkit.jspservices.transformers.TagTransformer;
import com.rombalabs.strutstospringtoolkit.jspservices.transformers.struts.BaseTagTransformer;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;

public class WriteTagTransformer extends BaseTagTransformer {

    public WriteTagTransformer() {
        replacementMap.put("bean:write", "");
    }

    @Override
    protected void convertElement(Element element, String newTagName) {
        var name = element.attr("name");
        var property = element.attr("property");

        if(StringUtils.isEmpty(name) && StringUtils.isEmpty(property))
            return;

        element.replaceWith(new TextNode(createExpressionLanguageString(name, property)));
    }

    private static String createExpressionLanguageString(String name, String property) {
        return "${" +
                name +
                (!name.isEmpty() && !property.isEmpty() ? "." : "") +
                property +
                "}";
    }
}
