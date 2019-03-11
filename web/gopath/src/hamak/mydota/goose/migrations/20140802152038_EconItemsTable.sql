
-- +goose Up
-- SQL in section 'Up' is executed when this migration is applied
CREATE TABLE econ_items(
  id BIGSERIAL PRIMARY KEY NOT NULL,
  def_index INTEGER NOT NULL UNIQUE,
  name VARCHAR(255) NOT NULL,
  image_url VARCHAR(255),
  image_url_large VARCHAR(255)
);


-- +goose Down
-- SQL section 'Down' is executed when this migration is rolled back
DROP TABLE econ_items;
