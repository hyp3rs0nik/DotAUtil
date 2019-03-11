package ops

import (
	"hamak/mydota/db"
)

type Op struct {
	dbConn *db.Connection
}

func NewOp(dbConn *db.Connection) Op {
	op := Op{
		dbConn: dbConn,
	}
	return op
}
