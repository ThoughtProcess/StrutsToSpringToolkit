package com.rombalabs.strutstospringtoolkit.jspservices.transformers.struts.html;

import com.rombalabs.strutstospringtoolkit.jspservices.transformers.struts.BaseTagTransformer;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.internal.StringUtil;
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

        if (!StringUtils.isEmpty(collection)) {
            element.renameTagPreserveProperties(newTagName);
            element.attr("items", "${" + collection + "}");
            if(!StringUtils.isEmpty(property))
                element.attr("itemValue", property);

            if(!StringUtils.isEmpty(labelProperty))
                element.attr("itemLabel", labelProperty);
        }
        else if (StringUtils.isEmpty(name) != StringUtils.isEmpty(property)) {
            // One of either name or property are defined.
            element.renameTagPreserveProperties(newTagName);
            element.attr("items", "${" + name + property + "}");
        }
        else if (!StringUtils.isEmpty(name) && !StringUtils.isEmpty(property)) {
            // Both name and property are defined.
            element.renameTagPreserveProperties(newTagName);
            element.attr("items", "${" + name + "." + property + "}");
        }
        else {
            // Special case:
            // If the "labelName" attribute is specified, we need to iterate on two lists,
            // one for the values, and another for the labels.

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
