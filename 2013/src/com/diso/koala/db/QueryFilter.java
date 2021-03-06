package com.diso.koala.db;

public class QueryFilter {
    private String field;
    private String value, value2;
    private FilterOption filterType = FilterOption.EQUALS;

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue2() {
        return value2;
    }

    public void setValue2(String value) {
        this.value2 = value;
    }

    public FilterOption getFilterType() {
        return filterType;
    }

    public void setFilterType(FilterOption filterType) {
        this.filterType = filterType;
    }

    public String GetFilter(){
        if (filterType == FilterOption.EQUALS){
            return String.format( "%s = '%s'", field, value );
        }
        else if (filterType == FilterOption.IN){
            return String.format( "%s IN (%s)", field, value );
        }
        else{
            return String.format( "%s BETWEEN '%s' AND '%s'", field, value, value2 );
        }
    }

    public enum FilterOption{
        EQUALS, IN, BETWEEN
    }
}
