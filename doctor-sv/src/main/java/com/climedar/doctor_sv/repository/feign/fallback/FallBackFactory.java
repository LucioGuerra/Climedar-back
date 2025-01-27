package com.climedar.doctor_sv.repository.feign.fallback;

import com.climedar.doctor_sv.external.model.Person;
import com.climedar.doctor_sv.repository.feign.PersonRepository;
import feign.FeignException;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class FallBackFactory  implements FallbackFactory<PersonRepository> {

    @Override
    public PersonRepository create(Throwable cause) {
        return new PersonRepository() {
            @Override
            public Person findById(Long id) {
                return null;
            }

            @Override
            public Optional<Person> findByDni(String dni) {
                if (cause instanceof FeignException.NotFound) {
                    return Optional.empty();
                }
                if (cause instanceof RuntimeException) {
                    throw (RuntimeException) cause;
                }
                throw new RuntimeException("Unhandled exception in Feign client", cause);
            }

            @Override
            public Person createPerson(Person person) {
                return null;
            }

            @Override
            public Person updatePerson(Long id, Person person) {
                return null;
            }
        };
    }
}
