package com.climedar.person_sv.service;

import com.climedar.person_sv.dto.request.AddressDTO;
import com.climedar.person_sv.entity.Address;
import com.climedar.person_sv.mapper.AddressMapper;
import com.climedar.person_sv.repository.AddressRepository;
import com.climedar.person_sv.specification.AddressSpecification;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class AddressService {
    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;

    public Address asigneeAddress(AddressDTO addressDTO) {
        Optional<Address> address =
                addressRepository.findOne(AddressSpecification.filterBy(addressDTO));

        return address.orElseGet(() -> addressMapper.toEntity(addressDTO));
    }
}
