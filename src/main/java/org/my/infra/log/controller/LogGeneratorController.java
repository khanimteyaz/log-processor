package org.my.infra.log.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;

@RestController
@RequestMapping("/log")
public class LogGeneratorController {

    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(value = "/error", method = RequestMethod.POST)
    public ResponseEntity<String> generateError(@RequestParam("type") int type) {
        if (type == 1) {
            try {
                throw new IllegalArgumentException("Invalid input");
            } catch (Exception ex) {
                LOGGER.error("Error", ex);
            }
        } else if (type == 2) {
            throw new NullPointerException("Jff");
        } else if (type == 3) {
            throw new RuntimeException("Unknown exception");
        }

        return ResponseEntity.ok("ok");
    }

    @RequestMapping(value = "/info", method = RequestMethod.POST)
    public ResponseEntity<String> generateInfo() {
        return ResponseEntity.ok("ok");
    }
}
