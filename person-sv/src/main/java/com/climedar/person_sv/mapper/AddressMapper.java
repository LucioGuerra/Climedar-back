package com.climedar.person_sv.mapper;

import com.climedar.person_sv.dto.request.AddressDTO;
import com.climedar.person_sv.dto.response.GetAddressDTO;
import com.climedar.person_sv.entity.Address;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    Address toEntity(AddressDTO addressDTO);

    GetAddressDTO toDTO(Address address);
}
