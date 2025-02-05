package com.climedar.doctor_sv.repository.feign.fallback;

import com.climedar.doctor_sv.external.model.Gender;
import com.climedar.doctor_sv.external.model.Person;
import com.climedar.doctor_sv.repository.feign.PersonRepository;
import feign.FeignException;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
            public List<Person> findAllById(Set<Long> ids) {
                return List.of();
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
            public Page<Person> getAllPersons(Pageable pageable, String fullName, String name, String surname, String dni, Gender gender) {
                return new PageImpl<>(new ArrayList<>());
            }

            @Override
            public Page<Person> getAllPersonsByFullName(Pageable pageable, String fullName) {
                return new PageImpl<>(new ArrayList<>());
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
