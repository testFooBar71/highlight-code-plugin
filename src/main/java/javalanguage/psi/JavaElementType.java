package javalanguage.psi;

import com.intellij.psi.tree.IElementType;
import javalanguage.JavaLanguage;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class JavaElementType extends IElementType {
    public JavaElementType(@NotNull @NonNls String debugName) {
        super(debugName, JavaLanguage.INSTANCE);
    }
}
