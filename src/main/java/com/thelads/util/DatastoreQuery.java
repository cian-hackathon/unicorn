package com.thelads.util;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.GqlQuery;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.Query.ResultType;
import com.google.cloud.datastore.QueryResults;

public class DatastoreQuery {

    private final Datastore datastore = DatastoreOptions.getDefaultInstance().getService();

    private static final String TASK_GQL_QUERY = "select * from Task ORDER BY statusTime DESC";

    public QueryResults<Entity> listTasks() {
        GqlQuery<Entity> qry = Query.newGqlQueryBuilder(ResultType.ENTITY, TASK_GQL_QUERY).build();
        return datastore.run(qry);
    }
}
