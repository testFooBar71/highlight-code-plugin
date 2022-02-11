// Copyright 2000-2020 JetBrains s.r.o. and other contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package javalanguage;

import changes.ChangesHighlighter;import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import difflogic.DiffHighlighter;
import javalanguage.psi.JavaTypes;
import com.intellij.psi.TokenType;
import java.io.FileReader;
import java.util.ArrayList;

%%

%class JavaLexer
%implements FlexLexer
%unicode
%char
%line
%column
%function advance
%type IElementType
%eof{  return;
%eof}

%init{
  changesHighlighter = new ChangesHighlighter();
%init}

%{
  ChangesHighlighter changesHighlighter;
  private IElementType getCorrespondingToken() {
      return changesHighlighter.getCharHighlight(yyline, yychar);
  }
%}

CRLF=\R
WHITE_SPACE=[\ \n\t\f]
FIRST_VALUE_CHARACTER=[^ \n\f\\] | "\\"{CRLF} | "\\".
VALUE_CHARACTER=[^\n\f\\] | "\\"{CRLF} | "\\".
END_OF_LINE_COMMENT=("//"|"/*")[^\r\n]*
SEPARATOR=[:=]
KEY_CHARACTER=[^:=\ \n\t\f\\] | "\\ "

%state WAITING_VALUE

%%
<YYINITIAL> .                                               { yybegin(YYINITIAL); return getCorrespondingToken(); }

<YYINITIAL> {END_OF_LINE_COMMENT}                           { yybegin(YYINITIAL); return JavaTypes.COMMENT; }

/*
<YYINITIAL> {KEY_CHARACTER}+                                { yybegin(YYINITIAL); return SimpleTypes.LLAVE; }

<YYINITIAL> {SEPARATOR}                                     { yybegin(WAITING_VALUE); return SimpleTypes.SEPARADOR; }

<WAITING_VALUE> {CRLF}({CRLF}|{WHITE_SPACE})+               { yybegin(YYINITIAL); return TokenType.WHITE_SPACE; }

<WAITING_VALUE> {WHITE_SPACE}+                              { yybegin(WAITING_VALUE); return TokenType.WHITE_SPACE; }

<WAITING_VALUE> {FIRST_VALUE_CHARACTER}{VALUE_CHARACTER}*   { yybegin(YYINITIAL); return SimpleTypes.VALOR; }
*/

({CRLF}|{WHITE_SPACE})+                                     { yybegin(YYINITIAL); return TokenType.WHITE_SPACE; }

[^]                                                         { return TokenType.BAD_CHARACTER; }
