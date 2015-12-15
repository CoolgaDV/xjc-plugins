package cdv.xjc.plugin;

import org.junit.Assert;
import org.junit.Test;
import plugin.xjc.cdv.test.Person;

/**
 * Tests for features provided by custom plugins to xjc
 *
 * @author Dmitry Coolga
 *         12.12.2015
 */
public class PluginsTest {

    /**
     * Test for constructor xjc plugin (flag -Xctor)
     */
    @Test
    public void testConstructor() {
        Person person = new Person("foo", 42, "bar");
        Assert.assertEquals("foo", person.getName());
        Assert.assertEquals(42, person.getAge());
        Assert.assertEquals("bar", person.getAddress());
    }

    /**
     * Test for toString xjc plugin (flag -Xstr)
     */
    @Test
    public void testToSting() {
        Person person = new Person();
        person.setName("foo");
        person.setAge(42);
        person.setAddress("bar");
        Assert.assertEquals("Person{name='foo', age=42, address='bar'}", person.toString());
    }

    /**
     * Test for fluent API xjc plugin (flag -Xfluent)
     */
    @Test
    public void testFluentApi() {
        Person person = new Person().name("foo").age(42).address("bar");
        Assert.assertEquals("foo", person.getName());
        Assert.assertEquals(42, person.getAge());
        Assert.assertEquals("bar", person.getAddress());
    }

}
