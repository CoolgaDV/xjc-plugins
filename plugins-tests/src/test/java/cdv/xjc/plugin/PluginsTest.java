package cdv.xjc.plugin;

import org.junit.Assert;
import org.junit.Test;
import plugin.xjc.cdv.test.Person;

/**
 * TODO: comment
 *
 * @author Dmitry Coolga
 *         12.12.2015
 */
public class PluginsTest {

    @Test
    public void testToSting() throws Exception {
        Person person = new Person();
        person.setName("foo");
        person.setAge(42);
        person.setAddress("bar");
        Assert.assertEquals("Person{name='foo', age=42, address='bar'}", person.toString());
    }

}
