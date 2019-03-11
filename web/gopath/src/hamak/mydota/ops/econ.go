package ops

import (
	"hamak/mydota/db"
	"hamak/mydota/utils"
	"hamak/steam"
)

// Gets econ items from the API and puts them in the db.
func (self Op) UpdateEconItems() error {
	client := steam.NewClient()
	items, err := client.GetEconItems()
	if err != nil {
		return err
	}

	econRepo := db.NewEconRepo(self.dbConn)
	for _, item := range items {
		dbItem := db.SteamEconItemToEconItem(item)
		err := econRepo.Save(dbItem)
		utils.PanicIfErr(err)
	}

	return nil
}
