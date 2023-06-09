package org.homs.lentejajdbc.script;

import java.util.ArrayList;
import java.util.List;

/**
 * Perfoms the same as the following regexp: <tt>
 * (([^;]*?)?('.*?')?(/\\*(.|\\n)*?\\*\\/)?(--.*?\\n)?)*;\\s*
 * </tt> , but preventing Regexp's StackOverflowErrors when working with large
 * scripts.
 */
public class SqlStatementParser {

    // String textSenseColon = "([^;]*?)?";
    // String literal = "('.*?')?";
    // String multiLineComment = "(/\\*(.|\\n)*?\\*/)?";
    // String singleLineComment = "(--.*?\\n)?";
    //
    // String regexp = "(" + textSenseColon + literal + multiLineComment +
    // singleLineComment + ")*;\\s*";

    public static final char DELIMITER = ';';
    public static final char LITERAL_DELIMITER = '\'';
    public static final String MULTI_LINE_COMMENT_START = "/*";
    public static final String MULTI_LINE_COMMENT_END = "*/";
    public static final String SINGLE_LINE_COMMENT_START = "--";
    public static final char SINGLE_LINE_COMMENT_END = '\n';

    final String script;
    int ssp;
    int p;

    public SqlStatementParser(final String script) {
        super();
        this.script = script;
    }

    public List<String> getStatementList() {
        final List<String> stms = new ArrayList<>();
        ssp = 0; // statement start pointer
        p = 0;

        while (noteof()) {
            chupa();
            if (noteof() && isColon()) {
                p++; // chupa ;
                String stm = script.substring(ssp, p).trim();
                stms.add(stm);
                ssp = p;
            }
        }
        return stms;
    }

    protected void chupa() {
        while (noteof() && !isColon()) {
            if (isLiteral()) {
                chupaLiteral();
            } else if (isMLComment()) {
                chupaMLComment();
            } else if (isSLComment()) {
                chupaSLComment();
            } else {
                chupaText();
            }
        }
    }

    protected boolean noteof() {
        return p < script.length();
    }

    protected boolean isText() {
        return
                /**/!isColon() &&
                /**/!isLiteral() &&
                /**/!isMLComment() &&
                /**/!isSLComment();
    }

    protected void chupaText() {
        while (noteof() && isText()) {
            p++;
        }
    }

    protected boolean isLiteral() {
        final char c = script.charAt(p);
        return c == LITERAL_DELIMITER;
    }

    protected void chupaLiteral() {
        p++; // chupa '
        while (!isLiteral()) {
            p++;
        }
        p++; // chupa '
    }

    protected boolean isMLComment() {
        final char c = script.charAt(p);
        final char c2 = p + 1 < script.length() ? script.charAt(p + 1) : 0;
        return c == MULTI_LINE_COMMENT_START.charAt(0) && c2 == MULTI_LINE_COMMENT_START.charAt(1);
    }

    protected boolean isMLCommentClosing() {
        final char c = script.charAt(p);
        final char c2 = p + 1 < script.length() ? script.charAt(p + 1) : 0;
        return c == MULTI_LINE_COMMENT_END.charAt(0) && c2 == MULTI_LINE_COMMENT_END.charAt(1);
    }

    protected void chupaMLComment() {
        p += MULTI_LINE_COMMENT_START.length(); // chupa
        while (!isMLCommentClosing()) {
            p++;
        }
        p += MULTI_LINE_COMMENT_END.length(); // chupa
    }

    protected boolean isSLComment() {
        final char c = script.charAt(p);
        final char c2 = p + 1 < script.length() ? script.charAt(p + 1) : 0;
        return c == SINGLE_LINE_COMMENT_START.charAt(0) && c2 == SINGLE_LINE_COMMENT_START.charAt(1);
    }

    protected void chupaSLComment() {
        p += SINGLE_LINE_COMMENT_START.length(); // chupa
        while (script.charAt(p) != SINGLE_LINE_COMMENT_END) {
            p++;
        }
        p++; // chupa \n
    }

    protected boolean isColon() {
        return script.charAt(p) == DELIMITER;
    }

}