package com.rombalabs.strutstospringtoolkit.jspservices.transformers.preprocessing;

import com.rombalabs.strutstospringtoolkit.jspservices.transformers.PreprocessTransformer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class BasePreprocessTransformer implements PreprocessTransformer {
    protected final Logger logger = LogManager.getLogger(this.getClass());

    @Override
    public abstract String processText(String inputText);
}
