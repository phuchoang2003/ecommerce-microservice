package com.hdp.core.request;

public enum FilterOperator {
    EQ,       // equals
    NEQ,      // not equals
    LIKE,     // contains (wrapped with %)
    GT,       // greater than
    GTE,      // greater than or equal
    LT,       // less than
    LTE,      // less than or equal
    IN,       // in list
    BETWEEN,  // between two values
    IS_NULL,
    IS_NOT_NULL
}