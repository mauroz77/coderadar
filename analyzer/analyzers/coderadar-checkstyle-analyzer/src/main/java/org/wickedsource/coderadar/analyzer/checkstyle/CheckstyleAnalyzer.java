package org.wickedsource.coderadar.analyzer.checkstyle;

import com.puppycrawl.tools.checkstyle.Checker;
import com.puppycrawl.tools.checkstyle.ConfigurationLoader;
import com.puppycrawl.tools.checkstyle.api.CheckstyleException;
import com.puppycrawl.tools.checkstyle.api.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wickedsource.coderadar.analyzer.api.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

public class CheckstyleAnalyzer implements Analyzer {

    private Logger logger = LoggerFactory.getLogger(CheckstyleAnalyzer.class);

    private CheckstyleAnalyzerConfiguration analyzerConfig;

    private Checker checker;

    private CoderadarAuditListener auditListener;

    @Override
    public void configure(Properties properties) throws AnalyzerConfigurationException {
        this.analyzerConfig = new CheckstyleAnalyzerConfiguration(properties);
        checker = new Checker();
        try {
            auditListener = new CoderadarAuditListener();
            Configuration checkstyleConfig = createCheckstyleConfiguration();
            final ClassLoader moduleClassLoader = Checker.class.getClassLoader();
            checker.setModuleClassLoader(moduleClassLoader);
            checker.configure(checkstyleConfig);
            checker.addListener(auditListener);
        } catch (CheckstyleException e) {
            throw new AnalyzerConfigurationException(e);
        }
    }

    @Override
    public AnalyzerFilter getFilter() {
        return new AnalyzerFilter() {
            @Override
            public boolean acceptFilename(String filename) {
                return filename.endsWith(".java");
            }

            @Override
            public boolean acceptBinary() {
                return false;
            }
        };
    }


    private Configuration createCheckstyleConfiguration() throws CheckstyleException {
        return ConfigurationLoader.loadConfiguration(
                analyzerConfig.getConfigLocation().getAbsolutePath(), new CheckstylePropertiesResolver(analyzerConfig.getBackingProperties()));
    }

    @Override
    public FileMetrics analyzeFile(byte[] fileContent) throws AnalyzerException {
        File fileToAnalyze = null;
        try {
            fileToAnalyze = createTempFile(fileContent);
            auditListener.reset();
            checker.process(Arrays.asList(fileToAnalyze));
            return auditListener.getMetrics();
        } catch (CheckstyleException | IOException e) {
            throw new AnalyzerException(e);
        } finally {
            if (fileToAnalyze != null && !fileToAnalyze.delete()) {
                logger.warn("Could not delete temporary file {}", fileToAnalyze);
            }
        }
    }

    @Override
    public void destroy() {
        checker.destroy();
    }

    private File createTempFile(byte[] fileContent) throws IOException {
        File file = File.createTempFile("coderadar-", ".java");
        file.deleteOnExit();
        FileOutputStream out = new FileOutputStream(file);
        out.write(fileContent);
        out.close();
        return file;
    }
}
