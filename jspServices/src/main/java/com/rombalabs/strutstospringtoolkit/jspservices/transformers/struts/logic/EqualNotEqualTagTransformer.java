package com.rombalabs.strutstospringtoolkit.jspservices.transformers.struts.logic;

import com.rombalabs.strutstospringtoolkit.jspservices.transformers.TagTransformer;
import com.rombalabs.strutstospringtoolkit.jspservices.transformers.struts.BaseTagTransformer;
import org.jsoup.nodes.Element;

import java.util.ArrayList;

public class EqualNotEqualTagTransformer extends BaseTagTransformer implements TagTransformer {

    @Override
    public boolean processElement(Element element) {

        if (!element.tagName().equals("logic:equal") &&
            !element.tagName().equals("logic:notEqual"))
            return false;

        var name = element.attr("name");
        var property = element.attr("property");


        // First, check if nearby siblings are logic:equal or logic:notequal with the same target variable
        var chooseChoices = new ArrayList<Element>();
        for (var sibling : element.siblingElements()) {
            if (siblingBelongsInsideChoose(sibling, name, property)) {
                chooseChoices.add(sibling);
            } else {
                break;
            }
        }

        // If it looks like this should be a c:choose, then take special action.
        if (!chooseChoices.isEmpty()) {
            var choose = element.ownerDocument().createElement("c:choose");

            // Make the c:choose our parent.
            element.before(choose);
            choose.appendChild(element);

            // Now reconfigure ourselves
            convertElement(element, "c:when");

            // Next, iterate on all the chooseChoices and do the same.
            for (var choice : chooseChoices) {
                convertElement(choice, "c:when");
                choose.appendChild(choice);
            }
        } else {
            convertElement(element, "c:if");
        }

        return true;
    }

    @Override
    protected void convertElement(Element element, String newTagName) {
        var oldTagName = element.tagName();
        boolean equal = oldTagName.equals("logic:equal");
        var name = element.attr("name");
        var property = element.attr("property");
        var value = element.attr("value");

        element.tagName(newTagName);
        element.attr("test", createEqualityTestString(equal, name, property, value));
        element.removeAttr("name");
        element.removeAttr("property");
        element.removeAttr("value");
    }

    private String createEqualityTestString(boolean equal, String name, String property, String value) {
        value = value.replaceAll("<%=(.*)%>", "$1").trim();

        if(value.contains("<%"))
            value = value.replace("<","")
                    .replace("%","")
                    .replace("=","")
                    .replace(">","");

        return "${" + name +
                (!property.isEmpty() ? "." + property : "") +
                (equal ? " eq " : " ne ") +
                value + "}";
    }

    public boolean siblingBelongsInsideChoose(Element sibling, String referenceName, String referenceProperty) {

        var nextName = sibling.attr("name");
        var nextProperty = sibling.attr("property");

        // "c:choose" scenarios:
        // Scenario 1:  Both are "logic:[not]equal", same property + name + parameter, different values
        // Scenario 2:  Sibling is inverse of first element, same property + name + parameter + value
        return ((sibling.tagName().equals("logic:equal") ||
                 sibling.tagName().equals("logic:notEqual")) &&
                referenceName.equals(nextName) &&
                referenceProperty.equals(nextProperty));
    }
}