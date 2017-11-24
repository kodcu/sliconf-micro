package javaday.istanbul.sliconf.micro.util.json;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

public class MapParameterizedType implements ParameterizedType {

    private Type type;

    public MapParameterizedType(Type type) {
        this.type = type;
    }

    @Override
    public Type[] getActualTypeArguments() {
        return new Type[]{type};
    }

    @Override
    public Type getRawType() {
        return HashMap.class;
    }

    @Override
    public Type getOwnerType() {
        return null;
    }
}