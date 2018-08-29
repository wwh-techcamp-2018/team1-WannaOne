create table comment (
    id bigint not null auto_increment,
    register_datetime datetime not null,
    update_datetime datetime,
    content varchar(255) not null,
    deleted bit default false not null,
    note_id bigint,
    writer_id bigint,
    primary key (id)
) ENGINE=InnoDB;

create table invitation (
    id bigint not null auto_increment,
    register_datetime datetime not null,
    update_datetime datetime,
    status varchar(255),
    guest_id bigint,
    host_id bigint,
    note_book_id bigint,
    primary key (id)
) ENGINE=InnoDB;

create table note (
    id bigint not null auto_increment,
    register_datetime datetime not null,
    update_datetime datetime,
    deleted bit default false not null,
    text LONGTEXT,
    title varchar(255),
    note_book_id bigint,
    writer_id bigint,
    primary key (id)
) ENGINE=InnoDB;


create table note_book (
    id bigint not null auto_increment,
    deleted bit default false not null,
    title varchar(255) not null,
    owner_id bigint,
    primary key (id)
) ENGINE=InnoDB;


create table shared_note_book (
    note_book_id bigint not null,
    user_id bigint not null
) ENGINE=InnoDB;

create table user (
    id bigint not null auto_increment,
    email varchar(255) not null,
    name varchar(255) not null,
    password varchar(255) not null,
    photo_url varchar(255),
    primary key (id)
) ENGINE=InnoDB;

alter table comment
    add constraint FK41heeawfghvw9jccau0d1tjox
    foreign key (note_id)
    references note (id)

alter table comment
    add constraint FKciieobgjeefmyp0mfyt1ehptd
    foreign key (writer_id)
    references user (id)

alter table invitation
       add constraint FKoadbyobtnkj8d0jk9u77icllj
       foreign key (guest_id)
       references user (id)


alter table invitation
    add constraint FKfxhrhurpl67d7q6qmvl681gxj
    foreign key (host_id)
    references user (id);


alter table invitation
    add constraint FKq2u2pi7qmdm6xlrwk0ux1ouhj
    foreign key (note_book_id)
    references note_book (id);


alter table note
    add constraint FKrvvxymn6mnvx141furq7gp6kf
    foreign key (note_book_id)
    references note_book (id);

alter table note
    add constraint FKhfjy6wwn1qkaxe6cyow7ymecv
    foreign key (writer_id)
    references user (id);

alter table note_book
    add constraint FK2oy7sj4kvqpja2mkrxyxl0w9m
    foreign key (owner_id)
    references user (id);

alter table shared_note_book
    add constraint FK1nsd5xlnculbpkqpymtohbvwh
    foreign key (user_id)
    references user (id);

alter table shared_note_book
    add constraint FKm1slcmjahsm6vegt18df7l74e
    foreign key (note_book_id)
    references note_book (id);