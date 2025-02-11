package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.model.hubs.HubEvent;
import ru.yandex.practicum.service.HubService;

@RestController
@RequestMapping("/events")
public class HubController {
    private final HubService hubService;

    @Autowired
    public HubController(HubService hubService) {
        this.hubService = hubService;
    }

    @PostMapping("/hubs")
    public void collectHubEvent(@Valid @RequestBody HubEvent event) {
        hubService.processHubEvent(event);
    }
}