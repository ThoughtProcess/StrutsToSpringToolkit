package com.rombalabs.strutstospringtoolkit.jspservices.transformers.struts.html;

import com.rombalabs.strutstospringtoolkit.jspservices.transformers.struts.BaseTagTransformer;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;

public class OptionsCollectionTagTransformer extends BaseTagTransformer {

    public OptionsCollectionTagTransformer() {
        replacementMap.put("html:optionsCollection", "form:options");
    }

    @Override
    protected void convertElement(Element element, String newTagName) {
        var styleClass = element.attr("styleClass");
        var label = element.attr("label");
        var name = element.attr("name");
        var property = element.attr("property");
        var value = element.attr("value");

        element.renameTagPreserveProperties(newTagName);
        element.attr("items", createExpressionLanguageString(name, property));

        if(!StringUtils.isEmpty(value))
            element.attr("itemValue", value);

        if(!StringUtils.isEmpty(label))
            element.attr("itemLabel", label);

        if (!StringUtils.isEmpty(styleClass)) {
            element.attr("cssClass", styleClass);
        }

        // Delete extraneous attributes
        element.removeAttr("label");
        element.removeAttr("name");
        element.removeAttr("property");
        element.removeAttr("styleClass");
        element.removeAttr("value");
    }

    private static String createExpressionLanguageString(String name, String property) {
        return "${" +
                name +
                (!name.isEmpty() && !property.isEmpty() ? "." : "") +
                property +
                "}";
    }
}
