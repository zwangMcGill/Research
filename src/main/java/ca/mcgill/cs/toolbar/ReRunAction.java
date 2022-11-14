package ca.mcgill.cs.toolbar;


import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAware;

/**
 * 重启 ConsoleTool
 */
public class ReRunAction extends AnAction implements DumbAware {

    /**
     * restart ToolWindow
     */
    private final Runnable rerun;

    public ReRunAction(Runnable rerun) {
        super("Rerun", "Rerun", AllIcons.Actions.Restart);
        this.rerun = rerun;
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        rerun.run();
    }

    /**
     * Listening to the action
     */
    @Override
    public void update(AnActionEvent e) {
        e.getPresentation().setVisible(rerun != null);
    }
}