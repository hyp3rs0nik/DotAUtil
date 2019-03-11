package ops

import (
	"hamak/mydota/db"
	"hamak/mydota/utils"
	"hamak/steam"
)

// Gets leagues data from the API and puts them in the db. Images are not fetched since those are
// taken from the econ items.
func (self Op) UpdateLeagues() error {
	client := steam.NewClient()
	leagues, err := client.GetLeagues()
	if err != nil {
		return err
	}

	leaguesRepo := db.NewLeagueRepo(self.dbConn)
	for _, league := range leagues {
		dbLeague := db.SteamLeagueToLeague(league)
		err := leaguesRepo.Save(dbLeague)
		utils.PanicIfErr(err)
	}

	return nil
}
