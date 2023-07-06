package com.rombalabs.strutstospringtoolkit.jspservices.transformers.struts;

import com.rombalabs.strutstospringtoolkit.jspservices.transformers.TagTransformer;
import org.jsoup.nodes.Element;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseTagTransformer implements TagTransformer {

    protected final Map<String, String> replacementMap = new HashMap<>();

    @Override
    public boolean processElement(Element element) {
        if (!replacementMap.containsKey(element.tagName()))
            return false;

        convertElement(element, replacementMap.get(element.tagName()));

        return true;
    }

    protected abstract void convertElement(Element element, String newTagName);
}
