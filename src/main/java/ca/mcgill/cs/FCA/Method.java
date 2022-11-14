package ca.mcgill.cs.FCA;

import ca.mcgill.cs.utils.ParsingUtil;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;

import java.util.List;

public class Method {
    private final MethodDeclaration methodDeclaration;

    public Method(MethodDeclaration methodDeclaration) {
        this.methodDeclaration = methodDeclaration;
    }

    public MethodDeclaration getMethodDeclaration() {
        return methodDeclaration;
    }

    public List<String> getProperties() {
        AnnotationExpr annotationExpr = ParsingUtil.findOneMethodAnnotations(methodDeclaration);
        return ParsingUtil.getMethodAnnotationValues(annotationExpr);
    }

    @Override
    public String toString() {
        return methodDeclaration.getNameAsString();
    }
}

