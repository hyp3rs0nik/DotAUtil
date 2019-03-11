package handlers

import (
	"github.com/zenazn/goji/web"
	"hamak/mydota/api/models"
	"hamak/mydota/db"
	"hamak/mydota/utils"
	"hamak/steam"
	"net/http"
)

type UsersHandler struct {
	config       Config
	dbConnection *db.Connection
	GetUser      JsonHandlerFunc
}

func NewUsersHandler(config Config, dbConn *db.Connection) *UsersHandler {
	handler := UsersHandler{
		config:       config,
		dbConnection: dbConn,
	}
	handler.GetUser = JsonHandlerFunc(handler.getUser)
	return &handler
}

func (self UsersHandler) getUser(c web.C, rw http.ResponseWriter, r *http.Request) JsonHandlerResponse {
	client := steam.NewClient()
	var apiUser *models.User = nil
	steamUser, err := client.GetUserById(c.URLParams["userId"])
	repo := db.NewUserRepo(self.dbConnection)
	if steamUser == nil {
		// Try the db if we have a record.
		dbUser := repo.FindById(int64(utils.GetIntFromURLParams(c.URLParams, "userId", 0)))
		if dbUser != nil {
			// Convert to API model
			converted := models.UserFromDbUser(*dbUser)
			apiUser = &converted
		}
		// TODO queue getting info from the API again
	} else {
		// Save it in the db.
		// TODO We should have a direct conversion from steam.User to db.User
		converted := models.UserFromSteamUser(*steamUser)
		dbUser := converted.ToDbUser()
		go repo.Save(dbUser)
		apiUser = &converted
	}

	if apiUser == nil && err != nil {
		panic(err)
	}

	if apiUser == nil {
		return JsonHandlerResponse{
			Status:       http.StatusNotFound,
			ErrorMessage: "User does not exist.",
		}
	} else {
		return JsonHandlerResponse{
			Status: http.StatusOK,
			Data:   apiUser,
		}
	}
}
