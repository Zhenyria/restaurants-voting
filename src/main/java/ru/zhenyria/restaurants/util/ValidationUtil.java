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

    public static void checkNew(AbstractBaseEntity entity) {
        if (!entity.isNew()) {
            throw new IllegalArgumentException(entity + " must be new (id=null)");
        }
    }
}
