package com.thelads.controller;

import com.thelads.model.Unicorn;
import com.thelads.service.UnicornService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/unicorn")
public class UnicornController {

    @Autowired
    UnicornService unicornService;

    @GetMapping("/recent")
    List<Unicorn> getUnicornUpdates() {
        return unicornService.getRecentUpdates();
    }
}
