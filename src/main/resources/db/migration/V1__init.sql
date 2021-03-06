create table category(
    category_id        bigint generated by default as identity,
    created_date       timestamp,
    modified_date      timestamp,
    depth              bigint       not null,
    left_node          bigint       not null,
    right_node         bigint       not null,
    is_deleted         boolean      not null,
    name               varchar(255) not null,
    parent_category_id bigint,
    root_category_id   bigint,
    primary key (category_id)
)