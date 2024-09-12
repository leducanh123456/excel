package org.example.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.SqlResultSetMapping;

@SqlResultSetMapping(
        name = "UserRoleDtoMapping",
        classes = {
                @ConstructorResult(
                        targetClass = UserDto.class,
                        columns = {
                                @ColumnResult(name = "id", type = Long.class),
                                @ColumnResult(name = "username", type = String.class),
                                @ColumnResult(name = "password", type = String.class),
                                @ColumnResult(name = "role_id", type = Long.class)
                        }
                )
        }
)

@Data
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String username;
    private String password;
    private Long roleId;
}
