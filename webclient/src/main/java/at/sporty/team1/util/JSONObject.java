package at.sporty.team1.util;


import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by sereGkaluv on 29-Dec-2015.
 */
public class JSONObject {
    private static final String GETTER_PREFIX = "get";
    private static final String GET_CLASS_METHOD = GETTER_PREFIX + "Class";

    private final List<String> _jsonBody;

    public JSONObject() {
        _jsonBody = new LinkedList<>();
    }

    /**
     * Appends simple key : value pair to JSON body.
     *
     * @param key key with which the specified value is to be associated.
     * @param value value for the given key.
     * @return instance of {@code this} JSONObject.
     */
    public JSONObject appendPair(String key, Object value)
    throws IllegalAccessException, IntrospectionException, InvocationTargetException {
        _jsonBody.add(String.format("\"%s\":%s", key, formatSingleValue(value)));
        return this;
    }

    /**
     * Appends simple key : [value1, ...] pair to JSON body.
     *
     * @param key key with which the specified value(-s) is to be associated.
     * @param values value(-s) for the given key.
     * @return instance of {@code this} JSONObject.
     */
    public JSONObject appendPair(String key, Object[] values)
    throws IllegalAccessException, IntrospectionException, InvocationTargetException {
        StringBuilder valueBody = new StringBuilder();

        valueBody.append("[");

        for (int i = 0; i < values.length; ++i) {
            valueBody.append(values[i]);
            if (i < values.length + 1) valueBody.append(",");
        }

        valueBody.append("]");

        return appendPair(key, valueBody);
    }

    /**
     * Appends object properties to the JSON body. Mapping is performed via getter methods.
     *
     * @param object object to be appended to JSON body.
     * @return instance of {@code this} JSONObject.
     * @throws IntrospectionException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public JSONObject appendObject(Object object)
    throws IntrospectionException, InvocationTargetException, IllegalAccessException {

        PropertyDescriptor[] descriptors = Introspector.getBeanInfo(object.getClass()).getPropertyDescriptors();

        for (PropertyDescriptor propertyDescriptor : descriptors) {
            Method method = propertyDescriptor.getReadMethod();

            if (
                method != null &&
                method.getName().startsWith(GETTER_PREFIX) &&
                !method.getName().equals(GET_CLASS_METHOD)
            ) {
                char[] methodName = method.getName().toCharArray();
                char key[] = Arrays.copyOfRange(methodName, GETTER_PREFIX.length(), methodName.length);

                if (key.length > 0) {
                    key[0] = Character.toLowerCase(key[0]);
                    appendPair(new String(key), method.invoke(object));
                }
            }
        }

        return this;
    }

    @Override
    public String toString() {
        StringBuilder jsonBuilder = new StringBuilder();

        jsonBuilder.append("{");

        Iterator<String> pairIterator = _jsonBody.iterator();
        while (pairIterator.hasNext()) {
            jsonBuilder.append(pairIterator.next());
            if (pairIterator.hasNext()) jsonBuilder.append(",");
        }

        jsonBuilder.append("}");

        return jsonBuilder.toString();
    }

    /**
     * Formats string key : value pair into a simple JSON object.
     * This method should be used only for simple objects.
     *
     * @param key key with which the specified value is to be associated.
     * @param value value for the given key.
     * @return formatted pair as JSON.
     */
    public static String formatPairAsJSON(String key, String value) {
        return String.format("{\"%s\":\"%s\"}", key, value);
    }

    /**
     * Formats single value according to its type.
     *
     * @param value value to be formatted.
     * @return formatted value.
     */
    private String formatSingleValue(Object value)
    throws IllegalAccessException, IntrospectionException, InvocationTargetException {
        if (value == null) return "\"N\\A\"";

        else if (value instanceof Integer || value instanceof Boolean) return value.toString();
        else if (value instanceof String) return String.format("\"%s\"", value);
        else return new JSONObject().appendObject(value).toString();
    }
}
