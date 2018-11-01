package com.thelads.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.QueryResults;
import com.thelads.model.Unicorn;
import com.thelads.util.DatastoreQuery;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class UnicornService {

    DatastoreQuery datastoreQuery;

    public List<Unicorn> getRecentUpdates() {
        ObjectMapper mapper = new ObjectMapper();

        datastoreQuery = new DatastoreQuery();
        QueryResults<Entity> allTasks = datastoreQuery.listTasks();
        List<Unicorn> unicornList = new ArrayList<>();
        allTasks.forEachRemaining(entity ->
        {
            unicornList.add(new Unicorn.Builder()
                .setName(entity.getValue("name").toString())
                .setDistance(Double.valueOf(entity.getValue("distance").toString()))
                .setLatitude(Double.valueOf(entity.getValue("latitude").toString()))
                .setLongitude(Double.valueOf(entity.getValue("longitude").toString()))
                .setStatusTime(LocalDateTime.now().toString())
                .setHealthPoints(Integer.valueOf(entity.getValue("healthPoints").toString()))
                .setMagicPoints(Integer.valueOf(entity.getValue("magicPoints").toString())).build());
        });
        return unicornList;
    }
}
