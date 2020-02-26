
/**
 *简单的词法分析器
 *为脚本语言产生token
 */
public class SimpleLexer {
    public static void main(String args[]) {
//        SimpleLexer lexer = new SimpleLexer();

        String script = "int age = 26;";
        System.out.println("parse :" + script);
    }
    private boolean isAlpha(int ch) {return ch >= 'a' && ch <='z' || ch >= 'A' && ch <='Z';}
    private DfaState initToken(char ch) {
        DfaState newState = DfaState.Initial;
        if (isAlpha(ch)) {
            token.type
        }
    }

    private enum DfaState {
        Initial,

        If, Id_if1, Id_if2, Else, Id_else1, Id_else2, Id_else3, Id_else4, Int, Id_int1, Id_int2, Id_int3, Id, GT, GE,

        Assignment,

        Plus, Minus, Star, Slash,

        SemiColon,
        LeftParen,
        RightParen,

        IntLiteral
    }
}

