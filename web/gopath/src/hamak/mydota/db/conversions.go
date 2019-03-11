package db

import (
	"database/sql"
	"hamak/mydota/utils"
	"hamak/steam"
)

func SteamEconItemToEconItem(src steam.EconItem) *EconItem {
	ret := EconItem{
		DefIndex:      src.DefIndex,
		Name:          src.Name,
		ImageUrl:      utils.StringToSqlNullString(src.ImageUrl),
		ImageUrlLarge: utils.StringToSqlNullString(src.ImageUrlLarge),
	}
	return &ret
}

func SteamLeagueToLeague(src steam.League) *League {
	league := League{
		Id:            int64(src.Id),
		Name:          src.Name,
		Description:   src.Description,
		TournamentUrl: src.TournamentUrl,
		ItemDef:       sql.NullInt64{int64(src.ItemDef), true},
	}
	return &league
}
