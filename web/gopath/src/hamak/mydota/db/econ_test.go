package db

import (
	"database/sql"
	"github.com/stretchr/testify/assert"
	appTesting "hamak/mydota/testing"
	"testing"
)

func TestCanSaveEconItem(t *testing.T) {
	// Delete all econ items
	config := appTesting.NewConfig()
	appTesting.ClearDbTables(config)
	conn, err := NewConnection(config)
	assert.NotNil(t, conn)
	assert.Nil(t, err)

	conn.DB.Exec("DELETE FROM leagues;")
	conn.DB.Exec("DELETE FROM econ_items;")

	repo := NewEconRepo(conn)
	item := EconItem{
		Id:   33,
		Name: "Le item",
	}

	found := repo.FindById(item.Id)
	assert.Nil(t, found)

	assert.Nil(t, repo.Save(&item))

	found = repo.FindById(item.Id)
	assert.NotNil(t, found)
	assert.Equal(t, item.Id, found.Id)
	assert.Equal(t, item.Name, found.Name)
	assert.False(t, found.ImageUrl.Valid)

	found.ImageUrl = sql.NullString{"http://image_url", true}
	found.ImageUrlLarge = sql.NullString{"http://image_url_big", true}
	assert.Nil(t, repo.Save(found))

	found = repo.FindById(item.Id)
	assert.True(t, found.ImageUrl.Valid)
}

func TestCanSaveZeroDefIndex(t *testing.T) {
	config := appTesting.NewConfig()
	conn, _ := NewConnection(config)

	appTesting.ClearDbTables(config)

	repo := NewEconRepo(conn)
	item := EconItem{
		DefIndex: 0,
		Name:     "Riki's Dagger",
	}

	assert.Nil(t, repo.Save(&item))
	assert.NotEqual(t, 0, item.Id)

	sameItem := EconItem{
		DefIndex: 0,
		Name:     "Digger",
	}
	assert.Nil(t, repo.Save(&sameItem))
	assert.Equal(t, item.Id, sameItem.Id)
}
