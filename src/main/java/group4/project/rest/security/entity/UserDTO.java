package group4.project.rest.security.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    Long id;
    String username;
//    String password;
    Double balance;
    Integer coin;
    String email;
    Boolean enabled;
    Date lastPasswordResetDate;

}
