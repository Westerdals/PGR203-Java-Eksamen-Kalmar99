DROP TABLE IF EXISTS project_member;
CREATE TABLE project_member (
            id serial primary key,
            projectName VARCHAR(100),
            memberName VARCHAR(100)
)