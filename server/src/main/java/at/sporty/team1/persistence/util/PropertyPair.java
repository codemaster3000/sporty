package at.sporty.team1.persistence.util;

/**
 * Created by sereGkaluv on 11-Nov-15.
 */
public class PropertyPair<T> {
    private final String _property;
    private final T _value;

    public PropertyPair(String property, T value){
        _property = property;
        _value = value;
    }

    public T getValue() {
        return _value;
    }

    public String getProperty() {
        return _property;
    }
}
