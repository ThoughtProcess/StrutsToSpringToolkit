package com.rombalabs.strutstospringtoolkit.jspservices;

import com.rombalabs.strutstospringtoolkit.jspservices.transformers.PreprocessTransformer;
import com.rombalabs.strutstospringtoolkit.jspservices.transformers.preprocessing.AttributeInlineLogicTagTransformer;
import com.rombalabs.strutstospringtoolkit.jspservices.transformers.preprocessing.HtmlTagTransformer;
import com.rombalabs.strutstospringtoolkit.jspservices.transformers.preprocessing.InlineBeanWriteTagTransformer;
import com.rombalabs.strutstospringtoolkit.jspservices.transformers.preprocessing.InlineScriptletTransformer;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PreProcessor implements FileProcessor {

    protected static final Logger logger = LogManager.getLogger();

    private final List<PreprocessTransformer> transformers;

    public PreProcessor() {
        transformers = new ArrayList<>();

        // struts:bean transformers
        transformers.add(new HtmlTagTransformer());
        transformers.add(new InlineScriptletTransformer());
        transformers.add(new AttributeInlineLogicTagTransformer());
        transformers.add(new InlineBeanWriteTagTransformer());
    }

    @Override
    public String processFile(String filename, boolean rewrite) {
        var outputPath = filename.replace(".jsp", "_converted.jsp");
        logger.info("Loading file " + filename);
        logger.info("Storing output in " + (rewrite ? "temporary " : "") + "location: " + outputPath);

        try (var reader = new BufferedReader(new FileReader(filename))) {
            try (var writer = new BufferedWriter(new FileWriter(outputPath))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    var processedLine = processContent(line);
                    writer.write(processedLine);
                    writer.newLine();
                }
            }

            if (rewrite) {
                File outputFile = new File(outputPath);
                FileUtils.copyFile(outputFile, new File(filename));
                FileUtils.delete(outputFile);
                outputPath = filename;
            }

        } catch (IOException e) {
            logger.error("Failed to load file.", e);
            throw new RuntimeException(e);
        }

        return outputPath;
    }

    public String processContent(String content) {
        String result = content;
        for (PreprocessTransformer transformer : transformers) {
            result = transformer.processText(content);
            if (!result.contentEquals(content)) break;
        }

        return result;
    }
}
