package javalanguage;

import com.intellij.lang.Language;

public class JavaLanguage extends Language {
    public static final JavaLanguage INSTANCE = new JavaLanguage();

    private JavaLanguage() {
        super("Java");
    }
}
