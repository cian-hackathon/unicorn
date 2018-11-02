package com.thelads.service;

import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.QueryResults;
import com.thelads.model.Unicorn;
import com.thelads.model.UnicornCreationRequest;
import com.thelads.publisher.UnicornPublisher;
import com.thelads.util.DatastoreQuery;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UnicornService {

    @Autowired
    private UnicornPublisher unicornPublisher;

    private DatastoreQuery datastoreQuery;

    public void createUnicorn(UnicornCreationRequest request) {
        if (request.getLatitude() == null || request.getLongitude() == null) {
            unicornPublisher.addUnicorn(new Unicorn(request.getName()));
        } else {
            unicornPublisher.addUnicorn(new Unicorn(request.getName(),
                request.getLatitude(), request.getLongitude()));
        }
    }

    public List<Unicorn> getRecentUpdates() {

        List<String> aliveUnicorns = unicornPublisher.getUnicorns().stream().map(Unicorn::getName)
            .collect(Collectors.toList());

        datastoreQuery = new DatastoreQuery();
        QueryResults<Entity> allTasks = datastoreQuery.listTasks();

        List<Unicorn> unicornList = new ArrayList<>();
        List<Entity> entityList = new ArrayList<>();
        List<String> distinctNames = new ArrayList<>();


        allTasks.forEachRemaining(entity ->
        {
            if(!distinctNames.contains(entity.getValue("name").get().toString())){
                distinctNames.add(entity.getValue("name").get().toString());
                entityList.add(entity);
            }
            String name = entity.getValue("name").get().toString();
            if (aliveUnicorns.contains(name)) {
                addUnicorn(unicornList, entity);
            }
        });

        entityList.forEach(entity -> addUnicorn(unicornList, entity));

        return unicornList;
    }

    private void addUnicorn(List<Unicorn> unicornList, Entity entity) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

        unicornList.add(new Unicorn.Builder()
            .setName(entity.getValue("name").get().toString())
            .setDistance(Double.valueOf(entity.getValue("distance").get().toString()))
            .setLatitude(Double.valueOf(entity.getValue("latitude").get().toString()))
            .setLongitude(Double.valueOf(entity.getValue("longitude").get().toString()))
            .setStatusTime(
                LocalDateTime.parse(entity.getValue("statusTime").get().toString(), formatter).toString())
            .setHealthPoints(Integer.valueOf(entity.getValue("healthPoints").get().toString()))
            .setMagicPoints(Integer.valueOf(entity.getValue("magicPoints").get().toString())).build());
    }

    public List<Unicorn> moveUnicorns() {
        unicornPublisher.getUnicorns().forEach(Unicorn::move);
        return unicornPublisher.getUnicorns();
    }

    public void removeUnicorn(String name) {
        unicornPublisher
            .getUnicorns()
            .stream()
            .filter(unicorn -> unicorn.getName().equals(name))
            .findFirst()
            .ifPresent(unicorn -> unicornPublisher.getUnicorns().remove(unicorn));
    }
}
