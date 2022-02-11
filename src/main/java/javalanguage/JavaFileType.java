package javalanguage;

import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class JavaFileType extends LanguageFileType {
    public static final JavaFileType INSTANCE = new JavaFileType();

    private JavaFileType() {
        super(JavaLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public String getName() {
        return "Java File";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Java language file";
    }

    @NotNull
    @Override
    public String getDefaultExtension() {
        return "java";
    }

    @Nullable
    @Override
    public Icon getIcon() {
        return JavaIcons.FILE;
    }
}
