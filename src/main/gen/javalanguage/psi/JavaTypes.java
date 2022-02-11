// This is a generated file. Not intended for manual editing.
package javalanguage.psi;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;
import com.intellij.lang.ASTNode;
import javalanguage.psi.impl.*;

public interface JavaTypes {

  IElementType MYLINE = new JavaElementType("MYLINE");

  IElementType COMMENT = new JavaTokenType("COMMENT");
  IElementType CRLF = new JavaTokenType("CRLF");
  IElementType INSERTED = new JavaTokenType("INSERTED");
  IElementType MOVED = new JavaTokenType("MOVED");
  IElementType NOTMODIFIED = new JavaTokenType("NOTMODIFIED");
  IElementType UPDATED = new JavaTokenType("UPDATED");
  IElementType UPDATEDMULTIPLETIMES = new JavaTokenType("UPDATEDMULTIPLETIMES");

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
      if (type == MYLINE) {
        return new JavaMylineImpl(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}
