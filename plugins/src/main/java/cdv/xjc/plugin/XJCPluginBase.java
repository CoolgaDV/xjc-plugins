package cdv.xjc.plugin;

import com.sun.codemodel.*;
import com.sun.tools.xjc.Options;
import com.sun.tools.xjc.Plugin;
import com.sun.tools.xjc.outline.ClassOutline;
import com.sun.tools.xjc.outline.Outline;
import org.xml.sax.ErrorHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Super class for XJC plugins
 *
 * @author Dmitry Coolga
 *         31.05.14 19:39
 */
public abstract class XJCPluginBase extends Plugin {

    /**
     * Returns plugin option name (without -X prefix)
     *
     * @return plugin option name
     */
    protected abstract String getOption();

    /**
     * Returns short plugin description
     *
     * @return short plugin description
     */
    protected abstract String getDescription();

    /**
     * Instrument target jaxb bean class providing additional facilities
     *
     * @param cls target jaxb bean class
     * @param model AST helper
     */
    protected abstract void makeInstrumentation(JDefinedClass cls, JCodeModel model);

    @Override
    public String getOptionName() {
        return "X" + getOption();
    }

    @Override
    public String getUsage() {
        return "  -X" + getOption() + "    :  " + getDescription();
    }

    @Override
    public boolean run(Outline outline, Options opt, ErrorHandler errorHandler) {
        for (ClassOutline classOutline : outline.getClasses()) {
            JDefinedClass cls = classOutline.implClass;
            if ( ! cls.isAbstract()) {
                makeInstrumentation(cls, outline.getCodeModel());
            }
        }
        return true;
    }

    protected List<JFieldVar> getInstanceFields(JDefinedClass cls) {
        List<JFieldVar> fields = new ArrayList<>();
        fields.addAll(collectInstanceFields(cls));
        JClass superClass = cls._extends();
        while (superClass instanceof JDefinedClass) {
            fields.addAll(collectInstanceFields((JDefinedClass) superClass));
            superClass = superClass._extends();
        }
        return fields;
    }

    private List<JFieldVar> collectInstanceFields(JDefinedClass cls) {
        return cls.fields().values().stream()
                .filter(field -> (field.mods().getValue() & JMod.STATIC) <= 0)
                .collect(Collectors.toList());
    }

}
