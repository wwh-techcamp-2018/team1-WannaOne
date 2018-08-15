package com.wannaone.woowanote.dto;

import com.wannaone.woowanote.domain.User;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 4, max = 30)
    private String password;

    @NotBlank
    @Size(max = 30)
    private String name;

    public UserDto(String email) {
        this.email = email;
        this.password = "12345";
        this.name = "name";
    }

    public User toEntity() {
        return new User(email, password, name);
    }

    public User toEntityWithPasswordEncode(PasswordEncoder bCryptPasswordEncoder) {
        return new User(email, bCryptPasswordEncoder.encode(password), name);
    }

    public UserDto setEmail(String email) {
        this.email = email;
        return this;
    }

    public UserDto setPassword(String password) {
        this.password = password;
        return this;
    }

    public UserDto setName(String name) {
        this.name = name;
        return this;
    }

    public static UserDto defaultUserDto() {
        return new UserDto("kyunam@woowahan.com", "1234", "kyunam");
    }
}
