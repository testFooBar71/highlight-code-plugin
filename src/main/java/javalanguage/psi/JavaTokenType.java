package javalanguage.psi;

import com.intellij.psi.tree.IElementType;
import javalanguage.JavaLanguage;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class JavaTokenType extends IElementType {
    public JavaTokenType(@NotNull @NonNls String debugName) {
        super(debugName, JavaLanguage.INSTANCE);
    }

    @Override
    public String toString() {
        return "JavaTokenType." + super.toString();
    }
}
