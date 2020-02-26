/**
 * 一个token,
 * 有类型和文本两个状态
 */
public interface Token{
    /**
     * Token的类型
     * @return
     */
    public TokenType getType();

    /**
     * Token的文本值
     * @return
     */
    public String getText();
}