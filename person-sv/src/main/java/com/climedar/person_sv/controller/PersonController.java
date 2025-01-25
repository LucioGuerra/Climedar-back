package com.climedar.person_sv.controller;

import com.climedar.person_sv.service.PersonService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/person")
public class PersonController {

    private final PersonService personService;

}
