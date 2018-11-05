package com.thelads.service;

import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.QueryResults;
import com.thelads.model.Unicorn;
import com.thelads.model.Unicorn.Builder;
import com.thelads.model.UnicornCreationRequest;
import com.thelads.publisher.UnicornPublisher;
import com.thelads.util.DatastoreQuery;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

        List<Entity> entityList = getAliveUnicornsWithLatestCoOrdinates();
        List<Unicorn> unicornList = new ArrayList<>();

        entityList.forEach(entity ->
            addUnicorn(unicornList, entity));

        return unicornList;
    }

    private List<Entity> getAliveUnicornsWithLatestCoOrdinates() {

        List<String> distinctNames = new ArrayList<>();
        List<Entity> entityList = new ArrayList<>();

        datastoreQuery = new DatastoreQuery();
        QueryResults<Entity> allTasks = datastoreQuery.listTasks();
        allTasks.forEachRemaining(entity ->
        {
            if (!distinctNames.contains(entity.getValue("name").get().toString())) {
                distinctNames.add(entity.getValue("name").get().toString());
                if (Boolean.valueOf(entity.getValue("alive").get().toString())) {
                    entityList.add(entity);
                }
            }
        });

        return entityList;
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

    public void removeUnicorn(String name) throws Exception {
        Optional<Entity> unicornToKill = getAliveUnicornsWithLatestCoOrdinates()
            .stream()
            .filter(entity -> entity.getValue("name").get().toString().equals(name))
            .findFirst();

        if (unicornToKill.isPresent()) {
            Optional<Unicorn> unicornByName = unicornPublisher.getUnicornByName(name);
            if (unicornByName.isPresent()) {
                Unicorn unicorn = unicornByName.get();
                unicorn.setAlive(false);
                unicornPublisher.getUnicorns().remove(unicorn);
                unicornPublisher.getPubSubMessagePublisher().publishMessage(unicorn);
            } else {
                // The DB contains a unicorn that's no longer available in memory
                Unicorn unicorn = new Builder()
                    .setName(name)
                    .setStatusTime(LocalDateTime.now().toString())
                    .build();
                unicorn.setAlive(false);
                unicornPublisher.getPubSubMessagePublisher().publishMessage(unicorn);
            }
        }
    }

    public List<String> getAliveUnicorns() {
        return getAliveUnicornsWithLatestCoOrdinates()
            .stream()
            .map(entity -> entity.getValue("name").get().toString())
            .collect(Collectors.toList());
    }
}
