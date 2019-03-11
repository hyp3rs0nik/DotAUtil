
-- +goose Up
-- SQL in section 'Up' is executed when this migration is applied
ALTER TABLE users 
  ALTER last_logoff DROP NOT NULL,
  ALTER time_created DROP NOT NULL;

-- +goose Down
-- SQL section 'Down' is executed when this migration is rolled back
ALTER TABLE users 
  ALTER last_logoff SET NOT NULL,
  ALTER time_created SET NOT NULL;

