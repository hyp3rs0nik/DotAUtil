
-- +goose Up
-- SQL in section 'Up' is executed when this migration is applied
CREATE TABLE match_players(
  id BIGSERIAL PRIMARY KEY NOT NULL, -- surrogate key
  match_id BIGINT NOT NULL,
  slot SMALLINT NOT NULL,
  hero_id SMALLINT NOT NULL,
  user_id BIGINT, -- NULL means it is an anonymous user
  item_ids SMALLINT[],
  kills SMALLINT NOT NULL,
  deaths SMALLINT NOT NULL,
  assists SMALLINT NOT NULL,
  leaver_status SMALLINT,
  gold INTEGER NOT NULL,
  last_hits SMALLINT NOT NULL,
  denies SMALLINT NOT NULL,
  gold_per_min SMALLINT NOT NULL,
  xp_per_min SMALLINT NOT NULL,
  gold_spent INTEGER NOT NULL,
  hero_damage INTEGER NOT NULL,
  tower_damage INTEGER NOT NULL,
  hero_healing INTEGER NOT NULL,
  level SMALLINT NOT NULL,
  UNIQUE (match_id, slot)
);

CREATE TABLE match_player_ability_upgrades(
  player_id BIGINT NOT NULL REFERENCES match_players(id) ON DELETE CASCADE,
  ability SMALLINT NOT NULL,
  time SMALLINT NOT NULL,
  level SMALLINT NOT NULL
);

-- +goose Down
-- SQL section 'Down' is executed when this migration is rolled back
DROP TABLE match_player_ability_upgrades;
DROP TABLE match_players;

