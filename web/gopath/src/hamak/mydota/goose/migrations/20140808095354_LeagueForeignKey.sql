
-- +goose Up
-- SQL in section 'Up' is executed when this migration is applied
ALTER TABLE leagues ADD CONSTRAINT fk_leagues_item_def FOREIGN KEY (item_def)
  REFERENCES econ_items(def_index) ON DELETE RESTRICT;


-- +goose Down
-- SQL section 'Down' is executed when this migration is rolled back
ALTER TABLE leagues DROP CONSTRAINT fk_leagues_item_def;

