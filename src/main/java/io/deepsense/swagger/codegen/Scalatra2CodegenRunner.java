package ai.deepsense.swagger.codegen;

import io.swagger.codegen.ClientOptInput;
import io.swagger.codegen.DefaultGenerator;
import io.swagger.codegen.config.CodegenConfigurator;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.apache.commons.io.filefilter.SuffixFileFilter;

import java.io.File;
import java.util.Collection;
import java.util.List;

public class Scalatra2CodegenRunner {

    // TODO Rewrite from java to scala

    /**
     * @param args should have 3 values: <br>
        args[0] - inputSpecPath - swagger.json schema file path <br>
        args[1] - outputDir - path where code will be generated to<br>
        args[2] - codePackage - java package to be used for generated code
     */
    public static void main(String[] args) {
        String inputSpecPath = args[0];
        String outputDir = args[1];
        String codePackage = args[2];
        generate(inputSpecPath, outputDir, codePackage);
    }

    public static Collection<File> generate(String inputSpecPath, String outputDir, String codePackage) {
        CodegenConfigurator configurator = new CodegenConfigurator();
        configurator.setInputSpec(inputSpecPath);
        configurator.setOutputDir(outputDir);
        configurator.setLang(Scalatra2ServerCodegen.class.getName());
        configurator.setApiPackage(codePackage + ".api");
        configurator.setModelPackage(codePackage + ".model");

        final ClientOptInput clientOptInput = configurator.toClientOptInput();
        new DefaultGenerator().opts(clientOptInput).generate();
        return getAllGeneratedFiles(outputDir);
    }

    private static Collection<File> getAllGeneratedFiles(String outputDir) {
        return FileUtils.listFiles(
                new File(outputDir),
                new SuffixFileFilter(".scala"),
                DirectoryFileFilter.DIRECTORY
        );
    }

}
