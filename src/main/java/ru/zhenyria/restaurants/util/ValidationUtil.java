package ru.zhenyria.restaurants.util;

import ru.zhenyria.restaurants.model.AbstractBaseEntity;

public class ValidationUtil {

    private ValidationUtil() {
    }

    public static <T extends AbstractBaseEntity> T checkExisting(T entity) {
        if (entity == null) {
            throw new RuntimeException("Not exist with this id");
        }
        return entity;
    }

    public static void checkUsing(boolean using) {
        if (using) {
            throw new RuntimeException("The entity is already in use");
        }
    }

    public static void checkExisting(boolean isExist) {
        if (!isExist) {
            throw new RuntimeException("Not exist with this id");
        }
    }

    public static void checkNew(AbstractBaseEntity entity) {
        if (!entity.isNew()) {
            throw new IllegalArgumentException(entity + " must be new (id=null)");
        }
    }

    //  http://stackoverflow.com/a/28565320/548473
    public static Throwable getRootCause(Throwable t) {
        Throwable result = t;
        Throwable cause;

        while (null != (cause = result.getCause()) && (result != cause)) {
            result = cause;
        }
        return result;
    }
}
