package com.diso.koala.db;

import java.util.ArrayList;

public class QueryBuilder {

    /**
     * Get a String with the query filter
     * @param queryFilter Array with the query filter
     * @return String with the query filter
     */
    public static String GetQueryFilter( ArrayList<QueryFilter> queryFilter ){
        StringBuilder sbQuery = new StringBuilder();
        int position = 0;
        for (QueryFilter filter : queryFilter){
            if (position == 0){ sbQuery.append( filter.GetFilter() ); position++; }
            else { sbQuery.append( " AND " + filter.GetFilter() ); }
        }

        return sbQuery.toString();
    }
}
