
-- +goose Up
-- SQL in section 'Up' is executed when this migration is applied
CREATE TABLE users(
  id BIGINT PRIMARY KEY, -- Saved as 32bit Steam Id
  community_visibility_state SMALLINT NOT NULL,
  profile_state SMALLINT NOT NULL, 
  persona_name VARCHAR(255) NOT NULL,
  last_logoff TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  profile_url VARCHAR(255) NOT NULL,
  avatar_url VARCHAR(255),
  persona_state SMALLINT NOT NULL,
  primary_clan_id BIGINT,
  time_created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  persona_state_flags SMALLINT NOT NULL
);

-- +goose Down
-- SQL section 'Down' is executed when this migration is rolled back
DROP TABLE users;
