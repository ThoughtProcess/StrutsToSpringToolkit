package com.rombalabs.strutstospringtoolkit.jspservices;

import com.rombalabs.strutstospringtoolkit.jspservices.transformers.TagTransformer;
import com.rombalabs.strutstospringtoolkit.jspservices.transformers.struts.bean.MessageTagTransformer;
import com.rombalabs.strutstospringtoolkit.jspservices.transformers.struts.bean.WriteTagTransformer;
import com.rombalabs.strutstospringtoolkit.jspservices.transformers.struts.html.DefaultHtmlTagTransformer;
import com.rombalabs.strutstospringtoolkit.jspservices.transformers.struts.html.OptionsCollectionTagTransformer;
import com.rombalabs.strutstospringtoolkit.jspservices.transformers.struts.html.OptionsTagTransformer;
import com.rombalabs.strutstospringtoolkit.jspservices.transformers.struts.html.SelectTagTransformer;
import com.rombalabs.strutstospringtoolkit.jspservices.transformers.struts.logic.DefaultLogicTagTransformer;
import com.rombalabs.strutstospringtoolkit.jspservices.transformers.struts.logic.EqualNotEqualTagTransformer;
import com.rombalabs.strutstospringtoolkit.jspservices.transformers.struts.logic.IterateTagTransformer;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Entities;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Parser;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class StrutsProcessor implements FileProcessor {

    protected static final Logger logger = LogManager.getLogger();

    private final List<TagTransformer> transformers;

    public StrutsProcessor() {
        transformers = new ArrayList<>();

        // struts:bean transformers
        transformers.add(new MessageTagTransformer());
        transformers.add(new WriteTagTransformer());

        // struts:html transformers
        transformers.add(new DefaultHtmlTagTransformer());
        transformers.add(new OptionsCollectionTagTransformer());
        transformers.add(new OptionsTagTransformer());
        transformers.add(new SelectTagTransformer());

        // struts:logic transformers
        transformers.add(new DefaultLogicTagTransformer());
        transformers.add(new EqualNotEqualTagTransformer());
        transformers.add(new IterateTagTransformer());
    }

    @Override
    public String processFile(String filename, boolean rewrite) {
        String content;
        try {
            logger.info("Loading file " + filename);
            content = FileUtils.readFileToString(new File(filename), StandardCharsets.UTF_8);
        } catch (IOException e) {
            logger.error("Failed to load file.", e);
            throw new RuntimeException(e);
        }

        var result = processContent(content);

        return save(result, filename, rewrite);
    }

    public Document processContent(String content) {
        logger.debug("Initializing parser and Jsoup components...");
        Parser parser = Parser.xmlParser();
        parser.settings(new ParseSettings(true, true)); // tag, attribute preserve case
        Document doc = Jsoup.parse(content,"", parser);

        logger.debug("Configuring document OutputSettings...");
        Document.OutputSettings settings = doc.outputSettings();
        settings.prettyPrint(false);
        settings.escapeMode(Entities.EscapeMode.extended);

        logger.info("Processing root element...");
        processElement(doc.root());

        return doc;
    }

    private Element processElement(Element targetElement) {
        if (targetElement != null) {
            logger.debug("Now on a " + targetElement.tagName() + " element");
            // Process the current element
            for (TagTransformer transformer : transformers) {
                if (transformer.processElement(targetElement)) break;
            }

            // Recurse down to children
            logger.debug("Recursively looping through its children...");
            for (Element child : targetElement.children()) {
                processElement(child);
            }
        }

        return targetElement;
    }

    private String save(Document doc, String originalFileName, boolean rewrite)
    {
        try {
            if (!rewrite)
                originalFileName = originalFileName.replace(".jsp", "_converted.jsp");

            logger.info("Saving output file to: " + originalFileName);
            File convertedFile = new File(originalFileName);
            FileUtils.writeStringToFile(convertedFile, Parser.unescapeEntities(doc.html(), false), "UTF-8");
            //FileUtils.writeStringToFile(convertedFile, doc.html(), "UTF-8");
        } catch (IOException e) {
            logger.fatal("Failed to save converted file...", e);
        }

        return originalFileName;
    }
}
