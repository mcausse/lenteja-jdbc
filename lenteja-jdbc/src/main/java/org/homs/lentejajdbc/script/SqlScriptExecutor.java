package org.homs.lentejajdbc.script;

import org.homs.lentejajdbc.IJdbcFacade;
import org.homs.lentejajdbc.query.QueryObject;

import java.util.List;

public class SqlScriptExecutor {

    public static final String DEFAULT_CHARSETNAME = "UTF-8";

    final IJdbcFacade facade;

    public SqlScriptExecutor(final IJdbcFacade facade) {
        super();
        this.facade = facade;
    }

    public void runFromClasspath(final String classPathFileName) {
        runFromClasspath(classPathFileName, DEFAULT_CHARSETNAME);
    }

    public void runFromClasspath(final String classPathFileName, final String charSetName) {
        final String text = FileUtils.loadFileFromClasspath(classPathFileName, charSetName);
        final List<String> stms = new SqlStatementParser(text).getStatementList();

        execute(stms);
    }

    public void execute(final List<String> stms) {
        for (final String stm : stms) {
            facade.update(new QueryObject(stm));
        }
    }
}