package handlers

import (
	"github.com/zenazn/goji/web"
	"hamak/mydota/api/models"
	"hamak/mydota/utils"
	"hamak/steam"
	"net/http"
)

type MatchesHandler struct {
	config           Config
	GetUserMatches   JsonHandlerFunc
	GetMatch         JsonHandlerFunc
	GetLeagueMatches JsonHandlerFunc
}

func NewMatchesHandler(config Config) *MatchesHandler {
	handler := MatchesHandler{config: config}
	handler.GetUserMatches = JsonHandlerFunc(handler.getUserMatches)
	handler.GetMatch = JsonHandlerFunc(handler.getMatch)
	handler.GetLeagueMatches = JsonHandlerFunc(handler.getLeagueMatches)
	return &handler
}

func (self MatchesHandler) getUserMatches(c web.C, rw http.ResponseWriter, r *http.Request) JsonHandlerResponse {
	client := steam.NewClient()
	params := steam.NewMatchesQueryParams()
	params.UserId = c.URLParams["userId"]
	params.BeforeMatchId = utils.GetIntFromValues(r.URL.Query(), "beforeId", params.BeforeMatchId)
	params.Count = utils.GetIntFromValues(r.URL.Query(), "count", params.Count)
	params.HeroId = utils.GetIntFromValues(r.URL.Query(), "heroId", params.HeroId)
	params.LeagueId = utils.GetIntFromValues(r.URL.Query(), "leagueId", params.LeagueId)
	matches, err := client.GetMatches(params)
	if err != nil {
		panic(err)
	}

	userIds := steam.GetUserIdsFromMatchCompacts(matches)
	users, err := client.GetUsers(userIds)
	if err != nil {
		panic(err)
	}
	apiModels := models.MatchCompactsFromSteamMatchCompacts(matches, models.UsersFromSteamUsers(users))

	return JsonHandlerResponse{Status: http.StatusOK, Data: apiModels}
}

func (self MatchesHandler) getLeagueMatches(c web.C, rw http.ResponseWriter, r *http.Request) JsonHandlerResponse {
	client := steam.NewClient()
	params := steam.NewMatchesQueryParams()
	params.LeagueId = utils.GetIntFromURLParams(c.URLParams, "leagueId", params.LeagueId)
	params.BeforeMatchId = utils.GetIntFromValues(r.URL.Query(), "beforeId", params.BeforeMatchId)
	params.Count = utils.GetIntFromValues(r.URL.Query(), "count", params.Count)
	params.HeroId = utils.GetIntFromValues(r.URL.Query(), "heroId", params.HeroId)
	params.UserId = r.URL.Query().Get("userId")
	matches, err := client.GetMatches(params)
	if err != nil {
		panic(err)
	}

	userIds := steam.GetUserIdsFromMatchCompacts(matches)
	users, err := client.GetUsers(userIds)
	if err != nil {
		panic(err)
	}
	apiModels := models.MatchCompactsFromSteamMatchCompacts(matches, models.UsersFromSteamUsers(users))

	return JsonHandlerResponse{Status: http.StatusOK, Data: apiModels}
}

func (self MatchesHandler) getMatch(c web.C, rw http.ResponseWriter, r *http.Request) JsonHandlerResponse {
	client := steam.NewClient()
	match, err := client.GetMatch(c.URLParams["matchId"])
	if err != nil {
		panic(err)
	}
	if match == nil {
		return JsonHandlerResponse{Status: http.StatusNotFound, ErrorMessage: "Match does not exist."}
	}
	userIds := steam.GetUserIdsFromMatch(*match)
	users, err := client.GetUsers(userIds)
	if err != nil {
		panic(err)
	}
	apiModels := models.MatchFromSteamMatch(*match, models.UsersFromSteamUsers(users))

	return JsonHandlerResponse{Status: http.StatusOK, Data: apiModels}
}
