drop table messages;

drop table threads;

drop table topics;

create table topics (
    topicID INT NOT NULL
            PRIMARY KEY GENERATED ALWAYS AS IDENTITY
                (START WITH 1, INCREMENT BY 1),
    title VARCHAR(40) NOT NULL,
    subject VARCHAR(160));

create table threads (
    threadID INT NOT NULL
            PRIMARY KEY GENERATED ALWAYS AS IDENTITY
                (START WITH 1, INCREMENT BY 1),
    topicID INT NOT NULL,
    title VARCHAR(40) NOT NULL,
    FOREIGN KEY (topicID) REFERENCES topics);

create table messages (
    messageID INT NOT NULL
            PRIMARY KEY GENERATED ALWAYS AS IDENTITY
                (START WITH 1, INCREMENT BY 1),
    threadID INT NOT NULL,
    subject VARCHAR(80),
    text VARCHAR(10000),
    creation_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    FOREIGN KEY (threadID) REFERENCES threads);