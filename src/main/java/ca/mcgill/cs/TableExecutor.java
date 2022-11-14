package ca.mcgill.cs;

import com.intellij.execution.Executor;
import com.intellij.icons.AllIcons;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class TableExecutor extends Executor {

    public static final String PLUGIN_ID = "TableExecutor";

    public static final String TOOL_WINDOW_ID = "TableExecutor";

    public static final String CONTEXT_ACTION_ID = "261006077";

    @NotNull
    @Override
    public String getId() {
        return PLUGIN_ID;
    }

    @Override
    public @NotNull String getToolWindowId() {
        return TOOL_WINDOW_ID;
    }

    @Override
    public @NotNull Icon getToolWindowIcon() {
        return AllIcons.Actions.Redo;
    }

    @NotNull
    @Override
    public Icon getIcon() {
        return AllIcons.Actions.Redo;
    }

    @Override
    public Icon getDisabledIcon() {
        return AllIcons.Actions.Redo;
    }

    @Override
    public String getDescription() {
        return TOOL_WINDOW_ID;
    }

    @NotNull
    @Override
    public String getActionName() {
        return TOOL_WINDOW_ID;
    }

    @NotNull
    @Override
    public String getStartActionText() {
        return TOOL_WINDOW_ID;
    }

    @Override
    public String getContextActionId() {
        return CONTEXT_ACTION_ID;
    }

    @Override
    public String getHelpId() {
        return TOOL_WINDOW_ID;
    }

}
