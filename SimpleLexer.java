import java.io.CharArrayReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *简单的词法分析器
 *为脚本语言产生token
 */
public class SimpleLexer {
    public static void main(String args[]) {
        SimpleLexer lexer = new SimpleLexer();
       var tokens = new ArrayList<Token>();
        System.out.println(tokens);
        String script = "int age = 26;";
        System.out.println("parse :" + script);
        SimpleTokenReader tokenReader = lexer.tokenize(script);
    }
    private StringBuffer tokenText = null;
    private SimpleToken token = null;
    private List<Token> tokens = null;
    private boolean isAlpha(int ch) {return ch >= 'a' && ch <='z' || ch >= 'A' && ch <='Z';}
    private boolean isDigit(int ch) {return ch >= '0' && ch <= '9';}
    private boolean isBlank(int ch) {return ch == ' ' || ch == '\t' || ch == '\n';}
    private DfaState initToken(char ch) {
        DfaState newState = DfaState.Initial;
        if (isAlpha(ch)) {
//            token.type
        }
        return newState;
    }
    public SimpleTokenReader tokenize(String code) {
        tokens = new ArrayList<Token>();
        CharArrayReader reader = new CharArrayReader(code.toCharArray());
        tokenText = new StringBuffer();
        token = new SimpleToken();
        int ich = 0;
        char ch = 0;
        DfaState state = DfaState.Initial;
        try {
            while ((ich = reader.read()) != -1) {
                ch = (char) ich;
                switch (state) {
                    case Initial:
                        state = initToken(ch);
                        break;
                    case Id:
                        if(isAlpha(ch) || isDigit(ch)) {
                            tokenText.append(ch);
                        } else {
                            state = initToken(ch);
                        }
                    case GT:
                        if (ch == '=') {
                            token.type = TokenType.GE;
                            state = DfaState.GE;
                            tokenText.append(ch);
                        } else {
                            state = initToken(ch);
                        }
                        break;
                    case GE:
                    case Assignment:
                    case RightParen:
                        state = initToken(ch);
                        break;
                }
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(tokens.size());

        return new SimpleTokenReader(tokens);
    }
    private class SimpleTokenReader implements TokenReader {
       List<Token> tokens = null;
       int pos = 0;

       public SimpleTokenReader(List<Token> tokens) {this.tokens = tokens;}

        @Override
        public Token read() {
           if (pos < tokens.size())  {
               return tokens.get(pos++);
           }
            return null;
        }

        @Override
        public Token peek() {
            if (pos < tokens.size()) {
                return tokens.get(pos);
            }
            return null;
        }

        @Override
        public void unread() {
            if (pos > 0) {
                pos--;
            }
        }

        @Override
        public int getPosition() {
            return pos;
        }

        @Override
        public void setPosition(int position) {
            if (position >= 0 && position < tokens.size()) {
                pos = position;
            }
        }
    }
    private final class SimpleToken implements Token {
        // Token 类型
        private TokenType type = null;
        // 文本值
        private String text = null;

        @Override
        public TokenType getType() { return type; }

        @Override
        public String getText() { return text; }
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

