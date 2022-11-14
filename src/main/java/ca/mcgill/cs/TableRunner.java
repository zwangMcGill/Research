package ca.mcgill.cs;

import ca.mcgill.cs.table.MyTable;
import ca.mcgill.cs.toolbar.GroupByFocalMethodAction;
import ca.mcgill.cs.toolbar.GroupByScenarioAction;
import ca.mcgill.cs.toolbar.ReRunAction;
import ca.mcgill.cs.utils.MyExecutorUtil;
import com.intellij.execution.DefaultExecutionResult;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.ExecutionManager;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.RunProfile;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.execution.filters.TextConsoleBuilder;
import com.intellij.execution.filters.TextConsoleBuilderFactory;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.execution.ui.RunContentDescriptor;
import com.intellij.execution.ui.RunnerLayoutUi;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.content.Content;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class TableRunner {

    private Project project = null;
    private VirtualFile vFile = null;
    private ConsoleView consoleView = null;
    private final Runnable rerun;

    public TableRunner(@NotNull Project project, @NotNull VirtualFile vFile, Runnable rerun) {
        this.project = project;
        this.vFile = vFile;
        this.consoleView = createConsoleView(project);
        this.rerun = rerun;
    }

    private ConsoleView createConsoleView(Project project) {
        TextConsoleBuilder consoleBuilder = TextConsoleBuilderFactory.getInstance().createBuilder(project);
        ConsoleView console = consoleBuilder.getConsole();
        return console;
    }

    public void run() {

        // return Executor
        Executor executor = MyExecutorUtil.getRunExecutorInstance(TableExecutor.PLUGIN_ID);
        if (executor == null) {
            return;
        }

        // create RunnerLayoutUi
        final RunnerLayoutUi.Factory factory = RunnerLayoutUi.Factory.getInstance(project);
        RunnerLayoutUi layoutUi = factory.create("id2", "title2", "session name2", project);

        // create description
        RunContentDescriptor descriptor = new RunContentDescriptor(new RunProfile() {
            @Nullable
            @Override
            public RunProfileState getState(@NotNull Executor executor, @NotNull ExecutionEnvironment environment) throws ExecutionException {
                return null;
            }

            @NotNull
            @Override
            public String getName() {
                return "name";
            }

            @Nullable
            @Override
            public Icon getIcon() {
                return null;
            }
        }, new DefaultExecutionResult(), layoutUi);
        descriptor.setExecutionId(System.nanoTime());

        MyTable myTable = new MyTable(vFile);

        final Content content = layoutUi.createContent("contentId", myTable.getBrowsersTable(), "Test Suite Info", AllIcons.Debugger.Console, myTable.getBrowsersTable());
        content.setCloseable(true);
        layoutUi.addContent(content);
        layoutUi.getOptions().setLeftToolbar(createActionToolbar(consoleView), "Toolbar");

        ExecutionManager.getInstance(project).getContentManager().showRunContent(executor, descriptor);
    }

    private ActionGroup createActionToolbar(ConsoleView consoleView) {
        final DefaultActionGroup actionGroup = new DefaultActionGroup();
        actionGroup.add(new GroupByFocalMethodAction());
        actionGroup.add(new GroupByScenarioAction());
        actionGroup.add(new ReRunAction(rerun));

        return actionGroup;
    }


}