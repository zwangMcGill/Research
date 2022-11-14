package ca.mcgill.cs.tokenIdentification.patternChecker;

import com.github.javaparser.ast.body.MethodDeclaration;

import java.awt.im.InputContext;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class PatternIdentifier {
    protected String REGEX ="";
    protected MethodDeclaration methodDeclaration;
    protected String template;

    public PatternIdentifier(MethodDeclaration methodDeclaration, String template){
        this.methodDeclaration = methodDeclaration;
        this.template = template;
    }

    public Boolean isValid(String actualString){
        Pattern pattern = Pattern.compile(REGEX);
        Matcher matcher = pattern.matcher(actualString);
        return matcher.matches();
    }

    public String[] chunk(String actualString) {

        assert actualString!= null;

        String[] res = {};

        if (isValid(actualString)){
            res = actualString.split(REGEX);
        }

        return res;
    }

    // generate formal context for POS Tagger
    public void generateFormalContext(String input) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append(methodDeclaration.getTypeAsString()).append(" ");
        sb.append(methodDeclaration.getNameAsString());
        sb.append("() " + "|");
        for (String s : chunk(methodDeclaration.getNameAsString())) {
            sb.append(" ").append(s);
        }

        File inputFile = new File(input);
        inputFile.createNewFile(); // if file already exists will do nothing
        FileOutputStream oFile = new FileOutputStream(inputFile, false);

        try {
            FileWriter myWriter = new FileWriter(input);
            myWriter.write(sb.toString());
            myWriter.close();
        } catch (IOException e) {
            System.out.println("Fail to write to Input file");
            e.printStackTrace();
        }
    }

    public abstract Map<String,String> operate();

}
