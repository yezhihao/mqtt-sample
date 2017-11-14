package org.sample.mqtt.commons;

import org.sample.mqtt.commons.annotation.CreatedAt;
import org.sample.mqtt.commons.annotation.LastModified;
import org.sample.mqtt.commons.util.BeanUtils;

import javax.persistence.*;
import java.beans.BeanInfo;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * Created by Alan on 2017/2/22.
 */
public class DateEntityListener {

    @PrePersist
    public void touchForCreate(Object target) throws IllegalAccessException {
        BeanInfo beanInfo = BeanUtils.getBeanInfo(target.getClass());
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();

        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            Method readMethod = propertyDescriptor.getReadMethod();

            if (readMethod.getAnnotation(CreatedAt.class) != null)
                BeanUtils.setValue(target, propertyDescriptor.getWriteMethod(), new Date());

            if (readMethod.getAnnotation(LastModified.class) != null)
                BeanUtils.setValue(target, propertyDescriptor.getWriteMethod(), new Date());
        }
    }

    @PreUpdate
    public void touchForUpdate(Object target) throws IllegalAccessException {
        BeanInfo beanInfo = BeanUtils.getBeanInfo(target.getClass());
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();

        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            Method readMethod = propertyDescriptor.getReadMethod();
            if (readMethod.getAnnotation(LastModified.class) != null)
                BeanUtils.setValue(target, propertyDescriptor.getWriteMethod(), new Date());
        }
    }

}