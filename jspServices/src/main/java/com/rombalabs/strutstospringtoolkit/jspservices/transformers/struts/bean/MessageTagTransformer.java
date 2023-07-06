package com.rombalabs.strutstospringtoolkit.jspservices.transformers.struts.bean;

import com.rombalabs.strutstospringtoolkit.jspservices.transformers.struts.BaseTagTransformer;
import org.jsoup.nodes.Element;

public class MessageTagTransformer extends BaseTagTransformer {
    public MessageTagTransformer() {
        replacementMap.put("bean:message", "spring:message");
    }

    @Override
    protected void convertElement(Element element, String newTagName) {
        var key = element.attr("key");
        element.tagName(newTagName);

        element.attr("code", key);
        element.removeAttr("key");
    }
}
