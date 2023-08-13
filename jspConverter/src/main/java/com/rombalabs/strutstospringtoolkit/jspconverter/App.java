package com.rombalabs.strutstospringtoolkit.jspconverter;

import com.rombalabs.strutstospringtoolkit.jspservices.PreProcessor;
import com.rombalabs.strutstospringtoolkit.jspservices.StrutsProcessor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import static java.lang.System.exit;

/*
 * This Java source file was generated by the Gradle 'init' task.
 */
public class App {

    private static final Logger logger = LogManager.getLogger();

    public static void main(String[] args) throws IOException {

        boolean rewriteInPlace = false;
        boolean preprocessOnly = false;
        boolean jsoupOnly = false;

        if(args.length < 1)
        {
            printUsage();
            exit(1);
        }

        //TODO This is gross, use a proper CLI arguments library!
        for (String arg : Arrays.stream(args).skip(1).toArray(String[]::new)) {
            switch (arg) {
                case "--replace":
                    rewriteInPlace = true;
                    break;

                case "--preprocessOnly":
                    preprocessOnly = true;
                    break;

                case "--jsoupOnly":
                    jsoupOnly = true;
                    break;

                default:
                    logger.fatal("Invalid argument: " + arg);
                    printUsage();
                    exit(1);
                    return;
            }
        }

        String inputFile = args[0];
        var outputPath = inputFile.replace(".jsp", "_converted.jsp");
        logger.info("Storing output in " + (rewriteInPlace ? "temporary " : "") + "location: " + outputPath);
        if (!jsoupOnly) {
            var preProcessor = new PreProcessor();
            preProcessor.processFile(inputFile, outputPath);
        }

        if (!preprocessOnly) {
            if (jsoupOnly) {
                outputPath = inputFile;
            }
            var strutsProcessor = new StrutsProcessor();
            strutsProcessor.processFile(outputPath, outputPath);
        }

        if (rewriteInPlace) {
            logger.info("Rewriting existing input file...");
            File outputFile = new File(outputPath);
            FileUtils.copyFile(outputFile, new File(inputFile));
            FileUtils.delete(outputFile);
        }

    }

    private static void printUsage() {
        System.out.println("Usage:");
        System.out.println("jspConverter [FILE] [OPTIONS]\n");
        System.out.println("Options:");
        System.out.println("\t--replace\tOverwrites the input file");
        System.out.println("\t--preprocessOnly\tOnly runs the preprocessor (doesn't load the whole document as DOM).");
        System.out.println("\t--jsoupOnly\tOnly runs the JSoup DOM processor.");
    }
}
