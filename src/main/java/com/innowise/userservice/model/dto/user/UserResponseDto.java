package com.innowise.userservice.model.dto.user;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class UserResponseDto extends BaseUserDto {
    private Long id;
    private boolean active;

}
