package ru.terra.tschat.shared.core;

import java.util.List;

/**
 * Date: 20.03.15
 * Time: 16:36
 */
public abstract class AbstractClassSearcher<T> {
    public abstract List<T> load(String packageName, Class annotaion);
}
