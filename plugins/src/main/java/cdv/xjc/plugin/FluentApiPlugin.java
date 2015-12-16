package cdv.xjc.plugin;

import com.sun.codemodel.*;

/**
 * XJC plugin providing setter methods in fluent style
 *
 * @author Dmitry Coolga
 *         15.12.2015
 */
public class FluentApiPlugin extends XJCPluginBase {

    @Override
    protected String getOption() {
        return "fluent";
    }

    @Override
    protected String getDescription() {
        return "generates fluent API";
    }

    @Override
    protected void makeInstrumentation(JDefinedClass cls, JCodeModel model) {
        for (JFieldVar field : getInstanceFields(cls)) {
            JMethod setter = cls.method(JMod.PUBLIC, cls, field.name());
            JVar parameter = setter.param(field.type(), "value");
            JBlock content = setter.body();
            content.assign(JExpr._this().ref(field.name()), parameter);
            content._return(JExpr._this());
        }
    }

}
