package com.climedar.person_sv.specification;

import com.climedar.person_sv.dto.request.AddressDTO;
import com.climedar.person_sv.entity.Address;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;

public class AddressSpecification {
    public static Specification<Address> filterBy(AddressDTO dto) {
        return (root, query, cb) -> {
            var predicates = new ArrayList<Predicate>();

            predicates.add(cb.equal(root.get("street"), dto.street()));
            predicates.add(cb.equal(root.get("number"), dto.number()));
            predicates.add(cb.equal(root.get("city"), dto.city()));
            predicates.add(cb.equal(root.get("province"), dto.province()));
            predicates.add(cb.equal(root.get("postalCode"), dto.postalCode()));

            if (dto.floor() != null) {
                predicates.add(cb.equal(root.get("floor"), dto.floor()));
            }
            if (dto.apartment() != null) {
                predicates.add(cb.equal(root.get("apartment"), dto.apartment()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
