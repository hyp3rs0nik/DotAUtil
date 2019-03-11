
-- +goose Up
-- SQL in section 'Up' is executed when this migration is applied
-- http://wiki.teamfortress.com/wiki/WebAPI/GetMatchDetails
CREATE TABLE matches(
  id BIGINT PRIMARY KEY,
  seq_num BIGINT NOT NULL,
  start_time TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  lobby_type SMALLINT NOT NULL,
  radiant_win SMALLINT NOT NULL,
  duration SMALLINT NOT NULL,
  tower_status_radiant INTEGER NOT NULL,
  tower_status_dire INTEGER NOT NULL,
  barracks_status_radiant SMALLINT NOT NULL,
  barracks_status_dire SMALLINT NOT NULL,
  cluster INTEGER NOT NULL,
  first_blood_time SMALLINT, 
  human_players SMALLINT NOT NULL,
  league_id BIGINT,
  positive_votes INTEGER NOT NULL,
  negative_votes INTEGER NOT NULL, 
  game_mode SMALLINT NOT NULL,
  radiant_captain BIGINT,
  dire_captain BIGINT
);

-- +goose Down
-- SQL section 'Down' is executed when this migration is rolled back
DROP TABLE matches;
