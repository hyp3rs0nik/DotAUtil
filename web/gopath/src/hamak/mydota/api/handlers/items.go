package handlers

import (
	"encoding/json"
	"github.com/zenazn/goji/web"
	"io/ioutil"
	"net/http"
)

type ItemsHandler struct {
	config   Config
	GetItems JsonHandlerFunc
}

func NewItemsHandler(config Config) *ItemsHandler {
	handler := ItemsHandler{config: config}
	handler.GetItems = JsonHandlerFunc(handler.getItems)
	return &handler
}

func (self ItemsHandler) getItems(c web.C, rw http.ResponseWriter, r *http.Request) JsonHandlerResponse {
	path := self.config.GetResourcesPath() + "/items-list.json"
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
