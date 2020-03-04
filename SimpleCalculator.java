import java.util.ArrayList;
import java.util.Collection;
import java.util.List;




public class SimpleCalculator {

    public static void main(String[] args) {

        String script = "int a = b+3;";
        System.out.println("parse variable declaration statements:" + script);
        SimpleLexer lexer = new SimpleLexer();
        TokenReader tokens = lexer.tokenize(script);
        try {
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    private class SimpleASTNode implements ASTNode {

    }
}