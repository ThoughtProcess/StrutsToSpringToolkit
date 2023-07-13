package com.rombalabs.strutstospringtoolkit.jspservices.transformers.struts;

import com.rombalabs.strutstospringtoolkit.jspservices.transformers.TagTransformer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.nodes.Element;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseTagTransformer implements TagTransformer {

    protected final Logger logger = LogManager.getLogger(this.getClass());
    protected final Map<String, String> replacementMap = new HashMap<>();

    @Override
    public boolean processElement(Element element) {
        if (!replacementMap.containsKey(element.tagName()))
            return false;

        logger.info("Now transforming: " + element.tagName());

        convertElement(element, replacementMap.get(element.tagName()));

        return true;
    }

    protected abstract void convertElement(Element element, String newTagName);
}
