package com.wannaone.woowanote.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Invitation {
    @Id
    private Long id;

    @ManyToOne
    private User host;

    @ManyToOne
    private User guest;

    @ManyToOne
    private NoteBook noteBook;
}
