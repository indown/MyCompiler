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
        String script = "int age = 26;";
        System.out.println("parse :" + script);
        SimpleTokenReader tokenReader = lexer.tokenize(script);
        dump(tokenReader);

        script = "inta age = 26;";
        System.out.println("\nparse :" + script);
        tokenReader = lexer.tokenize(script);
        dump(tokenReader);

        script = "in age = 26;";
        System.out.println("\nparse :" + script);
        tokenReader = lexer.tokenize(script);
        dump(tokenReader);

        script = "age >= 26;";
        System.out.println("\nparse :" + script);
        tokenReader = lexer.tokenize(script);
        dump(tokenReader);

        script = "age > 26;";
        System.out.println("\nparse :" + script);
        tokenReader = lexer.tokenize(script);
        dump(tokenReader);
    }
    private StringBuffer tokenText = null;
    private SimpleToken token = null;
    private List<Token> tokens = null;
    private boolean isAlpha(int ch) {return ch >= 'a' && ch <='z' || ch >= 'A' && ch <='Z';}
    private boolean isDigit(int ch) {return ch >= '0' && ch <= '9';}
    private boolean isBlank(int ch) {return ch == ' ' || ch == '\t' || ch == '\n';}
    private DfaState initToken(char ch) {
        if (tokenText.length() > 0) {
            token.text = tokenText.toString();
            tokens.add(token);
            tokenText = new StringBuffer();
            token = new SimpleToken();
        }
        DfaState newState = DfaState.Initial;
        if (isAlpha(ch)) {
            if (ch == 'i') {
                newState = DfaState.Id_int1;
            } else {
                newState = DfaState.Id;
            }
            token.type = TokenType.Identifier;
            tokenText.append(ch);
        }else if (isDigit(ch)) {
            newState = DfaState.IntLiteral;
            token.type = TokenType.IntLiteral;
            tokenText.append(ch);
        }
        else if (ch == '=') {
            newState = DfaState.Assignment;
            token.type = TokenType.Assignment;
            tokenText.append(ch);
        }
        else if (ch == '>') {
            newState = DfaState.GT;
            token.type = TokenType.GT;
            tokenText.append(ch);
        }
        else if (ch == '+') {
            newState = DfaState.Plus;
            token.type = TokenType.Plus;
            tokenText.append(ch);
        }
        else if (ch == '*') {
            newState = DfaState.Star;
            token.type = TokenType.Star;
            tokenText.append(ch);
        }
        else if (ch == ';') {
            newState = DfaState.SemiColon;
            token.type = TokenType.SemiColon;
            tokenText.append(ch);
        }
        else {
            newState = DfaState.Initial;
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
                        break;
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
                    case Plus:
                    case Star:
                    case SemiColon:
                    case RightParen:
                        state = initToken(ch);
                        break;
                    case IntLiteral:
                        if (isDigit(ch)){
                            tokenText.append(ch);
                        }else {
                            state = initToken(ch);
                        }
                        break;
                    case Id_int1:
                        if (ch == 'n') {
                            state = DfaState.Id_int2;
                            tokenText.append(ch);
                        }else if (isDigit(ch) || isAlpha(ch)) {
                            state = DfaState.Id;
                            tokenText.append(ch);
                        }else {
                            state = initToken(ch);
                        }
                        break;
                    case Id_int2:
                        if (ch == 't') {
                            state = DfaState.Id_int3;
                            tokenText.append(ch);
                        }else if (isDigit(ch) || isAlpha(ch)) {
                            state = DfaState.Id;
                            tokenText.append(ch);
                        }else {
                            state = initToken(ch);
                        }
                        break;
                    case Id_int3:
                        if (isBlank(ch)){
                            token.type = TokenType.Int;
                            state = initToken(ch);
                        }
                        else {
                            state = DfaState.Id;
                            tokenText.append(ch);
                        }
                        break;
                    default:

                }
            }
            if (tokenText.length() > 0) {
                initToken(ch);
            }
        }catch (IOException e) {
            e.printStackTrace();
        }

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
    public static void dump(SimpleTokenReader tokenReader) {
        System.out.println("text\ttype");
        Token token = null;
        while ((token = tokenReader.read()) != null) {
            System.out.println(token.getText()+"\t\t"+token.getType());
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

