package org.my.infra.log.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;

@RestController
@RequestMapping("/log")
public class LogGeneratorController {

    private Logger LOGGER=LoggerFactory.getLogger(this.getClass());

    @RequestMapping(value = "/error", method = RequestMethod.POST)
    public ResponseEntity<String> generateError() {
        try
        {
            throw new IllegalArgumentException("Invalid input");
        } catch(Exception ex) {
            LOGGER.error("Error",ex);
        }
        return ResponseEntity.ok("ok");
    }

    @RequestMapping(value = "/info", method = RequestMethod.POST)
    public ResponseEntity<String> generateInfo() {
        return ResponseEntity.ok("ok");
    }
}
