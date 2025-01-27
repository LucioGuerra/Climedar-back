package com.climedar.person_sv.repository;

import com.climedar.person_sv.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    @Query("SELECT m FROM Person m WHERE m.id = :id AND m.deleted = false")
    Optional<Person> findByIdAndNotDeleted(Long id);

    @Query("SELECT m FROM Person m WHERE m.dni = :dni")
    Optional<Person> findByDni(String dni);
}
