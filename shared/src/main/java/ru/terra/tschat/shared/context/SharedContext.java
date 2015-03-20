package ru.terra.tschat.shared.context;

import ru.terra.tschat.shared.core.AbstractClassSearcher;
import ru.terra.tschat.shared.core.impl.InfomasClassSearcher;

/**
 * Date: 20.03.15
 * Time: 16:35
 */
public class SharedContext {
    private static SharedContext instance = new SharedContext();
    private AbstractClassSearcher classSearcher;

    private SharedContext() {
        classSearcher = new InfomasClassSearcher();
    }

    public static SharedContext getInstance() {
        return instance;
    }

    public AbstractClassSearcher getClassSearcher() {
        return classSearcher;
    }

    public void setClassSearcher(AbstractClassSearcher classSearcher) {
        this.classSearcher = classSearcher;
    }
}
