CREATE TABLE IF NOT EXISTS users
(
    user_id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name    VARCHAR(255)                            NOT NULL,
    email   VARCHAR(512)                            NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (user_id),
    CONSTRAINT uq_email UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS items
(
    item_id      BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name         VARCHAR(255),
    description  VARCHAR(512),
    is_available BOOLEAN,
    owner_id     BIGINT                                  NOT NULL,
    item_request BIGINT,
    CONSTRAINT pk_item PRIMARY KEY (item_id)
);

CREATE TABLE IF NOT EXISTS bookings
(
    booking_id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    start_date TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    end_date   TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    status     SMALLINT,
    booker_id  BIGINT                                  NOT NULL,
    item_id    BIGINT                                  NOT NULL,
    CONSTRAINT pk_booking PRIMARY KEY (booking_id),
    CONSTRAINT fk_booker FOREIGN KEY (booker_id) REFERENCES users,
    CONSTRAINT fk_booking_item FOREIGN KEY (item_id) REFERENCES items
);

CREATE TABLE IF NOT EXISTS comments
(
    comment_id   BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    comment_text VARCHAR(255),
    item_id      BIGINT                                  NOT NULL,
    created      TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    author_id    BIGINT                                  NOT NULL,
    CONSTRAINT pk_comment PRIMARY KEY (comment_id),
    CONSTRAINT fk_item FOREIGN KEY (item_id) REFERENCES items,
    CONSTRAINT fk_user FOREIGN KEY (author_id) REFERENCES users
)


