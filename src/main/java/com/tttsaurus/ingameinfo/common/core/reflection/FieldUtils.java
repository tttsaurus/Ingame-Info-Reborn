package com.tttsaurus.ingameinfo.common.core.reflection;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public final class FieldUtils
{
    public static Field getFieldByNameIncludingSuperclasses(Class<?> clazz, String fieldName) throws NoSuchFieldException
    {
        List<Field> fields = getAllFieldsIncludingSuperclasses(clazz);
        for (Field field : fields)
            if (field.getName().equals(fieldName))
                return field;
        throw new NoSuchFieldException("Cannot find " + fieldName + " from " + clazz.getName() + " including its superclasses.");
    }

    public static List<Field> getAllFieldsIncludingSuperclasses(Class<?> clazz)
    {
        List<Field> fields = new ArrayList<>();
        Class<?> current = clazz;
        while (current != null && current != Object.class)
        {
            Field[] declaredFields = current.getDeclaredFields();
            for (Field field : declaredFields)
            {
                field.setAccessible(true);
                fields.add(field);
            }
            current = current.getSuperclass();
        }
        return fields;
    }
}
