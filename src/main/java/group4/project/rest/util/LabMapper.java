package group4.project.rest.util;

import group4.project.rest.entity.UserProfileDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import group4.project.rest.security.entity.*;
import group4.project.rest.security.entity.UserDTO;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(imports = Collectors.class)
public interface LabMapper {
    LabMapper INSTANCE = Mappers.getMapper(LabMapper.class);

//    @Mapping(target = "authorities", expression = "java(organizer.getUser().getAuthorities().stream().map(auth -> auth.getName().name()).collect(Collectors.toList()))")

    UserDTO getRegisterDto(User user);
    @Mapping(target = "authorities", expression = "java(user.getAuthorities().stream().map(auth -> auth.getName().name()).collect(Collectors.toList()))")
    UserAuthDTO getUserAuthDTO(User user);

    UserProfileDTO getUserDTO(User user);
//    UserDTO getUserDTO(User user);
//    AuthorityDTO getAuthorityDTO(Authority authority);
//    List<UserAuthDTO> getUserAuthDTO(List<User> user);
//    UserDetailDTO getUserDetailDTO(User user);
//    List<UserDetailDTO> getUserDetailDTO(List<User> users);


}
