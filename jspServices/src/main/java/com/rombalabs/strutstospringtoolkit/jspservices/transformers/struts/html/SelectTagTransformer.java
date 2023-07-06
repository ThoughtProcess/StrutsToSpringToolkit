package com.rombalabs.strutstospringtoolkit.jspservices.transformers.struts.html;

import com.rombalabs.strutstospringtoolkit.jspservices.transformers.struts.BaseTagTransformer;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;

public class SelectTagTransformer extends BaseTagTransformer {

    public SelectTagTransformer() {
        replacementMap.put("html:select", "form:select");
    }

    @Override
    protected void convertElement(Element element, String newTagName) {
        var styleClass = element.attr("styleClass");
        var name = element.attr("name");
        var property = element.attr("property");

        element.tagName(newTagName);
        element.attr("path", createExpressionLanguageString(name, property));

        if (!StringUtils.isEmpty(styleClass)) {
            element.attr("cssClass", styleClass);
        }

        // Delete extraneous attributes
        element.removeAttr("name");
        element.removeAttr("property");
        element.removeAttr("styleClass");
    }

    private static String createExpressionLanguageString(String name, String property) {
        return "${" +
                name +
                (!name.isEmpty() && !property.isEmpty() ? "." : "") +
                property +
                "}";
    }
}
