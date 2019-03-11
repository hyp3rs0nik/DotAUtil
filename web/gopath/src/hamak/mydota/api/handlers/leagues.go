package handlers

import (
	"github.com/zenazn/goji/web"
	//"io/ioutil"
	"hamak/mydota/api/models"
	"hamak/mydota/db"
	//"hamak/steam"
	"net/http"
)

type LeaguesHandler struct {
	config     Config
	dbConn     *db.Connection
	GetLeagues JsonHandlerFunc
}

func NewLeaguesHandler(config Config, dbConn *db.Connection) *LeaguesHandler {
	handler := LeaguesHandler{config: config, dbConn: dbConn}
	handler.GetLeagues = JsonHandlerFunc(handler.getLeagues)
	return &handler
}

func (self LeaguesHandler) getLeagues(c web.C, rw http.ResponseWriter, r *http.Request) JsonHandlerResponse {
	repo := db.NewLeagueRepo(self.dbConn)
	leagues := repo.FindAll()

	apiModels := []models.League{}
	for _, league := range leagues {
		model := models.LeagueFromDbLeague(league)
		apiModels = append(apiModels, model)
	}

	//client := steam.NewClient()
	//leagues, err := client.GetLeagues()
	//if err != nil {
	//  panic(err)
	//}

	//apiModels := []models.League{}
	//for _, stLeague := range leagues {
	//  apiModel := models.LeagueFromSteamLeague(stLeague)
	//  apiModels = append(apiModels, apiModel)
	//}

	return JsonHandlerResponse{
		Status: http.StatusOK,
		Data:   apiModels,
	}

	//rw.Header().Set("Content-Type", "application/json")
	//path := self.config.GetResourcesPath() + "/dummy-leagues.json"
	//fileData, _ := ioutil.ReadFile(path) // TODO handle error
	//rw.Write(fileData)
}
