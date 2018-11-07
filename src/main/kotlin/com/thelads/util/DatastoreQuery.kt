package com.thelads.util

import com.google.cloud.datastore.DatastoreOptions
import com.google.cloud.datastore.Entity
import com.google.cloud.datastore.Query
import com.google.cloud.datastore.Query.ResultType
import com.google.cloud.datastore.QueryResults

class DatastoreQuery {

    private val datastore = DatastoreOptions.getDefaultInstance().service

    fun listTasks(): QueryResults<Entity> {
        val qry = Query.newGqlQueryBuilder(ResultType.ENTITY, TASK_GQL_QUERY).build()
        return datastore.run(qry)
    }

    companion object {
        private const val TASK_GQL_QUERY = "select * from Task ORDER BY statusTime DESC"
    }
}
