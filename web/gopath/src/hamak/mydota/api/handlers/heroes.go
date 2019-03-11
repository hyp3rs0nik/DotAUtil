package handlers

import (
	"encoding/json"
	"github.com/zenazn/goji/web"
	"io/ioutil"
	"net/http"
)

type Config interface {
	GetResourcesPath() string
}

type HeroesHandler struct {
	config    Config
	GetHeroes JsonHandlerFunc
}

func NewHeroesHandler(config Config) *HeroesHandler {
	handler := HeroesHandler{config: config}
	handler.GetHeroes = JsonHandlerFunc(handler.getHeroes)
	return &handler
}

// TODO cache
func (self HeroesHandler) getHeroes(c web.C, rw http.ResponseWriter, r *http.Request) JsonHandlerResponse {
	path := self.config.GetResourcesPath() + "/heroes.json"
	fileData, err := ioutil.ReadFile(path)
	if err != nil {
		panic(err)
	}

	var data []interface{}
	if err := json.Unmarshal(fileData, &data); err == nil {
		return JsonHandlerResponse{
			Status: http.StatusOK,
			Data:   data,
		}
	} else {
		panic(err)
	}
}
