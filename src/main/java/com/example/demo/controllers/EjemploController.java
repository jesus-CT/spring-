package com.example.demo.controllers;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class EjemploController {

    @GetMapping(path = "/ejemplo")
    public Map<String,Object> ejemplo(){


        Map<String,Object> map = new HashMap<>();
        map.put("mapa","mapa2");


        return map;
    }
}
