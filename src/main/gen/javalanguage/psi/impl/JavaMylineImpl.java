// This is a generated file. Not intended for manual editing.
package javalanguage.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static javalanguage.psi.JavaTypes.*;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import javalanguage.psi.*;

public class JavaMylineImpl extends ASTWrapperPsiElement implements JavaMyline {

  public JavaMylineImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull JavaVisitor visitor) {
    visitor.visitMyline(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof JavaVisitor) accept((JavaVisitor)visitor);
    else super.accept(visitor);
  }

}
