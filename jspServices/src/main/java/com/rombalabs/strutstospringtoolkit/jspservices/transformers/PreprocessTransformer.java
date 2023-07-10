package com.rombalabs.strutstospringtoolkit.jspservices.transformers;

import org.jsoup.nodes.Element;

public interface PreprocessTransformer {
    String processText(String inputText);
}
