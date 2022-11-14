package ca.mcgill.cs.utils;

import com.intellij.execution.Executor;
import com.intellij.execution.ExecutorRegistry;

public enum MyExecutorUtil {
    ;

    /**
     * return running Executor
     *
     * @param id Executor id
     */
    public static Executor getRunExecutorInstance(String id) {
        return ExecutorRegistry.getInstance().getExecutorById(id);
    }
}