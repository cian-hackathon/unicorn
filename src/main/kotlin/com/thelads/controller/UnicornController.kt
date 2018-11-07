package com.thelads.controller

import com.thelads.model.Unicorn
import com.thelads.model.UnicornCreationRequest
import com.thelads.service.UnicornService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/unicorn")
class UnicornController {

    @Autowired
    private lateinit var unicornService: UnicornService

    @GetMapping("/recent")
    fun getUnicornUpdates(): List<Unicorn> {
        return unicornService.recentUpdates
    }

    @GetMapping("/alive")
    fun getAliveUnicorns(): List<String> {
        return unicornService.aliveUnicorns
    }

    @DeleteMapping("/remove")
    @ResponseStatus(HttpStatus.OK)
    fun removeUnicorn(@RequestParam name: String) {
        unicornService.removeUnicorn(name)
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createUnicorn(@RequestBody request: UnicornCreationRequest) {
        unicornService.createUnicorn(request)
    }
}
