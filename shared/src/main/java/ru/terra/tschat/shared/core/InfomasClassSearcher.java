package ru.terra.tschat.shared.core;

import eu.infomas.annotation.AnnotationDetector;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс используемый для поиска аннотированных классов
 */

public class InfomasClassSearcher<T> extends AbstractClassSearcher<T> {

    /**
     * Метод поиска аннотированных классов в
     * пакете @param packageName
     * по аннотации @param annotaion
     */
    @Override
    public List<T> load(String packageName, final Class annotaion) {
        final List<T> ret = new ArrayList<>();
        AnnotationDetector.TypeReporter typeReporter = new AnnotationDetector.TypeReporter() {
            @Override
            public void reportTypeAnnotation(Class<? extends Annotation> annotation, String className) {
                Object o = null;
                try {
                    Class<T> c = (Class<T>) ClassLoader.getSystemClassLoader().loadClass(className);
                    if (c.getAnnotation(annotation) != null)
                        o = c.newInstance();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                if (o != null)
                    ret.add((T) o);
            }

            @Override
            public Class<? extends Annotation>[] annotations() {
                return new Class[]{annotaion};
            }
        };

        final AnnotationDetector cf = new AnnotationDetector(typeReporter);
        try {
            cf.detect(packageName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }
}