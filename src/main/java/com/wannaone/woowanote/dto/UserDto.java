package com.wannaone.woowanote.dto;

import com.wannaone.woowanote.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    @NotBlank(message = "메일을 작성해주세요.")
    @Email(message = "메일의 양식을 지켜주세요.")
    private String email;

    @NotBlank(message = "비밀번호를 입력하세요")
    @Size(min = 4, max = 30, message = "비밀번호는 4자리 이상, 30자 이하이어야 합니다.")
    private String password;

    @NotBlank(message = "이름을 입력하세요")
    @Size(max = 30, message = "이름은 30자 이하이어야 합니다.")
    private String name;

    public UserDto(String email) {
        this.email = email;
        this.password = "1234";
        this.name = "name";
    }

    public User toEntity() {
        return new User(email, password, name);
    }

    public static UserDto defaultUserDto() {
        return new UserDto("kyunam@woowahan.com", "1234", "kyunam");
    }
}
