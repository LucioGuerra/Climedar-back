package com.climedar.person_sv.service;

import com.climedar.person_sv.dto.request.create.CreateAddressDTO;
import com.climedar.person_sv.dto.request.update.UpdateAddressDTO;
import com.climedar.person_sv.entity.Address;
import com.climedar.person_sv.mapper.AddressMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class AddressService {
    private final AddressMapper addressMapper;

    public Address createAddress(CreateAddressDTO createAddressDTO) {
        return  addressMapper.toEntity(createAddressDTO);
    }

    public Address updateAddress(Address address, UpdateAddressDTO addressDTO) {
        addressMapper.updateEntity(address, addressDTO);
        return address;
    }
}
