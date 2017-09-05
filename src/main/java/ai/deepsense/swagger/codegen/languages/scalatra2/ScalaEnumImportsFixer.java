package ai.deepsense.swagger.codegen.languages.scalatra2;

import io.swagger.codegen.CodegenModel;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/*
 * Swagger allows us to declare top-level objects in #/definitions as enums.
 *
 * Lets take for example enum StatusCompletion.
 *
 * Normal swagger generator would generate imports:
 *
 * import io.swagger.server.model.StatusCompletion
 *
 * Unfortunetely it doesn't work for scala. In scala in order to use enum we need to import its
 * inner type alias like this:
 *
 * import io.swagger.server.model.StatusCompletion.StatusCompletion
 *
 * This classes collects all top-level enum objects and then modifies
 * import in other model objects to work around this isue.
 */
public class ScalaEnumImportsFixer {

    public static Map<String, Object> fixImportForTopLevelEnums(Map<String, Object> objs) {
        final Set<CodegenModel> rootLevelEnums = getTopLevelEnums(objs);
        fixImportsForModels(objs, rootLevelEnums);
        return objs;
    }

    private static void fixImportsForModels(Map<String, Object> objs, Set<CodegenModel> rootLevelEnums) {
        final Set<String> rootLevelEnumClassNames =
                rootLevelEnums.stream()
                        .map(e -> e.classname)
                        .collect(Collectors.toSet());

        for (Object rawModelObjs : objs.values()) {
            Map<String, Object> modelObjs = (Map<String, Object>) rawModelObjs;
            List<Map<String, Object>> imports = (List<Map<String, Object>>) modelObjs.get("imports");
            for (Map<String, Object> impord : imports) {
                String importValue = (String) impord.get("import");
                String classNameFromImport = importValue.substring(importValue.lastIndexOf('.') + 1);
                if (rootLevelEnumClassNames.contains(classNameFromImport)) {
                    String validEnumImport = importValue + "." + classNameFromImport;
                    impord.put("import", validEnumImport);
                }
            }
        }
    }

    private static Set<CodegenModel> getTopLevelEnums(Map<String, Object> objs) {
        final Set<CodegenModel> rootLevelEnums = new HashSet<CodegenModel>();
        for (Object rawModelObjs : objs.values()) {
            Map<String, Object> modelObjs = (Map<String, Object>) rawModelObjs;
            List<Object> models = (List<Object>) modelObjs.get("models");
            for (Object rawModel : models) {
                Map<String, Object> model = (Map<String, Object>) rawModel;
                CodegenModel cm = (CodegenModel) model.get("model");
                if (cm.isEnum != null && cm.isEnum) {
                    rootLevelEnums.add(cm);
                }
            }
        }
        return rootLevelEnums;
    }

}
