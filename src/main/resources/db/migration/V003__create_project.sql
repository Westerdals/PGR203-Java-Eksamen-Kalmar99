DROP TABLE IF EXISTS projects;
CREATE TABLE projects (
                          id serial primary key,
                          name varchar(100) not null,
                          status int DEFAULT 1
)
