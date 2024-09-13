// Generated from /Users/andreasernst/projects/demo/src/main/kotlin/com/example/demo/OQL.g4 by ANTLR 4.13.1

package com.example.dorm.query.parser;
/**
 * @COPYRIGHT (C) 2024 Andreas ErnstX
 *
 * All rights reserved
 */

import com.example.dorm.query.QueryManager;
import com.example.dorm.query.Query;
import com.example.dorm.DataObject;
import com.example.dorm.model.ObjectDescriptor;
import com.example.dorm.query.*;

import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue"})
public class OQLParser extends com.example.dorm.query.parser.AbstractQueryParser {
    static { RuntimeMetaData.checkVersion("4.13.1", RuntimeMetaData.VERSION); }

    protected static final DFA[] _decisionToDFA;
    protected static final PredictionContextCache _sharedContextCache =
            new PredictionContextCache();
    public static final int
            T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9,
            T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, T__15=16, T__16=17,
            T__17=18, T__18=19, T__19=20, T__20=21, T__21=22, T__22=23, T__23=24,
            T__24=25, T__25=26, T__26=27, T__27=28, T__28=29, T__29=30, T__30=31,
            T__31=32, T__32=33, T__33=34, T__34=35, T__35=36, T__36=37, T__37=38,
            T__38=39, T__39=40, T__40=41, T__41=42, T__42=43, T__43=44, T__44=45,
            T__45=46, T__46=47, T__47=48, T__48=49, T__49=50, T__50=51, T__51=52,
            T__52=53, T__53=54, T__54=55, T__55=56, T__56=57, T__57=58, T__58=59,
            T__59=60, T__60=61, T__61=62, T__62=63, T__63=64, T__64=65, T__65=66,
            T__66=67, T__67=68, T__68=69, T__69=70, IDENTIFICATION_VARIABLE=71, NUMBER=72,
            CHARACTER=73, STRINGLITERAL=74, ESCAPE_CHARACTER=75, WS=76, TRIM_CHARACTER=77;
    public static final int
            RULE_file = 0, RULE_statement = 1, RULE_select_statement = 2, RULE_update_statement = 3,
            RULE_delete_statement = 4, RULE_from_clause = 5, RULE_identification_variable_declaration = 6,
            RULE_range_variable_declaration = 7, RULE_join = 8, RULE_fetch_join = 9,
            RULE_join_spec = 10, RULE_join_association_path_expression = 11, RULE_join_collection_valued_path_expression = 12,
            RULE_join_single_valued_association_path_expression = 13, RULE_collection_member_declaration = 14,
            RULE_single_valued_path_expression = 15, RULE_state_field_path_expression = 16,
            RULE_single_valued_association_path_expression = 17, RULE_collection_valued_path_expression = 18,
            RULE_state_field = 19, RULE_update_clause = 20, RULE_update_item = 21,
            RULE_new_value = 22, RULE_delete_clause = 23, RULE_select_clause = 24,
            RULE_select_expression = 25, RULE_constructor_expression = 26, RULE_constructor_item = 27,
            RULE_aggregate_expression = 28, RULE_where_clause = 29, RULE_groupby_clause = 30,
            RULE_groupby_item = 31, RULE_having_clause = 32, RULE_orderby_clause = 33,
            RULE_orderby_item = 34, RULE_subquery = 35, RULE_subquery_from_clause = 36,
            RULE_subselect_identification_variable_declaration = 37, RULE_association_path_expression = 38,
            RULE_simple_select_clause = 39, RULE_simple_select_expression = 40, RULE_conditional_expression = 41,
            RULE_conditional_term = 42, RULE_conditional_factor = 43, RULE_conditional_primary = 44,
            RULE_simple_cond_expression = 45, RULE_between_expression = 46, RULE_in_expression = 47,
            RULE_in_item = 48, RULE_like_expression = 49, RULE_null_comparison_expression = 50,
            RULE_empty_collection_comparison_expression = 51, RULE_collection_member_expression = 52,
            RULE_exists_expression = 53, RULE_all_or_any_expression = 54, RULE_comparison_expression = 55,
            RULE_path_expression = 56, RULE_comparison_operator = 57, RULE_arithmetic_expression = 58,
            RULE_simple_arithmetic_expression = 59, RULE_arithmetic_term = 60, RULE_arithmetic_factor = 61,
            RULE_arithmetic_primary = 62, RULE_string_expression = 63, RULE_string_primary = 64,
            RULE_datetime_expression = 65, RULE_datetime_primary = 66, RULE_boolean_expression = 67,
            RULE_boolean_primary = 68, RULE_enum_expression = 69, RULE_enum_primary = 70,
            RULE_entity_expression = 71, RULE_simple_entity_expression = 72, RULE_functions_returning_numerics = 73,
            RULE_functions_returning_datetime = 74, RULE_functions_returning_strings = 75,
            RULE_trim_specification = 76, RULE_numeric_literal = 77, RULE_pattern_value = 78,
            RULE_input_parameter = 79, RULE_literal = 80, RULE_constructor_name = 81,
            RULE_enum_literal = 82, RULE_boolean_literal = 83, RULE_simple_state_field = 84,
            RULE_embedded_class_state_field = 85, RULE_single_valued_association_field = 86,
            RULE_collection_valued_association_field = 87, RULE_abstract_schema_name = 88,
            RULE_leg = 89;
    private static String[] makeRuleNames() {
        return new String[] {
                "file", "statement", "select_statement", "update_statement", "delete_statement",
                "from_clause", "identification_variable_declaration", "range_variable_declaration",
                "join", "fetch_join", "join_spec", "join_association_path_expression",
                "join_collection_valued_path_expression", "join_single_valued_association_path_expression",
                "collection_member_declaration", "single_valued_path_expression", "state_field_path_expression",
                "single_valued_association_path_expression", "collection_valued_path_expression",
                "state_field", "update_clause", "update_item", "new_value", "delete_clause",
                "select_clause", "select_expression", "constructor_expression", "constructor_item",
                "aggregate_expression", "where_clause", "groupby_clause", "groupby_item",
                "having_clause", "orderby_clause", "orderby_item", "subquery", "subquery_from_clause",
                "subselect_identification_variable_declaration", "association_path_expression",
                "simple_select_clause", "simple_select_expression", "conditional_expression",
                "conditional_term", "conditional_factor", "conditional_primary", "simple_cond_expression",
                "between_expression", "in_expression", "in_item", "like_expression",
                "null_comparison_expression", "empty_collection_comparison_expression",
                "collection_member_expression", "exists_expression", "all_or_any_expression",
                "comparison_expression", "path_expression", "comparison_operator", "arithmetic_expression",
                "simple_arithmetic_expression", "arithmetic_term", "arithmetic_factor",
                "arithmetic_primary", "string_expression", "string_primary", "datetime_expression",
                "datetime_primary", "boolean_expression", "boolean_primary", "enum_expression",
                "enum_primary", "entity_expression", "simple_entity_expression", "functions_returning_numerics",
                "functions_returning_datetime", "functions_returning_strings", "trim_specification",
                "numeric_literal", "pattern_value", "input_parameter", "literal", "constructor_name",
                "enum_literal", "boolean_literal", "simple_state_field", "embedded_class_state_field",
                "single_valued_association_field", "collection_valued_association_field",
                "abstract_schema_name", "leg"
        };
    }
    public static final String[] ruleNames = makeRuleNames();

    private static String[] makeLiteralNames() {
        return new String[] {
                null, "'FROM'", "'AS'", "'FETCH'", "'LEFT'", "'OUTER'", "'INNER'", "'JOIN'",
                "'.'", "'IN'", "'('", "')'", "'UPDATE'", "'SET'", "','", "'='", "'NULL'",
                "'DELETE'", "'SELECT'", "'DISTINCT'", "'NEW'", "'AVG'", "'MAX'", "'MIN'",
                "'SUM'", "'COUNT'", "'WHERE'", "'GROUP'", "'BY'", "'HAVING'", "'ORDER'",
                "'ASC'", "'DESC'", "'NOT'", "'BETWEEN'", "'AND'", "'LIKE'", "'ESCAPE'",
                "'IS'", "'EMPTY'", "'MEMBER'", "'OF'", "'EXISTS'", "'ALL'", "'ANY'",
                "'SOME'", "'>'", "'>='", "'<'", "'<='", "'<>'", "'LENGTH'", "'LOCATE'",
                "'ABS'", "'SQRT'", "'MOD'", "'SIZE'", "'CURRENT_DATE'", "'CURRENT_TIME'",
                "'CURRENT_TIMESTAMP'", "'CONCAT'", "'SUBSTRING'", "'TRIM'", "'LOWER'",
                "'UPPER'", "'LEADING'", "'TRAILING'", "'BOTH'", "':'", "'true'", "'false'"
        };
    }
    private static final String[] _LITERAL_NAMES = makeLiteralNames();
    private static String[] makeSymbolicNames() {
        return new String[] {
                null, null, null, null, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null, null, "IDENTIFICATION_VARIABLE",
                "NUMBER", "CHARACTER", "STRINGLITERAL", "ESCAPE_CHARACTER", "WS", "TRIM_CHARACTER"
        };
    }
    private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
    public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

    /**
     * @deprecated Use {@link #VOCABULARY} instead.
     */
    @Deprecated
    public static final String[] tokenNames;
    static {
        tokenNames = new String[_SYMBOLIC_NAMES.length];
        for (int i = 0; i < tokenNames.length; i++) {
            tokenNames[i] = VOCABULARY.getLiteralName(i);
            if (tokenNames[i] == null) {
                tokenNames[i] = VOCABULARY.getSymbolicName(i);
            }

            if (tokenNames[i] == null) {
                tokenNames[i] = "<INVALID>";
            }
        }
    }

    @Override
    @Deprecated
    public String[] getTokenNames() {
        return tokenNames;
    }

    @Override

    public Vocabulary getVocabulary() {
        return VOCABULARY;
    }

    @Override
    public String getGrammarFileName() { return "OQL.g4"; }

    @Override
    public String[] getRuleNames() { return ruleNames; }

    @Override
    public String getSerializedATN() { return _serializedATN; }

    @Override
    public ATN getATN() { return _ATN; }

    public OQLParser(TokenStream input) {
        super(input);
        _interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
    }

    @SuppressWarnings("CheckReturnValue")
    public static class FileContext extends ParserRuleContext {
        public StatementContext statement() {
            return getRuleContext(StatementContext.class,0);
        }
        public TerminalNode EOF() { return getToken(OQLParser.EOF, 0); }
        public FileContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_file; }
    }

    public final FileContext file() throws RecognitionException {
        FileContext _localctx = new FileContext(_ctx, getState());
        enterRule(_localctx, 0, RULE_file);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(180);
                statement();
                setState(181);
                match(EOF);
            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class StatementContext extends ParserRuleContext {
        public Select_statementContext select_statement() {
            return getRuleContext(Select_statementContext.class,0);
        }
        public StatementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_statement; }
    }

    public final StatementContext statement() throws RecognitionException {
        StatementContext _localctx = new StatementContext(_ctx, getState());
        enterRule(_localctx, 2, RULE_statement);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(183);
                select_statement();
            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class Select_statementContext extends ParserRuleContext {
        public Select_clauseContext select_clause;
        public From_clauseContext from_clause;
        public Select_clauseContext select_clause() {
            return getRuleContext(Select_clauseContext.class,0);
        }
        public From_clauseContext from_clause() {
            return getRuleContext(From_clauseContext.class,0);
        }
        public Where_clauseContext where_clause() {
            return getRuleContext(Where_clauseContext.class,0);
        }
        public Select_statementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_select_statement; }
    }

    public final Select_statementContext select_statement() throws RecognitionException {
        Select_statementContext _localctx = new Select_statementContext(_ctx, getState());
        enterRule(_localctx, 4, RULE_select_statement);
        this.setQuery(queryManager.create());
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(185);
                ((Select_statementContext)_localctx).select_clause = select_clause();
                setState(186);
                ((Select_statementContext)_localctx).from_clause = from_clause();
                getQuery().from(((Select_statementContext)_localctx).from_clause.from); select( getQuery(), ((Select_statementContext)_localctx).select_clause.select);
                setState(189);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la==T__25) {
                    {
                        setState(188);
                        where_clause();
                    }
                }

            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class Update_statementContext extends ParserRuleContext {
        public Update_clauseContext update_clause() {
            return getRuleContext(Update_clauseContext.class,0);
        }
        public Where_clauseContext where_clause() {
            return getRuleContext(Where_clauseContext.class,0);
        }
        public Update_statementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_update_statement; }
    }

    public final Update_statementContext update_statement() throws RecognitionException {
        Update_statementContext _localctx = new Update_statementContext(_ctx, getState());
        enterRule(_localctx, 6, RULE_update_statement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(191);
                update_clause();
                setState(193);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la==T__25) {
                    {
                        setState(192);
                        where_clause();
                    }
                }

            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class Delete_statementContext extends ParserRuleContext {
        public Delete_clauseContext delete_clause() {
            return getRuleContext(Delete_clauseContext.class,0);
        }
        public Where_clauseContext where_clause() {
            return getRuleContext(Where_clauseContext.class,0);
        }
        public Delete_statementContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_delete_statement; }
    }

    public final Delete_statementContext delete_statement() throws RecognitionException {
        Delete_statementContext _localctx = new Delete_statementContext(_ctx, getState());
        enterRule(_localctx, 8, RULE_delete_statement);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(195);
                delete_clause();
                setState(197);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la==T__25) {
                    {
                        setState(196);
                        where_clause();
                    }
                }

            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class From_clauseContext extends ParserRuleContext {
        public From from;
        public Identification_variable_declarationContext identification_variable_declaration;
        public Identification_variable_declarationContext identification_variable_declaration() {
            return getRuleContext(Identification_variable_declarationContext.class,0);
        }
        public From_clauseContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_from_clause; }
    }

    public final From_clauseContext from_clause() throws RecognitionException {
        From_clauseContext _localctx = new From_clauseContext(_ctx, getState());
        enterRule(_localctx, 10, RULE_from_clause);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(199);
                match(T__0);
                setState(200);
                ((From_clauseContext)_localctx).identification_variable_declaration = identification_variable_declaration();
                ((From_clauseContext)_localctx).from =  ((From_clauseContext)_localctx).identification_variable_declaration.from;
            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class Identification_variable_declarationContext extends ParserRuleContext {
        public From from;
        public Range_variable_declarationContext range_variable_declaration;
        public Range_variable_declarationContext range_variable_declaration() {
            return getRuleContext(Range_variable_declarationContext.class,0);
        }
        public Identification_variable_declarationContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_identification_variable_declaration; }
    }

    public final Identification_variable_declarationContext identification_variable_declaration() throws RecognitionException {
        Identification_variable_declarationContext _localctx = new Identification_variable_declarationContext(_ctx, getState());
        enterRule(_localctx, 12, RULE_identification_variable_declaration);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(203);
                ((Identification_variable_declarationContext)_localctx).range_variable_declaration = range_variable_declaration();
                ((Identification_variable_declarationContext)_localctx).from =  ((Identification_variable_declarationContext)_localctx).range_variable_declaration.from;
            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class Range_variable_declarationContext extends ParserRuleContext {
        public From from;
        public Abstract_schema_nameContext abstract_schema_name;
        public Token IDENTIFICATION_VARIABLE;
        public Abstract_schema_nameContext abstract_schema_name() {
            return getRuleContext(Abstract_schema_nameContext.class,0);
        }
        public TerminalNode IDENTIFICATION_VARIABLE() { return getToken(OQLParser.IDENTIFICATION_VARIABLE, 0); }
        public Range_variable_declarationContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_range_variable_declaration; }
    }

    public final Range_variable_declarationContext range_variable_declaration() throws RecognitionException {
        Range_variable_declarationContext _localctx = new Range_variable_declarationContext(_ctx, getState());
        enterRule(_localctx, 14, RULE_range_variable_declaration);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(206);
                ((Range_variable_declarationContext)_localctx).abstract_schema_name = abstract_schema_name();
                setState(208);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la==T__1) {
                    {
                        setState(207);
                        match(T__1);
                    }
                }

                setState(210);
                ((Range_variable_declarationContext)_localctx).IDENTIFICATION_VARIABLE = match(IDENTIFICATION_VARIABLE);
                ((Range_variable_declarationContext)_localctx).from =  rememberSchema(((Range_variable_declarationContext)_localctx).abstract_schema_name.name, (((Range_variable_declarationContext)_localctx).IDENTIFICATION_VARIABLE!=null?((Range_variable_declarationContext)_localctx).IDENTIFICATION_VARIABLE.getText():null));
            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class JoinContext extends ParserRuleContext {
        public Join_specContext join_spec() {
            return getRuleContext(Join_specContext.class,0);
        }
        public Join_association_path_expressionContext join_association_path_expression() {
            return getRuleContext(Join_association_path_expressionContext.class,0);
        }
        public TerminalNode IDENTIFICATION_VARIABLE() { return getToken(OQLParser.IDENTIFICATION_VARIABLE, 0); }
        public JoinContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_join; }
    }

    public final JoinContext join() throws RecognitionException {
        JoinContext _localctx = new JoinContext(_ctx, getState());
        enterRule(_localctx, 16, RULE_join);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(213);
                join_spec();
                setState(214);
                join_association_path_expression();
                setState(216);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la==T__1) {
                    {
                        setState(215);
                        match(T__1);
                    }
                }

                setState(218);
                match(IDENTIFICATION_VARIABLE);
            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class Fetch_joinContext extends ParserRuleContext {
        public Join_specContext join_spec() {
            return getRuleContext(Join_specContext.class,0);
        }
        public Join_association_path_expressionContext join_association_path_expression() {
            return getRuleContext(Join_association_path_expressionContext.class,0);
        }
        public Fetch_joinContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_fetch_join; }
    }

    public final Fetch_joinContext fetch_join() throws RecognitionException {
        Fetch_joinContext _localctx = new Fetch_joinContext(_ctx, getState());
        enterRule(_localctx, 18, RULE_fetch_join);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(220);
                join_spec();
                setState(221);
                match(T__2);
                setState(222);
                join_association_path_expression();
            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class Join_specContext extends ParserRuleContext {
        public Join_specContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_join_spec; }
    }

    public final Join_specContext join_spec() throws RecognitionException {
        Join_specContext _localctx = new Join_specContext(_ctx, getState());
        enterRule(_localctx, 20, RULE_join_spec);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(229);
                _errHandler.sync(this);
                switch (_input.LA(1)) {
                    case T__3:
                    {
                        {
                            setState(224);
                            match(T__3);
                        }
                        setState(226);
                        _errHandler.sync(this);
                        _la = _input.LA(1);
                        if (_la==T__4) {
                            {
                                setState(225);
                                match(T__4);
                            }
                        }

                    }
                    break;
                    case T__5:
                    {
                        setState(228);
                        match(T__5);
                    }
                    break;
                    case T__6:
                        break;
                    default:
                        break;
                }
                setState(231);
                match(T__6);
            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class Join_association_path_expressionContext extends ParserRuleContext {
        public Join_collection_valued_path_expressionContext join_collection_valued_path_expression() {
            return getRuleContext(Join_collection_valued_path_expressionContext.class,0);
        }
        public Join_single_valued_association_path_expressionContext join_single_valued_association_path_expression() {
            return getRuleContext(Join_single_valued_association_path_expressionContext.class,0);
        }
        public Join_association_path_expressionContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_join_association_path_expression; }
    }

    public final Join_association_path_expressionContext join_association_path_expression() throws RecognitionException {
        Join_association_path_expressionContext _localctx = new Join_association_path_expressionContext(_ctx, getState());
        enterRule(_localctx, 22, RULE_join_association_path_expression);
        try {
            setState(235);
            _errHandler.sync(this);
            switch ( getInterpreter().adaptivePredict(_input,7,_ctx) ) {
                case 1:
                    enterOuterAlt(_localctx, 1);
                {
                    setState(233);
                    join_collection_valued_path_expression();
                }
                break;
                case 2:
                    enterOuterAlt(_localctx, 2);
                {
                    setState(234);
                    join_single_valued_association_path_expression();
                }
                break;
            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class Join_collection_valued_path_expressionContext extends ParserRuleContext {
        public TerminalNode IDENTIFICATION_VARIABLE() { return getToken(OQLParser.IDENTIFICATION_VARIABLE, 0); }
        public Collection_valued_association_fieldContext collection_valued_association_field() {
            return getRuleContext(Collection_valued_association_fieldContext.class,0);
        }
        public Join_collection_valued_path_expressionContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_join_collection_valued_path_expression; }
    }

    public final Join_collection_valued_path_expressionContext join_collection_valued_path_expression() throws RecognitionException {
        Join_collection_valued_path_expressionContext _localctx = new Join_collection_valued_path_expressionContext(_ctx, getState());
        enterRule(_localctx, 24, RULE_join_collection_valued_path_expression);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(237);
                match(IDENTIFICATION_VARIABLE);
                setState(238);
                match(T__7);
                setState(239);
                collection_valued_association_field();
            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class Join_single_valued_association_path_expressionContext extends ParserRuleContext {
        public TerminalNode IDENTIFICATION_VARIABLE() { return getToken(OQLParser.IDENTIFICATION_VARIABLE, 0); }
        public Single_valued_association_fieldContext single_valued_association_field() {
            return getRuleContext(Single_valued_association_fieldContext.class,0);
        }
        public Join_single_valued_association_path_expressionContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_join_single_valued_association_path_expression; }
    }

    public final Join_single_valued_association_path_expressionContext join_single_valued_association_path_expression() throws RecognitionException {
        Join_single_valued_association_path_expressionContext _localctx = new Join_single_valued_association_path_expressionContext(_ctx, getState());
        enterRule(_localctx, 26, RULE_join_single_valued_association_path_expression);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(241);
                match(IDENTIFICATION_VARIABLE);
                setState(242);
                match(T__7);
                setState(243);
                single_valued_association_field();
            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class Collection_member_declarationContext extends ParserRuleContext {
        public Collection_valued_path_expressionContext collection_valued_path_expression() {
            return getRuleContext(Collection_valued_path_expressionContext.class,0);
        }
        public TerminalNode IDENTIFICATION_VARIABLE() { return getToken(OQLParser.IDENTIFICATION_VARIABLE, 0); }
        public Collection_member_declarationContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_collection_member_declaration; }
    }

    public final Collection_member_declarationContext collection_member_declaration() throws RecognitionException {
        Collection_member_declarationContext _localctx = new Collection_member_declarationContext(_ctx, getState());
        enterRule(_localctx, 28, RULE_collection_member_declaration);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(245);
                match(T__8);
                setState(246);
                match(T__9);
                setState(247);
                collection_valued_path_expression();
                setState(248);
                match(T__10);
                setState(250);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la==T__1) {
                    {
                        setState(249);
                        match(T__1);
                    }
                }

                setState(252);
                match(IDENTIFICATION_VARIABLE);
            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class Single_valued_path_expressionContext extends ParserRuleContext {
        public State_field_path_expressionContext state_field_path_expression() {
            return getRuleContext(State_field_path_expressionContext.class,0);
        }
        public Single_valued_association_path_expressionContext single_valued_association_path_expression() {
            return getRuleContext(Single_valued_association_path_expressionContext.class,0);
        }
        public Single_valued_path_expressionContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_single_valued_path_expression; }
    }

    public final Single_valued_path_expressionContext single_valued_path_expression() throws RecognitionException {
        Single_valued_path_expressionContext _localctx = new Single_valued_path_expressionContext(_ctx, getState());
        enterRule(_localctx, 30, RULE_single_valued_path_expression);
        try {
            setState(256);
            _errHandler.sync(this);
            switch ( getInterpreter().adaptivePredict(_input,9,_ctx) ) {
                case 1:
                    enterOuterAlt(_localctx, 1);
                {
                    setState(254);
                    state_field_path_expression();
                }
                break;
                case 2:
                    enterOuterAlt(_localctx, 2);
                {
                    setState(255);
                    single_valued_association_path_expression();
                }
                break;
            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class State_field_path_expressionContext extends ParserRuleContext {
        public State_fieldContext state_field() {
            return getRuleContext(State_fieldContext.class,0);
        }
        public TerminalNode IDENTIFICATION_VARIABLE() { return getToken(OQLParser.IDENTIFICATION_VARIABLE, 0); }
        public Single_valued_association_path_expressionContext single_valued_association_path_expression() {
            return getRuleContext(Single_valued_association_path_expressionContext.class,0);
        }
        public State_field_path_expressionContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_state_field_path_expression; }
    }

    public final State_field_path_expressionContext state_field_path_expression() throws RecognitionException {
        State_field_path_expressionContext _localctx = new State_field_path_expressionContext(_ctx, getState());
        enterRule(_localctx, 32, RULE_state_field_path_expression);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(260);
                _errHandler.sync(this);
                switch ( getInterpreter().adaptivePredict(_input,10,_ctx) ) {
                    case 1:
                    {
                        setState(258);
                        match(IDENTIFICATION_VARIABLE);
                    }
                    break;
                    case 2:
                    {
                        setState(259);
                        single_valued_association_path_expression();
                    }
                    break;
                }
                setState(262);
                match(T__7);
                setState(263);
                state_field();
            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class Single_valued_association_path_expressionContext extends ParserRuleContext {
        public TerminalNode IDENTIFICATION_VARIABLE() { return getToken(OQLParser.IDENTIFICATION_VARIABLE, 0); }
        public List<Single_valued_association_fieldContext> single_valued_association_field() {
            return getRuleContexts(Single_valued_association_fieldContext.class);
        }
        public Single_valued_association_fieldContext single_valued_association_field(int i) {
            return getRuleContext(Single_valued_association_fieldContext.class,i);
        }
        public Single_valued_association_path_expressionContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_single_valued_association_path_expression; }
    }

    public final Single_valued_association_path_expressionContext single_valued_association_path_expression() throws RecognitionException {
        Single_valued_association_path_expressionContext _localctx = new Single_valued_association_path_expressionContext(_ctx, getState());
        enterRule(_localctx, 34, RULE_single_valued_association_path_expression);
        try {
            int _alt;
            enterOuterAlt(_localctx, 1);
            {
                setState(265);
                match(IDENTIFICATION_VARIABLE);
                setState(266);
                match(T__7);
                setState(272);
                _errHandler.sync(this);
                _alt = getInterpreter().adaptivePredict(_input,11,_ctx);
                while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
                    if ( _alt==1 ) {
                        {
                            {
                                setState(267);
                                single_valued_association_field();
                                setState(268);
                                match(T__7);
                            }
                        }
                    }
                    setState(274);
                    _errHandler.sync(this);
                    _alt = getInterpreter().adaptivePredict(_input,11,_ctx);
                }
                setState(275);
                single_valued_association_field();
            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class Collection_valued_path_expressionContext extends ParserRuleContext {
        public TerminalNode IDENTIFICATION_VARIABLE() { return getToken(OQLParser.IDENTIFICATION_VARIABLE, 0); }
        public Collection_valued_association_fieldContext collection_valued_association_field() {
            return getRuleContext(Collection_valued_association_fieldContext.class,0);
        }
        public List<Single_valued_association_fieldContext> single_valued_association_field() {
            return getRuleContexts(Single_valued_association_fieldContext.class);
        }
        public Single_valued_association_fieldContext single_valued_association_field(int i) {
            return getRuleContext(Single_valued_association_fieldContext.class,i);
        }
        public Collection_valued_path_expressionContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_collection_valued_path_expression; }
    }

    public final Collection_valued_path_expressionContext collection_valued_path_expression() throws RecognitionException {
        Collection_valued_path_expressionContext _localctx = new Collection_valued_path_expressionContext(_ctx, getState());
        enterRule(_localctx, 36, RULE_collection_valued_path_expression);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(277);
                match(IDENTIFICATION_VARIABLE);
                setState(278);
                match(T__7);
                setState(284);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la==T__7) {
                    {
                        {
                            setState(279);
                            single_valued_association_field();
                            setState(280);
                            match(T__7);
                        }
                    }
                    setState(286);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                setState(287);
                collection_valued_association_field();
            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class State_fieldContext extends ParserRuleContext {
        public Simple_state_fieldContext simple_state_field() {
            return getRuleContext(Simple_state_fieldContext.class,0);
        }
        public List<Embedded_class_state_fieldContext> embedded_class_state_field() {
            return getRuleContexts(Embedded_class_state_fieldContext.class);
        }
        public Embedded_class_state_fieldContext embedded_class_state_field(int i) {
            return getRuleContext(Embedded_class_state_fieldContext.class,i);
        }
        public State_fieldContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_state_field; }
    }

    public final State_fieldContext state_field() throws RecognitionException {
        State_fieldContext _localctx = new State_fieldContext(_ctx, getState());
        enterRule(_localctx, 38, RULE_state_field);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(294);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la==T__7) {
                    {
                        {
                            setState(289);
                            embedded_class_state_field();
                            setState(290);
                            match(T__7);
                        }
                    }
                    setState(296);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                setState(297);
                simple_state_field();
            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class Update_clauseContext extends ParserRuleContext {
        public Abstract_schema_nameContext abstract_schema_name() {
            return getRuleContext(Abstract_schema_nameContext.class,0);
        }
        public List<Update_itemContext> update_item() {
            return getRuleContexts(Update_itemContext.class);
        }
        public Update_itemContext update_item(int i) {
            return getRuleContext(Update_itemContext.class,i);
        }
        public TerminalNode IDENTIFICATION_VARIABLE() { return getToken(OQLParser.IDENTIFICATION_VARIABLE, 0); }
        public Update_clauseContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_update_clause; }
    }

    public final Update_clauseContext update_clause() throws RecognitionException {
        Update_clauseContext _localctx = new Update_clauseContext(_ctx, getState());
        enterRule(_localctx, 40, RULE_update_clause);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(299);
                match(T__11);
                setState(300);
                abstract_schema_name();
                setState(305);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la==T__1 || _la==IDENTIFICATION_VARIABLE) {
                    {
                        setState(302);
                        _errHandler.sync(this);
                        _la = _input.LA(1);
                        if (_la==T__1) {
                            {
                                setState(301);
                                match(T__1);
                            }
                        }

                        setState(304);
                        match(IDENTIFICATION_VARIABLE);
                    }
                }

                setState(307);
                match(T__12);
                setState(308);
                update_item();
                setState(313);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la==T__13) {
                    {
                        {
                            setState(309);
                            match(T__13);
                            setState(310);
                            update_item();
                        }
                    }
                    setState(315);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class Update_itemContext extends ParserRuleContext {
        public New_valueContext new_value() {
            return getRuleContext(New_valueContext.class,0);
        }
        public State_fieldContext state_field() {
            return getRuleContext(State_fieldContext.class,0);
        }
        public Single_valued_association_fieldContext single_valued_association_field() {
            return getRuleContext(Single_valued_association_fieldContext.class,0);
        }
        public TerminalNode IDENTIFICATION_VARIABLE() { return getToken(OQLParser.IDENTIFICATION_VARIABLE, 0); }
        public Update_itemContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_update_item; }
    }

    public final Update_itemContext update_item() throws RecognitionException {
        Update_itemContext _localctx = new Update_itemContext(_ctx, getState());
        enterRule(_localctx, 42, RULE_update_item);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(318);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la==IDENTIFICATION_VARIABLE) {
                    {
                        setState(316);
                        match(IDENTIFICATION_VARIABLE);
                        setState(317);
                        match(T__7);
                    }
                }

                setState(322);
                _errHandler.sync(this);
                switch ( getInterpreter().adaptivePredict(_input,18,_ctx) ) {
                    case 1:
                    {
                        setState(320);
                        state_field();
                    }
                    break;
                    case 2:
                    {
                        setState(321);
                        single_valued_association_field();
                    }
                    break;
                }
                setState(324);
                match(T__14);
                setState(325);
                new_value();
            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class New_valueContext extends ParserRuleContext {
        public Simple_arithmetic_expressionContext simple_arithmetic_expression() {
            return getRuleContext(Simple_arithmetic_expressionContext.class,0);
        }
        public String_primaryContext string_primary() {
            return getRuleContext(String_primaryContext.class,0);
        }
        public Datetime_primaryContext datetime_primary() {
            return getRuleContext(Datetime_primaryContext.class,0);
        }
        public Boolean_primaryContext boolean_primary() {
            return getRuleContext(Boolean_primaryContext.class,0);
        }
        public Enum_primaryContext enum_primary() {
            return getRuleContext(Enum_primaryContext.class,0);
        }
        public Simple_entity_expressionContext simple_entity_expression() {
            return getRuleContext(Simple_entity_expressionContext.class,0);
        }
        public New_valueContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_new_value; }
    }

    public final New_valueContext new_value() throws RecognitionException {
        New_valueContext _localctx = new New_valueContext(_ctx, getState());
        enterRule(_localctx, 44, RULE_new_value);
        try {
            setState(334);
            _errHandler.sync(this);
            switch ( getInterpreter().adaptivePredict(_input,19,_ctx) ) {
                case 1:
                    enterOuterAlt(_localctx, 1);
                {
                    setState(327);
                    simple_arithmetic_expression();
                }
                break;
                case 2:
                    enterOuterAlt(_localctx, 2);
                {
                    setState(328);
                    string_primary();
                }
                break;
                case 3:
                    enterOuterAlt(_localctx, 3);
                {
                    setState(329);
                    datetime_primary();
                }
                break;
                case 4:
                    enterOuterAlt(_localctx, 4);
                {
                    setState(330);
                    boolean_primary();
                }
                break;
                case 5:
                    enterOuterAlt(_localctx, 5);
                {
                    setState(331);
                    enum_primary();
                }
                break;
                case 6:
                    enterOuterAlt(_localctx, 6);
                {
                    setState(332);
                    simple_entity_expression();
                }
                break;
                case 7:
                    enterOuterAlt(_localctx, 7);
                {
                    setState(333);
                    match(T__15);
                }
                break;
            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class Delete_clauseContext extends ParserRuleContext {
        public Abstract_schema_nameContext abstract_schema_name() {
            return getRuleContext(Abstract_schema_nameContext.class,0);
        }
        public TerminalNode IDENTIFICATION_VARIABLE() { return getToken(OQLParser.IDENTIFICATION_VARIABLE, 0); }
        public Delete_clauseContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_delete_clause; }
    }

    public final Delete_clauseContext delete_clause() throws RecognitionException {
        Delete_clauseContext _localctx = new Delete_clauseContext(_ctx, getState());
        enterRule(_localctx, 46, RULE_delete_clause);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(336);
                match(T__16);
                setState(337);
                match(T__0);
                setState(338);
                abstract_schema_name();
                setState(343);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la==T__1 || _la==IDENTIFICATION_VARIABLE) {
                    {
                        setState(340);
                        _errHandler.sync(this);
                        _la = _input.LA(1);
                        if (_la==T__1) {
                            {
                                setState(339);
                                match(T__1);
                            }
                        }

                        setState(342);
                        match(IDENTIFICATION_VARIABLE);
                    }
                }

            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class Select_clauseContext extends ParserRuleContext {
        public List<SelectPath> select;
        public Select_expressionContext select_expression;
        public List<Select_expressionContext> select_expression() {
            return getRuleContexts(Select_expressionContext.class);
        }
        public Select_expressionContext select_expression(int i) {
            return getRuleContext(Select_expressionContext.class,i);
        }
        public Select_clauseContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_select_clause; }
    }

    public final Select_clauseContext select_clause() throws RecognitionException {
        Select_clauseContext _localctx = new Select_clauseContext(_ctx, getState());
        enterRule(_localctx, 48, RULE_select_clause);
        ((Select_clauseContext)_localctx).select =  new ArrayList<SelectPath>();
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(345);
                match(T__17);
                setState(347);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la==T__18) {
                    {
                        setState(346);
                        match(T__18);
                    }
                }

                setState(349);
                ((Select_clauseContext)_localctx).select_expression = select_expression();
                _localctx.select.add(((Select_clauseContext)_localctx).select_expression.select);
                setState(357);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la==T__13) {
                    {
                        {
                            setState(351);
                            match(T__13);
                            setState(352);
                            ((Select_clauseContext)_localctx).select_expression = select_expression();
                            _localctx.select.add(((Select_clauseContext)_localctx).select_expression.select);
                        }
                    }
                    setState(359);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class Select_expressionContext extends ParserRuleContext {
        public SelectPath select;
        public Token IDENTIFICATION_VARIABLE;
        public List<TerminalNode> IDENTIFICATION_VARIABLE() { return getTokens(OQLParser.IDENTIFICATION_VARIABLE); }
        public TerminalNode IDENTIFICATION_VARIABLE(int i) {
            return getToken(OQLParser.IDENTIFICATION_VARIABLE, i);
        }
        public Select_expressionContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_select_expression; }
    }

    public final Select_expressionContext select_expression() throws RecognitionException {
        Select_expressionContext _localctx = new Select_expressionContext(_ctx, getState());
        enterRule(_localctx, 50, RULE_select_expression);
        ((Select_expressionContext)_localctx).select =  new SelectPath();
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(360);
                ((Select_expressionContext)_localctx).IDENTIFICATION_VARIABLE = match(IDENTIFICATION_VARIABLE);
                _localctx.select.add((((Select_expressionContext)_localctx).IDENTIFICATION_VARIABLE!=null?((Select_expressionContext)_localctx).IDENTIFICATION_VARIABLE.getText():null));
                setState(367);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la==T__7) {
                    {
                        {
                            setState(362);
                            match(T__7);
                            setState(363);
                            ((Select_expressionContext)_localctx).IDENTIFICATION_VARIABLE = match(IDENTIFICATION_VARIABLE);
                            _localctx.select.add((((Select_expressionContext)_localctx).IDENTIFICATION_VARIABLE!=null?((Select_expressionContext)_localctx).IDENTIFICATION_VARIABLE.getText():null));
                        }
                    }
                    setState(369);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class Constructor_expressionContext extends ParserRuleContext {
        public Constructor_nameContext constructor_name() {
            return getRuleContext(Constructor_nameContext.class,0);
        }
        public List<Constructor_itemContext> constructor_item() {
            return getRuleContexts(Constructor_itemContext.class);
        }
        public Constructor_itemContext constructor_item(int i) {
            return getRuleContext(Constructor_itemContext.class,i);
        }
        public Constructor_expressionContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_constructor_expression; }
    }

    public final Constructor_expressionContext constructor_expression() throws RecognitionException {
        Constructor_expressionContext _localctx = new Constructor_expressionContext(_ctx, getState());
        enterRule(_localctx, 52, RULE_constructor_expression);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(370);
                match(T__19);
                setState(371);
                constructor_name();
                setState(372);
                match(T__9);
                setState(373);
                constructor_item();
                setState(378);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la==T__13) {
                    {
                        {
                            setState(374);
                            match(T__13);
                            setState(375);
                            constructor_item();
                        }
                    }
                    setState(380);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                setState(381);
                match(T__10);
            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class Constructor_itemContext extends ParserRuleContext {
        public Single_valued_path_expressionContext single_valued_path_expression() {
            return getRuleContext(Single_valued_path_expressionContext.class,0);
        }
        public Aggregate_expressionContext aggregate_expression() {
            return getRuleContext(Aggregate_expressionContext.class,0);
        }
        public Constructor_itemContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_constructor_item; }
    }

    public final Constructor_itemContext constructor_item() throws RecognitionException {
        Constructor_itemContext _localctx = new Constructor_itemContext(_ctx, getState());
        enterRule(_localctx, 54, RULE_constructor_item);
        try {
            setState(385);
            _errHandler.sync(this);
            switch (_input.LA(1)) {
                case IDENTIFICATION_VARIABLE:
                    enterOuterAlt(_localctx, 1);
                {
                    setState(383);
                    single_valued_path_expression();
                }
                break;
                case T__20:
                case T__21:
                case T__22:
                case T__23:
                case T__24:
                    enterOuterAlt(_localctx, 2);
                {
                    setState(384);
                    aggregate_expression();
                }
                break;
                default:
                    throw new NoViableAltException(this);
            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class Aggregate_expressionContext extends ParserRuleContext {
        public State_field_path_expressionContext state_field_path_expression() {
            return getRuleContext(State_field_path_expressionContext.class,0);
        }
        public TerminalNode IDENTIFICATION_VARIABLE() { return getToken(OQLParser.IDENTIFICATION_VARIABLE, 0); }
        public Single_valued_association_path_expressionContext single_valued_association_path_expression() {
            return getRuleContext(Single_valued_association_path_expressionContext.class,0);
        }
        public Aggregate_expressionContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_aggregate_expression; }
    }

    public final Aggregate_expressionContext aggregate_expression() throws RecognitionException {
        Aggregate_expressionContext _localctx = new Aggregate_expressionContext(_ctx, getState());
        enterRule(_localctx, 56, RULE_aggregate_expression);
        int _la;
        try {
            setState(406);
            _errHandler.sync(this);
            switch (_input.LA(1)) {
                case T__20:
                case T__21:
                case T__22:
                case T__23:
                    enterOuterAlt(_localctx, 1);
                {
                    setState(387);
                    _la = _input.LA(1);
                    if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 31457280L) != 0)) ) {
                        _errHandler.recoverInline(this);
                    }
                    else {
                        if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
                        _errHandler.reportMatch(this);
                        consume();
                    }
                    setState(388);
                    match(T__9);
                    setState(390);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                    if (_la==T__18) {
                        {
                            setState(389);
                            match(T__18);
                        }
                    }

                    setState(392);
                    state_field_path_expression();
                    setState(393);
                    match(T__10);
                }
                break;
                case T__24:
                    enterOuterAlt(_localctx, 2);
                {
                    setState(395);
                    match(T__24);
                    setState(396);
                    match(T__9);
                    setState(398);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                    if (_la==T__18) {
                        {
                            setState(397);
                            match(T__18);
                        }
                    }

                    setState(403);
                    _errHandler.sync(this);
                    switch ( getInterpreter().adaptivePredict(_input,29,_ctx) ) {
                        case 1:
                        {
                            setState(400);
                            match(IDENTIFICATION_VARIABLE);
                        }
                        break;
                        case 2:
                        {
                            setState(401);
                            state_field_path_expression();
                        }
                        break;
                        case 3:
                        {
                            setState(402);
                            single_valued_association_path_expression();
                        }
                        break;
                    }
                    setState(405);
                    match(T__10);
                }
                break;
                default:
                    throw new NoViableAltException(this);
            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class Where_clauseContext extends ParserRuleContext {
        public Conditional_expressionContext conditional_expression;
        public Conditional_expressionContext conditional_expression() {
            return getRuleContext(Conditional_expressionContext.class,0);
        }
        public Where_clauseContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_where_clause; }
    }

    public final Where_clauseContext where_clause() throws RecognitionException {
        Where_clauseContext _localctx = new Where_clauseContext(_ctx, getState());
        enterRule(_localctx, 58, RULE_where_clause);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(408);
                match(T__25);
                setState(409);
                ((Where_clauseContext)_localctx).conditional_expression = conditional_expression();
                getQuery().where(((Where_clauseContext)_localctx).conditional_expression.expression);
            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class Groupby_clauseContext extends ParserRuleContext {
        public List<Groupby_itemContext> groupby_item() {
            return getRuleContexts(Groupby_itemContext.class);
        }
        public Groupby_itemContext groupby_item(int i) {
            return getRuleContext(Groupby_itemContext.class,i);
        }
        public Groupby_clauseContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_groupby_clause; }
    }

    public final Groupby_clauseContext groupby_clause() throws RecognitionException {
        Groupby_clauseContext _localctx = new Groupby_clauseContext(_ctx, getState());
        enterRule(_localctx, 60, RULE_groupby_clause);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(412);
                match(T__26);
                setState(413);
                match(T__27);
                setState(414);
                groupby_item();
                setState(419);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la==T__13) {
                    {
                        {
                            setState(415);
                            match(T__13);
                            setState(416);
                            groupby_item();
                        }
                    }
                    setState(421);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class Groupby_itemContext extends ParserRuleContext {
        public Single_valued_path_expressionContext single_valued_path_expression() {
            return getRuleContext(Single_valued_path_expressionContext.class,0);
        }
        public TerminalNode IDENTIFICATION_VARIABLE() { return getToken(OQLParser.IDENTIFICATION_VARIABLE, 0); }
        public Groupby_itemContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_groupby_item; }
    }

    public final Groupby_itemContext groupby_item() throws RecognitionException {
        Groupby_itemContext _localctx = new Groupby_itemContext(_ctx, getState());
        enterRule(_localctx, 62, RULE_groupby_item);
        try {
            setState(424);
            _errHandler.sync(this);
            switch ( getInterpreter().adaptivePredict(_input,32,_ctx) ) {
                case 1:
                    enterOuterAlt(_localctx, 1);
                {
                    setState(422);
                    single_valued_path_expression();
                }
                break;
                case 2:
                    enterOuterAlt(_localctx, 2);
                {
                    setState(423);
                    match(IDENTIFICATION_VARIABLE);
                }
                break;
            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class Having_clauseContext extends ParserRuleContext {
        public Conditional_expressionContext conditional_expression() {
            return getRuleContext(Conditional_expressionContext.class,0);
        }
        public Having_clauseContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_having_clause; }
    }

    public final Having_clauseContext having_clause() throws RecognitionException {
        Having_clauseContext _localctx = new Having_clauseContext(_ctx, getState());
        enterRule(_localctx, 64, RULE_having_clause);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(426);
                match(T__28);
                setState(427);
                conditional_expression();
            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class Orderby_clauseContext extends ParserRuleContext {
        public List<Orderby_itemContext> orderby_item() {
            return getRuleContexts(Orderby_itemContext.class);
        }
        public Orderby_itemContext orderby_item(int i) {
            return getRuleContext(Orderby_itemContext.class,i);
        }
        public Orderby_clauseContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_orderby_clause; }
    }

    public final Orderby_clauseContext orderby_clause() throws RecognitionException {
        Orderby_clauseContext _localctx = new Orderby_clauseContext(_ctx, getState());
        enterRule(_localctx, 66, RULE_orderby_clause);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(429);
                match(T__29);
                setState(430);
                match(T__27);
                setState(431);
                orderby_item();
                setState(436);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la==T__13) {
                    {
                        {
                            setState(432);
                            match(T__13);
                            setState(433);
                            orderby_item();
                        }
                    }
                    setState(438);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class Orderby_itemContext extends ParserRuleContext {
        public State_field_path_expressionContext state_field_path_expression() {
            return getRuleContext(State_field_path_expressionContext.class,0);
        }
        public Orderby_itemContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_orderby_item; }
    }

    public final Orderby_itemContext orderby_item() throws RecognitionException {
        Orderby_itemContext _localctx = new Orderby_itemContext(_ctx, getState());
        enterRule(_localctx, 68, RULE_orderby_item);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(439);
                state_field_path_expression();
                setState(441);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la==T__30 || _la==T__31) {
                    {
                        setState(440);
                        _la = _input.LA(1);
                        if ( !(_la==T__30 || _la==T__31) ) {
                            _errHandler.recoverInline(this);
                        }
                        else {
                            if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
                            _errHandler.reportMatch(this);
                            consume();
                        }
                    }
                }

            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class SubqueryContext extends ParserRuleContext {
        public Simple_select_clauseContext simple_select_clause() {
            return getRuleContext(Simple_select_clauseContext.class,0);
        }
        public Subquery_from_clauseContext subquery_from_clause() {
            return getRuleContext(Subquery_from_clauseContext.class,0);
        }
        public Where_clauseContext where_clause() {
            return getRuleContext(Where_clauseContext.class,0);
        }
        public Groupby_clauseContext groupby_clause() {
            return getRuleContext(Groupby_clauseContext.class,0);
        }
        public Having_clauseContext having_clause() {
            return getRuleContext(Having_clauseContext.class,0);
        }
        public SubqueryContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_subquery; }
    }

    public final SubqueryContext subquery() throws RecognitionException {
        SubqueryContext _localctx = new SubqueryContext(_ctx, getState());
        enterRule(_localctx, 70, RULE_subquery);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(443);
                simple_select_clause();
                setState(444);
                subquery_from_clause();
                setState(446);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la==T__25) {
                    {
                        setState(445);
                        where_clause();
                    }
                }

                setState(449);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la==T__26) {
                    {
                        setState(448);
                        groupby_clause();
                    }
                }

                setState(452);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la==T__28) {
                    {
                        setState(451);
                        having_clause();
                    }
                }

            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class Subquery_from_clauseContext extends ParserRuleContext {
        public List<Subselect_identification_variable_declarationContext> subselect_identification_variable_declaration() {
            return getRuleContexts(Subselect_identification_variable_declarationContext.class);
        }
        public Subselect_identification_variable_declarationContext subselect_identification_variable_declaration(int i) {
            return getRuleContext(Subselect_identification_variable_declarationContext.class,i);
        }
        public Subquery_from_clauseContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_subquery_from_clause; }
    }

    public final Subquery_from_clauseContext subquery_from_clause() throws RecognitionException {
        Subquery_from_clauseContext _localctx = new Subquery_from_clauseContext(_ctx, getState());
        enterRule(_localctx, 72, RULE_subquery_from_clause);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(454);
                match(T__0);
                setState(455);
                subselect_identification_variable_declaration();
                setState(460);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la==T__13) {
                    {
                        {
                            setState(456);
                            match(T__13);
                            setState(457);
                            subselect_identification_variable_declaration();
                        }
                    }
                    setState(462);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class Subselect_identification_variable_declarationContext extends ParserRuleContext {
        public Identification_variable_declarationContext identification_variable_declaration() {
            return getRuleContext(Identification_variable_declarationContext.class,0);
        }
        public Association_path_expressionContext association_path_expression() {
            return getRuleContext(Association_path_expressionContext.class,0);
        }
        public TerminalNode IDENTIFICATION_VARIABLE() { return getToken(OQLParser.IDENTIFICATION_VARIABLE, 0); }
        public Collection_member_declarationContext collection_member_declaration() {
            return getRuleContext(Collection_member_declarationContext.class,0);
        }
        public Subselect_identification_variable_declarationContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_subselect_identification_variable_declaration; }
    }

    public final Subselect_identification_variable_declarationContext subselect_identification_variable_declaration() throws RecognitionException {
        Subselect_identification_variable_declarationContext _localctx = new Subselect_identification_variable_declarationContext(_ctx, getState());
        enterRule(_localctx, 74, RULE_subselect_identification_variable_declaration);
        int _la;
        try {
            setState(471);
            _errHandler.sync(this);
            switch ( getInterpreter().adaptivePredict(_input,40,_ctx) ) {
                case 1:
                    enterOuterAlt(_localctx, 1);
                {
                    setState(463);
                    identification_variable_declaration();
                }
                break;
                case 2:
                    enterOuterAlt(_localctx, 2);
                {
                    setState(464);
                    association_path_expression();
                    setState(466);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                    if (_la==T__1) {
                        {
                            setState(465);
                            match(T__1);
                        }
                    }

                    setState(468);
                    match(IDENTIFICATION_VARIABLE);
                }
                break;
                case 3:
                    enterOuterAlt(_localctx, 3);
                {
                    setState(470);
                    collection_member_declaration();
                }
                break;
            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class Association_path_expressionContext extends ParserRuleContext {
        public Collection_valued_path_expressionContext collection_valued_path_expression() {
            return getRuleContext(Collection_valued_path_expressionContext.class,0);
        }
        public Single_valued_association_path_expressionContext single_valued_association_path_expression() {
            return getRuleContext(Single_valued_association_path_expressionContext.class,0);
        }
        public Association_path_expressionContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_association_path_expression; }
    }

    public final Association_path_expressionContext association_path_expression() throws RecognitionException {
        Association_path_expressionContext _localctx = new Association_path_expressionContext(_ctx, getState());
        enterRule(_localctx, 76, RULE_association_path_expression);
        try {
            setState(475);
            _errHandler.sync(this);
            switch ( getInterpreter().adaptivePredict(_input,41,_ctx) ) {
                case 1:
                    enterOuterAlt(_localctx, 1);
                {
                    setState(473);
                    collection_valued_path_expression();
                }
                break;
                case 2:
                    enterOuterAlt(_localctx, 2);
                {
                    setState(474);
                    single_valued_association_path_expression();
                }
                break;
            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class Simple_select_clauseContext extends ParserRuleContext {
        public Simple_select_expressionContext simple_select_expression() {
            return getRuleContext(Simple_select_expressionContext.class,0);
        }
        public Simple_select_clauseContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_simple_select_clause; }
    }

    public final Simple_select_clauseContext simple_select_clause() throws RecognitionException {
        Simple_select_clauseContext _localctx = new Simple_select_clauseContext(_ctx, getState());
        enterRule(_localctx, 78, RULE_simple_select_clause);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(477);
                match(T__17);
                setState(479);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la==T__18) {
                    {
                        setState(478);
                        match(T__18);
                    }
                }

                setState(481);
                simple_select_expression();
            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class Simple_select_expressionContext extends ParserRuleContext {
        public Single_valued_path_expressionContext single_valued_path_expression() {
            return getRuleContext(Single_valued_path_expressionContext.class,0);
        }
        public Aggregate_expressionContext aggregate_expression() {
            return getRuleContext(Aggregate_expressionContext.class,0);
        }
        public TerminalNode IDENTIFICATION_VARIABLE() { return getToken(OQLParser.IDENTIFICATION_VARIABLE, 0); }
        public Simple_select_expressionContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_simple_select_expression; }
    }

    public final Simple_select_expressionContext simple_select_expression() throws RecognitionException {
        Simple_select_expressionContext _localctx = new Simple_select_expressionContext(_ctx, getState());
        enterRule(_localctx, 80, RULE_simple_select_expression);
        try {
            setState(486);
            _errHandler.sync(this);
            switch ( getInterpreter().adaptivePredict(_input,43,_ctx) ) {
                case 1:
                    enterOuterAlt(_localctx, 1);
                {
                    setState(483);
                    single_valued_path_expression();
                }
                break;
                case 2:
                    enterOuterAlt(_localctx, 2);
                {
                    setState(484);
                    aggregate_expression();
                }
                break;
                case 3:
                    enterOuterAlt(_localctx, 3);
                {
                    setState(485);
                    match(IDENTIFICATION_VARIABLE);
                }
                break;
            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class Conditional_expressionContext extends ParserRuleContext {
        public ObjectExpression expression;
        public Conditional_termContext conditional_term;
        public Conditional_termContext conditional_term() {
            return getRuleContext(Conditional_termContext.class,0);
        }
        public Conditional_expressionContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_conditional_expression; }
    }

    public final Conditional_expressionContext conditional_expression() throws RecognitionException {
        Conditional_expressionContext _localctx = new Conditional_expressionContext(_ctx, getState());
        enterRule(_localctx, 82, RULE_conditional_expression);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(488);
                ((Conditional_expressionContext)_localctx).conditional_term = conditional_term();
                ((Conditional_expressionContext)_localctx).expression =  ((Conditional_expressionContext)_localctx).conditional_term.expression;
            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class Conditional_termContext extends ParserRuleContext {
        public ObjectExpression expression;
        public Conditional_factorContext conditional_factor;
        public Conditional_factorContext conditional_factor() {
            return getRuleContext(Conditional_factorContext.class,0);
        }
        public Conditional_termContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_conditional_term; }
    }

    public final Conditional_termContext conditional_term() throws RecognitionException {
        Conditional_termContext _localctx = new Conditional_termContext(_ctx, getState());
        enterRule(_localctx, 84, RULE_conditional_term);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(491);
                ((Conditional_termContext)_localctx).conditional_factor = conditional_factor();
                ((Conditional_termContext)_localctx).expression =  ((Conditional_termContext)_localctx).conditional_factor.expression;
            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class Conditional_factorContext extends ParserRuleContext {
        public ObjectExpression expression;
        public Conditional_primaryContext conditional_primary;
        public Conditional_primaryContext conditional_primary() {
            return getRuleContext(Conditional_primaryContext.class,0);
        }
        public Conditional_factorContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_conditional_factor; }
    }

    public final Conditional_factorContext conditional_factor() throws RecognitionException {
        Conditional_factorContext _localctx = new Conditional_factorContext(_ctx, getState());
        enterRule(_localctx, 86, RULE_conditional_factor);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(494);
                ((Conditional_factorContext)_localctx).conditional_primary = conditional_primary();
                ((Conditional_factorContext)_localctx).expression =  ((Conditional_factorContext)_localctx).conditional_primary.expression;
            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class Conditional_primaryContext extends ParserRuleContext {
        public ObjectExpression expression;
        public Simple_cond_expressionContext simple_cond_expression;
        public Simple_cond_expressionContext simple_cond_expression() {
            return getRuleContext(Simple_cond_expressionContext.class,0);
        }
        public Conditional_primaryContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_conditional_primary; }
    }

    public final Conditional_primaryContext conditional_primary() throws RecognitionException {
        Conditional_primaryContext _localctx = new Conditional_primaryContext(_ctx, getState());
        enterRule(_localctx, 88, RULE_conditional_primary);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(497);
                ((Conditional_primaryContext)_localctx).simple_cond_expression = simple_cond_expression();
                ((Conditional_primaryContext)_localctx).expression =  ((Conditional_primaryContext)_localctx).simple_cond_expression.expression;
            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class Simple_cond_expressionContext extends ParserRuleContext {
        public ObjectExpression expression;
        public Comparison_expressionContext comparison_expression;
        public Comparison_expressionContext comparison_expression() {
            return getRuleContext(Comparison_expressionContext.class,0);
        }
        public Simple_cond_expressionContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_simple_cond_expression; }
    }

    public final Simple_cond_expressionContext simple_cond_expression() throws RecognitionException {
        Simple_cond_expressionContext _localctx = new Simple_cond_expressionContext(_ctx, getState());
        enterRule(_localctx, 90, RULE_simple_cond_expression);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(500);
                ((Simple_cond_expressionContext)_localctx).comparison_expression = comparison_expression();
                ((Simple_cond_expressionContext)_localctx).expression =  ((Simple_cond_expressionContext)_localctx).comparison_expression.expression;
            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class Between_expressionContext extends ParserRuleContext {
        public List<Arithmetic_expressionContext> arithmetic_expression() {
            return getRuleContexts(Arithmetic_expressionContext.class);
        }
        public Arithmetic_expressionContext arithmetic_expression(int i) {
            return getRuleContext(Arithmetic_expressionContext.class,i);
        }
        public List<String_expressionContext> string_expression() {
            return getRuleContexts(String_expressionContext.class);
        }
        public String_expressionContext string_expression(int i) {
            return getRuleContext(String_expressionContext.class,i);
        }
        public List<Datetime_expressionContext> datetime_expression() {
            return getRuleContexts(Datetime_expressionContext.class);
        }
        public Datetime_expressionContext datetime_expression(int i) {
            return getRuleContext(Datetime_expressionContext.class,i);
        }
        public Between_expressionContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_between_expression; }
    }

    public final Between_expressionContext between_expression() throws RecognitionException {
        Between_expressionContext _localctx = new Between_expressionContext(_ctx, getState());
        enterRule(_localctx, 92, RULE_between_expression);
        int _la;
        try {
            setState(530);
            _errHandler.sync(this);
            switch ( getInterpreter().adaptivePredict(_input,47,_ctx) ) {
                case 1:
                    enterOuterAlt(_localctx, 1);
                {
                    setState(503);
                    arithmetic_expression();
                    setState(505);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                    if (_la==T__32) {
                        {
                            setState(504);
                            match(T__32);
                        }
                    }

                    setState(507);
                    match(T__33);
                    setState(508);
                    arithmetic_expression();
                    setState(509);
                    match(T__34);
                    setState(510);
                    arithmetic_expression();
                }
                break;
                case 2:
                    enterOuterAlt(_localctx, 2);
                {
                    setState(512);
                    string_expression();
                    setState(514);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                    if (_la==T__32) {
                        {
                            setState(513);
                            match(T__32);
                        }
                    }

                    setState(516);
                    match(T__33);
                    setState(517);
                    string_expression();
                    setState(518);
                    match(T__34);
                    setState(519);
                    string_expression();
                }
                break;
                case 3:
                    enterOuterAlt(_localctx, 3);
                {
                    setState(521);
                    datetime_expression();
                    setState(523);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                    if (_la==T__32) {
                        {
                            setState(522);
                            match(T__32);
                        }
                    }

                    setState(525);
                    match(T__33);
                    setState(526);
                    datetime_expression();
                    setState(527);
                    match(T__34);
                    setState(528);
                    datetime_expression();
                }
                break;
            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class In_expressionContext extends ParserRuleContext {
        public State_field_path_expressionContext state_field_path_expression() {
            return getRuleContext(State_field_path_expressionContext.class,0);
        }
        public List<In_itemContext> in_item() {
            return getRuleContexts(In_itemContext.class);
        }
        public In_itemContext in_item(int i) {
            return getRuleContext(In_itemContext.class,i);
        }
        public SubqueryContext subquery() {
            return getRuleContext(SubqueryContext.class,0);
        }
        public In_expressionContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_in_expression; }
    }

    public final In_expressionContext in_expression() throws RecognitionException {
        In_expressionContext _localctx = new In_expressionContext(_ctx, getState());
        enterRule(_localctx, 94, RULE_in_expression);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(532);
                state_field_path_expression();
                setState(534);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la==T__32) {
                    {
                        setState(533);
                        match(T__32);
                    }
                }

                setState(536);
                match(T__8);
                setState(537);
                match(T__9);
                setState(547);
                _errHandler.sync(this);
                switch (_input.LA(1)) {
                    case T__10:
                    case T__13:
                    case T__67:
                    {
                        setState(538);
                        in_item();
                        setState(543);
                        _errHandler.sync(this);
                        _la = _input.LA(1);
                        while (_la==T__13) {
                            {
                                {
                                    setState(539);
                                    match(T__13);
                                    setState(540);
                                    in_item();
                                }
                            }
                            setState(545);
                            _errHandler.sync(this);
                            _la = _input.LA(1);
                        }
                    }
                    break;
                    case T__17:
                    {
                        setState(546);
                        subquery();
                    }
                    break;
                    default:
                        throw new NoViableAltException(this);
                }
                setState(549);
                match(T__10);
            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class In_itemContext extends ParserRuleContext {
        public LiteralContext literal() {
            return getRuleContext(LiteralContext.class,0);
        }
        public Input_parameterContext input_parameter() {
            return getRuleContext(Input_parameterContext.class,0);
        }
        public In_itemContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_in_item; }
    }

    public final In_itemContext in_item() throws RecognitionException {
        In_itemContext _localctx = new In_itemContext(_ctx, getState());
        enterRule(_localctx, 96, RULE_in_item);
        try {
            setState(553);
            _errHandler.sync(this);
            switch (_input.LA(1)) {
                case T__10:
                case T__13:
                    enterOuterAlt(_localctx, 1);
                {
                    setState(551);
                    literal();
                }
                break;
                case T__67:
                    enterOuterAlt(_localctx, 2);
                {
                    setState(552);
                    input_parameter();
                }
                break;
                default:
                    throw new NoViableAltException(this);
            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class Like_expressionContext extends ParserRuleContext {
        public String_expressionContext string_expression() {
            return getRuleContext(String_expressionContext.class,0);
        }
        public Pattern_valueContext pattern_value() {
            return getRuleContext(Pattern_valueContext.class,0);
        }
        public TerminalNode ESCAPE_CHARACTER() { return getToken(OQLParser.ESCAPE_CHARACTER, 0); }
        public Like_expressionContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_like_expression; }
    }

    public final Like_expressionContext like_expression() throws RecognitionException {
        Like_expressionContext _localctx = new Like_expressionContext(_ctx, getState());
        enterRule(_localctx, 98, RULE_like_expression);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(555);
                string_expression();
                setState(557);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la==T__32) {
                    {
                        setState(556);
                        match(T__32);
                    }
                }

                setState(559);
                match(T__35);
                setState(560);
                pattern_value();
                setState(563);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la==T__36) {
                    {
                        setState(561);
                        match(T__36);
                        setState(562);
                        match(ESCAPE_CHARACTER);
                    }
                }

            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class Null_comparison_expressionContext extends ParserRuleContext {
        public Single_valued_path_expressionContext single_valued_path_expression() {
            return getRuleContext(Single_valued_path_expressionContext.class,0);
        }
        public Input_parameterContext input_parameter() {
            return getRuleContext(Input_parameterContext.class,0);
        }
        public Null_comparison_expressionContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_null_comparison_expression; }
    }

    public final Null_comparison_expressionContext null_comparison_expression() throws RecognitionException {
        Null_comparison_expressionContext _localctx = new Null_comparison_expressionContext(_ctx, getState());
        enterRule(_localctx, 100, RULE_null_comparison_expression);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(567);
                _errHandler.sync(this);
                switch (_input.LA(1)) {
                    case IDENTIFICATION_VARIABLE:
                    {
                        setState(565);
                        single_valued_path_expression();
                    }
                    break;
                    case T__67:
                    {
                        setState(566);
                        input_parameter();
                    }
                    break;
                    default:
                        throw new NoViableAltException(this);
                }
                setState(569);
                match(T__37);
                setState(571);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la==T__32) {
                    {
                        setState(570);
                        match(T__32);
                    }
                }

                setState(573);
                match(T__15);
            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class Empty_collection_comparison_expressionContext extends ParserRuleContext {
        public Collection_valued_path_expressionContext collection_valued_path_expression() {
            return getRuleContext(Collection_valued_path_expressionContext.class,0);
        }
        public Empty_collection_comparison_expressionContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_empty_collection_comparison_expression; }
    }

    public final Empty_collection_comparison_expressionContext empty_collection_comparison_expression() throws RecognitionException {
        Empty_collection_comparison_expressionContext _localctx = new Empty_collection_comparison_expressionContext(_ctx, getState());
        enterRule(_localctx, 102, RULE_empty_collection_comparison_expression);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(575);
                collection_valued_path_expression();
                setState(576);
                match(T__37);
                setState(578);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la==T__32) {
                    {
                        setState(577);
                        match(T__32);
                    }
                }

                setState(580);
                match(T__38);
            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class Collection_member_expressionContext extends ParserRuleContext {
        public Entity_expressionContext entity_expression() {
            return getRuleContext(Entity_expressionContext.class,0);
        }
        public Collection_valued_path_expressionContext collection_valued_path_expression() {
            return getRuleContext(Collection_valued_path_expressionContext.class,0);
        }
        public Collection_member_expressionContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_collection_member_expression; }
    }

    public final Collection_member_expressionContext collection_member_expression() throws RecognitionException {
        Collection_member_expressionContext _localctx = new Collection_member_expressionContext(_ctx, getState());
        enterRule(_localctx, 104, RULE_collection_member_expression);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(582);
                entity_expression();
                setState(584);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la==T__32) {
                    {
                        setState(583);
                        match(T__32);
                    }
                }

                setState(586);
                match(T__39);
                setState(588);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la==T__40) {
                    {
                        setState(587);
                        match(T__40);
                    }
                }

                setState(590);
                collection_valued_path_expression();
            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class Exists_expressionContext extends ParserRuleContext {
        public SubqueryContext subquery() {
            return getRuleContext(SubqueryContext.class,0);
        }
        public Exists_expressionContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_exists_expression; }
    }

    public final Exists_expressionContext exists_expression() throws RecognitionException {
        Exists_expressionContext _localctx = new Exists_expressionContext(_ctx, getState());
        enterRule(_localctx, 106, RULE_exists_expression);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(593);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la==T__32) {
                    {
                        setState(592);
                        match(T__32);
                    }
                }

                setState(595);
                match(T__41);
                setState(596);
                match(T__9);
                setState(597);
                subquery();
                setState(598);
                match(T__10);
            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class All_or_any_expressionContext extends ParserRuleContext {
        public SubqueryContext subquery() {
            return getRuleContext(SubqueryContext.class,0);
        }
        public All_or_any_expressionContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_all_or_any_expression; }
    }

    public final All_or_any_expressionContext all_or_any_expression() throws RecognitionException {
        All_or_any_expressionContext _localctx = new All_or_any_expressionContext(_ctx, getState());
        enterRule(_localctx, 108, RULE_all_or_any_expression);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(600);
                _la = _input.LA(1);
                if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 61572651155456L) != 0)) ) {
                    _errHandler.recoverInline(this);
                }
                else {
                    if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
                    _errHandler.reportMatch(this);
                    consume();
                }
                setState(601);
                match(T__9);
                setState(602);
                subquery();
                setState(603);
                match(T__10);
            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class Comparison_expressionContext extends ParserRuleContext {
        public ObjectExpression expression;
        public Select_expressionContext select_expression;
        public Comparison_operatorContext comparison_operator;
        public Arithmetic_expressionContext arithmetic_expression;
        public Select_expressionContext select_expression() {
            return getRuleContext(Select_expressionContext.class,0);
        }
        public Comparison_operatorContext comparison_operator() {
            return getRuleContext(Comparison_operatorContext.class,0);
        }
        public Arithmetic_expressionContext arithmetic_expression() {
            return getRuleContext(Arithmetic_expressionContext.class,0);
        }
        public Comparison_expressionContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_comparison_expression; }
    }

    public final Comparison_expressionContext comparison_expression() throws RecognitionException {
        Comparison_expressionContext _localctx = new Comparison_expressionContext(_ctx, getState());
        enterRule(_localctx, 110, RULE_comparison_expression);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(605);
                ((Comparison_expressionContext)_localctx).select_expression = select_expression();
                setState(606);
                ((Comparison_expressionContext)_localctx).comparison_operator = comparison_operator();
                setState(607);
                ((Comparison_expressionContext)_localctx).arithmetic_expression = arithmetic_expression();
                ((Comparison_expressionContext)_localctx).expression =  condition(((Comparison_expressionContext)_localctx).select_expression.select, (((Comparison_expressionContext)_localctx).comparison_operator!=null?_input.getText(((Comparison_expressionContext)_localctx).comparison_operator.start,((Comparison_expressionContext)_localctx).comparison_operator.stop):null), ((Comparison_expressionContext)_localctx).arithmetic_expression.value);
            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class Path_expressionContext extends ParserRuleContext {
        public ObjectPath path;
        public LegContext leg;
        public LegContext leg() {
            return getRuleContext(LegContext.class,0);
        }
        public Path_expressionContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_path_expression; }
    }

    public final Path_expressionContext path_expression() throws RecognitionException {
        Path_expressionContext _localctx = new Path_expressionContext(_ctx, getState());
        enterRule(_localctx, 112, RULE_path_expression);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(610);
                ((Path_expressionContext)_localctx).leg = leg();
                ((Path_expressionContext)_localctx).path =  this.getFrom().get(((Path_expressionContext)_localctx).leg.value);
            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class Comparison_operatorContext extends ParserRuleContext {
        public String op;
        public Comparison_operatorContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_comparison_operator; }
    }

    public final Comparison_operatorContext comparison_operator() throws RecognitionException {
        Comparison_operatorContext _localctx = new Comparison_operatorContext(_ctx, getState());
        enterRule(_localctx, 114, RULE_comparison_operator);
        try {
            setState(625);
            _errHandler.sync(this);
            switch (_input.LA(1)) {
                case T__14:
                    enterOuterAlt(_localctx, 1);
                {
                    setState(613);
                    match(T__14);
                    ((Comparison_operatorContext)_localctx).op =  "=";
                }
                break;
                case T__45:
                    enterOuterAlt(_localctx, 2);
                {
                    setState(615);
                    match(T__45);
                    ((Comparison_operatorContext)_localctx).op =  ">";
                }
                break;
                case T__46:
                    enterOuterAlt(_localctx, 3);
                {
                    setState(617);
                    match(T__46);
                    ((Comparison_operatorContext)_localctx).op =  ">=";
                }
                break;
                case T__47:
                    enterOuterAlt(_localctx, 4);
                {
                    setState(619);
                    match(T__47);
                    ((Comparison_operatorContext)_localctx).op =  "<";
                }
                break;
                case T__48:
                    enterOuterAlt(_localctx, 5);
                {
                    setState(621);
                    match(T__48);
                    ((Comparison_operatorContext)_localctx).op =  "<=";
                }
                break;
                case T__49:
                    enterOuterAlt(_localctx, 6);
                {
                    setState(623);
                    match(T__49);
                    ((Comparison_operatorContext)_localctx).op =  "<>";
                }
                break;
                default:
                    throw new NoViableAltException(this);
            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class Arithmetic_expressionContext extends ParserRuleContext {
        public Value value;
        public Simple_arithmetic_expressionContext simple_arithmetic_expression;
        public Simple_arithmetic_expressionContext simple_arithmetic_expression() {
            return getRuleContext(Simple_arithmetic_expressionContext.class,0);
        }
        public Arithmetic_expressionContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_arithmetic_expression; }
    }

    public final Arithmetic_expressionContext arithmetic_expression() throws RecognitionException {
        Arithmetic_expressionContext _localctx = new Arithmetic_expressionContext(_ctx, getState());
        enterRule(_localctx, 116, RULE_arithmetic_expression);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(627);
                ((Arithmetic_expressionContext)_localctx).simple_arithmetic_expression = simple_arithmetic_expression();
                ((Arithmetic_expressionContext)_localctx).value =  ((Arithmetic_expressionContext)_localctx).simple_arithmetic_expression.value;
            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class Simple_arithmetic_expressionContext extends ParserRuleContext {
        public Value value;
        public Arithmetic_termContext arithmetic_term;
        public Arithmetic_termContext arithmetic_term() {
            return getRuleContext(Arithmetic_termContext.class,0);
        }
        public Simple_arithmetic_expressionContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_simple_arithmetic_expression; }
    }

    public final Simple_arithmetic_expressionContext simple_arithmetic_expression() throws RecognitionException {
        Simple_arithmetic_expressionContext _localctx = new Simple_arithmetic_expressionContext(_ctx, getState());
        enterRule(_localctx, 118, RULE_simple_arithmetic_expression);
        try {
            enterOuterAlt(_localctx, 1);
            {
                {
                    setState(630);
                    ((Simple_arithmetic_expressionContext)_localctx).arithmetic_term = arithmetic_term();
                }
                ((Simple_arithmetic_expressionContext)_localctx).value =  ((Simple_arithmetic_expressionContext)_localctx).arithmetic_term.value;
            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class Arithmetic_termContext extends ParserRuleContext {
        public Value value;
        public Arithmetic_factorContext arithmetic_factor;
        public Arithmetic_factorContext arithmetic_factor() {
            return getRuleContext(Arithmetic_factorContext.class,0);
        }
        public Arithmetic_termContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_arithmetic_term; }
    }

    public final Arithmetic_termContext arithmetic_term() throws RecognitionException {
        Arithmetic_termContext _localctx = new Arithmetic_termContext(_ctx, getState());
        enterRule(_localctx, 120, RULE_arithmetic_term);
        try {
            enterOuterAlt(_localctx, 1);
            {
                {
                    setState(633);
                    ((Arithmetic_termContext)_localctx).arithmetic_factor = arithmetic_factor();
                }
                ((Arithmetic_termContext)_localctx).value =  ((Arithmetic_termContext)_localctx).arithmetic_factor.value;
            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class Arithmetic_factorContext extends ParserRuleContext {
        public Value value;
        public Arithmetic_primaryContext arithmetic_primary;
        public Arithmetic_primaryContext arithmetic_primary() {
            return getRuleContext(Arithmetic_primaryContext.class,0);
        }
        public Arithmetic_factorContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_arithmetic_factor; }
    }

    public final Arithmetic_factorContext arithmetic_factor() throws RecognitionException {
        Arithmetic_factorContext _localctx = new Arithmetic_factorContext(_ctx, getState());
        enterRule(_localctx, 122, RULE_arithmetic_factor);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(636);
                ((Arithmetic_factorContext)_localctx).arithmetic_primary = arithmetic_primary();
                ((Arithmetic_factorContext)_localctx).value =  ((Arithmetic_factorContext)_localctx).arithmetic_primary.value;
            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class Arithmetic_primaryContext extends ParserRuleContext {
        public Value value;
        public Numeric_literalContext numeric_literal;
        public Input_parameterContext input_parameter;
        public Numeric_literalContext numeric_literal() {
            return getRuleContext(Numeric_literalContext.class,0);
        }
        public Input_parameterContext input_parameter() {
            return getRuleContext(Input_parameterContext.class,0);
        }
        public Arithmetic_primaryContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_arithmetic_primary; }
    }

    public final Arithmetic_primaryContext arithmetic_primary() throws RecognitionException {
        Arithmetic_primaryContext _localctx = new Arithmetic_primaryContext(_ctx, getState());
        enterRule(_localctx, 124, RULE_arithmetic_primary);
        try {
            setState(645);
            _errHandler.sync(this);
            switch (_input.LA(1)) {
                case NUMBER:
                    enterOuterAlt(_localctx, 1);
                {
                    setState(639);
                    ((Arithmetic_primaryContext)_localctx).numeric_literal = numeric_literal();
                    ((Arithmetic_primaryContext)_localctx).value =  new Constant(((Arithmetic_primaryContext)_localctx).numeric_literal.value);
                }
                break;
                case T__67:
                    enterOuterAlt(_localctx, 2);
                {
                    setState(642);
                    ((Arithmetic_primaryContext)_localctx).input_parameter = input_parameter();
                    ((Arithmetic_primaryContext)_localctx).value =  ((Arithmetic_primaryContext)_localctx).input_parameter.value;
                }
                break;
                default:
                    throw new NoViableAltException(this);
            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class String_expressionContext extends ParserRuleContext {
        public String_primaryContext string_primary() {
            return getRuleContext(String_primaryContext.class,0);
        }
        public String_expressionContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_string_expression; }
    }

    public final String_expressionContext string_expression() throws RecognitionException {
        String_expressionContext _localctx = new String_expressionContext(_ctx, getState());
        enterRule(_localctx, 126, RULE_string_expression);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(647);
                string_primary();
            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class String_primaryContext extends ParserRuleContext {
        public State_field_path_expressionContext state_field_path_expression() {
            return getRuleContext(State_field_path_expressionContext.class,0);
        }
        public TerminalNode STRINGLITERAL() { return getToken(OQLParser.STRINGLITERAL, 0); }
        public Input_parameterContext input_parameter() {
            return getRuleContext(Input_parameterContext.class,0);
        }
        public String_primaryContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_string_primary; }
    }

    public final String_primaryContext string_primary() throws RecognitionException {
        String_primaryContext _localctx = new String_primaryContext(_ctx, getState());
        enterRule(_localctx, 128, RULE_string_primary);
        try {
            setState(652);
            _errHandler.sync(this);
            switch (_input.LA(1)) {
                case IDENTIFICATION_VARIABLE:
                    enterOuterAlt(_localctx, 1);
                {
                    setState(649);
                    state_field_path_expression();
                }
                break;
                case STRINGLITERAL:
                    enterOuterAlt(_localctx, 2);
                {
                    setState(650);
                    match(STRINGLITERAL);
                }
                break;
                case T__67:
                    enterOuterAlt(_localctx, 3);
                {
                    setState(651);
                    input_parameter();
                }
                break;
                default:
                    throw new NoViableAltException(this);
            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class Datetime_expressionContext extends ParserRuleContext {
        public Datetime_primaryContext datetime_primary() {
            return getRuleContext(Datetime_primaryContext.class,0);
        }
        public SubqueryContext subquery() {
            return getRuleContext(SubqueryContext.class,0);
        }
        public Datetime_expressionContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_datetime_expression; }
    }

    public final Datetime_expressionContext datetime_expression() throws RecognitionException {
        Datetime_expressionContext _localctx = new Datetime_expressionContext(_ctx, getState());
        enterRule(_localctx, 130, RULE_datetime_expression);
        try {
            setState(659);
            _errHandler.sync(this);
            switch (_input.LA(1)) {
                case T__20:
                case T__21:
                case T__22:
                case T__23:
                case T__24:
                case T__56:
                case T__57:
                case T__58:
                case T__67:
                case IDENTIFICATION_VARIABLE:
                    enterOuterAlt(_localctx, 1);
                {
                    setState(654);
                    datetime_primary();
                }
                break;
                case T__9:
                    enterOuterAlt(_localctx, 2);
                {
                    setState(655);
                    match(T__9);
                    setState(656);
                    subquery();
                    setState(657);
                    match(T__10);
                }
                break;
                default:
                    throw new NoViableAltException(this);
            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class Datetime_primaryContext extends ParserRuleContext {
        public State_field_path_expressionContext state_field_path_expression() {
            return getRuleContext(State_field_path_expressionContext.class,0);
        }
        public Input_parameterContext input_parameter() {
            return getRuleContext(Input_parameterContext.class,0);
        }
        public Functions_returning_datetimeContext functions_returning_datetime() {
            return getRuleContext(Functions_returning_datetimeContext.class,0);
        }
        public Aggregate_expressionContext aggregate_expression() {
            return getRuleContext(Aggregate_expressionContext.class,0);
        }
        public Datetime_primaryContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_datetime_primary; }
    }

    public final Datetime_primaryContext datetime_primary() throws RecognitionException {
        Datetime_primaryContext _localctx = new Datetime_primaryContext(_ctx, getState());
        enterRule(_localctx, 132, RULE_datetime_primary);
        try {
            setState(665);
            _errHandler.sync(this);
            switch (_input.LA(1)) {
                case IDENTIFICATION_VARIABLE:
                    enterOuterAlt(_localctx, 1);
                {
                    setState(661);
                    state_field_path_expression();
                }
                break;
                case T__67:
                    enterOuterAlt(_localctx, 2);
                {
                    setState(662);
                    input_parameter();
                }
                break;
                case T__56:
                case T__57:
                case T__58:
                    enterOuterAlt(_localctx, 3);
                {
                    setState(663);
                    functions_returning_datetime();
                }
                break;
                case T__20:
                case T__21:
                case T__22:
                case T__23:
                case T__24:
                    enterOuterAlt(_localctx, 4);
                {
                    setState(664);
                    aggregate_expression();
                }
                break;
                default:
                    throw new NoViableAltException(this);
            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class Boolean_expressionContext extends ParserRuleContext {
        public Boolean_primaryContext boolean_primary() {
            return getRuleContext(Boolean_primaryContext.class,0);
        }
        public Boolean_expressionContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_boolean_expression; }
    }

    public final Boolean_expressionContext boolean_expression() throws RecognitionException {
        Boolean_expressionContext _localctx = new Boolean_expressionContext(_ctx, getState());
        enterRule(_localctx, 134, RULE_boolean_expression);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(667);
                boolean_primary();
            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class Boolean_primaryContext extends ParserRuleContext {
        public State_field_path_expressionContext state_field_path_expression() {
            return getRuleContext(State_field_path_expressionContext.class,0);
        }
        public Boolean_literalContext boolean_literal() {
            return getRuleContext(Boolean_literalContext.class,0);
        }
        public Input_parameterContext input_parameter() {
            return getRuleContext(Input_parameterContext.class,0);
        }
        public Boolean_primaryContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_boolean_primary; }
    }

    public final Boolean_primaryContext boolean_primary() throws RecognitionException {
        Boolean_primaryContext _localctx = new Boolean_primaryContext(_ctx, getState());
        enterRule(_localctx, 136, RULE_boolean_primary);
        try {
            setState(672);
            _errHandler.sync(this);
            switch (_input.LA(1)) {
                case IDENTIFICATION_VARIABLE:
                    enterOuterAlt(_localctx, 1);
                {
                    setState(669);
                    state_field_path_expression();
                }
                break;
                case T__68:
                case T__69:
                    enterOuterAlt(_localctx, 2);
                {
                    setState(670);
                    boolean_literal();
                }
                break;
                case T__67:
                    enterOuterAlt(_localctx, 3);
                {
                    setState(671);
                    input_parameter();
                }
                break;
                default:
                    throw new NoViableAltException(this);
            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class Enum_expressionContext extends ParserRuleContext {
        public Enum_primaryContext enum_primary() {
            return getRuleContext(Enum_primaryContext.class,0);
        }
        public SubqueryContext subquery() {
            return getRuleContext(SubqueryContext.class,0);
        }
        public Enum_expressionContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_enum_expression; }
    }

    public final Enum_expressionContext enum_expression() throws RecognitionException {
        Enum_expressionContext _localctx = new Enum_expressionContext(_ctx, getState());
        enterRule(_localctx, 138, RULE_enum_expression);
        try {
            setState(679);
            _errHandler.sync(this);
            switch (_input.LA(1)) {
                case EOF:
                case T__67:
                case IDENTIFICATION_VARIABLE:
                    enterOuterAlt(_localctx, 1);
                {
                    setState(674);
                    enum_primary();
                }
                break;
                case T__9:
                    enterOuterAlt(_localctx, 2);
                {
                    setState(675);
                    match(T__9);
                    setState(676);
                    subquery();
                    setState(677);
                    match(T__10);
                }
                break;
                default:
                    throw new NoViableAltException(this);
            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class Enum_primaryContext extends ParserRuleContext {
        public State_field_path_expressionContext state_field_path_expression() {
            return getRuleContext(State_field_path_expressionContext.class,0);
        }
        public Enum_literalContext enum_literal() {
            return getRuleContext(Enum_literalContext.class,0);
        }
        public Input_parameterContext input_parameter() {
            return getRuleContext(Input_parameterContext.class,0);
        }
        public Enum_primaryContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_enum_primary; }
    }

    public final Enum_primaryContext enum_primary() throws RecognitionException {
        Enum_primaryContext _localctx = new Enum_primaryContext(_ctx, getState());
        enterRule(_localctx, 140, RULE_enum_primary);
        try {
            setState(684);
            _errHandler.sync(this);
            switch (_input.LA(1)) {
                case IDENTIFICATION_VARIABLE:
                    enterOuterAlt(_localctx, 1);
                {
                    setState(681);
                    state_field_path_expression();
                }
                break;
                case EOF:
                case T__13:
                case T__25:
                    enterOuterAlt(_localctx, 2);
                {
                    setState(682);
                    enum_literal();
                }
                break;
                case T__67:
                    enterOuterAlt(_localctx, 3);
                {
                    setState(683);
                    input_parameter();
                }
                break;
                default:
                    throw new NoViableAltException(this);
            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class Entity_expressionContext extends ParserRuleContext {
        public Single_valued_association_path_expressionContext single_valued_association_path_expression() {
            return getRuleContext(Single_valued_association_path_expressionContext.class,0);
        }
        public Simple_entity_expressionContext simple_entity_expression() {
            return getRuleContext(Simple_entity_expressionContext.class,0);
        }
        public Entity_expressionContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_entity_expression; }
    }

    public final Entity_expressionContext entity_expression() throws RecognitionException {
        Entity_expressionContext _localctx = new Entity_expressionContext(_ctx, getState());
        enterRule(_localctx, 142, RULE_entity_expression);
        try {
            setState(688);
            _errHandler.sync(this);
            switch ( getInterpreter().adaptivePredict(_input,68,_ctx) ) {
                case 1:
                    enterOuterAlt(_localctx, 1);
                {
                    setState(686);
                    single_valued_association_path_expression();
                }
                break;
                case 2:
                    enterOuterAlt(_localctx, 2);
                {
                    setState(687);
                    simple_entity_expression();
                }
                break;
            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class Simple_entity_expressionContext extends ParserRuleContext {
        public TerminalNode IDENTIFICATION_VARIABLE() { return getToken(OQLParser.IDENTIFICATION_VARIABLE, 0); }
        public Input_parameterContext input_parameter() {
            return getRuleContext(Input_parameterContext.class,0);
        }
        public Simple_entity_expressionContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_simple_entity_expression; }
    }

    public final Simple_entity_expressionContext simple_entity_expression() throws RecognitionException {
        Simple_entity_expressionContext _localctx = new Simple_entity_expressionContext(_ctx, getState());
        enterRule(_localctx, 144, RULE_simple_entity_expression);
        try {
            setState(692);
            _errHandler.sync(this);
            switch (_input.LA(1)) {
                case IDENTIFICATION_VARIABLE:
                    enterOuterAlt(_localctx, 1);
                {
                    setState(690);
                    match(IDENTIFICATION_VARIABLE);
                }
                break;
                case T__67:
                    enterOuterAlt(_localctx, 2);
                {
                    setState(691);
                    input_parameter();
                }
                break;
                default:
                    throw new NoViableAltException(this);
            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class Functions_returning_numericsContext extends ParserRuleContext {
        public List<String_primaryContext> string_primary() {
            return getRuleContexts(String_primaryContext.class);
        }
        public String_primaryContext string_primary(int i) {
            return getRuleContext(String_primaryContext.class,i);
        }
        public List<Simple_arithmetic_expressionContext> simple_arithmetic_expression() {
            return getRuleContexts(Simple_arithmetic_expressionContext.class);
        }
        public Simple_arithmetic_expressionContext simple_arithmetic_expression(int i) {
            return getRuleContext(Simple_arithmetic_expressionContext.class,i);
        }
        public Collection_valued_path_expressionContext collection_valued_path_expression() {
            return getRuleContext(Collection_valued_path_expressionContext.class,0);
        }
        public Functions_returning_numericsContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_functions_returning_numerics; }
    }

    public final Functions_returning_numericsContext functions_returning_numerics() throws RecognitionException {
        Functions_returning_numericsContext _localctx = new Functions_returning_numericsContext(_ctx, getState());
        enterRule(_localctx, 146, RULE_functions_returning_numerics);
        int _la;
        try {
            setState(732);
            _errHandler.sync(this);
            switch (_input.LA(1)) {
                case T__50:
                    enterOuterAlt(_localctx, 1);
                {
                    setState(694);
                    match(T__50);
                    setState(695);
                    match(T__9);
                    setState(696);
                    string_primary();
                    setState(697);
                    match(T__10);
                }
                break;
                case T__51:
                    enterOuterAlt(_localctx, 2);
                {
                    setState(699);
                    match(T__51);
                    setState(700);
                    match(T__9);
                    setState(701);
                    string_primary();
                    setState(702);
                    match(T__13);
                    setState(703);
                    string_primary();
                    setState(706);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                    if (_la==T__13) {
                        {
                            setState(704);
                            match(T__13);
                            setState(705);
                            simple_arithmetic_expression();
                        }
                    }

                    setState(708);
                    match(T__10);
                }
                break;
                case T__52:
                    enterOuterAlt(_localctx, 3);
                {
                    setState(710);
                    match(T__52);
                    setState(711);
                    match(T__9);
                    setState(712);
                    simple_arithmetic_expression();
                    setState(713);
                    match(T__10);
                }
                break;
                case T__53:
                    enterOuterAlt(_localctx, 4);
                {
                    setState(715);
                    match(T__53);
                    setState(716);
                    match(T__9);
                    setState(717);
                    simple_arithmetic_expression();
                    setState(718);
                    match(T__10);
                }
                break;
                case T__54:
                    enterOuterAlt(_localctx, 5);
                {
                    setState(720);
                    match(T__54);
                    setState(721);
                    match(T__9);
                    setState(722);
                    simple_arithmetic_expression();
                    setState(723);
                    match(T__13);
                    setState(724);
                    simple_arithmetic_expression();
                    setState(725);
                    match(T__10);
                }
                break;
                case T__55:
                    enterOuterAlt(_localctx, 6);
                {
                    setState(727);
                    match(T__55);
                    setState(728);
                    match(T__9);
                    setState(729);
                    collection_valued_path_expression();
                    setState(730);
                    match(T__10);
                }
                break;
                default:
                    throw new NoViableAltException(this);
            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class Functions_returning_datetimeContext extends ParserRuleContext {
        public Functions_returning_datetimeContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_functions_returning_datetime; }
    }

    public final Functions_returning_datetimeContext functions_returning_datetime() throws RecognitionException {
        Functions_returning_datetimeContext _localctx = new Functions_returning_datetimeContext(_ctx, getState());
        enterRule(_localctx, 148, RULE_functions_returning_datetime);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(734);
                _la = _input.LA(1);
                if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 1008806316530991104L) != 0)) ) {
                    _errHandler.recoverInline(this);
                }
                else {
                    if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
                    _errHandler.reportMatch(this);
                    consume();
                }
            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class Functions_returning_stringsContext extends ParserRuleContext {
        public List<String_primaryContext> string_primary() {
            return getRuleContexts(String_primaryContext.class);
        }
        public String_primaryContext string_primary(int i) {
            return getRuleContext(String_primaryContext.class,i);
        }
        public List<Simple_arithmetic_expressionContext> simple_arithmetic_expression() {
            return getRuleContexts(Simple_arithmetic_expressionContext.class);
        }
        public Simple_arithmetic_expressionContext simple_arithmetic_expression(int i) {
            return getRuleContext(Simple_arithmetic_expressionContext.class,i);
        }
        public Trim_specificationContext trim_specification() {
            return getRuleContext(Trim_specificationContext.class,0);
        }
        public TerminalNode TRIM_CHARACTER() { return getToken(OQLParser.TRIM_CHARACTER, 0); }
        public Functions_returning_stringsContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_functions_returning_strings; }
    }

    public final Functions_returning_stringsContext functions_returning_strings() throws RecognitionException {
        Functions_returning_stringsContext _localctx = new Functions_returning_stringsContext(_ctx, getState());
        enterRule(_localctx, 150, RULE_functions_returning_strings);
        int _la;
        try {
            setState(776);
            _errHandler.sync(this);
            switch (_input.LA(1)) {
                case T__59:
                    enterOuterAlt(_localctx, 1);
                {
                    setState(736);
                    match(T__59);
                    setState(737);
                    match(T__9);
                    setState(738);
                    string_primary();
                    setState(739);
                    match(T__13);
                    setState(740);
                    string_primary();
                    setState(741);
                    match(T__10);
                }
                break;
                case T__60:
                    enterOuterAlt(_localctx, 2);
                {
                    setState(743);
                    match(T__60);
                    setState(744);
                    match(T__9);
                    setState(745);
                    string_primary();
                    setState(746);
                    match(T__13);
                    setState(747);
                    simple_arithmetic_expression();
                    setState(748);
                    match(T__13);
                    setState(749);
                    simple_arithmetic_expression();
                    setState(750);
                    match(T__10);
                }
                break;
                case T__61:
                    enterOuterAlt(_localctx, 3);
                {
                    setState(752);
                    match(T__61);
                    setState(753);
                    match(T__9);
                    setState(761);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                    if (_la==T__0 || ((((_la - 65)) & ~0x3f) == 0 && ((1L << (_la - 65)) & 4103L) != 0)) {
                        {
                            setState(755);
                            _errHandler.sync(this);
                            _la = _input.LA(1);
                            if (((((_la - 65)) & ~0x3f) == 0 && ((1L << (_la - 65)) & 7L) != 0)) {
                                {
                                    setState(754);
                                    trim_specification();
                                }
                            }

                            setState(758);
                            _errHandler.sync(this);
                            _la = _input.LA(1);
                            if (_la==TRIM_CHARACTER) {
                                {
                                    setState(757);
                                    match(TRIM_CHARACTER);
                                }
                            }

                            setState(760);
                            match(T__0);
                        }
                    }

                    setState(763);
                    string_primary();
                    setState(764);
                    match(T__10);
                }
                break;
                case T__62:
                    enterOuterAlt(_localctx, 4);
                {
                    setState(766);
                    match(T__62);
                    setState(767);
                    match(T__9);
                    setState(768);
                    string_primary();
                    setState(769);
                    match(T__10);
                }
                break;
                case T__63:
                    enterOuterAlt(_localctx, 5);
                {
                    setState(771);
                    match(T__63);
                    setState(772);
                    match(T__9);
                    setState(773);
                    string_primary();
                    setState(774);
                    match(T__10);
                }
                break;
                default:
                    throw new NoViableAltException(this);
            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class Trim_specificationContext extends ParserRuleContext {
        public Trim_specificationContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_trim_specification; }
    }

    public final Trim_specificationContext trim_specification() throws RecognitionException {
        Trim_specificationContext _localctx = new Trim_specificationContext(_ctx, getState());
        enterRule(_localctx, 152, RULE_trim_specification);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(778);
                _la = _input.LA(1);
                if ( !(((((_la - 65)) & ~0x3f) == 0 && ((1L << (_la - 65)) & 7L) != 0)) ) {
                    _errHandler.recoverInline(this);
                }
                else {
                    if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
                    _errHandler.reportMatch(this);
                    consume();
                }
            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class Numeric_literalContext extends ParserRuleContext {
        public int value;
        public Token NUMBER;
        public TerminalNode NUMBER() { return getToken(OQLParser.NUMBER, 0); }
        public Numeric_literalContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_numeric_literal; }
    }

    public final Numeric_literalContext numeric_literal() throws RecognitionException {
        Numeric_literalContext _localctx = new Numeric_literalContext(_ctx, getState());
        enterRule(_localctx, 154, RULE_numeric_literal);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(780);
                ((Numeric_literalContext)_localctx).NUMBER = match(NUMBER);
                ((Numeric_literalContext)_localctx).value =  Integer.valueOf((((Numeric_literalContext)_localctx).NUMBER!=null?((Numeric_literalContext)_localctx).NUMBER.getText():null));
            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class Pattern_valueContext extends ParserRuleContext {
        public Pattern_valueContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_pattern_value; }
    }

    public final Pattern_valueContext pattern_value() throws RecognitionException {
        Pattern_valueContext _localctx = new Pattern_valueContext(_ctx, getState());
        enterRule(_localctx, 156, RULE_pattern_value);
        try {
            enterOuterAlt(_localctx, 1);
            {
            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class Input_parameterContext extends ParserRuleContext {
        public Parameter value;
        public Token IDENTIFICATION_VARIABLE;
        public TerminalNode IDENTIFICATION_VARIABLE() { return getToken(OQLParser.IDENTIFICATION_VARIABLE, 0); }
        public Input_parameterContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_input_parameter; }
    }

    public final Input_parameterContext input_parameter() throws RecognitionException {
        Input_parameterContext _localctx = new Input_parameterContext(_ctx, getState());
        enterRule(_localctx, 158, RULE_input_parameter);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(785);
                match(T__67);
                setState(786);
                ((Input_parameterContext)_localctx).IDENTIFICATION_VARIABLE = match(IDENTIFICATION_VARIABLE);
                ((Input_parameterContext)_localctx).value = this.getQuery().parameter((((Input_parameterContext)_localctx).IDENTIFICATION_VARIABLE!=null?((Input_parameterContext)_localctx).IDENTIFICATION_VARIABLE.getText():null));
            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class LiteralContext extends ParserRuleContext {
        public LiteralContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_literal; }
    }

    public final LiteralContext literal() throws RecognitionException {
        LiteralContext _localctx = new LiteralContext(_ctx, getState());
        enterRule(_localctx, 160, RULE_literal);
        try {
            enterOuterAlt(_localctx, 1);
            {
            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class Constructor_nameContext extends ParserRuleContext {
        public Constructor_nameContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_constructor_name; }
    }

    public final Constructor_nameContext constructor_name() throws RecognitionException {
        Constructor_nameContext _localctx = new Constructor_nameContext(_ctx, getState());
        enterRule(_localctx, 162, RULE_constructor_name);
        try {
            enterOuterAlt(_localctx, 1);
            {
            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class Enum_literalContext extends ParserRuleContext {
        public Enum_literalContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_enum_literal; }
    }

    public final Enum_literalContext enum_literal() throws RecognitionException {
        Enum_literalContext _localctx = new Enum_literalContext(_ctx, getState());
        enterRule(_localctx, 164, RULE_enum_literal);
        try {
            enterOuterAlt(_localctx, 1);
            {
            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class Boolean_literalContext extends ParserRuleContext {
        public Boolean_literalContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_boolean_literal; }
    }

    public final Boolean_literalContext boolean_literal() throws RecognitionException {
        Boolean_literalContext _localctx = new Boolean_literalContext(_ctx, getState());
        enterRule(_localctx, 166, RULE_boolean_literal);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(795);
                _la = _input.LA(1);
                if ( !(_la==T__68 || _la==T__69) ) {
                    _errHandler.recoverInline(this);
                }
                else {
                    if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
                    _errHandler.reportMatch(this);
                    consume();
                }
            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class Simple_state_fieldContext extends ParserRuleContext {
        public Simple_state_fieldContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_simple_state_field; }
    }

    public final Simple_state_fieldContext simple_state_field() throws RecognitionException {
        Simple_state_fieldContext _localctx = new Simple_state_fieldContext(_ctx, getState());
        enterRule(_localctx, 168, RULE_simple_state_field);
        try {
            enterOuterAlt(_localctx, 1);
            {
            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class Embedded_class_state_fieldContext extends ParserRuleContext {
        public Embedded_class_state_fieldContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_embedded_class_state_field; }
    }

    public final Embedded_class_state_fieldContext embedded_class_state_field() throws RecognitionException {
        Embedded_class_state_fieldContext _localctx = new Embedded_class_state_fieldContext(_ctx, getState());
        enterRule(_localctx, 170, RULE_embedded_class_state_field);
        try {
            enterOuterAlt(_localctx, 1);
            {
            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class Single_valued_association_fieldContext extends ParserRuleContext {
        public Single_valued_association_fieldContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_single_valued_association_field; }
    }

    public final Single_valued_association_fieldContext single_valued_association_field() throws RecognitionException {
        Single_valued_association_fieldContext _localctx = new Single_valued_association_fieldContext(_ctx, getState());
        enterRule(_localctx, 172, RULE_single_valued_association_field);
        try {
            enterOuterAlt(_localctx, 1);
            {
            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class Collection_valued_association_fieldContext extends ParserRuleContext {
        public Collection_valued_association_fieldContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_collection_valued_association_field; }
    }

    public final Collection_valued_association_fieldContext collection_valued_association_field() throws RecognitionException {
        Collection_valued_association_fieldContext _localctx = new Collection_valued_association_fieldContext(_ctx, getState());
        enterRule(_localctx, 174, RULE_collection_valued_association_field);
        try {
            enterOuterAlt(_localctx, 1);
            {
            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class Abstract_schema_nameContext extends ParserRuleContext {
        public String name;
        public Token IDENTIFICATION_VARIABLE;
        public TerminalNode IDENTIFICATION_VARIABLE() { return getToken(OQLParser.IDENTIFICATION_VARIABLE, 0); }
        public Abstract_schema_nameContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_abstract_schema_name; }
    }

    public final Abstract_schema_nameContext abstract_schema_name() throws RecognitionException {
        Abstract_schema_nameContext _localctx = new Abstract_schema_nameContext(_ctx, getState());
        enterRule(_localctx, 176, RULE_abstract_schema_name);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(805);
                ((Abstract_schema_nameContext)_localctx).IDENTIFICATION_VARIABLE = match(IDENTIFICATION_VARIABLE);
                ((Abstract_schema_nameContext)_localctx).name =  (((Abstract_schema_nameContext)_localctx).IDENTIFICATION_VARIABLE!=null?((Abstract_schema_nameContext)_localctx).IDENTIFICATION_VARIABLE.getText():null);
            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class LegContext extends ParserRuleContext {
        public String value;
        public Token IDENTIFICATION_VARIABLE;
        public TerminalNode IDENTIFICATION_VARIABLE() { return getToken(OQLParser.IDENTIFICATION_VARIABLE, 0); }
        public LegContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_leg; }
    }

    public final LegContext leg() throws RecognitionException {
        LegContext _localctx = new LegContext(_ctx, getState());
        enterRule(_localctx, 178, RULE_leg);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(808);
                ((LegContext)_localctx).IDENTIFICATION_VARIABLE = match(IDENTIFICATION_VARIABLE);
                ((LegContext)_localctx).value =  (((LegContext)_localctx).IDENTIFICATION_VARIABLE!=null?((LegContext)_localctx).IDENTIFICATION_VARIABLE.getText():null);
            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    public static final String _serializedATN =
            "\u0004\u0001M\u032c\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
                    "\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002"+
                    "\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007\u0002"+
                    "\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b\u0002"+
                    "\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0002\u000f\u0007\u000f"+
                    "\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011\u0002\u0012\u0007\u0012"+
                    "\u0002\u0013\u0007\u0013\u0002\u0014\u0007\u0014\u0002\u0015\u0007\u0015"+
                    "\u0002\u0016\u0007\u0016\u0002\u0017\u0007\u0017\u0002\u0018\u0007\u0018"+
                    "\u0002\u0019\u0007\u0019\u0002\u001a\u0007\u001a\u0002\u001b\u0007\u001b"+
                    "\u0002\u001c\u0007\u001c\u0002\u001d\u0007\u001d\u0002\u001e\u0007\u001e"+
                    "\u0002\u001f\u0007\u001f\u0002 \u0007 \u0002!\u0007!\u0002\"\u0007\"\u0002"+
                    "#\u0007#\u0002$\u0007$\u0002%\u0007%\u0002&\u0007&\u0002\'\u0007\'\u0002"+
                    "(\u0007(\u0002)\u0007)\u0002*\u0007*\u0002+\u0007+\u0002,\u0007,\u0002"+
                    "-\u0007-\u0002.\u0007.\u0002/\u0007/\u00020\u00070\u00021\u00071\u0002"+
                    "2\u00072\u00023\u00073\u00024\u00074\u00025\u00075\u00026\u00076\u0002"+
                    "7\u00077\u00028\u00078\u00029\u00079\u0002:\u0007:\u0002;\u0007;\u0002"+
                    "<\u0007<\u0002=\u0007=\u0002>\u0007>\u0002?\u0007?\u0002@\u0007@\u0002"+
                    "A\u0007A\u0002B\u0007B\u0002C\u0007C\u0002D\u0007D\u0002E\u0007E\u0002"+
                    "F\u0007F\u0002G\u0007G\u0002H\u0007H\u0002I\u0007I\u0002J\u0007J\u0002"+
                    "K\u0007K\u0002L\u0007L\u0002M\u0007M\u0002N\u0007N\u0002O\u0007O\u0002"+
                    "P\u0007P\u0002Q\u0007Q\u0002R\u0007R\u0002S\u0007S\u0002T\u0007T\u0002"+
                    "U\u0007U\u0002V\u0007V\u0002W\u0007W\u0002X\u0007X\u0002Y\u0007Y\u0001"+
                    "\u0000\u0001\u0000\u0001\u0000\u0001\u0001\u0001\u0001\u0001\u0002\u0001"+
                    "\u0002\u0001\u0002\u0001\u0002\u0003\u0002\u00be\b\u0002\u0001\u0003\u0001"+
                    "\u0003\u0003\u0003\u00c2\b\u0003\u0001\u0004\u0001\u0004\u0003\u0004\u00c6"+
                    "\b\u0004\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0006\u0001"+
                    "\u0006\u0001\u0006\u0001\u0007\u0001\u0007\u0003\u0007\u00d1\b\u0007\u0001"+
                    "\u0007\u0001\u0007\u0001\u0007\u0001\b\u0001\b\u0001\b\u0003\b\u00d9\b"+
                    "\b\u0001\b\u0001\b\u0001\t\u0001\t\u0001\t\u0001\t\u0001\n\u0001\n\u0003"+
                    "\n\u00e3\b\n\u0001\n\u0003\n\u00e6\b\n\u0001\n\u0001\n\u0001\u000b\u0001"+
                    "\u000b\u0003\u000b\u00ec\b\u000b\u0001\f\u0001\f\u0001\f\u0001\f\u0001"+
                    "\r\u0001\r\u0001\r\u0001\r\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e"+
                    "\u0001\u000e\u0003\u000e\u00fb\b\u000e\u0001\u000e\u0001\u000e\u0001\u000f"+
                    "\u0001\u000f\u0003\u000f\u0101\b\u000f\u0001\u0010\u0001\u0010\u0003\u0010"+
                    "\u0105\b\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0011\u0001\u0011"+
                    "\u0001\u0011\u0001\u0011\u0001\u0011\u0005\u0011\u010f\b\u0011\n\u0011"+
                    "\f\u0011\u0112\t\u0011\u0001\u0011\u0001\u0011\u0001\u0012\u0001\u0012"+
                    "\u0001\u0012\u0001\u0012\u0001\u0012\u0005\u0012\u011b\b\u0012\n\u0012"+
                    "\f\u0012\u011e\t\u0012\u0001\u0012\u0001\u0012\u0001\u0013\u0001\u0013"+
                    "\u0001\u0013\u0005\u0013\u0125\b\u0013\n\u0013\f\u0013\u0128\t\u0013\u0001"+
                    "\u0013\u0001\u0013\u0001\u0014\u0001\u0014\u0001\u0014\u0003\u0014\u012f"+
                    "\b\u0014\u0001\u0014\u0003\u0014\u0132\b\u0014\u0001\u0014\u0001\u0014"+
                    "\u0001\u0014\u0001\u0014\u0005\u0014\u0138\b\u0014\n\u0014\f\u0014\u013b"+
                    "\t\u0014\u0001\u0015\u0001\u0015\u0003\u0015\u013f\b\u0015\u0001\u0015"+
                    "\u0001\u0015\u0003\u0015\u0143\b\u0015\u0001\u0015\u0001\u0015\u0001\u0015"+
                    "\u0001\u0016\u0001\u0016\u0001\u0016\u0001\u0016\u0001\u0016\u0001\u0016"+
                    "\u0001\u0016\u0003\u0016\u014f\b\u0016\u0001\u0017\u0001\u0017\u0001\u0017"+
                    "\u0001\u0017\u0003\u0017\u0155\b\u0017\u0001\u0017\u0003\u0017\u0158\b"+
                    "\u0017\u0001\u0018\u0001\u0018\u0003\u0018\u015c\b\u0018\u0001\u0018\u0001"+
                    "\u0018\u0001\u0018\u0001\u0018\u0001\u0018\u0001\u0018\u0005\u0018\u0164"+
                    "\b\u0018\n\u0018\f\u0018\u0167\t\u0018\u0001\u0019\u0001\u0019\u0001\u0019"+
                    "\u0001\u0019\u0001\u0019\u0005\u0019\u016e\b\u0019\n\u0019\f\u0019\u0171"+
                    "\t\u0019\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a\u0001"+
                    "\u001a\u0005\u001a\u0179\b\u001a\n\u001a\f\u001a\u017c\t\u001a\u0001\u001a"+
                    "\u0001\u001a\u0001\u001b\u0001\u001b\u0003\u001b\u0182\b\u001b\u0001\u001c"+
                    "\u0001\u001c\u0001\u001c\u0003\u001c\u0187\b\u001c\u0001\u001c\u0001\u001c"+
                    "\u0001\u001c\u0001\u001c\u0001\u001c\u0001\u001c\u0003\u001c\u018f\b\u001c"+
                    "\u0001\u001c\u0001\u001c\u0001\u001c\u0003\u001c\u0194\b\u001c\u0001\u001c"+
                    "\u0003\u001c\u0197\b\u001c\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d"+
                    "\u0001\u001e\u0001\u001e\u0001\u001e\u0001\u001e\u0001\u001e\u0005\u001e"+
                    "\u01a2\b\u001e\n\u001e\f\u001e\u01a5\t\u001e\u0001\u001f\u0001\u001f\u0003"+
                    "\u001f\u01a9\b\u001f\u0001 \u0001 \u0001 \u0001!\u0001!\u0001!\u0001!"+
                    "\u0001!\u0005!\u01b3\b!\n!\f!\u01b6\t!\u0001\"\u0001\"\u0003\"\u01ba\b"+
                    "\"\u0001#\u0001#\u0001#\u0003#\u01bf\b#\u0001#\u0003#\u01c2\b#\u0001#"+
                    "\u0003#\u01c5\b#\u0001$\u0001$\u0001$\u0001$\u0005$\u01cb\b$\n$\f$\u01ce"+
                    "\t$\u0001%\u0001%\u0001%\u0003%\u01d3\b%\u0001%\u0001%\u0001%\u0003%\u01d8"+
                    "\b%\u0001&\u0001&\u0003&\u01dc\b&\u0001\'\u0001\'\u0003\'\u01e0\b\'\u0001"+
                    "\'\u0001\'\u0001(\u0001(\u0001(\u0003(\u01e7\b(\u0001)\u0001)\u0001)\u0001"+
                    "*\u0001*\u0001*\u0001+\u0001+\u0001+\u0001,\u0001,\u0001,\u0001-\u0001"+
                    "-\u0001-\u0001.\u0001.\u0003.\u01fa\b.\u0001.\u0001.\u0001.\u0001.\u0001"+
                    ".\u0001.\u0001.\u0003.\u0203\b.\u0001.\u0001.\u0001.\u0001.\u0001.\u0001"+
                    ".\u0001.\u0003.\u020c\b.\u0001.\u0001.\u0001.\u0001.\u0001.\u0003.\u0213"+
                    "\b.\u0001/\u0001/\u0003/\u0217\b/\u0001/\u0001/\u0001/\u0001/\u0001/\u0005"+
                    "/\u021e\b/\n/\f/\u0221\t/\u0001/\u0003/\u0224\b/\u0001/\u0001/\u00010"+
                    "\u00010\u00030\u022a\b0\u00011\u00011\u00031\u022e\b1\u00011\u00011\u0001"+
                    "1\u00011\u00031\u0234\b1\u00012\u00012\u00032\u0238\b2\u00012\u00012\u0003"+
                    "2\u023c\b2\u00012\u00012\u00013\u00013\u00013\u00033\u0243\b3\u00013\u0001"+
                    "3\u00014\u00014\u00034\u0249\b4\u00014\u00014\u00034\u024d\b4\u00014\u0001"+
                    "4\u00015\u00035\u0252\b5\u00015\u00015\u00015\u00015\u00015\u00016\u0001"+
                    "6\u00016\u00016\u00016\u00017\u00017\u00017\u00017\u00017\u00018\u0001"+
                    "8\u00018\u00019\u00019\u00019\u00019\u00019\u00019\u00019\u00019\u0001"+
                    "9\u00019\u00019\u00019\u00039\u0272\b9\u0001:\u0001:\u0001:\u0001;\u0001"+
                    ";\u0001;\u0001<\u0001<\u0001<\u0001=\u0001=\u0001=\u0001>\u0001>\u0001"+
                    ">\u0001>\u0001>\u0001>\u0003>\u0286\b>\u0001?\u0001?\u0001@\u0001@\u0001"+
                    "@\u0003@\u028d\b@\u0001A\u0001A\u0001A\u0001A\u0001A\u0003A\u0294\bA\u0001"+
                    "B\u0001B\u0001B\u0001B\u0003B\u029a\bB\u0001C\u0001C\u0001D\u0001D\u0001"+
                    "D\u0003D\u02a1\bD\u0001E\u0001E\u0001E\u0001E\u0001E\u0003E\u02a8\bE\u0001"+
                    "F\u0001F\u0001F\u0003F\u02ad\bF\u0001G\u0001G\u0003G\u02b1\bG\u0001H\u0001"+
                    "H\u0003H\u02b5\bH\u0001I\u0001I\u0001I\u0001I\u0001I\u0001I\u0001I\u0001"+
                    "I\u0001I\u0001I\u0001I\u0001I\u0003I\u02c3\bI\u0001I\u0001I\u0001I\u0001"+
                    "I\u0001I\u0001I\u0001I\u0001I\u0001I\u0001I\u0001I\u0001I\u0001I\u0001"+
                    "I\u0001I\u0001I\u0001I\u0001I\u0001I\u0001I\u0001I\u0001I\u0001I\u0001"+
                    "I\u0003I\u02dd\bI\u0001J\u0001J\u0001K\u0001K\u0001K\u0001K\u0001K\u0001"+
                    "K\u0001K\u0001K\u0001K\u0001K\u0001K\u0001K\u0001K\u0001K\u0001K\u0001"+
                    "K\u0001K\u0001K\u0001K\u0003K\u02f4\bK\u0001K\u0003K\u02f7\bK\u0001K\u0003"+
                    "K\u02fa\bK\u0001K\u0001K\u0001K\u0001K\u0001K\u0001K\u0001K\u0001K\u0001"+
                    "K\u0001K\u0001K\u0001K\u0001K\u0003K\u0309\bK\u0001L\u0001L\u0001M\u0001"+
                    "M\u0001M\u0001N\u0001N\u0001O\u0001O\u0001O\u0001O\u0001P\u0001P\u0001"+
                    "Q\u0001Q\u0001R\u0001R\u0001S\u0001S\u0001T\u0001T\u0001U\u0001U\u0001"+
                    "V\u0001V\u0001W\u0001W\u0001X\u0001X\u0001X\u0001Y\u0001Y\u0001Y\u0001"+
                    "Y\u0000\u0000Z\u0000\u0002\u0004\u0006\b\n\f\u000e\u0010\u0012\u0014\u0016"+
                    "\u0018\u001a\u001c\u001e \"$&(*,.02468:<>@BDFHJLNPRTVXZ\\^`bdfhjlnprt"+
                    "vxz|~\u0080\u0082\u0084\u0086\u0088\u008a\u008c\u008e\u0090\u0092\u0094"+
                    "\u0096\u0098\u009a\u009c\u009e\u00a0\u00a2\u00a4\u00a6\u00a8\u00aa\u00ac"+
                    "\u00ae\u00b0\u00b2\u0000\u0006\u0001\u0000\u0015\u0018\u0001\u0000\u001f"+
                    " \u0001\u0000+-\u0001\u00009;\u0001\u0000AC\u0001\u0000EF\u0337\u0000"+
                    "\u00b4\u0001\u0000\u0000\u0000\u0002\u00b7\u0001\u0000\u0000\u0000\u0004"+
                    "\u00b9\u0001\u0000\u0000\u0000\u0006\u00bf\u0001\u0000\u0000\u0000\b\u00c3"+
                    "\u0001\u0000\u0000\u0000\n\u00c7\u0001\u0000\u0000\u0000\f\u00cb\u0001"+
                    "\u0000\u0000\u0000\u000e\u00ce\u0001\u0000\u0000\u0000\u0010\u00d5\u0001"+
                    "\u0000\u0000\u0000\u0012\u00dc\u0001\u0000\u0000\u0000\u0014\u00e5\u0001"+
                    "\u0000\u0000\u0000\u0016\u00eb\u0001\u0000\u0000\u0000\u0018\u00ed\u0001"+
                    "\u0000\u0000\u0000\u001a\u00f1\u0001\u0000\u0000\u0000\u001c\u00f5\u0001"+
                    "\u0000\u0000\u0000\u001e\u0100\u0001\u0000\u0000\u0000 \u0104\u0001\u0000"+
                    "\u0000\u0000\"\u0109\u0001\u0000\u0000\u0000$\u0115\u0001\u0000\u0000"+
                    "\u0000&\u0126\u0001\u0000\u0000\u0000(\u012b\u0001\u0000\u0000\u0000*"+
                    "\u013e\u0001\u0000\u0000\u0000,\u014e\u0001\u0000\u0000\u0000.\u0150\u0001"+
                    "\u0000\u0000\u00000\u0159\u0001\u0000\u0000\u00002\u0168\u0001\u0000\u0000"+
                    "\u00004\u0172\u0001\u0000\u0000\u00006\u0181\u0001\u0000\u0000\u00008"+
                    "\u0196\u0001\u0000\u0000\u0000:\u0198\u0001\u0000\u0000\u0000<\u019c\u0001"+
                    "\u0000\u0000\u0000>\u01a8\u0001\u0000\u0000\u0000@\u01aa\u0001\u0000\u0000"+
                    "\u0000B\u01ad\u0001\u0000\u0000\u0000D\u01b7\u0001\u0000\u0000\u0000F"+
                    "\u01bb\u0001\u0000\u0000\u0000H\u01c6\u0001\u0000\u0000\u0000J\u01d7\u0001"+
                    "\u0000\u0000\u0000L\u01db\u0001\u0000\u0000\u0000N\u01dd\u0001\u0000\u0000"+
                    "\u0000P\u01e6\u0001\u0000\u0000\u0000R\u01e8\u0001\u0000\u0000\u0000T"+
                    "\u01eb\u0001\u0000\u0000\u0000V\u01ee\u0001\u0000\u0000\u0000X\u01f1\u0001"+
                    "\u0000\u0000\u0000Z\u01f4\u0001\u0000\u0000\u0000\\\u0212\u0001\u0000"+
                    "\u0000\u0000^\u0214\u0001\u0000\u0000\u0000`\u0229\u0001\u0000\u0000\u0000"+
                    "b\u022b\u0001\u0000\u0000\u0000d\u0237\u0001\u0000\u0000\u0000f\u023f"+
                    "\u0001\u0000\u0000\u0000h\u0246\u0001\u0000\u0000\u0000j\u0251\u0001\u0000"+
                    "\u0000\u0000l\u0258\u0001\u0000\u0000\u0000n\u025d\u0001\u0000\u0000\u0000"+
                    "p\u0262\u0001\u0000\u0000\u0000r\u0271\u0001\u0000\u0000\u0000t\u0273"+
                    "\u0001\u0000\u0000\u0000v\u0276\u0001\u0000\u0000\u0000x\u0279\u0001\u0000"+
                    "\u0000\u0000z\u027c\u0001\u0000\u0000\u0000|\u0285\u0001\u0000\u0000\u0000"+
                    "~\u0287\u0001\u0000\u0000\u0000\u0080\u028c\u0001\u0000\u0000\u0000\u0082"+
                    "\u0293\u0001\u0000\u0000\u0000\u0084\u0299\u0001\u0000\u0000\u0000\u0086"+
                    "\u029b\u0001\u0000\u0000\u0000\u0088\u02a0\u0001\u0000\u0000\u0000\u008a"+
                    "\u02a7\u0001\u0000\u0000\u0000\u008c\u02ac\u0001\u0000\u0000\u0000\u008e"+
                    "\u02b0\u0001\u0000\u0000\u0000\u0090\u02b4\u0001\u0000\u0000\u0000\u0092"+
                    "\u02dc\u0001\u0000\u0000\u0000\u0094\u02de\u0001\u0000\u0000\u0000\u0096"+
                    "\u0308\u0001\u0000\u0000\u0000\u0098\u030a\u0001\u0000\u0000\u0000\u009a"+
                    "\u030c\u0001\u0000\u0000\u0000\u009c\u030f\u0001\u0000\u0000\u0000\u009e"+
                    "\u0311\u0001\u0000\u0000\u0000\u00a0\u0315\u0001\u0000\u0000\u0000\u00a2"+
                    "\u0317\u0001\u0000\u0000\u0000\u00a4\u0319\u0001\u0000\u0000\u0000\u00a6"+
                    "\u031b\u0001\u0000\u0000\u0000\u00a8\u031d\u0001\u0000\u0000\u0000\u00aa"+
                    "\u031f\u0001\u0000\u0000\u0000\u00ac\u0321\u0001\u0000\u0000\u0000\u00ae"+
                    "\u0323\u0001\u0000\u0000\u0000\u00b0\u0325\u0001\u0000\u0000\u0000\u00b2"+
                    "\u0328\u0001\u0000\u0000\u0000\u00b4\u00b5\u0003\u0002\u0001\u0000\u00b5"+
                    "\u00b6\u0005\u0000\u0000\u0001\u00b6\u0001\u0001\u0000\u0000\u0000\u00b7"+
                    "\u00b8\u0003\u0004\u0002\u0000\u00b8\u0003\u0001\u0000\u0000\u0000\u00b9"+
                    "\u00ba\u00030\u0018\u0000\u00ba\u00bb\u0003\n\u0005\u0000\u00bb\u00bd"+
                    "\u0006\u0002\uffff\uffff\u0000\u00bc\u00be\u0003:\u001d\u0000\u00bd\u00bc"+
                    "\u0001\u0000\u0000\u0000\u00bd\u00be\u0001\u0000\u0000\u0000\u00be\u0005"+
                    "\u0001\u0000\u0000\u0000\u00bf\u00c1\u0003(\u0014\u0000\u00c0\u00c2\u0003"+
                    ":\u001d\u0000\u00c1\u00c0\u0001\u0000\u0000\u0000\u00c1\u00c2\u0001\u0000"+
                    "\u0000\u0000\u00c2\u0007\u0001\u0000\u0000\u0000\u00c3\u00c5\u0003.\u0017"+
                    "\u0000\u00c4\u00c6\u0003:\u001d\u0000\u00c5\u00c4\u0001\u0000\u0000\u0000"+
                    "\u00c5\u00c6\u0001\u0000\u0000\u0000\u00c6\t\u0001\u0000\u0000\u0000\u00c7"+
                    "\u00c8\u0005\u0001\u0000\u0000\u00c8\u00c9\u0003\f\u0006\u0000\u00c9\u00ca"+
                    "\u0006\u0005\uffff\uffff\u0000\u00ca\u000b\u0001\u0000\u0000\u0000\u00cb"+
                    "\u00cc\u0003\u000e\u0007\u0000\u00cc\u00cd\u0006\u0006\uffff\uffff\u0000"+
                    "\u00cd\r\u0001\u0000\u0000\u0000\u00ce\u00d0\u0003\u00b0X\u0000\u00cf"+
                    "\u00d1\u0005\u0002\u0000\u0000\u00d0\u00cf\u0001\u0000\u0000\u0000\u00d0"+
                    "\u00d1\u0001\u0000\u0000\u0000\u00d1\u00d2\u0001\u0000\u0000\u0000\u00d2"+
                    "\u00d3\u0005G\u0000\u0000\u00d3\u00d4\u0006\u0007\uffff\uffff\u0000\u00d4"+
                    "\u000f\u0001\u0000\u0000\u0000\u00d5\u00d6\u0003\u0014\n\u0000\u00d6\u00d8"+
                    "\u0003\u0016\u000b\u0000\u00d7\u00d9\u0005\u0002\u0000\u0000\u00d8\u00d7"+
                    "\u0001\u0000\u0000\u0000\u00d8\u00d9\u0001\u0000\u0000\u0000\u00d9\u00da"+
                    "\u0001\u0000\u0000\u0000\u00da\u00db\u0005G\u0000\u0000\u00db\u0011\u0001"+
                    "\u0000\u0000\u0000\u00dc\u00dd\u0003\u0014\n\u0000\u00dd\u00de\u0005\u0003"+
                    "\u0000\u0000\u00de\u00df\u0003\u0016\u000b\u0000\u00df\u0013\u0001\u0000"+
                    "\u0000\u0000\u00e0\u00e2\u0005\u0004\u0000\u0000\u00e1\u00e3\u0005\u0005"+
                    "\u0000\u0000\u00e2\u00e1\u0001\u0000\u0000\u0000\u00e2\u00e3\u0001\u0000"+
                    "\u0000\u0000\u00e3\u00e6\u0001\u0000\u0000\u0000\u00e4\u00e6\u0005\u0006"+
                    "\u0000\u0000\u00e5\u00e0\u0001\u0000\u0000\u0000\u00e5\u00e4\u0001\u0000"+
                    "\u0000\u0000\u00e5\u00e6\u0001\u0000\u0000\u0000\u00e6\u00e7\u0001\u0000"+
                    "\u0000\u0000\u00e7\u00e8\u0005\u0007\u0000\u0000\u00e8\u0015\u0001\u0000"+
                    "\u0000\u0000\u00e9\u00ec\u0003\u0018\f\u0000\u00ea\u00ec\u0003\u001a\r"+
                    "\u0000\u00eb\u00e9\u0001\u0000\u0000\u0000\u00eb\u00ea\u0001\u0000\u0000"+
                    "\u0000\u00ec\u0017\u0001\u0000\u0000\u0000\u00ed\u00ee\u0005G\u0000\u0000"+
                    "\u00ee\u00ef\u0005\b\u0000\u0000\u00ef\u00f0\u0003\u00aeW\u0000\u00f0"+
                    "\u0019\u0001\u0000\u0000\u0000\u00f1\u00f2\u0005G\u0000\u0000\u00f2\u00f3"+
                    "\u0005\b\u0000\u0000\u00f3\u00f4\u0003\u00acV\u0000\u00f4\u001b\u0001"+
                    "\u0000\u0000\u0000\u00f5\u00f6\u0005\t\u0000\u0000\u00f6\u00f7\u0005\n"+
                    "\u0000\u0000\u00f7\u00f8\u0003$\u0012\u0000\u00f8\u00fa\u0005\u000b\u0000"+
                    "\u0000\u00f9\u00fb\u0005\u0002\u0000\u0000\u00fa\u00f9\u0001\u0000\u0000"+
                    "\u0000\u00fa\u00fb\u0001\u0000\u0000\u0000\u00fb\u00fc\u0001\u0000\u0000"+
                    "\u0000\u00fc\u00fd\u0005G\u0000\u0000\u00fd\u001d\u0001\u0000\u0000\u0000"+
                    "\u00fe\u0101\u0003 \u0010\u0000\u00ff\u0101\u0003\"\u0011\u0000\u0100"+
                    "\u00fe\u0001\u0000\u0000\u0000\u0100\u00ff\u0001\u0000\u0000\u0000\u0101"+
                    "\u001f\u0001\u0000\u0000\u0000\u0102\u0105\u0005G\u0000\u0000\u0103\u0105"+
                    "\u0003\"\u0011\u0000\u0104\u0102\u0001\u0000\u0000\u0000\u0104\u0103\u0001"+
                    "\u0000\u0000\u0000\u0105\u0106\u0001\u0000\u0000\u0000\u0106\u0107\u0005"+
                    "\b\u0000\u0000\u0107\u0108\u0003&\u0013\u0000\u0108!\u0001\u0000\u0000"+
                    "\u0000\u0109\u010a\u0005G\u0000\u0000\u010a\u0110\u0005\b\u0000\u0000"+
                    "\u010b\u010c\u0003\u00acV\u0000\u010c\u010d\u0005\b\u0000\u0000\u010d"+
                    "\u010f\u0001\u0000\u0000\u0000\u010e\u010b\u0001\u0000\u0000\u0000\u010f"+
                    "\u0112\u0001\u0000\u0000\u0000\u0110\u010e\u0001\u0000\u0000\u0000\u0110"+
                    "\u0111\u0001\u0000\u0000\u0000\u0111\u0113\u0001\u0000\u0000\u0000\u0112"+
                    "\u0110\u0001\u0000\u0000\u0000\u0113\u0114\u0003\u00acV\u0000\u0114#\u0001"+
                    "\u0000\u0000\u0000\u0115\u0116\u0005G\u0000\u0000\u0116\u011c\u0005\b"+
                    "\u0000\u0000\u0117\u0118\u0003\u00acV\u0000\u0118\u0119\u0005\b\u0000"+
                    "\u0000\u0119\u011b\u0001\u0000\u0000\u0000\u011a\u0117\u0001\u0000\u0000"+
                    "\u0000\u011b\u011e\u0001\u0000\u0000\u0000\u011c\u011a\u0001\u0000\u0000"+
                    "\u0000\u011c\u011d\u0001\u0000\u0000\u0000\u011d\u011f\u0001\u0000\u0000"+
                    "\u0000\u011e\u011c\u0001\u0000\u0000\u0000\u011f\u0120\u0003\u00aeW\u0000"+
                    "\u0120%\u0001\u0000\u0000\u0000\u0121\u0122\u0003\u00aaU\u0000\u0122\u0123"+
                    "\u0005\b\u0000\u0000\u0123\u0125\u0001\u0000\u0000\u0000\u0124\u0121\u0001"+
                    "\u0000\u0000\u0000\u0125\u0128\u0001\u0000\u0000\u0000\u0126\u0124\u0001"+
                    "\u0000\u0000\u0000\u0126\u0127\u0001\u0000\u0000\u0000\u0127\u0129\u0001"+
                    "\u0000\u0000\u0000\u0128\u0126\u0001\u0000\u0000\u0000\u0129\u012a\u0003"+
                    "\u00a8T\u0000\u012a\'\u0001\u0000\u0000\u0000\u012b\u012c\u0005\f\u0000"+
                    "\u0000\u012c\u0131\u0003\u00b0X\u0000\u012d\u012f\u0005\u0002\u0000\u0000"+
                    "\u012e\u012d\u0001\u0000\u0000\u0000\u012e\u012f\u0001\u0000\u0000\u0000"+
                    "\u012f\u0130\u0001\u0000\u0000\u0000\u0130\u0132\u0005G\u0000\u0000\u0131"+
                    "\u012e\u0001\u0000\u0000\u0000\u0131\u0132\u0001\u0000\u0000\u0000\u0132"+
                    "\u0133\u0001\u0000\u0000\u0000\u0133\u0134\u0005\r\u0000\u0000\u0134\u0139"+
                    "\u0003*\u0015\u0000\u0135\u0136\u0005\u000e\u0000\u0000\u0136\u0138\u0003"+
                    "*\u0015\u0000\u0137\u0135\u0001\u0000\u0000\u0000\u0138\u013b\u0001\u0000"+
                    "\u0000\u0000\u0139\u0137\u0001\u0000\u0000\u0000\u0139\u013a\u0001\u0000"+
                    "\u0000\u0000\u013a)\u0001\u0000\u0000\u0000\u013b\u0139\u0001\u0000\u0000"+
                    "\u0000\u013c\u013d\u0005G\u0000\u0000\u013d\u013f\u0005\b\u0000\u0000"+
                    "\u013e\u013c\u0001\u0000\u0000\u0000\u013e\u013f\u0001\u0000\u0000\u0000"+
                    "\u013f\u0142\u0001\u0000\u0000\u0000\u0140\u0143\u0003&\u0013\u0000\u0141"+
                    "\u0143\u0003\u00acV\u0000\u0142\u0140\u0001\u0000\u0000\u0000\u0142\u0141"+
                    "\u0001\u0000\u0000\u0000\u0143\u0144\u0001\u0000\u0000\u0000\u0144\u0145"+
                    "\u0005\u000f\u0000\u0000\u0145\u0146\u0003,\u0016\u0000\u0146+\u0001\u0000"+
                    "\u0000\u0000\u0147\u014f\u0003v;\u0000\u0148\u014f\u0003\u0080@\u0000"+
                    "\u0149\u014f\u0003\u0084B\u0000\u014a\u014f\u0003\u0088D\u0000\u014b\u014f"+
                    "\u0003\u008cF\u0000\u014c\u014f\u0003\u0090H\u0000\u014d\u014f\u0005\u0010"+
                    "\u0000\u0000\u014e\u0147\u0001\u0000\u0000\u0000\u014e\u0148\u0001\u0000"+
                    "\u0000\u0000\u014e\u0149\u0001\u0000\u0000\u0000\u014e\u014a\u0001\u0000"+
                    "\u0000\u0000\u014e\u014b\u0001\u0000\u0000\u0000\u014e\u014c\u0001\u0000"+
                    "\u0000\u0000\u014e\u014d\u0001\u0000\u0000\u0000\u014f-\u0001\u0000\u0000"+
                    "\u0000\u0150\u0151\u0005\u0011\u0000\u0000\u0151\u0152\u0005\u0001\u0000"+
                    "\u0000\u0152\u0157\u0003\u00b0X\u0000\u0153\u0155\u0005\u0002\u0000\u0000"+
                    "\u0154\u0153\u0001\u0000\u0000\u0000\u0154\u0155\u0001\u0000\u0000\u0000"+
                    "\u0155\u0156\u0001\u0000\u0000\u0000\u0156\u0158\u0005G\u0000\u0000\u0157"+
                    "\u0154\u0001\u0000\u0000\u0000\u0157\u0158\u0001\u0000\u0000\u0000\u0158"+
                    "/\u0001\u0000\u0000\u0000\u0159\u015b\u0005\u0012\u0000\u0000\u015a\u015c"+
                    "\u0005\u0013\u0000\u0000\u015b\u015a\u0001\u0000\u0000\u0000\u015b\u015c"+
                    "\u0001\u0000\u0000\u0000\u015c\u015d\u0001\u0000\u0000\u0000\u015d\u015e"+
                    "\u00032\u0019\u0000\u015e\u0165\u0006\u0018\uffff\uffff\u0000\u015f\u0160"+
                    "\u0005\u000e\u0000\u0000\u0160\u0161\u00032\u0019\u0000\u0161\u0162\u0006"+
                    "\u0018\uffff\uffff\u0000\u0162\u0164\u0001\u0000\u0000\u0000\u0163\u015f"+
                    "\u0001\u0000\u0000\u0000\u0164\u0167\u0001\u0000\u0000\u0000\u0165\u0163"+
                    "\u0001\u0000\u0000\u0000\u0165\u0166\u0001\u0000\u0000\u0000\u01661\u0001"+
                    "\u0000\u0000\u0000\u0167\u0165\u0001\u0000\u0000\u0000\u0168\u0169\u0005"+
                    "G\u0000\u0000\u0169\u016f\u0006\u0019\uffff\uffff\u0000\u016a\u016b\u0005"+
                    "\b\u0000\u0000\u016b\u016c\u0005G\u0000\u0000\u016c\u016e\u0006\u0019"+
                    "\uffff\uffff\u0000\u016d\u016a\u0001\u0000\u0000\u0000\u016e\u0171\u0001"+
                    "\u0000\u0000\u0000\u016f\u016d\u0001\u0000\u0000\u0000\u016f\u0170\u0001"+
                    "\u0000\u0000\u0000\u01703\u0001\u0000\u0000\u0000\u0171\u016f\u0001\u0000"+
                    "\u0000\u0000\u0172\u0173\u0005\u0014\u0000\u0000\u0173\u0174\u0003\u00a2"+
                    "Q\u0000\u0174\u0175\u0005\n\u0000\u0000\u0175\u017a\u00036\u001b\u0000"+
                    "\u0176\u0177\u0005\u000e\u0000\u0000\u0177\u0179\u00036\u001b\u0000\u0178"+
                    "\u0176\u0001\u0000\u0000\u0000\u0179\u017c\u0001\u0000\u0000\u0000\u017a"+
                    "\u0178\u0001\u0000\u0000\u0000\u017a\u017b\u0001\u0000\u0000\u0000\u017b"+
                    "\u017d\u0001\u0000\u0000\u0000\u017c\u017a\u0001\u0000\u0000\u0000\u017d"+
                    "\u017e\u0005\u000b\u0000\u0000\u017e5\u0001\u0000\u0000\u0000\u017f\u0182"+
                    "\u0003\u001e\u000f\u0000\u0180\u0182\u00038\u001c\u0000\u0181\u017f\u0001"+
                    "\u0000\u0000\u0000\u0181\u0180\u0001\u0000\u0000\u0000\u01827\u0001\u0000"+
                    "\u0000\u0000\u0183\u0184\u0007\u0000\u0000\u0000\u0184\u0186\u0005\n\u0000"+
                    "\u0000\u0185\u0187\u0005\u0013\u0000\u0000\u0186\u0185\u0001\u0000\u0000"+
                    "\u0000\u0186\u0187\u0001\u0000\u0000\u0000\u0187\u0188\u0001\u0000\u0000"+
                    "\u0000\u0188\u0189\u0003 \u0010\u0000\u0189\u018a\u0005\u000b\u0000\u0000"+
                    "\u018a\u0197\u0001\u0000\u0000\u0000\u018b\u018c\u0005\u0019\u0000\u0000"+
                    "\u018c\u018e\u0005\n\u0000\u0000\u018d\u018f\u0005\u0013\u0000\u0000\u018e"+
                    "\u018d\u0001\u0000\u0000\u0000\u018e\u018f\u0001\u0000\u0000\u0000\u018f"+
                    "\u0193\u0001\u0000\u0000\u0000\u0190\u0194\u0005G\u0000\u0000\u0191\u0194"+
                    "\u0003 \u0010\u0000\u0192\u0194\u0003\"\u0011\u0000\u0193\u0190\u0001"+
                    "\u0000\u0000\u0000\u0193\u0191\u0001\u0000\u0000\u0000\u0193\u0192\u0001"+
                    "\u0000\u0000\u0000\u0194\u0195\u0001\u0000\u0000\u0000\u0195\u0197\u0005"+
                    "\u000b\u0000\u0000\u0196\u0183\u0001\u0000\u0000\u0000\u0196\u018b\u0001"+
                    "\u0000\u0000\u0000\u01979\u0001\u0000\u0000\u0000\u0198\u0199\u0005\u001a"+
                    "\u0000\u0000\u0199\u019a\u0003R)\u0000\u019a\u019b\u0006\u001d\uffff\uffff"+
                    "\u0000\u019b;\u0001\u0000\u0000\u0000\u019c\u019d\u0005\u001b\u0000\u0000"+
                    "\u019d\u019e\u0005\u001c\u0000\u0000\u019e\u01a3\u0003>\u001f\u0000\u019f"+
                    "\u01a0\u0005\u000e\u0000\u0000\u01a0\u01a2\u0003>\u001f\u0000\u01a1\u019f"+
                    "\u0001\u0000\u0000\u0000\u01a2\u01a5\u0001\u0000\u0000\u0000\u01a3\u01a1"+
                    "\u0001\u0000\u0000\u0000\u01a3\u01a4\u0001\u0000\u0000\u0000\u01a4=\u0001"+
                    "\u0000\u0000\u0000\u01a5\u01a3\u0001\u0000\u0000\u0000\u01a6\u01a9\u0003"+
                    "\u001e\u000f\u0000\u01a7\u01a9\u0005G\u0000\u0000\u01a8\u01a6\u0001\u0000"+
                    "\u0000\u0000\u01a8\u01a7\u0001\u0000\u0000\u0000\u01a9?\u0001\u0000\u0000"+
                    "\u0000\u01aa\u01ab\u0005\u001d\u0000\u0000\u01ab\u01ac\u0003R)\u0000\u01ac"+
                    "A\u0001\u0000\u0000\u0000\u01ad\u01ae\u0005\u001e\u0000\u0000\u01ae\u01af"+
                    "\u0005\u001c\u0000\u0000\u01af\u01b4\u0003D\"\u0000\u01b0\u01b1\u0005"+
                    "\u000e\u0000\u0000\u01b1\u01b3\u0003D\"\u0000\u01b2\u01b0\u0001\u0000"+
                    "\u0000\u0000\u01b3\u01b6\u0001\u0000\u0000\u0000\u01b4\u01b2\u0001\u0000"+
                    "\u0000\u0000\u01b4\u01b5\u0001\u0000\u0000\u0000\u01b5C\u0001\u0000\u0000"+
                    "\u0000\u01b6\u01b4\u0001\u0000\u0000\u0000\u01b7\u01b9\u0003 \u0010\u0000"+
                    "\u01b8\u01ba\u0007\u0001\u0000\u0000\u01b9\u01b8\u0001\u0000\u0000\u0000"+
                    "\u01b9\u01ba\u0001\u0000\u0000\u0000\u01baE\u0001\u0000\u0000\u0000\u01bb"+
                    "\u01bc\u0003N\'\u0000\u01bc\u01be\u0003H$\u0000\u01bd\u01bf\u0003:\u001d"+
                    "\u0000\u01be\u01bd\u0001\u0000\u0000\u0000\u01be\u01bf\u0001\u0000\u0000"+
                    "\u0000\u01bf\u01c1\u0001\u0000\u0000\u0000\u01c0\u01c2\u0003<\u001e\u0000"+
                    "\u01c1\u01c0\u0001\u0000\u0000\u0000\u01c1\u01c2\u0001\u0000\u0000\u0000"+
                    "\u01c2\u01c4\u0001\u0000\u0000\u0000\u01c3\u01c5\u0003@ \u0000\u01c4\u01c3"+
                    "\u0001\u0000\u0000\u0000\u01c4\u01c5\u0001\u0000\u0000\u0000\u01c5G\u0001"+
                    "\u0000\u0000\u0000\u01c6\u01c7\u0005\u0001\u0000\u0000\u01c7\u01cc\u0003"+
                    "J%\u0000\u01c8\u01c9\u0005\u000e\u0000\u0000\u01c9\u01cb\u0003J%\u0000"+
                    "\u01ca\u01c8\u0001\u0000\u0000\u0000\u01cb\u01ce\u0001\u0000\u0000\u0000"+
                    "\u01cc\u01ca\u0001\u0000\u0000\u0000\u01cc\u01cd\u0001\u0000\u0000\u0000"+
                    "\u01cdI\u0001\u0000\u0000\u0000\u01ce\u01cc\u0001\u0000\u0000\u0000\u01cf"+
                    "\u01d8\u0003\f\u0006\u0000\u01d0\u01d2\u0003L&\u0000\u01d1\u01d3\u0005"+
                    "\u0002\u0000\u0000\u01d2\u01d1\u0001\u0000\u0000\u0000\u01d2\u01d3\u0001"+
                    "\u0000\u0000\u0000\u01d3\u01d4\u0001\u0000\u0000\u0000\u01d4\u01d5\u0005"+
                    "G\u0000\u0000\u01d5\u01d8\u0001\u0000\u0000\u0000\u01d6\u01d8\u0003\u001c"+
                    "\u000e\u0000\u01d7\u01cf\u0001\u0000\u0000\u0000\u01d7\u01d0\u0001\u0000"+
                    "\u0000\u0000\u01d7\u01d6\u0001\u0000\u0000\u0000\u01d8K\u0001\u0000\u0000"+
                    "\u0000\u01d9\u01dc\u0003$\u0012\u0000\u01da\u01dc\u0003\"\u0011\u0000"+
                    "\u01db\u01d9\u0001\u0000\u0000\u0000\u01db\u01da\u0001\u0000\u0000\u0000"+
                    "\u01dcM\u0001\u0000\u0000\u0000\u01dd\u01df\u0005\u0012\u0000\u0000\u01de"+
                    "\u01e0\u0005\u0013\u0000\u0000\u01df\u01de\u0001\u0000\u0000\u0000\u01df"+
                    "\u01e0\u0001\u0000\u0000\u0000\u01e0\u01e1\u0001\u0000\u0000\u0000\u01e1"+
                    "\u01e2\u0003P(\u0000\u01e2O\u0001\u0000\u0000\u0000\u01e3\u01e7\u0003"+
                    "\u001e\u000f\u0000\u01e4\u01e7\u00038\u001c\u0000\u01e5\u01e7\u0005G\u0000"+
                    "\u0000\u01e6\u01e3\u0001\u0000\u0000\u0000\u01e6\u01e4\u0001\u0000\u0000"+
                    "\u0000\u01e6\u01e5\u0001\u0000\u0000\u0000\u01e7Q\u0001\u0000\u0000\u0000"+
                    "\u01e8\u01e9\u0003T*\u0000\u01e9\u01ea\u0006)\uffff\uffff\u0000\u01ea"+
                    "S\u0001\u0000\u0000\u0000\u01eb\u01ec\u0003V+\u0000\u01ec\u01ed\u0006"+
                    "*\uffff\uffff\u0000\u01edU\u0001\u0000\u0000\u0000\u01ee\u01ef\u0003X"+
                    ",\u0000\u01ef\u01f0\u0006+\uffff\uffff\u0000\u01f0W\u0001\u0000\u0000"+
                    "\u0000\u01f1\u01f2\u0003Z-\u0000\u01f2\u01f3\u0006,\uffff\uffff\u0000"+
                    "\u01f3Y\u0001\u0000\u0000\u0000\u01f4\u01f5\u0003n7\u0000\u01f5\u01f6"+
                    "\u0006-\uffff\uffff\u0000\u01f6[\u0001\u0000\u0000\u0000\u01f7\u01f9\u0003"+
                    "t:\u0000\u01f8\u01fa\u0005!\u0000\u0000\u01f9\u01f8\u0001\u0000\u0000"+
                    "\u0000\u01f9\u01fa\u0001\u0000\u0000\u0000\u01fa\u01fb\u0001\u0000\u0000"+
                    "\u0000\u01fb\u01fc\u0005\"\u0000\u0000\u01fc\u01fd\u0003t:\u0000\u01fd"+
                    "\u01fe\u0005#\u0000\u0000\u01fe\u01ff\u0003t:\u0000\u01ff\u0213\u0001"+
                    "\u0000\u0000\u0000\u0200\u0202\u0003~?\u0000\u0201\u0203\u0005!\u0000"+
                    "\u0000\u0202\u0201\u0001\u0000\u0000\u0000\u0202\u0203\u0001\u0000\u0000"+
                    "\u0000\u0203\u0204\u0001\u0000\u0000\u0000\u0204\u0205\u0005\"\u0000\u0000"+
                    "\u0205\u0206\u0003~?\u0000\u0206\u0207\u0005#\u0000\u0000\u0207\u0208"+
                    "\u0003~?\u0000\u0208\u0213\u0001\u0000\u0000\u0000\u0209\u020b\u0003\u0082"+
                    "A\u0000\u020a\u020c\u0005!\u0000\u0000\u020b\u020a\u0001\u0000\u0000\u0000"+
                    "\u020b\u020c\u0001\u0000\u0000\u0000\u020c\u020d\u0001\u0000\u0000\u0000"+
                    "\u020d\u020e\u0005\"\u0000\u0000\u020e\u020f\u0003\u0082A\u0000\u020f"+
                    "\u0210\u0005#\u0000\u0000\u0210\u0211\u0003\u0082A\u0000\u0211\u0213\u0001"+
                    "\u0000\u0000\u0000\u0212\u01f7\u0001\u0000\u0000\u0000\u0212\u0200\u0001"+
                    "\u0000\u0000\u0000\u0212\u0209\u0001\u0000\u0000\u0000\u0213]\u0001\u0000"+
                    "\u0000\u0000\u0214\u0216\u0003 \u0010\u0000\u0215\u0217\u0005!\u0000\u0000"+
                    "\u0216\u0215\u0001\u0000\u0000\u0000\u0216\u0217\u0001\u0000\u0000\u0000"+
                    "\u0217\u0218\u0001\u0000\u0000\u0000\u0218\u0219\u0005\t\u0000\u0000\u0219"+
                    "\u0223\u0005\n\u0000\u0000\u021a\u021f\u0003`0\u0000\u021b\u021c\u0005"+
                    "\u000e\u0000\u0000\u021c\u021e\u0003`0\u0000\u021d\u021b\u0001\u0000\u0000"+
                    "\u0000\u021e\u0221\u0001\u0000\u0000\u0000\u021f\u021d\u0001\u0000\u0000"+
                    "\u0000\u021f\u0220\u0001\u0000\u0000\u0000\u0220\u0224\u0001\u0000\u0000"+
                    "\u0000\u0221\u021f\u0001\u0000\u0000\u0000\u0222\u0224\u0003F#\u0000\u0223"+
                    "\u021a\u0001\u0000\u0000\u0000\u0223\u0222\u0001\u0000\u0000\u0000\u0224"+
                    "\u0225\u0001\u0000\u0000\u0000\u0225\u0226\u0005\u000b\u0000\u0000\u0226"+
                    "_\u0001\u0000\u0000\u0000\u0227\u022a\u0003\u00a0P\u0000\u0228\u022a\u0003"+
                    "\u009eO\u0000\u0229\u0227\u0001\u0000\u0000\u0000\u0229\u0228\u0001\u0000"+
                    "\u0000\u0000\u022aa\u0001\u0000\u0000\u0000\u022b\u022d\u0003~?\u0000"+
                    "\u022c\u022e\u0005!\u0000\u0000\u022d\u022c\u0001\u0000\u0000\u0000\u022d"+
                    "\u022e\u0001\u0000\u0000\u0000\u022e\u022f\u0001\u0000\u0000\u0000\u022f"+
                    "\u0230\u0005$\u0000\u0000\u0230\u0233\u0003\u009cN\u0000\u0231\u0232\u0005"+
                    "%\u0000\u0000\u0232\u0234\u0005K\u0000\u0000\u0233\u0231\u0001\u0000\u0000"+
                    "\u0000\u0233\u0234\u0001\u0000\u0000\u0000\u0234c\u0001\u0000\u0000\u0000"+
                    "\u0235\u0238\u0003\u001e\u000f\u0000\u0236\u0238\u0003\u009eO\u0000\u0237"+
                    "\u0235\u0001\u0000\u0000\u0000\u0237\u0236\u0001\u0000\u0000\u0000\u0238"+
                    "\u0239\u0001\u0000\u0000\u0000\u0239\u023b\u0005&\u0000\u0000\u023a\u023c"+
                    "\u0005!\u0000\u0000\u023b\u023a\u0001\u0000\u0000\u0000\u023b\u023c\u0001"+
                    "\u0000\u0000\u0000\u023c\u023d\u0001\u0000\u0000\u0000\u023d\u023e\u0005"+
                    "\u0010\u0000\u0000\u023ee\u0001\u0000\u0000\u0000\u023f\u0240\u0003$\u0012"+
                    "\u0000\u0240\u0242\u0005&\u0000\u0000\u0241\u0243\u0005!\u0000\u0000\u0242"+
                    "\u0241\u0001\u0000\u0000\u0000\u0242\u0243\u0001\u0000\u0000\u0000\u0243"+
                    "\u0244\u0001\u0000\u0000\u0000\u0244\u0245\u0005\'\u0000\u0000\u0245g"+
                    "\u0001\u0000\u0000\u0000\u0246\u0248\u0003\u008eG\u0000\u0247\u0249\u0005"+
                    "!\u0000\u0000\u0248\u0247\u0001\u0000\u0000\u0000\u0248\u0249\u0001\u0000"+
                    "\u0000\u0000\u0249\u024a\u0001\u0000\u0000\u0000\u024a\u024c\u0005(\u0000"+
                    "\u0000\u024b\u024d\u0005)\u0000\u0000\u024c\u024b\u0001\u0000\u0000\u0000"+
                    "\u024c\u024d\u0001\u0000\u0000\u0000\u024d\u024e\u0001\u0000\u0000\u0000"+
                    "\u024e\u024f\u0003$\u0012\u0000\u024fi\u0001\u0000\u0000\u0000\u0250\u0252"+
                    "\u0005!\u0000\u0000\u0251\u0250\u0001\u0000\u0000\u0000\u0251\u0252\u0001"+
                    "\u0000\u0000\u0000\u0252\u0253\u0001\u0000\u0000\u0000\u0253\u0254\u0005"+
                    "*\u0000\u0000\u0254\u0255\u0005\n\u0000\u0000\u0255\u0256\u0003F#\u0000"+
                    "\u0256\u0257\u0005\u000b\u0000\u0000\u0257k\u0001\u0000\u0000\u0000\u0258"+
                    "\u0259\u0007\u0002\u0000\u0000\u0259\u025a\u0005\n\u0000\u0000\u025a\u025b"+
                    "\u0003F#\u0000\u025b\u025c\u0005\u000b\u0000\u0000\u025cm\u0001\u0000"+
                    "\u0000\u0000\u025d\u025e\u00032\u0019\u0000\u025e\u025f\u0003r9\u0000"+
                    "\u025f\u0260\u0003t:\u0000\u0260\u0261\u00067\uffff\uffff\u0000\u0261"+
                    "o\u0001\u0000\u0000\u0000\u0262\u0263\u0003\u00b2Y\u0000\u0263\u0264\u0006"+
                    "8\uffff\uffff\u0000\u0264q\u0001\u0000\u0000\u0000\u0265\u0266\u0005\u000f"+
                    "\u0000\u0000\u0266\u0272\u00069\uffff\uffff\u0000\u0267\u0268\u0005.\u0000"+
                    "\u0000\u0268\u0272\u00069\uffff\uffff\u0000\u0269\u026a\u0005/\u0000\u0000"+
                    "\u026a\u0272\u00069\uffff\uffff\u0000\u026b\u026c\u00050\u0000\u0000\u026c"+
                    "\u0272\u00069\uffff\uffff\u0000\u026d\u026e\u00051\u0000\u0000\u026e\u0272"+
                    "\u00069\uffff\uffff\u0000\u026f\u0270\u00052\u0000\u0000\u0270\u0272\u0006"+
                    "9\uffff\uffff\u0000\u0271\u0265\u0001\u0000\u0000\u0000\u0271\u0267\u0001"+
                    "\u0000\u0000\u0000\u0271\u0269\u0001\u0000\u0000\u0000\u0271\u026b\u0001"+
                    "\u0000\u0000\u0000\u0271\u026d\u0001\u0000\u0000\u0000\u0271\u026f\u0001"+
                    "\u0000\u0000\u0000\u0272s\u0001\u0000\u0000\u0000\u0273\u0274\u0003v;"+
                    "\u0000\u0274\u0275\u0006:\uffff\uffff\u0000\u0275u\u0001\u0000\u0000\u0000"+
                    "\u0276\u0277\u0003x<\u0000\u0277\u0278\u0006;\uffff\uffff\u0000\u0278"+
                    "w\u0001\u0000\u0000\u0000\u0279\u027a\u0003z=\u0000\u027a\u027b\u0006"+
                    "<\uffff\uffff\u0000\u027by\u0001\u0000\u0000\u0000\u027c\u027d\u0003|"+
                    ">\u0000\u027d\u027e\u0006=\uffff\uffff\u0000\u027e{\u0001\u0000\u0000"+
                    "\u0000\u027f\u0280\u0003\u009aM\u0000\u0280\u0281\u0006>\uffff\uffff\u0000"+
                    "\u0281\u0286\u0001\u0000\u0000\u0000\u0282\u0283\u0003\u009eO\u0000\u0283"+
                    "\u0284\u0006>\uffff\uffff\u0000\u0284\u0286\u0001\u0000\u0000\u0000\u0285"+
                    "\u027f\u0001\u0000\u0000\u0000\u0285\u0282\u0001\u0000\u0000\u0000\u0286"+
                    "}\u0001\u0000\u0000\u0000\u0287\u0288\u0003\u0080@\u0000\u0288\u007f\u0001"+
                    "\u0000\u0000\u0000\u0289\u028d\u0003 \u0010\u0000\u028a\u028d\u0005J\u0000"+
                    "\u0000\u028b\u028d\u0003\u009eO\u0000\u028c\u0289\u0001\u0000\u0000\u0000"+
                    "\u028c\u028a\u0001\u0000\u0000\u0000\u028c\u028b\u0001\u0000\u0000\u0000"+
                    "\u028d\u0081\u0001\u0000\u0000\u0000\u028e\u0294\u0003\u0084B\u0000\u028f"+
                    "\u0290\u0005\n\u0000\u0000\u0290\u0291\u0003F#\u0000\u0291\u0292\u0005"+
                    "\u000b\u0000\u0000\u0292\u0294\u0001\u0000\u0000\u0000\u0293\u028e\u0001"+
                    "\u0000\u0000\u0000\u0293\u028f\u0001\u0000\u0000\u0000\u0294\u0083\u0001"+
                    "\u0000\u0000\u0000\u0295\u029a\u0003 \u0010\u0000\u0296\u029a\u0003\u009e"+
                    "O\u0000\u0297\u029a\u0003\u0094J\u0000\u0298\u029a\u00038\u001c\u0000"+
                    "\u0299\u0295\u0001\u0000\u0000\u0000\u0299\u0296\u0001\u0000\u0000\u0000"+
                    "\u0299\u0297\u0001\u0000\u0000\u0000\u0299\u0298\u0001\u0000\u0000\u0000"+
                    "\u029a\u0085\u0001\u0000\u0000\u0000\u029b\u029c\u0003\u0088D\u0000\u029c"+
                    "\u0087\u0001\u0000\u0000\u0000\u029d\u02a1\u0003 \u0010\u0000\u029e\u02a1"+
                    "\u0003\u00a6S\u0000\u029f\u02a1\u0003\u009eO\u0000\u02a0\u029d\u0001\u0000"+
                    "\u0000\u0000\u02a0\u029e\u0001\u0000\u0000\u0000\u02a0\u029f\u0001\u0000"+
                    "\u0000\u0000\u02a1\u0089\u0001\u0000\u0000\u0000\u02a2\u02a8\u0003\u008c"+
                    "F\u0000\u02a3\u02a4\u0005\n\u0000\u0000\u02a4\u02a5\u0003F#\u0000\u02a5"+
                    "\u02a6\u0005\u000b\u0000\u0000\u02a6\u02a8\u0001\u0000\u0000\u0000\u02a7"+
                    "\u02a2\u0001\u0000\u0000\u0000\u02a7\u02a3\u0001\u0000\u0000\u0000\u02a8"+
                    "\u008b\u0001\u0000\u0000\u0000\u02a9\u02ad\u0003 \u0010\u0000\u02aa\u02ad"+
                    "\u0003\u00a4R\u0000\u02ab\u02ad\u0003\u009eO\u0000\u02ac\u02a9\u0001\u0000"+
                    "\u0000\u0000\u02ac\u02aa\u0001\u0000\u0000\u0000\u02ac\u02ab\u0001\u0000"+
                    "\u0000\u0000\u02ad\u008d\u0001\u0000\u0000\u0000\u02ae\u02b1\u0003\"\u0011"+
                    "\u0000\u02af\u02b1\u0003\u0090H\u0000\u02b0\u02ae\u0001\u0000\u0000\u0000"+
                    "\u02b0\u02af\u0001\u0000\u0000\u0000\u02b1\u008f\u0001\u0000\u0000\u0000"+
                    "\u02b2\u02b5\u0005G\u0000\u0000\u02b3\u02b5\u0003\u009eO\u0000\u02b4\u02b2"+
                    "\u0001\u0000\u0000\u0000\u02b4\u02b3\u0001\u0000\u0000\u0000\u02b5\u0091"+
                    "\u0001\u0000\u0000\u0000\u02b6\u02b7\u00053\u0000\u0000\u02b7\u02b8\u0005"+
                    "\n\u0000\u0000\u02b8\u02b9\u0003\u0080@\u0000\u02b9\u02ba\u0005\u000b"+
                    "\u0000\u0000\u02ba\u02dd\u0001\u0000\u0000\u0000\u02bb\u02bc\u00054\u0000"+
                    "\u0000\u02bc\u02bd\u0005\n\u0000\u0000\u02bd\u02be\u0003\u0080@\u0000"+
                    "\u02be\u02bf\u0005\u000e\u0000\u0000\u02bf\u02c2\u0003\u0080@\u0000\u02c0"+
                    "\u02c1\u0005\u000e\u0000\u0000\u02c1\u02c3\u0003v;\u0000\u02c2\u02c0\u0001"+
                    "\u0000\u0000\u0000\u02c2\u02c3\u0001\u0000\u0000\u0000\u02c3\u02c4\u0001"+
                    "\u0000\u0000\u0000\u02c4\u02c5\u0005\u000b\u0000\u0000\u02c5\u02dd\u0001"+
                    "\u0000\u0000\u0000\u02c6\u02c7\u00055\u0000\u0000\u02c7\u02c8\u0005\n"+
                    "\u0000\u0000\u02c8\u02c9\u0003v;\u0000\u02c9\u02ca\u0005\u000b\u0000\u0000"+
                    "\u02ca\u02dd\u0001\u0000\u0000\u0000\u02cb\u02cc\u00056\u0000\u0000\u02cc"+
                    "\u02cd\u0005\n\u0000\u0000\u02cd\u02ce\u0003v;\u0000\u02ce\u02cf\u0005"+
                    "\u000b\u0000\u0000\u02cf\u02dd\u0001\u0000\u0000\u0000\u02d0\u02d1\u0005"+
                    "7\u0000\u0000\u02d1\u02d2\u0005\n\u0000\u0000\u02d2\u02d3\u0003v;\u0000"+
                    "\u02d3\u02d4\u0005\u000e\u0000\u0000\u02d4\u02d5\u0003v;\u0000\u02d5\u02d6"+
                    "\u0005\u000b\u0000\u0000\u02d6\u02dd\u0001\u0000\u0000\u0000\u02d7\u02d8"+
                    "\u00058\u0000\u0000\u02d8\u02d9\u0005\n\u0000\u0000\u02d9\u02da\u0003"+
                    "$\u0012\u0000\u02da\u02db\u0005\u000b\u0000\u0000\u02db\u02dd\u0001\u0000"+
                    "\u0000\u0000\u02dc\u02b6\u0001\u0000\u0000\u0000\u02dc\u02bb\u0001\u0000"+
                    "\u0000\u0000\u02dc\u02c6\u0001\u0000\u0000\u0000\u02dc\u02cb\u0001\u0000"+
                    "\u0000\u0000\u02dc\u02d0\u0001\u0000\u0000\u0000\u02dc\u02d7\u0001\u0000"+
                    "\u0000\u0000\u02dd\u0093\u0001\u0000\u0000\u0000\u02de\u02df\u0007\u0003"+
                    "\u0000\u0000\u02df\u0095\u0001\u0000\u0000\u0000\u02e0\u02e1\u0005<\u0000"+
                    "\u0000\u02e1\u02e2\u0005\n\u0000\u0000\u02e2\u02e3\u0003\u0080@\u0000"+
                    "\u02e3\u02e4\u0005\u000e\u0000\u0000\u02e4\u02e5\u0003\u0080@\u0000\u02e5"+
                    "\u02e6\u0005\u000b\u0000\u0000\u02e6\u0309\u0001\u0000\u0000\u0000\u02e7"+
                    "\u02e8\u0005=\u0000\u0000\u02e8\u02e9\u0005\n\u0000\u0000\u02e9\u02ea"+
                    "\u0003\u0080@\u0000\u02ea\u02eb\u0005\u000e\u0000\u0000\u02eb\u02ec\u0003"+
                    "v;\u0000\u02ec\u02ed\u0005\u000e\u0000\u0000\u02ed\u02ee\u0003v;\u0000"+
                    "\u02ee\u02ef\u0005\u000b\u0000\u0000\u02ef\u0309\u0001\u0000\u0000\u0000"+
                    "\u02f0\u02f1\u0005>\u0000\u0000\u02f1\u02f9\u0005\n\u0000\u0000\u02f2"+
                    "\u02f4\u0003\u0098L\u0000\u02f3\u02f2\u0001\u0000\u0000\u0000\u02f3\u02f4"+
                    "\u0001\u0000\u0000\u0000\u02f4\u02f6\u0001\u0000\u0000\u0000\u02f5\u02f7"+
                    "\u0005M\u0000\u0000\u02f6\u02f5\u0001\u0000\u0000\u0000\u02f6\u02f7\u0001"+
                    "\u0000\u0000\u0000\u02f7\u02f8\u0001\u0000\u0000\u0000\u02f8\u02fa\u0005"+
                    "\u0001\u0000\u0000\u02f9\u02f3\u0001\u0000\u0000\u0000\u02f9\u02fa\u0001"+
                    "\u0000\u0000\u0000\u02fa\u02fb\u0001\u0000\u0000\u0000\u02fb\u02fc\u0003"+
                    "\u0080@\u0000\u02fc\u02fd\u0005\u000b\u0000\u0000\u02fd\u0309\u0001\u0000"+
                    "\u0000\u0000\u02fe\u02ff\u0005?\u0000\u0000\u02ff\u0300\u0005\n\u0000"+
                    "\u0000\u0300\u0301\u0003\u0080@\u0000\u0301\u0302\u0005\u000b\u0000\u0000"+
                    "\u0302\u0309\u0001\u0000\u0000\u0000\u0303\u0304\u0005@\u0000\u0000\u0304"+
                    "\u0305\u0005\n\u0000\u0000\u0305\u0306\u0003\u0080@\u0000\u0306\u0307"+
                    "\u0005\u000b\u0000\u0000\u0307\u0309\u0001\u0000\u0000\u0000\u0308\u02e0"+
                    "\u0001\u0000\u0000\u0000\u0308\u02e7\u0001\u0000\u0000\u0000\u0308\u02f0"+
                    "\u0001\u0000\u0000\u0000\u0308\u02fe\u0001\u0000\u0000\u0000\u0308\u0303"+
                    "\u0001\u0000\u0000\u0000\u0309\u0097\u0001\u0000\u0000\u0000\u030a\u030b"+
                    "\u0007\u0004\u0000\u0000\u030b\u0099\u0001\u0000\u0000\u0000\u030c\u030d"+
                    "\u0005H\u0000\u0000\u030d\u030e\u0006M\uffff\uffff\u0000\u030e\u009b\u0001"+
                    "\u0000\u0000\u0000\u030f\u0310\u0001\u0000\u0000\u0000\u0310\u009d\u0001"+
                    "\u0000\u0000\u0000\u0311\u0312\u0005D\u0000\u0000\u0312\u0313\u0005G\u0000"+
                    "\u0000\u0313\u0314\u0006O\uffff\uffff\u0000\u0314\u009f\u0001\u0000\u0000"+
                    "\u0000\u0315\u0316\u0001\u0000\u0000\u0000\u0316\u00a1\u0001\u0000\u0000"+
                    "\u0000\u0317\u0318\u0001\u0000\u0000\u0000\u0318\u00a3\u0001\u0000\u0000"+
                    "\u0000\u0319\u031a\u0001\u0000\u0000\u0000\u031a\u00a5\u0001\u0000\u0000"+
                    "\u0000\u031b\u031c\u0007\u0005\u0000\u0000\u031c\u00a7\u0001\u0000\u0000"+
                    "\u0000\u031d\u031e\u0001\u0000\u0000\u0000\u031e\u00a9\u0001\u0000\u0000"+
                    "\u0000\u031f\u0320\u0001\u0000\u0000\u0000\u0320\u00ab\u0001\u0000\u0000"+
                    "\u0000\u0321\u0322\u0001\u0000\u0000\u0000\u0322\u00ad\u0001\u0000\u0000"+
                    "\u0000\u0323\u0324\u0001\u0000\u0000\u0000\u0324\u00af\u0001\u0000\u0000"+
                    "\u0000\u0325\u0326\u0005G\u0000\u0000\u0326\u0327\u0006X\uffff\uffff\u0000"+
                    "\u0327\u00b1\u0001\u0000\u0000\u0000\u0328\u0329\u0005G\u0000\u0000\u0329"+
                    "\u032a\u0006Y\uffff\uffff\u0000\u032a\u00b3\u0001\u0000\u0000\u0000L\u00bd"+
                    "\u00c1\u00c5\u00d0\u00d8\u00e2\u00e5\u00eb\u00fa\u0100\u0104\u0110\u011c"+
                    "\u0126\u012e\u0131\u0139\u013e\u0142\u014e\u0154\u0157\u015b\u0165\u016f"+
                    "\u017a\u0181\u0186\u018e\u0193\u0196\u01a3\u01a8\u01b4\u01b9\u01be\u01c1"+
                    "\u01c4\u01cc\u01d2\u01d7\u01db\u01df\u01e6\u01f9\u0202\u020b\u0212\u0216"+
                    "\u021f\u0223\u0229\u022d\u0233\u0237\u023b\u0242\u0248\u024c\u0251\u0271"+
                    "\u0285\u028c\u0293\u0299\u02a0\u02a7\u02ac\u02b0\u02b4\u02c2\u02dc\u02f3"+
                    "\u02f6\u02f9\u0308";
    public static final ATN _ATN =
            new ATNDeserializer().deserialize(_serializedATN.toCharArray());
    static {
        _decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
        for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
            _decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
        }
    }
}