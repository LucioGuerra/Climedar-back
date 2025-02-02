package com.climedar.person_sv.mapper;


import com.climedar.person_sv.dto.request.create.CreatePersonDTO;
import com.climedar.person_sv.dto.request.update.UpdatePersonDTO;
import com.climedar.person_sv.dto.response.GetPersonDTO;
import com.climedar.person_sv.entity.Person;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring", uses = AddressMapper.class)
public interface PersonMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "address", ignore = true)
    Person toEntity(CreatePersonDTO personDTO);

    @Mapping(target = "personId", source = "id")
    GetPersonDTO toDTO(Person person);

    @Mapping(target = "address", ignore = true)
    void updateEntity(@MappingTarget Person person, UpdatePersonDTO personDTO);
}
