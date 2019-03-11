package models

import (
	"hamak/mydota/db"
	"hamak/mydota/utils"
)

func UserFromDbUser(src db.User) User {
	ret := User{
		Id: src.IdAsString(),
		CommunityVisibilityState: src.CommunityVisibilityState,
		ProfileState:             src.ProfileState,
		PersonaName:              src.PersonaName,
		LastLogoff:               utils.TimeToInt32(src.LastLogoff.Time),
		ProfileUrl:               src.ProfileUrl,
		AvatarUrl:                src.AvatarUrl,
		PersonaState:             src.PersonaState,
		PrimaryClanId:            src.PrimaryClanIdAsString(),
		TimeCreated:              utils.TimeToInt32(src.TimeCreated.Time),
		PersonaStateFlags:        src.PersonaStateFlags,
	}
	return ret
}

func (s User) ToDbUser() db.User {
	ret := db.User{
		Id: utils.StringToInt64(s.Id),
		CommunityVisibilityState: s.CommunityVisibilityState,
		ProfileState:             s.ProfileState,
		PersonaName:              s.PersonaName,
		LastLogoff:               utils.Int32ToPQTime(s.LastLogoff),
		ProfileUrl:               s.ProfileUrl,
		AvatarUrl:                s.AvatarUrl,
		PersonaState:             s.PersonaState,
		PrimaryClanId:            utils.StringToInt64(s.PrimaryClanId),
		TimeCreated:              utils.Int32ToPQTime((s.TimeCreated)),
		PersonaStateFlags:        s.PersonaStateFlags,
	}
	return ret
}

func LeagueFromDbLeague(src db.League) League {
	ret := League{
		Id:            int32(src.Id),
		Name:          src.Name,
		Description:   src.Description,
		TournamentUrl: src.TournamentUrl,
	}

	if src.Item.Valid() {
		ret.ImageUrl = src.Item.ImageUrl.String
		ret.ImageUrlLarge = src.Item.ImageUrlLarge.String
	}

	return ret
}
