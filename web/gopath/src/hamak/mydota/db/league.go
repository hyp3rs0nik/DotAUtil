package db

import (
	"database/sql"
	"errors"
)

type League struct {
	Id            int64
	Name          string
	Description   string
	TournamentUrl string
	ItemDef       sql.NullInt64
	Item          EconItem
}

type LeagueRepo struct {
	connection *Connection
}

func NewLeagueRepo(connection *Connection) LeagueRepo {
	repo := LeagueRepo{}
	repo.connection = connection
	return repo
}

func (self LeagueRepo) FindById(id int64) *League {
	league := League{}
	result := self.connection.DB.First(&league, id)
	if result.Error != nil || league.Id == 0 {
		return nil
	} else {
		return &league
	}
}

func (self LeagueRepo) Save(league *League) error {
	if league.Id == 0 {
		return errors.New("Leagues must have ids.")
	}

	// Not immediately using DB.Save() because that method only checks if
	// Id == 0 but we have ids that are not really from the db, but from Steam.
	// So we'll have to find the record ourselves.
	existing := self.FindById(league.Id)
	var err error = nil
	if existing == nil {
		err = self.connection.DB.Create(league).Error
	} else {
		err = self.connection.DB.Save(league).Error
	}
	return err
}

func (self LeagueRepo) FindAll() []League {
	leagues := []League{}

	self.connection.DB.Find(&leagues)

	econRepo := NewEconRepo(self.connection)

	for i := range leagues {
		league := &leagues[i]
		if league.ItemDef.Valid {
			if econItem := econRepo.FindByDefIndex(int32(league.ItemDef.Int64)); econItem != nil {
				league.Item = *econItem
			}
		}
	}

	return leagues
}
