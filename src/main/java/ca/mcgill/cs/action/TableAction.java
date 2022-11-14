package ca.mcgill.cs.action;

import ca.mcgill.cs.TableRunner;
import ca.mcgill.cs.utils.MyProjectUtil;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

public class TableAction extends AnAction {

    private static AnActionEvent INITIAL_ACTION = null;

    public static AnActionEvent getInitialAction() {
        return INITIAL_ACTION;
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        INITIAL_ACTION = e;
        runExecutor(e, e.getProject(), e.getData(PlatformDataKeys.VIRTUAL_FILE));

    }

    public void runExecutor(AnActionEvent e, Project project, VirtualFile vFile) {
        assert (project!=null && vFile!=null);

        Runnable rerun = () -> runExecutor(e, project, vFile);

        MyProjectUtil.setRunning(project, "running", true);

        TableRunner executor = new TableRunner(project, vFile, rerun);

        executor.run();
    }
}