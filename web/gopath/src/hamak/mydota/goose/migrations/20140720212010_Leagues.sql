
-- +goose Up
-- SQL in section 'Up' is executed when this migration is applied
CREATE TABLE leagues(
  id BIGINT PRIMARY KEY NOT NULL, 
  name VARCHAR(255) NOT NULL,
  description TEXT,
  tournament_url VARCHAR(255),
  item_def INTEGER
);


-- +goose Down
-- SQL section 'Down' is executed when this migration is rolled back
DROP TABLE leagues;

