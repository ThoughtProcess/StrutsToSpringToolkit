package com.rombalabs.strutstospringtoolkit.jspservices.transformers.struts.html;

import com.rombalabs.strutstospringtoolkit.jspservices.transformers.TagTransformer;
import com.rombalabs.strutstospringtoolkit.jspservices.transformers.struts.BaseTagTransformer;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;

/**
 A catch-all class to handle several kinds of Struts html TLD primitives.
 */
public class DefaultHtmlTagTransformer extends BaseTagTransformer {

    public DefaultHtmlTagTransformer() {
        replacementMap.put("html:checkbox", "form:checkbox");
        replacementMap.put("html:errors", "form:errors");
        replacementMap.put("html:form", "form:form");
        replacementMap.put("html:hidden", "form:hidden");
        replacementMap.put("html:multibox", "form:checkboxes");
        replacementMap.put("html:option", "form:option");
        replacementMap.put("html:radio", "form:radiobutton");
        replacementMap.put("html:text", "form:input");
        replacementMap.put("html:textarea", "form:textarea");
    }

    @Override
    protected void convertElement(Element element, String newTagName) {
        var property = element.attr("property");
        var cssClass = element.attr("styleClass");

        element.tagName(newTagName);

        if (!StringUtils.isEmpty(cssClass)) {
            element.attr("cssClass", cssClass);
        }
        if (!StringUtils.isEmpty(property)) {
            element.attr("path",property);
        }

        element.removeAttr("name");
        element.removeAttr("property");
        element.removeAttr("styleClass");
    }
}
