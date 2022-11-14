package ca.mcgill.cs.tokenIdentification.patternChecker;

import com.github.javaparser.ast.body.MethodDeclaration;

import java.util.Map;

public class CamelPatternChecker extends PatternIdentifier {
    protected String REGEX = "(?<=[a-z])(?=[A-Z])|(?<=[A-Z])(?=[A-Z][a-z])|(?<=[0-9])(?=[A-Z][a-z])|(?<=[a-zA-Z])(?=[0-9])";

    public CamelPatternChecker(MethodDeclaration methodDeclaration, String template){
        super(methodDeclaration,template);
    }

    @Override
    public Map<String, String> operate() {
        return null;
    }
}
