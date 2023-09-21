package com.rombalabs.strutstospringtoolkit.jspservices.transformers.struts.logic;

import com.rombalabs.strutstospringtoolkit.jspservices.transformers.struts.BaseTagTransformer;
import org.jsoup.nodes.Element;

public class IterateTagTransformer extends BaseTagTransformer {

    public IterateTagTransformer() {
        replacementMap.put("logic:iterate", "c:forEach");
    }

    @Override
    protected void convertElement(Element element, String newTagName) {
        var id = element.attr("id");
        var name = element.attr("name");
        var property = element.attr("property");

        element.renameTagPreserveProperties(newTagName);
        element.attr("items", createExpressionLanguageString(name, property));
        element.attr("var", id);

        element.removeAttr("id");
        element.removeAttr("name");
        element.removeAttr("property");
    }

    private static String createExpressionLanguageString(String name, String property) {
        return "${" +
                name +
                (!name.isEmpty() && !property.isEmpty() ? "." : "") +
                property +
                "}";
    }
}
