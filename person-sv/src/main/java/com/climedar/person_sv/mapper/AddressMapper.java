package com.climedar.person_sv.mapper;

import com.climedar.person_sv.dto.request.create.CreateAddressDTO;
import com.climedar.person_sv.dto.request.update.UpdateAddressDTO;
import com.climedar.person_sv.dto.response.GetAddressDTO;
import com.climedar.person_sv.entity.Address;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    Address toEntity(CreateAddressDTO createAddressDTO);

    void updateEntity(@MappingTarget Address address, UpdateAddressDTO updateAddressDTO);

    GetAddressDTO toDTO(Address address);
}
