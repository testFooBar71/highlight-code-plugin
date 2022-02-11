// This is a generated file. Not intended for manual editing.
package javalanguage.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import static javalanguage.psi.JavaTypes.*;
import static com.intellij.lang.parser.GeneratedParserUtilBase.*;
import com.intellij.psi.tree.IElementType;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.TokenSet;
import com.intellij.lang.PsiParser;
import com.intellij.lang.LightPsiParser;

@SuppressWarnings({"SimplifiableIfStatement", "UnusedAssignment"})
public class JavaParser implements PsiParser, LightPsiParser {

  public ASTNode parse(IElementType t, PsiBuilder b) {
    parseLight(t, b);
    return b.getTreeBuilt();
  }

  public void parseLight(IElementType t, PsiBuilder b) {
    boolean r;
    b = adapt_builder_(t, b, this, null);
    Marker m = enter_section_(b, 0, _COLLAPSE_, null);
    r = parse_root_(t, b);
    exit_section_(b, 0, m, t, r, true, TRUE_CONDITION);
  }

  protected boolean parse_root_(IElementType t, PsiBuilder b) {
    return parse_root_(t, b, 0);
  }

  static boolean parse_root_(IElementType t, PsiBuilder b, int l) {
    return javaFile(b, l + 1);
  }

  /* ********************************************************** */
  // myline|COMMENT|CRLF
  static boolean item_(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "item_")) return false;
    boolean r;
    r = myline(b, l + 1);
    if (!r) r = consumeToken(b, COMMENT);
    if (!r) r = consumeToken(b, CRLF);
    return r;
  }

  /* ********************************************************** */
  // item_*
  static boolean javaFile(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "javaFile")) return false;
    while (true) {
      int c = current_position_(b);
      if (!item_(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "javaFile", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // NOTMODIFIED | INSERTED | UPDATED | UPDATEDMULTIPLETIMES | MOVED
  public static boolean myline(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "myline")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, MYLINE, "<myline>");
    r = consumeToken(b, NOTMODIFIED);
    if (!r) r = consumeToken(b, INSERTED);
    if (!r) r = consumeToken(b, UPDATED);
    if (!r) r = consumeToken(b, UPDATEDMULTIPLETIMES);
    if (!r) r = consumeToken(b, MOVED);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

}
