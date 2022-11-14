package ca.mcgill.cs.toolbar;

import ca.mcgill.cs.action.TableAction;
import ca.mcgill.cs.utils.ParsingUtil;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import org.jetbrains.annotations.NotNull;

public class GroupByFocalMethodAction extends AnAction {

    public GroupByFocalMethodAction() {
        super("Group by FocalMethod", "Group by FocalMethod", AllIcons.Actions.GroupByMethod);
    }

    /**
     * Implement this method to provide your action handler.
     *
     * @param e Carries information on the invocation place
     */
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        // implement a method at backend
        WriteCommandAction.runWriteCommandAction(e.getData(PlatformDataKeys.PROJECT), () -> {
            AnActionEvent action = TableAction.getInitialAction();
            CompilationUnit cu = StaticJavaParser.parse(ParsingUtil.getSelectedFileObject(action.getData(PlatformDataKeys.VIRTUAL_FILE)));
            ParsingUtil parser = new ParsingUtil(cu);
            parser.addNestedClass(action.getData(PlatformDataKeys.PROJECT), action.getData(PlatformDataKeys.PSI_FILE), "focalMethod");
        });
    }
}
