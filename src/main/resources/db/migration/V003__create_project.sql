DROP TABLE IF EXISTS projects;
CREATE TABLE projects (
                          id serial primary key,
                          name varchar(100) not null,
                          status varchar(100) not null
)