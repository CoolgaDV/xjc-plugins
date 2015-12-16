package cdv.xjc.plugin;

import com.sun.codemodel.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * XJC plugin providing both constructor with parameter for every field and default constructor
 *
 * @author Dmitry Coolga
 *         31.05.14 19:05
 */
public class ConstructorPlugin extends XJCPluginBase {

    @Override
    protected String getOption() {
        return "ctor";
    }

    @Override
    protected String getDescription() {
        return "generate constructors";
    }

    @Override
    protected void makeInstrumentation(JDefinedClass cls, JCodeModel model) {
        generateDefaultConstructor(cls);
        generateConstructor(cls);
    }

    private void generateDefaultConstructor(JDefinedClass cls) {
        cls.constructor(JMod.PUBLIC);
    }

    private void generateConstructor(JDefinedClass cls) {
        JMethod ctor = cls.constructor(JMod.PUBLIC);
        List<JFieldVar> fields = getInstanceFields(cls);
        Map<String, JVar> params = new HashMap<>();
        for (JFieldVar field : fields) {
            String name = field.name();
            JVar variable = ctor.param(field.type(), name);
            params.put(name, variable);
        }
        for (JFieldVar field : fields) {
            String name = field.name();
            ctor.body().assign(JExpr._this().ref(name), params.get(name));
        }
    }

}
