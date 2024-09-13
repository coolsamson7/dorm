grammar OQL;

options {
    superClass = 'com.example.dorm.query.parser.AbstractQueryParser';
}

@header {
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
}

file
    : statement EOF
    ;

statement
    : select_statement
    //| update_statement
    //| delete_statement
    ;

select_statement @init {this.setQuery(queryManager.create());}
    : select_clause from_clause { getQuery().from($from_clause.from); select( getQuery(), $select_clause.select); } (where_clause)?  // (groupby_clause)? (having_clause)? (orderby_clause)?
    ;

update_statement
    : update_clause (where_clause)?
    ;

delete_statement
    : delete_clause (where_clause)?
    ;

from_clause returns[From from]
    : 'FROM' identification_variable_declaration { $from = $identification_variable_declaration.from; }
    //(
    //    ',' (identification_variable_declaration | collection_member_declaration)
    //)*
    ;

identification_variable_declaration returns[From from]
    : range_variable_declaration  { $from = $range_variable_declaration.from; }// (join | fetch_join)*
    ;

range_variable_declaration returns[From from]
    : abstract_schema_name ('AS')? IDENTIFICATION_VARIABLE { $from = rememberSchema($abstract_schema_name.name, $IDENTIFICATION_VARIABLE.text); }
    ;

join
    : join_spec join_association_path_expression ('AS')? IDENTIFICATION_VARIABLE
    ;

fetch_join
    : join_spec 'FETCH' join_association_path_expression
    ;

join_spec
    : (('LEFT') ('OUTER')? | 'INNER')? 'JOIN'
    ;

join_association_path_expression
    : join_collection_valued_path_expression
    | join_single_valued_association_path_expression
    ;

join_collection_valued_path_expression
    : IDENTIFICATION_VARIABLE '.' collection_valued_association_field
    ;

join_single_valued_association_path_expression
    : IDENTIFICATION_VARIABLE '.' single_valued_association_field
    ;

collection_member_declaration
    : 'IN' '(' collection_valued_path_expression ')' ('AS')? IDENTIFICATION_VARIABLE
    ;

single_valued_path_expression
    : state_field_path_expression
    | single_valued_association_path_expression
    ;

state_field_path_expression
    : (IDENTIFICATION_VARIABLE | single_valued_association_path_expression) '.' state_field
    ;

single_valued_association_path_expression
    : IDENTIFICATION_VARIABLE '.' (single_valued_association_field '.')* single_valued_association_field
    ;

collection_valued_path_expression
    : IDENTIFICATION_VARIABLE '.' (single_valued_association_field '.')* collection_valued_association_field
    ;

state_field
    : (embedded_class_state_field '.')* simple_state_field
    ;

update_clause
    : 'UPDATE' abstract_schema_name (('AS')? IDENTIFICATION_VARIABLE)? 'SET' update_item (
        ',' update_item
    )*
    ;

update_item
    : (IDENTIFICATION_VARIABLE '.')? (state_field | single_valued_association_field) '=' new_value
    ;

new_value
    : simple_arithmetic_expression
    | string_primary
    | datetime_primary
    | boolean_primary
    | enum_primary
    | simple_entity_expression
    | 'NULL'
    ;

delete_clause
    : 'DELETE' 'FROM' abstract_schema_name (('AS')? IDENTIFICATION_VARIABLE)?
    ;

select_clause returns[List<SelectPath> select] @init{ $select = new ArrayList<SelectPath>();  }
    : 'SELECT' ('DISTINCT')? select_expression { $select.add($select_expression.select); } (',' select_expression {$select.add($select_expression.select);} )*
    ;

select_expression  returns[SelectPath select] @init{ $select = new SelectPath();  }
    : IDENTIFICATION_VARIABLE  { $select.add($IDENTIFICATION_VARIABLE.text); }  ('.' IDENTIFICATION_VARIABLE { $select.add($IDENTIFICATION_VARIABLE.text); } )*
    //: single_valued_path_expression
    //| aggregate_expression
    //| 'OBJECT' '(' IDENTIFICATION_VARIABLE ')'
    //| constructor_expression
    ;

constructor_expression
    : 'NEW' constructor_name '(' constructor_item (',' constructor_item)* ')'
    ;

constructor_item
    : single_valued_path_expression
    | aggregate_expression
    ;

aggregate_expression
    : ('AVG' | 'MAX' | 'MIN' | 'SUM') '(' ('DISTINCT')? state_field_path_expression ')'
    | 'COUNT' '(' ('DISTINCT')? (
        IDENTIFICATION_VARIABLE
        | state_field_path_expression
        | single_valued_association_path_expression
    ) ')'
    ;

where_clause
    : 'WHERE' conditional_expression { getQuery().where($conditional_expression.expression); }
    ;

groupby_clause
    : 'GROUP' 'BY' groupby_item (',' groupby_item)*
    ;

groupby_item
    : single_valued_path_expression
    | IDENTIFICATION_VARIABLE
    ;

having_clause
    : 'HAVING' conditional_expression
    ;

orderby_clause
    : 'ORDER' 'BY' orderby_item (',' orderby_item)*
    ;

orderby_item
    : state_field_path_expression ('ASC' | 'DESC')?
    ;

subquery
    : simple_select_clause subquery_from_clause (where_clause)? (groupby_clause)? (having_clause)?
    ;

subquery_from_clause
    : 'FROM' subselect_identification_variable_declaration (
        ',' subselect_identification_variable_declaration
    )*
    ;

subselect_identification_variable_declaration
    : identification_variable_declaration
    | association_path_expression ('AS')? IDENTIFICATION_VARIABLE
    | collection_member_declaration
    ;

association_path_expression
    : collection_valued_path_expression
    | single_valued_association_path_expression
    ;

simple_select_clause
    : 'SELECT' ('DISTINCT')? simple_select_expression
    ;

simple_select_expression
    : single_valued_path_expression
    | aggregate_expression
    | IDENTIFICATION_VARIABLE
    ;

conditional_expression returns[ObjectExpression expression]
    //: (conditional_term) ('OR' conditional_term)*
    : conditional_term {$expression = $conditional_term.expression; }
    ;

conditional_term  returns[ObjectExpression expression]
    //: (conditional_factor) ('AND' conditional_factor)*
    : conditional_factor  {$expression = $conditional_factor.expression; }
    ;

conditional_factor  returns[ObjectExpression expression]
    //: ('NOT')? conditional_primary
    : conditional_primary  {$expression = $conditional_primary.expression; }
    ;

conditional_primary  returns[ObjectExpression expression]
    : simple_cond_expression {$expression = $simple_cond_expression.expression; }
    //| '(' conditional_expression ')'
    ;

simple_cond_expression returns[ObjectExpression expression]
    : comparison_expression {$expression = $comparison_expression.expression; }
    //| like_expression
    //| in_expression
    //| null_comparison_expression
    //| empty_collection_comparison_expression
    //| collection_member_expression
    //| exists_expression
    ;

between_expression
    : arithmetic_expression ('NOT')? 'BETWEEN' arithmetic_expression 'AND' arithmetic_expression
    | string_expression ('NOT')? 'BETWEEN' string_expression 'AND' string_expression
    | datetime_expression ('NOT')? 'BETWEEN' datetime_expression 'AND' datetime_expression
    ;

in_expression
    : state_field_path_expression ('NOT')? 'IN' '(' (in_item (',' in_item)* | subquery) ')'
    ;

in_item
    : literal
    | input_parameter
    ;

like_expression
    : string_expression ('NOT')? 'LIKE' pattern_value ('ESCAPE' ESCAPE_CHARACTER)?
    ;

null_comparison_expression
    : (single_valued_path_expression | input_parameter) 'IS' ('NOT')? 'NULL'
    ;

empty_collection_comparison_expression
    : collection_valued_path_expression 'IS' ('NOT')? 'EMPTY'
    ;

collection_member_expression
    : entity_expression ('NOT')? 'MEMBER' ('OF')? collection_valued_path_expression
    ;

exists_expression
    : ('NOT')? 'EXISTS' '(' subquery ')'
    ;

all_or_any_expression
    : ('ALL' | 'ANY' | 'SOME') '(' subquery ')'
    ;

comparison_expression returns[ObjectExpression expression] // was path_expression
    : select_expression comparison_operator arithmetic_expression   {$expression = condition($select_expression.select, $comparison_operator.text, $arithmetic_expression.value); }
    //: string_expression comparison_operator (string_expression | all_or_any_expression) { this.query.eq(); }
    //| boolean_expression ('=' | '<>') (boolean_expression | all_or_any_expression)
    //| enum_expression ('=' | '<>') (enum_expression | all_or_any_expression)
    //| datetime_expression comparison_operator (datetime_expression | all_or_any_expression)
    //| entity_expression ('=' | '<>') (entity_expression | all_or_any_expression)
    //| arithmetic_expression comparison_operator (arithmetic_expression | all_or_any_expression)
    ;

//select_expression
path_expression  returns[ObjectPath path]
    : leg {$path = this.getFrom().get($leg.value); }
    ;

comparison_operator returns[String op]
    : '='  {$op = "=";}
    | '>'  {$op = ">";}
    | '>=' {$op = ">=";}
    | '<'  {$op = "<";}
    | '<=' {$op = "<=";}
    | '<>' {$op = "<>";}
    ;

arithmetic_expression returns[Value value]
    : simple_arithmetic_expression {$value = $simple_arithmetic_expression.value; }
    //| '(' subquery ')'
    ;

simple_arithmetic_expression returns[Value value]
    : (arithmetic_term) {$value = $arithmetic_term.value; } // (('+' | '-') arithmetic_term)*
    ;

arithmetic_term returns[Value value]
    : (arithmetic_factor) {$value = $arithmetic_factor.value; }// (('*' | '/') arithmetic_factor)*
    ;

arithmetic_factor returns[Value value]
    //: ('+' | '-')? arithmetic_primary
    : arithmetic_primary {$value = $arithmetic_primary.value; }
    ;

arithmetic_primary returns[Value value]
    : numeric_literal {$value = new Constant($numeric_literal.value);}
    //| state_field_path_expression
    //| '(' simple_arithmetic_expression ')'
    | input_parameter {$value = $input_parameter.value;}
    //| functions_returning_numerics
    //| aggregate_expression
    ;

string_expression
    : string_primary
    //| '(' subquery ')'
    ;

string_primary
    : state_field_path_expression
    | STRINGLITERAL
    | input_parameter
    //| functions_returning_strings
    //| aggregate_expression
    ;

datetime_expression
    : datetime_primary
    | '(' subquery ')'
    ;

datetime_primary
    : state_field_path_expression
    | input_parameter
    | functions_returning_datetime
    | aggregate_expression
    ;

boolean_expression
    : boolean_primary
    //| '(' subquery ')'
    ;

boolean_primary
    : state_field_path_expression
    | boolean_literal
    | input_parameter
    ;

enum_expression
    : enum_primary
    | '(' subquery ')'
    ;

enum_primary
    : state_field_path_expression
    | enum_literal
    | input_parameter
    ;

entity_expression
    : single_valued_association_path_expression
    | simple_entity_expression
    ;

simple_entity_expression
    : IDENTIFICATION_VARIABLE
    | input_parameter
    ;

functions_returning_numerics
    : 'LENGTH' '(' string_primary ')'
    | 'LOCATE' '(' string_primary ',' string_primary (',' simple_arithmetic_expression)? ')'
    | 'ABS' '(' simple_arithmetic_expression ')'
    | 'SQRT' '(' simple_arithmetic_expression ')'
    | 'MOD' '(' simple_arithmetic_expression ',' simple_arithmetic_expression ')'
    | 'SIZE' '(' collection_valued_path_expression ')'
    ;

functions_returning_datetime
    : 'CURRENT_DATE'
    | 'CURRENT_TIME'
    | 'CURRENT_TIMESTAMP'
    ;

functions_returning_strings
    : 'CONCAT' '(' string_primary ',' string_primary ')'
    | 'SUBSTRING' '(' string_primary ',' simple_arithmetic_expression ',' simple_arithmetic_expression ')'
    | 'TRIM' '(' ((trim_specification)? (TRIM_CHARACTER)? 'FROM')? string_primary ')'
    | 'LOWER' '(' string_primary ')'
    | 'UPPER' '(' string_primary ')'
    ;

trim_specification
    : 'LEADING'
    | 'TRAILING'
    | 'BOTH'
    ;

numeric_literal returns[int value]
    : NUMBER {$value = Integer.valueOf($NUMBER.text); }
    ;

pattern_value
    :
    ;

input_parameter returns[Parameter value]
    //: '?' INT_NUMERAL
    : ':' IDENTIFICATION_VARIABLE {$value = getQuery().parameter($IDENTIFICATION_VARIABLE.text, Object.class);}
    ;

literal
    :
    ;

constructor_name
    :
    ;

enum_literal
    :
    ;

boolean_literal
    : 'true'
    | 'false'
    ;

simple_state_field
    :
    ;

embedded_class_state_field
    :
    ;

single_valued_association_field
    :
    ;

collection_valued_association_field
    :
    ;

abstract_schema_name returns[String name]
    : IDENTIFICATION_VARIABLE { $name = $IDENTIFICATION_VARIABLE.text; }
    ;

leg returns[String value]
    : IDENTIFICATION_VARIABLE {$value = $IDENTIFICATION_VARIABLE.text; }
    ;

IDENTIFICATION_VARIABLE
    : ('a' .. 'z' | 'A' .. 'Z' | '_') ('a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_')*
    ;

NUMBER: ('0'..'9')+ ('.' ('1'..'9')+)?;

CHARACTER
    : '\'' (~ ('\'' | '\\')) '\''
    ;

STRINGLITERAL
    : ('\'' (~ ('\\' | '"'))* '\'')
    ;

ESCAPE_CHARACTER
    : CHARACTER
    ;

WS
    : [ \t\r\n] -> skip
    ;