package cdv.xjc.plugin;

import com.sun.codemodel.*;

/**
 * XJC plugin providing toString method implementation
 *
 * @author Dmitry Coolga
 *         30.05.14 22:45
 */
public class ToStringPlugin extends XJCPluginBase {

    private static final String TO_STRING_METHOD = "toString";
    private static final String APPEND_METHOD = "append";

    @Override
    protected String getOption() {
        return "str";
    }

    @Override
    protected String getDescription() {
        return "generate simple implementation of toString method";
    }

    @Override
    protected void makeInstrumentation(JDefinedClass cls, JCodeModel model) {
        JMethod toStringMethod = cls.method(JMod.PUBLIC, String.class, TO_STRING_METHOD);
        toStringMethod.annotate(Override.class);
        JBlock content = toStringMethod.body();

        JType builderType = model.ref(StringBuilder.class);
        JVar result = content.decl(builderType, "description",
                JExpr._new(builderType).arg(JExpr.lit(cls.name() + "{")));

        boolean first = true;
        for (JFieldVar field : getInstanceFields(cls)) {
            String line = field.name() + "=";
            if ( ! first) {
                line = ", " + line;
            }
            boolean isString = field.type().equals(model.ref(String.class));
            if (isString) {
                line += "'";
            }
            JInvocation invocation = result
                    .invoke(APPEND_METHOD).arg(JExpr.lit(line))
                    .invoke(APPEND_METHOD).arg(field);
            if (isString) {
                invocation = invocation.invoke(APPEND_METHOD).arg(JExpr.lit('\''));
            }
            content.add(invocation);
            first = false;
        }
        content.add(result.invoke(APPEND_METHOD).arg(JExpr.lit('}')));
        content._return(result.invoke(TO_STRING_METHOD));
    }

}
