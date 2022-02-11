package javalanguage;

import com.intellij.lexer.FlexAdapter;

public class JavaLexerAdapter extends FlexAdapter {
    public JavaLexerAdapter() {
        super(new JavaLexer(null));
    }
}
