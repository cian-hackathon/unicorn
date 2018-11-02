package com.thelads.controller;

import com.thelads.model.Unicorn;
import com.thelads.model.UnicornCreationRequest;
import com.thelads.service.UnicornService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/unicorn")
public class UnicornController {

    @Autowired
    private UnicornService unicornService;

    @GetMapping("/recent")
    List<Unicorn> getUnicornUpdates() {
        return unicornService.getRecentUpdates();
    }

    @DeleteMapping("/remove")
    @ResponseStatus(HttpStatus.OK)
    public void removeUnicorn(@RequestParam String name){
        unicornService.removeUnicorn(name);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createUnicorn(@RequestBody UnicornCreationRequest request) {
        unicornService.createUnicorn(request);
    }
}
