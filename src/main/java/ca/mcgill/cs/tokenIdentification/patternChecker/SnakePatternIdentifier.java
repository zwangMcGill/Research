package ca.mcgill.cs.tokenIdentification.patternChecker;

import com.github.javaparser.ast.body.MethodDeclaration;

import java.util.HashMap;
import java.util.Map;

public class SnakePatternIdentifier extends PatternIdentifier {
    protected String REGEX = "_";

    public SnakePatternIdentifier(MethodDeclaration methodDeclaration, String template) {
        super(methodDeclaration, template);
    }

    @Override
    public Map<String, String> operate() {
        String[] templateTokens = super.chunk(template);
        String[] methodTokens = super.chunk(methodDeclaration.getNameAsString());

        assert(templateTokens.length == methodTokens.length);

        HashMap<String, String> map
                = new HashMap<String, String>();

        for (int i = 0; i < templateTokens.length; i++) {
            map.put(templateTokens[i], methodTokens[i]);
        }

        return map;
    }
}
