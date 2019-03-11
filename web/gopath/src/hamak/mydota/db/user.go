package db

import (
	"errors"
	"github.com/lib/pq"
	"strconv"
)

type User struct {
	Id                       int64
	CommunityVisibilityState int8
	ProfileState             int8
	PersonaName              string
	LastLogoff               pq.NullTime
	ProfileUrl               string
	AvatarUrl                string
	PersonaState             int8
	PrimaryClanId            int64
	TimeCreated              pq.NullTime
	PersonaStateFlags        int16
}

func (s User) IdAsString() string {
	return strconv.FormatInt(s.Id, 10)
}

func (s User) PrimaryClanIdAsString() string {
	if s.PrimaryClanId == 0 {
		return ""
	} else {
		return strconv.FormatInt(s.PrimaryClanId, 10)
	}
}

type UserRepo struct {
	connection *Connection
}

func NewUserRepo(connection *Connection) UserRepo {
	repo := UserRepo{}
	repo.connection = connection
	return repo
}

func (self UserRepo) FindById(id int64) *User {
	user := User{}
	result := self.connection.DB.First(&user, id)
	if result.Error != nil || user.Id == 0 {
		return nil
	} else {
		return &user
	}
}

func (self UserRepo) Save(user User) error {
	if user.Id == 0 {
		return errors.New("Users must have ids.")
	}

	// Not immediately using DB.Save() because that method only checks if
	// Id == 0 but we have ids that are not really from the db, but from Steam.
	// So we'll have to find the record ourselves.
	existing := self.FindById(user.Id)
	var err error = nil
	if existing == nil {
		err = self.connection.DB.Create(&user).Error
	} else {
		err = self.connection.DB.Save(&user).Error
	}
	return err
}
