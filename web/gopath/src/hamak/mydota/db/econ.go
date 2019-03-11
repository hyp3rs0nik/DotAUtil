package db

import (
	"database/sql"
)

type EconItem struct {
	Id            int64
	DefIndex      int32
	Name          string
	ImageUrl      sql.NullString
	ImageUrlLarge sql.NullString
}

func (self EconItem) Valid() bool {
	return self.Id != 0
}

type EconRepo struct {
	connection *Connection
}

func NewEconRepo(connection *Connection) EconRepo {
	repo := EconRepo{}
	repo.connection = connection
	return repo
}

func (self EconRepo) FindById(id int64) *EconItem {
	item := EconItem{}
	result := self.connection.DB.First(&item, id)
	if result.Error != nil || item.Id == 0 {
		return nil
	} else {
		return &item
	}
}

func (self EconRepo) FindByDefIndex(index int32) *EconItem {
	item := EconItem{}
	result := self.connection.DB.Where("def_index = ?", index).First(&item)
	if result.Error != nil || item.Id == 0 {
		return nil
	} else {
		return &item
	}
}

// Econ items can have 0 as ids.
func (self EconRepo) Save(econItem *EconItem) error {
	var existing *EconItem
	if econItem.Id != 0 {
		existing = self.FindById(econItem.Id)
	} else {
		existing = self.FindByDefIndex(econItem.DefIndex)
	}

	var err error = nil
	if existing == nil {
		err = self.connection.DB.Create(econItem).Error
	} else {
		econItem.Id = existing.Id
		err = self.connection.DB.Save(econItem).Error
	}
	return err
}

func (self EconRepo) Count() int64 {
	var count int64 = 0
	self.connection.DB.Model(EconItem{}).Count(&count)
	return count
}
