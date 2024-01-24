package com.website.repository.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Id;
import java.lang.reflect.Field;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
public class EntityUpdater {

    private final EntityManager entityManager;

    @Transactional
    public <T> void updateEntity(T sourceEntity, Long targetId) {
        T targetEntity = entityManager.find((Class<? extends T>) sourceEntity.getClass(), targetId);

        if (targetEntity != null) {
            updateNonNullFields(sourceEntity, targetEntity);

            entityManager.merge(targetEntity);
        }
    }

    private <T> void updateNonNullFields(T sourceEntity, T targetEntity) {
        Class<?> clazz = sourceEntity.getClass();
        Field[] fields = clazz.getFields();

        for (Field field : fields) {
            //Enable access to private fields
            field.setAccessible(true);
            //Check if the fields has the Id annotation, skip it if true
            if (field.isAnnotationPresent(Id.class)) {
                continue;
            }

            //Get the value of the field from the source entity
            try {
                Object sourceValue = field.get(sourceEntity);

                //Update the corresponding field in the target entity
                field.set(targetEntity, sourceValue);
            } catch (IllegalAccessException e) {
                log.error("EntityUpdator sett data err", e);
            }
        }
    }
}
