package com.climedar.person_sv.specification;

import com.climedar.person_sv.entity.Gender;
import com.climedar.person_sv.entity.Person;
import org.springframework.data.jpa.domain.Specification;

public class PersonSpecification {

    public static Specification<Person> nameLike(String name) {
        return (root, query, cb) -> name == null? cb.conjunction() : cb.like(root.get("name"), "%" + name + "%");
    }

    public static Specification<Person> surnameLike(String surname) {
        return (root, query, cb) -> surname == null? cb.conjunction() : cb.like(root.get("surname"), "%" + surname + "%");
    }

    public static Specification<Person> dniLike(String dni) {
        return (root, query, cb) -> dni == null? cb.conjunction() : cb.like(root.get("dni"), "%" + dni + "%");
    }

    public static Specification<Person> genderEqual(Gender gender){
        return (root, query, cb) -> gender == null? cb.conjunction() : cb.equal(root.get("gender"), gender);
    }

    public static Specification<Person> deletedEqual(Boolean deleted){
        return (root, query, cb) -> deleted == null? cb.conjunction() : cb.equal(root.get("deleted"), deleted);
    }
}
