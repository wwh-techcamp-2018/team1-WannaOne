CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `email` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `photo_url` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=innoDB;

CREATE TABLE `note_book` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `deleted` bit(1) NOT NULL DEFAULT b'0',
  `title` varchar(255) NOT NULL,
  `owner_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK2oy7sj4kvqpja2mkrxyxl0w9m` (`owner_id`)
) ENGINE=innoDB;

CREATE TABLE `note` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `register_datetime` datetime NOT NULL,
  `update_datetime` datetime DEFAULT NULL,
  `deleted` bit(1) NOT NULL DEFAULT b'0',
  `text` longtext,
  `title` varchar(255) DEFAULT NULL,
  `note_book_id` bigint(20) DEFAULT NULL,
  `writer_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKrvvxymn6mnvx141furq7gp6kf` (`note_book_id`),
  KEY `FKhfjy6wwn1qkaxe6cyow7ymecv` (`writer_id`)
) ENGINE=innoDB;

CREATE TABLE `comment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `register_datetime` datetime NOT NULL,
  `update_datetime` datetime DEFAULT NULL,
  `content` varchar(255) NOT NULL,
  `deleted` bit(1) NOT NULL DEFAULT b'0',
  `note_id` bigint(20) DEFAULT NULL,
  `writer_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK41heeawfghvw9jccau0d1tjox` (`note_id`),
  KEY `FKciieobgjeefmyp0mfyt1ehptd` (`writer_id`)
) ENGINE=innoDB;

CREATE TABLE `invitation` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `register_datetime` datetime NOT NULL,
  `update_datetime` datetime DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `guest_id` bigint(20) DEFAULT NULL,
  `host_id` bigint(20) DEFAULT NULL,
  `note_book_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKoadbyobtnkj8d0jk9u77icllj` (`guest_id`),
  KEY `FKfxhrhurpl67d7q6qmvl681gxj` (`host_id`),
  KEY `FKq2u2pi7qmdm6xlrwk0ux1ouhj` (`note_book_id`)
) ENGINE=innoDB;

CREATE TABLE `shared_note_book` (
  `note_book_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  KEY `FK1nsd5xlnculbpkqpymtohbvwh` (`user_id`),
  KEY `FKm1slcmjahsm6vegt18df7l74e` (`note_book_id`)
) ENGINE=innoDB;