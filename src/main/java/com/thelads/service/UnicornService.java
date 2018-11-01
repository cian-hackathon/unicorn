package com.thelads.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.QueryResults;
import com.thelads.model.Unicorn;
import com.thelads.util.DatastoreQuery;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class UnicornService {

    DatastoreQuery datastoreQuery;

    public List<Unicorn> getRecentUpdates() {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

        datastoreQuery = new DatastoreQuery();
        QueryResults<Entity> allTasks = datastoreQuery.listTasks();
        List<Unicorn> unicornList = new ArrayList<>();
        allTasks.forEachRemaining(entity ->
        {
            unicornList.add(new Unicorn.Builder()
                .setName(entity.getValue("name").get().toString())
                .setDistance(Double.valueOf(entity.getValue("distance").get().toString()))
                .setLatitude(Double.valueOf(entity.getValue("latitude").get().toString()))
                .setLongitude(Double.valueOf(entity.getValue("longitude").get().toString()))
                .setStatusTime(LocalDateTime.parse(entity.getValue("statusTime").get().toString(), formatter).toString())
                .setHealthPoints(Integer.valueOf(entity.getValue("healthPoints").get().toString()))
                .setMagicPoints(Integer.valueOf(entity.getValue("magicPoints").get().toString())).build());
        });
        return unicornList;
    }
}
