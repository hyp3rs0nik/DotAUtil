package cron

import (
	"github.com/robfig/cron"
	"hamak/mydota/db"
	"hamak/mydota/ops"
	"log"
	"sync"
)

type Cron struct {
	dbConn *db.Connection

	econAndLeagueUpdateMutex *sync.Mutex
}

func NewCron(dbConn *db.Connection) *Cron {
	cron := Cron{
		dbConn: dbConn,
		econAndLeagueUpdateMutex: &sync.Mutex{},
	}
	return &cron
}

func (self *Cron) Start() {
	// Initial update
	go self.updateEconAndLeagues()

	service := cron.New()
	service.AddFunc("0 0 * * * *", self.updateEconAndLeagues)
	service.Start()

	log.Println("Cron started")
}

func (self *Cron) updateEconAndLeagues() {
	self.econAndLeagueUpdateMutex.Lock()
	defer self.econAndLeagueUpdateMutex.Unlock()

	op := ops.NewOp(self.dbConn)

	log.Printf("Fetching and updating econ items...")
	if err := op.UpdateEconItems(); err != nil {
		log.Printf("ERROR: %+v", err)
	} else {
		log.Println("Done")
	}

	log.Printf("Fetching and updating leagues...")
	if err := op.UpdateLeagues(); err != nil {
		log.Printf("ERROR: %+v", err)
	} else {
		log.Println("Done")
	}
}
