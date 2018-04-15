package org.pi4jrest.common;

import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Field;

public class ReflectionUtils {

    private static final boolean springAopPresent = ClassUtils.isPresent(
            "org.springframework.aop.framework.Advised", ReflectionUtils.class.getClassLoader());


    public static Object getField(@Nullable Object targetObject, String name) {
        return getField(targetObject, null, name);
    }

    public static Object getField(@Nullable Object targetObject, @Nullable Class<?> targetClass, String name) {
        Assert.isTrue(targetObject != null || targetClass != null,
                "Either targetObject or targetClass for the field must be specified");

        if (targetObject != null && springAopPresent) {
            targetObject = getUltimateTargetObject(targetObject);
        }

        if (targetClass == null) {
            targetClass = targetObject.getClass();
        }

        Field field = org.springframework.util.ReflectionUtils.findField(targetClass, name);
        if (field == null) {
            throw new IllegalArgumentException(String.format("Could not find field '%s' on %s or target class [%s]",
                    name, safeToString(targetObject), targetClass));
        }

        org.springframework.util.ReflectionUtils.makeAccessible(field);
        return org.springframework.util.ReflectionUtils.getField(field, targetObject);
    }

    private static <T> T getUltimateTargetObject(Object candidate) {
        Assert.notNull(candidate, "Candidate must not be null");
        try {
            if (AopUtils.isAopProxy(candidate) && candidate instanceof Advised) {
                Object target = ((Advised) candidate).getTargetSource().getTarget();
                if (target != null) {
                    return (T) getUltimateTargetObject(target);
                }
            }
        } catch (Throwable ex) {
            throw new IllegalStateException("Failed to unwrap proxied object", ex);
        }
        return (T) candidate;
    }

    private static String safeToString(@Nullable Object target) {
        try {
            return String.format("target object [%s]", target);
        } catch (Exception ex) {
            return String.format("target of type [%s] whose toString() method threw [%s]",
                    (target != null ? target.getClass().getName() : "unknown"), ex);
        }
    }
}
