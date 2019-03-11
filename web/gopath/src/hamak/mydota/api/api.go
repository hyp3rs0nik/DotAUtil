package api

import (
	"github.com/zenazn/goji"
	"github.com/zenazn/goji/graceful"
	"hamak/mydota/api/handlers"
	"hamak/mydota/components"
	"hamak/mydota/db"
	"log"
	"net"
	"net/http"
)

type Api struct {
	dbConn *db.Connection
	config *components.Config
}

func NewApi(config *components.Config, dbConn *db.Connection) *Api {
	api := Api{
		dbConn: dbConn,
		config: config,
	}
	return &api
}

func (self Api) Start() {
	heroes := handlers.NewHeroesHandler(self.config)
	items := handlers.NewItemsHandler(self.config)
	users := handlers.NewUsersHandler(self.config, self.dbConn)
	matches := handlers.NewMatchesHandler(self.config)
	leagues := handlers.NewLeaguesHandler(self.config, self.dbConn)

	goji.Get("/heroes", heroes.GetHeroes)
	goji.Get("/items", items.GetItems)
	goji.Get("/users/:userId", users.GetUser)
	goji.Get("/users/:userId/matches", matches.GetUserMatches)
	goji.Get("/leagues", leagues.GetLeagues)
	goji.Get("/leagues/:leagueId/matches", matches.GetLeagueMatches)
	goji.Get("/matches/:matchId", matches.GetMatch)

	http.Handle("/", goji.DefaultMux)
	listener, err := net.Listen("tcp", "0.0.0.0:3000")
	if err != nil {
		panic(err)
	}

	log.Println("Listening...")
	err = graceful.Serve(listener, http.DefaultServeMux)
	if err != nil {
		panic(err)
	}
	graceful.Wait()
}
