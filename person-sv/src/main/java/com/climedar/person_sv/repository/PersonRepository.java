package com.climedar.person_sv.repository;

import com.climedar.person_sv.entity.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long>, JpaSpecificationExecutor<Person> {

    @Query("SELECT m FROM Person m WHERE m.id = :id AND m.deleted = false")
    Optional<Person> findByIdAndNotDeleted(Long id);

    @Query("SELECT m FROM Person m WHERE m.dni = :dni")
    Optional<Person> findByDni(String dni);

    List<Person> findAllByIdAndDeleted(Long id, boolean deleted);

    @Query("SELECT m FROM Person m WHERE m.id IN :ids AND m.deleted = false")
    List<Person> findAllByIdAndNotDeleted(Set<Long> ids);

    @Query("SELECT m FROM Person m WHERE m.deleted = false AND " +
            "LOWER(CONCAT(m.surname, ' ', m.name)) LIKE LOWER(CONCAT('%', :fullName, '%')) ")
    Page<Person> findAllByFullNameAndNotDeleted(@Param("fullName") String fullName, Pageable pageable);

    @Query("SELECT m FROM Person m WHERE m.deleted = false AND " +
            "(LOWER(m.surname) LIKE LOWER(CONCAT('%', :surname, '%')) " +
            "and LOWER(m.name) LIKE LOWER(CONCAT('%', :name, '%')))")
    Page<Person> findAllByNamesAndNotDeleted(@Param("surname") String surname, @Param("name") String name,
                                             Pageable pageable);
}
