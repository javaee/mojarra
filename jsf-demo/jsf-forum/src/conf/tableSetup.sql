drop table messages;

drop table threads;

drop table topics;

create table topics (
    topicID INT NOT NULL,
    title VARCHAR(40) NOT NULL,
    subject VARCHAR(160),
    PRIMARY KEY (topicID));

create table threads (
    threadID INT NOT NULL,
    topicID INT NOT NULL,
    title VARCHAR(40) NOT NULL,
    PRIMARY KEY (threadID),
    FOREIGN KEY (topicID) REFERENCES topics);

create table messages (
    messageID INT NOT NULL,
    threadID INT NOT NULL,
    subject VARCHAR(80),
    text VARCHAR(10000),
    PRIMARY KEY (messageID),
    FOREIGN KEY (threadID) REFERENCES threads);