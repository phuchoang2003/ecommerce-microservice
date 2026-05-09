package com.hdp.notification_service.infrastructure.adapter.inbound.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class NotificationController {

    @GetMapping("/pin")
    public String pin() throws Exception {
        int threadCount = 10;
        List<Thread> threads = new ArrayList<>();
        while(true){
            

        }
            // Create virtual threads that block while holding synchronized lock
        // BLOCKING while holding lock = PINNED

        // All threads should complete, but due to pinning they run sequentially on limited carrier threads
    }
}
