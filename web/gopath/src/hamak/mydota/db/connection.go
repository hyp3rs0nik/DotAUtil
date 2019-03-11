package db

import (
	"github.com/jinzhu/gorm"
	_ "github.com/lib/pq"
)

type Config interface {
	GetDbConnectionString() string
}

type Connection struct {
	DB gorm.DB
}

func NewConnection(config Config) (*Connection, error) {
	db, err := gorm.Open("postgres", config.GetDbConnectionString())
	if err != nil {
		return nil, err
	}

	db.DB().SetMaxIdleConns(10)
	db.DB().SetMaxOpenConns(100)
	if err := db.DB().Ping(); err != nil {
		return nil, err
	}

	conn := Connection{}
	conn.DB = db
	return &conn, nil
}
