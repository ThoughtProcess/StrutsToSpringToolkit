package com.rombalabs.strutstospringtoolkit.jspservices.transformers.struts.logic;

import com.rombalabs.strutstospringtoolkit.jspservices.transformers.TagTransformer;
import com.rombalabs.strutstospringtoolkit.jspservices.transformers.struts.BaseTagTransformer;
import org.jsoup.nodes.Element;

/**
 * A catch-all class to handle several kinds of Struts logic TLD primitives.
 */
public class DefaultLogicTagTransformer extends BaseTagTransformer {

    public DefaultLogicTagTransformer() {
        replacementMap.put("logic:present", "c:if");
        replacementMap.put("logic:empty", "c:if");
        replacementMap.put("logic:notEmpty", "c:if");
    }

    @Override
    protected void convertElement(Element element, String newTagName) {
        var oldTagName = element.tagName();
        boolean emptyTag = oldTagName.equals("logic:empty");
        var name = element.attr("name");
        var property = element.attr("property");

        element.renameTagPreserveProperties(newTagName);
        element.attr("test", createEqualityTestString(emptyTag, name, property));
        element.removeAttr("name");
        element.removeAttr("property");
        element.removeAttr("value");
    }

    private static String createEqualityTestString(boolean testForEmpty, String name, String property) {
        return "${" +
                (testForEmpty ? "empty" : "!empty") + " " +
                name +
                (!property.isEmpty() ? "." + property : "") +
                "}";
    }
}
