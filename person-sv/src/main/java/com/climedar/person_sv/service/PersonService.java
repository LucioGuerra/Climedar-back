package com.climedar.person_sv.service;

import com.climedar.person_sv.repository.PersonRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class PersonService {

    private final PersonRepository personRepository;


}
