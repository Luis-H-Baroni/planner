CREATE TABLE trips (
    id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    destination varchar(255) NOT NULL,
    starts_at TIMESTAMP NOT NULL,
    ends_at TIMESTAMP NOT NULL,
    is_confirmed BOOLEAN NOT NULL,
    owner_name varchar(255) NOT NULL,
    owner_email varchar(100) NOT NULL
);

