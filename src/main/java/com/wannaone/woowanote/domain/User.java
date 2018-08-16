package com.wannaone.woowanote.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wannaone.woowanote.dto.LoginDto;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@EqualsAndHashCode
@ToString
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

    private String photoUrl = "http://mblogthumb2.phinf.naver.net/20150427_261/ninevincent_1430122791768m7oO1_JPEG/kakao_1.jpg?type=w2";

    @OneToMany(mappedBy = "owner")
    private List<NoteBook> noteBooks = new ArrayList<>();

    public User(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public boolean matchPassword(LoginDto loginDto, PasswordEncoder bCryptPasswordEncoder) {
        return bCryptPasswordEncoder.matches(loginDto.getPassword(), this.password);
    }
}
