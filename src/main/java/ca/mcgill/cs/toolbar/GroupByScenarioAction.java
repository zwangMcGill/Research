package ca.mcgill.cs.toolbar;

import ca.mcgill.cs.FCA.ECLATree;
import ca.mcgill.cs.FCA.Method;
import ca.mcgill.cs.FCA.Node;
import ca.mcgill.cs.FCA.StaticFCA;
import ca.mcgill.cs.action.TableAction;
import ca.mcgill.cs.utils.ParsingUtil;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

public class GroupByScenarioAction extends AnAction {

    public GroupByScenarioAction() {
        super("Group by Scenario", "Group by scenario", AllIcons.Actions.GroupByTestProduction);
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
//            parser.addNestedClass(action.getData(PlatformDataKeys.PROJECT),action.getData(PlatformDataKeys.PSI_FILE),"scenario");

            //this is a test for new adding nested class
            Map<MethodDeclaration, Map<String, String>> properties = parser.getAnnotationProperty();
            List<MethodDeclaration> objects = new ArrayList<MethodDeclaration>(properties.keySet());
            List<Method> methods = objects.stream().map(Method::new).collect(Collectors.toList());

            /*this is the default right FCA
            FCAMatrix<Method> test = new FCAMatrix<Method>(methods);
            test.powerSet();
            test.findAllIntersection(test.powerSet());
            HashMap<List<Method>, List<String>> data = FCAMatrix.getSortedIntersections();
            */

            //this is a test of static FCA file

            HashMap<List<Method>, List<String>> data = StaticFCA.createFCAMAtrix(methods);

            ECLATree eclat;
            Node parent = new Node(methods);
            eclat = new ECLATree(data, parent);
            Node root = eclat.addAllLevel();
//            parser.addMultipleNestedCLass(action.getData(PlatformDataKeys.PROJECT),action.getData(PlatformDataKeys.PSI_FILE),root);

            parser.JPaddMultipleNestedCLass(root);

            try {
                PsiFile psiFile = action.getData(PlatformDataKeys.PSI_FILE);
                assert psiFile != null;
                VirtualFile vFile = psiFile.getOriginalFile().getVirtualFile();
                String path = vFile.getPath();
                Files.write(new File(path).toPath(), Collections.singleton(cu.toString()), StandardCharsets.UTF_8);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        });
    }
}
