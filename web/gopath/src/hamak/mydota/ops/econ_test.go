package ops

import (
	"github.com/stretchr/testify/assert"
	"hamak/mydota/db"
	appTesting "hamak/mydota/testing"
	"testing"
)

func TestCanFetchAndSaveToDb(t *testing.T) {
	// Delete all econ items
	config := appTesting.NewConfig()
	conn, err := db.NewConnection(config)
	assert.NotNil(t, conn)
	assert.Nil(t, err)

	conn.DB.Exec("DELETE FROM econ_items;")

	op := NewOp(conn)
	op.UpdateEconItems()

	repo := db.NewEconRepo(conn)

	assert.Equal(t, 5775, repo.Count())
}
