package com.wannaone.woowanote.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wannaone.woowanote.dto.LoginDto;
import com.wannaone.woowanote.dto.UserDto;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Table(name = "useruser")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    @JsonIgnore
    private String password;

    @Column(nullable = false)
    private String name;

    private String imgUrl = "http://mblogthumb2.phinf.naver.net/20150427_261/ninevincent_1430122791768m7oO1_JPEG/kakao_1.jpg?type=w2";

    public boolean matchPassword(LoginDto loginDto, PasswordEncoder bCryptPasswordEncoder) {
        return bCryptPasswordEncoder.matches(loginDto.getPassword(), this.password);
    }

    public User(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }
}
