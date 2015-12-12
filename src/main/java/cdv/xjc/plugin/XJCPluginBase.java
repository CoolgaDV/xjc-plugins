package cdv.xjc.plugin;

import com.sun.codemodel.*;
import com.sun.tools.xjc.Options;
import com.sun.tools.xjc.Plugin;
import com.sun.tools.xjc.outline.ClassOutline;
import com.sun.tools.xjc.outline.Outline;
import org.xml.sax.ErrorHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Super class for XJC plugins
 *
 * @author Dmitry Coolga
 *         31.05.14 19:39
 */
public abstract class XJCPluginBase extends Plugin {

    protected abstract String getOption();

    protected abstract String getDescription();

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

    protected List<JFieldVar> getFields(JDefinedClass cls) {
        List<JFieldVar> fields = new ArrayList<JFieldVar>();
        extractFields(cls, fields);
        JClass superclass = cls._extends();
        while (superclass instanceof JDefinedClass) {
            extractFields((JDefinedClass) superclass, fields);
            superclass = superclass._extends();
        }
        return fields;
    }

    protected void extractFields(JDefinedClass cls, List<JFieldVar> fields) {
        for (final JFieldVar field : cls.fields().values()) {
            boolean isStatic = (field.mods().getValue() & JMod.STATIC) > 0;
            if ( ! isStatic) {
                fields.add(field);
            }
        }
    }

}
