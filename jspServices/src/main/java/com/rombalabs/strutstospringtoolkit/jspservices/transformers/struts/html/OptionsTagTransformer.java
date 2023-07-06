package com.rombalabs.strutstospringtoolkit.jspservices.transformers.struts.html;

import com.rombalabs.strutstospringtoolkit.jspservices.transformers.struts.BaseTagTransformer;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;

public class OptionsTagTransformer extends BaseTagTransformer {

    public OptionsTagTransformer() {
        replacementMap.put("html:options", "form:options");
    }

    @Override
    protected void convertElement(Element element, String newTagName) {
        var collection = element.attr("collection");
        var cssClass = element.attr("styleClass");
        var labelName = element.attr("labelName");
        var labelProperty = element.attr("labelProperty");
        var name = element.attr("name");
        var property = element.attr("property");

        // Special case:
        // If the "labelName" attribute is specified, we need to iterate on two lists,
        // one for the values, and another for the labels.
        if (StringUtils.isEmpty(collection) &&
                !StringUtils.isEmpty(name) &&
                !StringUtils.isEmpty(labelName)) {
            element.tagName("c:forEach");
            element.attr("var", "idx");
            element.attr("begin", "0");
            element.attr("end", "${fn:length(" + labelName + ") - 1}");

            // Create a form:option child
            var optionElement = element.ownerDocument().createElement("form:option");
            optionElement.attr("value", "${" + name + "[idx]}");
            optionElement.attr("label", "${" + labelName + "[idx]}");
            element.appendChild(optionElement);
        }
        else {
            element.tagName(newTagName);
            element.attr("items", "${" +
                    (!StringUtils.isEmpty(collection) ? collection : name) + "}");
            if(!StringUtils.isEmpty(property))
                element.attr("itemValue", property);

            if(!StringUtils.isEmpty(labelProperty))
                element.attr("itemLabel", labelProperty);
        }

        if (!StringUtils.isEmpty(cssClass)) {
            element.attr("cssClass", cssClass);
        }

        // Delete extraneous attributes
        element.removeAttr("collection");
        element.removeAttr("labelName");
        element.removeAttr("labelProperty");
        element.removeAttr("name");
        element.removeAttr("property");
        element.removeAttr("styleClass");
    }
}
