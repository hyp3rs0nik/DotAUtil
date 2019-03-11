package db

import (
	"database/sql"
	"github.com/stretchr/testify/assert"
	appTesting "hamak/mydota/testing"
	"testing"
)

func TestCanSaveLeague(t *testing.T) {
	// Delete all leagues
	config := appTesting.NewConfig()
	conn, err := NewConnection(config)
	assert.NotNil(t, conn)
	assert.Nil(t, err)

	appTesting.ClearDbTables(config)

	repo := NewLeagueRepo(conn)
	league := League{
		Id:            331,
		Name:          "Duota 2 International",
		Description:   "Dutoanay dabest",
		TournamentUrl: "http://dota2.com",
		ItemDef:       sql.NullInt64{0, false},
	}

	found := repo.FindById(league.Id)
	assert.Nil(t, found)

	assert.Nil(t, repo.Save(&league))

	found = repo.FindById(league.Id)
	assert.NotNil(t, found)
	assert.Equal(t, league.Name, found.Name)
	assert.Equal(t, league.Description, found.Description)
	assert.Equal(t, league.ItemDef, found.ItemDef)
}
