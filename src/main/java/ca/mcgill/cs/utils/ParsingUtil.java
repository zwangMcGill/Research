package ca.mcgill.cs.utils;

import ca.mcgill.cs.FCA.Node;
import ca.mcgill.cs.annotations.Tokens;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.testFramework.LightVirtualFile;
import org.junit.Test;
import org.junit.jupiter.api.Nested;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class ParsingUtil {

    CompilationUnit cu;

    public ParsingUtil(CompilationUnit cu) {

        this.cu = cu;
    }

    // create the file object of selected java file
    public static FileInputStream getSelectedFileObject(VirtualFile vFile) {
        String path = vFile != null ? vFile.getPath() : ((LightVirtualFile) vFile).getOriginalFile().getPath();
        FileInputStream file = null;

        try {
            file = new FileInputStream(path);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }

        return file;
    }

    // find @Tokens annotation
    public static AnnotationExpr findOneMethodAnnotations(MethodDeclaration methodDeclaration) {
        if (methodDeclaration.getAnnotationByClass(Tokens.class).isPresent())
            return methodDeclaration.getAnnotationByClass(Tokens.class).get();
        return null;
    }

    /**
     * get the values of an annotation expression
     */
    public static List<String> getMethodAnnotationValues(AnnotationExpr annotationExpr) {
        Map<String, String> annotations = getAnnotationProperty(annotationExpr);
        return new ArrayList<>(annotations.values());
    }

    /**
     * get the properties and values of an annotation
     *
     * @return Map<Property, Value>
     */
    private static Map<String, String> getAnnotationProperty(AnnotationExpr annotationExpr) {
        Map<String, String> annotations = new HashMap<>();
        String nameAsString = annotationExpr.getNameAsString();
        if (nameAsString.equals("Tokens")) {
            if (annotationExpr.isMarkerAnnotationExpr()) {
                MarkerAnnotationExpr markerAnnotationExpr = annotationExpr.asMarkerAnnotationExpr();
                // No value within a "marker annotation" (e.g. `@Deprecated`)
            } else if (annotationExpr.isSingleMemberAnnotationExpr()) {
                SingleMemberAnnotationExpr singleMemberAnnotationExpr = annotationExpr.asSingleMemberAnnotationExpr();
                // A single member annotation expression has just ONE value and no key (e.g. `@Deprecated("reason")`)
                Expression memberValue = singleMemberAnnotationExpr.getMemberValue();
//                System.out.println("memberValue = " + memberValue);
            } else if (annotationExpr.isNormalAnnotationExpr()) {
                NormalAnnotationExpr normalAnnotationExpr = annotationExpr.asNormalAnnotationExpr();
                // A "Normal" annotation has one or more keys and values (e.g. `@Annotation3(param1 = "value1", param2 = "value2")`)
                NodeList<MemberValuePair> pairs = normalAnnotationExpr.getPairs();

                pairs.forEach(pair -> {
                    SimpleName name = pair.getName();
                    Expression value = pair.getValue();
                    annotations.put(name.asString(), value.toString());
                });
            } else {
                throw new IllegalStateException("Unknown type of annotation");
            }

            // once you have located the node -- remove self from the AST:
//            boolean removeResult = annotationExpr.remove();
        }
        return annotations;
    }

    /**
     * retrieve the Token annotation expression of all methods
     *
     * @return Map<Method Name, Annotation>
     */
    public Map<MethodDeclaration, List<AnnotationExpr>> findAllMethodsAnnotations() {

        List<MethodDeclaration> methodDeclarations = findAllTestMethods();

        return methodDeclarations.stream()
                .filter(e -> e.isAnnotationPresent(Tokens.class))
                .collect(Collectors.toMap(e -> e, e -> e.getAnnotationByClass(Tokens.class).stream().collect(Collectors.toList())));

    }

    // find all methods annotated with @Test and @Tokens from JUnit framework
    public List<MethodDeclaration> findAllTestMethods() {
        return cu.findAll(MethodDeclaration.class)
                .stream()
                .filter(e -> e.isAnnotationPresent(Test.class) && e.isAnnotationPresent(Tokens.class))
                .collect(Collectors.toList());
    }

    /**
     * get all the properties and values of all Method annotated with @Tokens
     *
     * @return Map<Method Name, Map < Property, Value>>
     */
    public Map<MethodDeclaration, Map<String, String>> getAnnotationProperty() {

        Map<MethodDeclaration, Map<String, String>> properties = new HashMap<>();
        Map<MethodDeclaration, List<AnnotationExpr>> annotationExprs = findAllMethodsAnnotations();

        for (Map.Entry<MethodDeclaration, List<AnnotationExpr>> entry : annotationExprs.entrySet()) {
            for (AnnotationExpr annotationExpr : entry.getValue()) {
                properties.put(entry.getKey(), getAnnotationProperty(annotationExpr));
            }
        }
        return properties;
    }

    public Map<MethodDeclaration, String> getSpecifiedAnnotationProperty(String Property) {
        Map<MethodDeclaration, String> methodGroupByProperty = new HashMap<>();
        Map<MethodDeclaration, Map<String, String>> annotations = this.getAnnotationProperty();

        for (Map.Entry<MethodDeclaration, Map<String, String>> entry : annotations.entrySet()) {
            for (Map.Entry<String, String> subEntry : entry.getValue().entrySet()) {
                if (Objects.equals(subEntry.getKey(), Property)) {
                    methodGroupByProperty.put(entry.getKey(), subEntry.getValue());
                }
            }
        }

        return methodGroupByProperty;
    }

    public Map<String, List<MethodDeclaration>> groupBySpecifiedToken(String Property) {
        Map<MethodDeclaration, String> methodsWithSpecifiedProperty = getSpecifiedAnnotationProperty(Property);
        Map<String, List<MethodDeclaration>> methodGroupByProperty = new HashMap<>();

        for (Map.Entry<MethodDeclaration, String> entry : methodsWithSpecifiedProperty.entrySet()) {
            List<MethodDeclaration> list = methodGroupByProperty.computeIfAbsent(entry.getValue(), k -> new ArrayList<MethodDeclaration>());
            list.add(entry.getKey());
        }

        return methodGroupByProperty;
    }

    // add nested class code to file
    public void addNestedClass(Project project, PsiFile psiFile, String token) {
        Map<String, List<MethodDeclaration>> methods = groupBySpecifiedToken(token);
        PsiJavaFile psiJavaFile = (PsiJavaFile) psiFile;

        assert psiJavaFile != null;

        final PsiClass[] classes = psiJavaFile.getClasses();

        assert classes.length != 0;

        Arrays.stream(classes).forEach(e -> System.out.println(e.getName()));

        Arrays.stream(classes[0].getAllInnerClasses()).forEach(e -> System.out.println(e.getName()));

        PsiElementFactory elementFactory = JavaPsiFacade.getElementFactory(project);

        methods.forEach((k, v) -> {
//            PsiElementFactory elementFactory = JavaPsiFacade.getElementFactory(project);
            PsiClass innerClass = elementFactory.createClass(k.replace("\"", ""));
            PsiModifierList classModifierList = innerClass.getModifierList();
            if (classModifierList != null) {
                classModifierList.addAnnotation(Nested.class.getName());

                v.forEach(e -> {
                    innerClass.add(elementFactory.createMethodFromText(e.toString().replace("\r\n", ""), innerClass));
                });
            }
            classes[0].add(innerClass);
        });
    }

    // this is a test for adding nested class
    public void addMultipleNestedCLass(Project project, PsiFile psiFile, Node root) {
        PsiJavaFile psiJavaFile = (PsiJavaFile) psiFile;
        assert psiJavaFile != null;

        final PsiClass[] classes = psiJavaFile.getClasses();
        PsiElementFactory elementFactory = JavaPsiFacade.getElementFactory(project);
        PsiClass innerClass = elementFactory.createClass("Test");
        PsiClass newCLass = addMultipleNestedCLass(elementFactory, innerClass, root);

        classes[0].add(newCLass);
    }

    private PsiClass addMultipleNestedCLass(PsiElementFactory elementFactory, PsiClass psiClass, Node root) {
        if (root == null) return psiClass;
        if (root.children == null || root.children.size() == 0) {
            PsiElement newMethod = elementFactory.createMethodFromText(root.values.get(0).getMethodDeclaration().toString().replace("\r\n", ""), psiClass);
            psiClass.add(newMethod);
            System.out.println(Arrays.toString(psiClass.getAllMethods()));
            return psiClass;
        }

        List<Node> children = root.children;

        PsiClass innerClass = elementFactory.createClass("Test");

        PsiModifierList classModifierList = innerClass.getModifierList();
        if (classModifierList != null) {
            classModifierList.addAnnotation(Nested.class.getName());
        }

        psiClass.add(innerClass);

        for (Node aChild : children) {
            addMultipleNestedCLass(elementFactory, innerClass, aChild);
        }
        System.out.println(Arrays.toString(psiClass.getAllInnerClasses()));
        return psiClass;
    }


    //try Java Parser to add nested class
    public void JPaddMultipleNestedCLass(Node root) {
        // get the class declaration 'Outer'
        ClassOrInterfaceDeclaration cid = cu.findFirst(ClassOrInterfaceDeclaration.class).get();
        // create a new class declaration
//        ClassOrInterfaceDeclaration inner = new ClassOrInterfaceDeclaration(new NodeList(), false, cid.getNameAsString());
        ClassOrInterfaceDeclaration result = JPaddMultipleNestedCLass(cid, root);
        // set the new class declaration into the members list
//        cid.getMembers().add(result);

//        System.out.println(cu.toString());
    }

    //this is the default correct method using FCAMatrix not Static FCA
//    private ClassOrInterfaceDeclaration JPaddMultipleNestedCLass(ClassOrInterfaceDeclaration inner, Node root){
//        if (root == null) return inner;
//        if (root.children == null || root.children.size() == 0) {
//            MethodDeclaration newMethod = root.values.get(0).getMethodDeclaration();
//            inner.addMember(newMethod);
//            return inner;
//        }
//
//        List<Node> children = root.children;
//
//        ClassOrInterfaceDeclaration nestedInner = new ClassOrInterfaceDeclaration(new NodeList(), false, "Inner");
//
//        inner.getMembers().add(nestedInner);
//        for (Node aChild : children){
//            JPaddMultipleNestedCLass(nestedInner,aChild);
//        }
//        return inner;
//    }

    // new adding nested method using Staic FCA remove if not working.
    private ClassOrInterfaceDeclaration JPaddMultipleNestedCLass(ClassOrInterfaceDeclaration inner, Node root) {
        if (root == null) return inner;
        if (root.children == null || root.children.size() == 0) {
            MethodDeclaration newMethod = root.values.get(0).getMethodDeclaration();
            removeMethod(cu, newMethod.getNameAsString());
            inner.addMember(newMethod);
            return inner;
        }

        List<Node> children = root.children;

        String NestedName;
        if (!root.getNestedName().isEmpty()) {

            NestedName = String.join("", root.getNestedName());
            System.out.println("this is the name " + NestedName);
        } else {
            NestedName = "Inner";

        }

        ClassOrInterfaceDeclaration nestedInner = new ClassOrInterfaceDeclaration(new NodeList(), false, NestedName.replaceAll("\"", ""));
        nestedInner.addAnnotation(Nested.class);
        inner.getMembers().add(nestedInner);
        for (Node aChild : children) {
            JPaddMultipleNestedCLass(nestedInner, aChild);
        }

        return inner;
    }

    private void removeMethod(CompilationUnit cu, String methodName) {
        cu.walk(MethodDeclaration.class, e -> {
            if (methodName.equals(e.getNameAsString())) {
                e.remove();
            }
        });
    }

    public void addAnnotation(MethodDeclaration methodDeclaration, Map<String, String> properties) {
        assert methodDeclaration.getAnnotationByClass(Test.class).isPresent();

        if (methodDeclaration.getAnnotationByClass(Tokens.class).isEmpty()) {
            methodDeclaration.addAnnotation(Tokens.class);
            AnnotationExpr annotationExpr = methodDeclaration.getAnnotationByClass(Tokens.class).get();

            for (Map.Entry<String, String> entry : properties.entrySet()) {
                ((NormalAnnotationExpr) annotationExpr).addPair(entry.getKey(), entry.getValue());
            }
        } else {
            AnnotationExpr annotationExpr = methodDeclaration.getAnnotationByClass(Tokens.class).get();
            Map<String, String> map = getAnnotationProperty(annotationExpr);
            for (Map.Entry<String, String> entry : properties.entrySet()) {
                if (!map.containsKey(entry.getKey())) {
                    ((NormalAnnotationExpr) annotationExpr).addPair(entry.getKey(), entry.getValue());
                }
            }

        }
    }
}
















































