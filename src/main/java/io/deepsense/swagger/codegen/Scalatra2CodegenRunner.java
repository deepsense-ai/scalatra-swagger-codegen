package io.deepsense.swagger.codegen;

import io.swagger.codegen.ClientOptInput;
import io.swagger.codegen.DefaultGenerator;
import io.swagger.codegen.config.CodegenConfigurator;

public class Scalatra2CodegenRunner {

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

    public static void generate(String inputSpecPath, String outputDir, String codePackage) {
        CodegenConfigurator configurator = new CodegenConfigurator();
        configurator.setInputSpec(inputSpecPath);
        configurator.setOutputDir(outputDir);
        configurator.setLang(Scalatra2ServerCodegen.class.getName());
        configurator.setApiPackage(codePackage + ".api");
        configurator.setModelPackage(codePackage + ".model");

        final ClientOptInput clientOptInput = configurator.toClientOptInput();

        new DefaultGenerator().opts(clientOptInput).generate();
    }

}
