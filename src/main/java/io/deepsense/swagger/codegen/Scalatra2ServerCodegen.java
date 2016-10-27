package io.swagger.codegen.languages;

import io.swagger.codegen.*;
import io.swagger.codegen.languages.scalatra2.ScalaEnumImportsFixer;
import io.swagger.models.Swagger;
import io.swagger.models.properties.ArrayProperty;
import io.swagger.models.properties.MapProperty;
import io.swagger.models.properties.Property;

import java.io.File;
import java.util.*;

public class Scalatra2ServerCodegen extends DefaultCodegen implements CodegenConfig {
    protected String invokerPackage = "io.swagger.server";
    protected String groupId = "io.swagger";
    protected String artifactId = "swagger-server";
    protected String artifactVersion = "1.0.0";
    protected String sourceFolder = "src/main/scala";

    public Scalatra2ServerCodegen() {
        super();
        outputFolder = "generated-code/scalatra2";
        modelTemplateFiles.put("model.mustache", ".scala");
        apiTemplateFiles.put("api.mustache", ".scala");
        embeddedTemplateDir = templateDir = "scalatra2";
        apiPackage = "com.wordnik.server.api";
        modelPackage = "com.wordnik.server.model";

        reservedWords.addAll(
                Arrays.asList(
                        "abstract", "continue", "for", "new", "switch", "assert",
                        "default", "if", "package", "synchronized", "boolean", "do", "goto", "private",
                        "this", "break", "double", "implements", "protected", "throw", "byte", "else",
                        "import", "public", "throws", "case", "enum", "instanceof", "return", "transient",
                        "catch", "extends", "int", "short", "try", "char", "final", "interface", "static",
                        "void", "class", "finally", "long", "strictfp", "volatile", "const", "float",
                        "native", "super", "while")
        );

        defaultIncludes = new HashSet<String>(
                Arrays.asList("double",
                        "Int",
                        "Long",
                        "Float",
                        "Double",
                        "char",
                        "float",
                        "String",
                        "boolean",
                        "Boolean",
                        "Double",
                        "Integer",
                        "Long",
                        "Float",
                        "List",
                        "Set",
                        "Map")
        );

        typeMapping.put("integer", "Int");
        typeMapping.put("long", "Long");
        //TODO binary should be mapped to byte array
        // mapped to String as a workaround
        typeMapping.put("binary", "String");

        additionalProperties.put(CodegenConstants.INVOKER_PACKAGE, invokerPackage);
        additionalProperties.put(CodegenConstants.GROUP_ID, groupId);
        additionalProperties.put(CodegenConstants.ARTIFACT_ID, artifactId);
        additionalProperties.put(CodegenConstants.ARTIFACT_VERSION, artifactVersion);

        languageSpecificPrimitives = new HashSet<String>(
                Arrays.asList(
                        "String",
                        "boolean",
                        "Boolean",
                        "Double",
                        "Integer",
                        "Long",
                        "Float",
                        "Object")
        );
        instantiationTypes.put("array", "ArrayList");
        instantiationTypes.put("map", "HashMap");

        importMapping = new HashMap<String, String>();
        importMapping.put("BigDecimal", "java.math.BigDecimal");
        importMapping.put("UUID", "java.util.UUID");
        importMapping.put("File", "java.io.File");
        importMapping.put("Date", "java.util.Date");
        importMapping.put("Timestamp", "java.sql.Timestamp");
        importMapping.put("Map", "java.util.Map");
        importMapping.put("HashMap", "java.util.HashMap");
        importMapping.put("Array", "java.util.List");
        importMapping.put("ArrayList", "java.util.ArrayList");
        importMapping.put("DateTime", "org.joda.time.DateTime");
        importMapping.put("LocalDateTime", "org.joda.time.LocalDateTime");
        importMapping.put("LocalDate", "org.joda.time.LocalDate");
        importMapping.put("LocalTime", "org.joda.time.LocalTime");

        cliOptions.add(new CliOption(CodegenConstants.MODEL_PACKAGE, CodegenConstants.MODEL_PACKAGE_DESC));
        cliOptions.add(new CliOption(CodegenConstants.API_PACKAGE, CodegenConstants.API_PACKAGE_DESC));
    }

    @Override
    public void preprocessSwagger(Swagger swagger) {
        super.preprocessSwagger(swagger);
        supportingFiles.add(new SupportingFile("EnumJsonFormats.mustache", apiRelativeFileFolder(), "EnumJsonFormats.scala"));
        supportingFiles.add(new SupportingFile("ApiException.mustache", apiRelativeFileFolder(), "ApiException.scala"));
        supportingFiles.add(new SupportingFile("TypeConverters.mustache", apiRelativeFileFolder(), "TypeConverters.scala"));
    }

    @Override
    public Map<String, Object> postProcessSupportingFileData(Map<String, Object> objs) {
        return super.postProcessSupportingFileData(objs);
    }

    @Override
    public CodegenType getTag() {
        return CodegenType.SERVER;
    }

    @Override
    public String getName() {
        return "scalatra2";
    }

    @Override
    public String getHelp() {
        return "Generates yet another Scala server application with Scalatra.";
    }

    @Override
    public String escapeReservedWord(String name) {
        return "_" + name;
    }

    @Override
    public String apiFileFolder() {
        return outputFolder + "/" + apiRelativeFileFolder();
    }

    private String apiRelativeFileFolder() {
        return sourceFolder + "/" + apiPackage().replace('.', File.separatorChar);
    }

    @Override
    public String modelFileFolder() {
        return outputFolder + "/" + sourceFolder + "/" + modelPackage().replace('.', File.separatorChar);
    }

    @Override
    public Map<String, Object> postProcessOperations(Map<String, Object> objs) {
        Map<String, Object> operations = (Map<String, Object>) objs.get("operations");
        List<CodegenOperation> operationList = (List<CodegenOperation>) operations.get("operation");
        for (CodegenOperation op : operationList) {
            op.httpMethod = op.httpMethod.toLowerCase();

            // Transforms /{id}/ to /{:id}/
            op.path = op.path.replace('{', ':').replace("}", "");
        }
        return objs;
    }

    @Override
    public Map<String, Object> postProcessAllModels(Map<String, Object> objs) {
        ScalaEnumImportsFixer.fixImportForTopLevelEnums(objs);
        return super.postProcessAllModels(objs);
    }

    @Override
    public String getTypeDeclaration(Property p) {
        if (p instanceof ArrayProperty) {
            ArrayProperty ap = (ArrayProperty) p;
            Property inner = ap.getItems();
            return getSwaggerType(p) + "[" + getTypeDeclaration(inner) + "]";
        } else if (p instanceof MapProperty) {
            MapProperty mp = (MapProperty) p;
            Property inner = mp.getAdditionalProperties();

            return getSwaggerType(p) + "[String, " + getTypeDeclaration(inner) + "]";
        }
        return super.getTypeDeclaration(p);
    }

    @Override
    public String getSwaggerType(Property p) {
        String swaggerType = super.getSwaggerType(p);
        String type;
        if (typeMapping.containsKey(swaggerType)) {
            type = typeMapping.get(swaggerType);
            if (languageSpecificPrimitives.contains(type)) {
                return toModelName(type);
            }
        } else {
            type = swaggerType;
        }
        return toModelName(type);
    }
}
