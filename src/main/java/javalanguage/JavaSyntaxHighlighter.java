package javalanguage;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.HighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import javalanguage.psi.JavaTypes;
import org.jetbrains.annotations.NotNull;

import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;

public class JavaSyntaxHighlighter extends SyntaxHighlighterBase {
    public static final TextAttributesKey NOTMODIFIED =
            createTextAttributesKey("JAVA_NOTMODIFIED", DefaultLanguageHighlighterColors.OPERATION_SIGN);
    public static final TextAttributesKey INSERTED =
            createTextAttributesKey("JAVA_INSERTED", DefaultLanguageHighlighterColors.NUMBER);
    public static final TextAttributesKey UPDATED =
            createTextAttributesKey("JAVA_UPDATED", DefaultLanguageHighlighterColors.STRING);
    public static final TextAttributesKey UPDATEDMULTIPLETIMES =
            createTextAttributesKey("JAVA_UPDATEDMULTIPLETIMES", DefaultLanguageHighlighterColors.INLINE_PARAMETER_HINT_HIGHLIGHTED);
    public static final TextAttributesKey MOVED =
            createTextAttributesKey("JAVA_MOVED", DefaultLanguageHighlighterColors.METADATA);
    public static final TextAttributesKey COMMENT =
            createTextAttributesKey("JAVA_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT);
    public static final TextAttributesKey BAD_CHARACTER =
            createTextAttributesKey("JAVA_BAD_CHARACTER", HighlighterColors.BAD_CHARACTER);


    private static final TextAttributesKey[] BAD_CHAR_KEYS = new TextAttributesKey[]{BAD_CHARACTER};
    private static final TextAttributesKey[] NOTMODIFIED_KEYS = new TextAttributesKey[]{NOTMODIFIED};
    private static final TextAttributesKey[] INSERTED_KEYS = new TextAttributesKey[]{INSERTED};
    private static final TextAttributesKey[] UPDATED_KEYS = new TextAttributesKey[]{UPDATED};
    private static final TextAttributesKey[] UPDATEDMULTIPLETIMES_KEYS = new TextAttributesKey[]{UPDATEDMULTIPLETIMES};
    private static final TextAttributesKey[] MOVED_KEYS = new TextAttributesKey[]{MOVED};
    private static final TextAttributesKey[] COMMENT_KEYS = new TextAttributesKey[]{COMMENT};
    private static final TextAttributesKey[] EMPTY_KEYS = new TextAttributesKey[0];

    @NotNull
    @Override
    public Lexer getHighlightingLexer() {
        return new JavaLexerAdapter();
    }

    @NotNull
    @Override
    public TextAttributesKey[] getTokenHighlights(IElementType tokenType) {
        if (tokenType.equals(JavaTypes.NOTMODIFIED)) {
            return NOTMODIFIED_KEYS;
        } else if (tokenType.equals(JavaTypes.INSERTED)) {
            return INSERTED_KEYS;
        } else if (tokenType.equals(JavaTypes.UPDATED)) {
            return UPDATED_KEYS;
        } else if (tokenType.equals(JavaTypes.UPDATEDMULTIPLETIMES)) {
            return UPDATEDMULTIPLETIMES_KEYS;
        } else if (tokenType.equals(JavaTypes.MOVED)) {
            return MOVED_KEYS;
        } else if (tokenType.equals(JavaTypes.COMMENT)) {
            return COMMENT_KEYS;
        } else if (tokenType.equals(TokenType.BAD_CHARACTER)) {
            return BAD_CHAR_KEYS;
        } else {
            return EMPTY_KEYS;
        }
    }
}
