package com.rombalabs.strutstospringtoolkit.jspservices.transformers;

import org.jsoup.nodes.Element;

public interface TagTransformer {
    boolean processElement(Element element);
}
