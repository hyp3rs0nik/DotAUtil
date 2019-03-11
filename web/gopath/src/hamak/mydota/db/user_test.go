package db

import (
	"github.com/lib/pq"
	"github.com/stretchr/testify/assert"
	appTesting "hamak/mydota/testing"
	"testing"
	"time"
)

func TestCanSaveUser(t *testing.T) {
	// Delete all users
	config := appTesting.NewConfig()
	conn, err := NewConnection(config)
	assert.NotNil(t, conn)
	assert.Nil(t, err)

	conn.DB.Exec("DELETE FROM users;")

	repo := NewUserRepo(conn)
	user := User{
		Id: 123,
		CommunityVisibilityState: 3,
		ProfileState:             4,
		PersonaName:              "Shiki",
		ProfileUrl:               "http://shiki.me",
		AvatarUrl:                "http://shiki.me/me.png",
		PersonaState:             9,
		PrimaryClanId:            190,
		PersonaStateFlags:        1,
		LastLogoff:               pq.NullTime{Time: time.Date(2014, 11, 2, 11, 12, 30, 0, time.UTC), Valid: true},
		TimeCreated:              pq.NullTime{Time: time.Unix(100, 0).UTC(), Valid: true},
	}

	found := repo.FindById(user.Id)
	assert.Nil(t, found)

	assert.Nil(t, repo.Save(user))

	found = repo.FindById(user.Id)
	assert.NotNil(t, found)
	assert.Equal(t, user.PersonaName, found.PersonaName)
	assert.True(t, user.LastLogoff.Time.Equal(found.LastLogoff.Time))
	assert.True(t, user.TimeCreated.Time.Equal(found.TimeCreated.Time))
	//assert.Equal(t, user, found) // This doesn't work because of the time.Time data types. Weird.
}
