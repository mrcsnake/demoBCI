package com.example.demo.mapper;

import com.example.demo.dto.PhoneDTO;
import com.example.demo.dto.RegisterDTO;
import com.example.demo.model.Telefono;
import com.example.demo.model.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import java.util.List;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    UsuarioMapper INSTANCE = Mappers.getMapper(UsuarioMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "created", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "lastLogin", ignore = true)
    @Mapping(target = "token", ignore = true)
    @Mapping(target = "active", constant = "true")
    @Mapping(target = "phones", source = "phones")
    Usuario registerDTOToUsuario(RegisterDTO registerDTO);

    @Mapping(target = "usuario", ignore = true)
    Telefono phoneDTOToTelefono(PhoneDTO phone);

    List<Telefono> phonesToTelefonos(List<PhoneDTO> phones);
}
