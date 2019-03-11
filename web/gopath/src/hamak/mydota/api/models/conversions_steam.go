package models

import (
	"github.com/jinzhu/copier"
	"hamak/steam"
)

func UserFromSteamUser(source steam.User) User {
	user := User{}
	copier.Copy(&user, &source)
	return user
}

func UsersFromSteamUsers(source []steam.User) []User {
	ret := []User{}
	for _, sourceUser := range source {
		ret = append(ret, UserFromSteamUser(sourceUser))
	}
	return ret
}

func MatchCompactFromSteamMatchCompact(stMatch steam.MatchCompact, users []User) MatchCompact {
	match := MatchCompact{}
	match.Id = stMatch.Id
	match.SeqNum = stMatch.SeqNum
	match.StartTime = stMatch.StartTime
	match.LobbyType = stMatch.LobbyType

	players := []MatchPlayerCompact{}
	for _, stPlayerCompact := range stMatch.Players {
		player := MatchPlayerCompact{
			Slot:   stPlayerCompact.Slot,
			HeroId: stPlayerCompact.HeroId,
			UserId: stPlayerCompact.UserIdAsString(),
		}

		player.User = findUserFromSlice(users, stPlayerCompact.UserIdAsString())
		players = append(players, player)
	}
	match.Players = players

	return match
}

func MatchCompactsFromSteamMatchCompacts(stMatches []steam.MatchCompact, users []User) []MatchCompact {
	matches := []MatchCompact{}
	for _, stMatchCompact := range stMatches {
		match := MatchCompactFromSteamMatchCompact(stMatchCompact, users)
		matches = append(matches, match)
	}

	return matches
}

func MatchFromSteamMatch(src steam.Match, users []User) Match {
	ret := Match{
		Id:                    src.Id,
		SeqNum:                src.SeqNum,
		StartTime:             src.StartTime,
		LobbyType:             src.LobbyType,
		RadiantWin:            src.RadiantWin,
		Duration:              src.Duration,
		TowerStatusRadiant:    src.TowerStatusRadiant,
		TowerStatusDire:       src.TowerStatusDire,
		BarracksStatusRadiant: src.BarracksStatusRadiant,
		BarracksStatusDire:    src.BarracksStatusDire,
		Cluster:               src.Cluster,
		FirstBloodTime:        src.FirstBloodTime,
		HumanPlayers:          src.HumanPlayers,
		LeagueId:              src.LeagueId,
		PositiveVotes:         src.PositiveVotes,
		NegativeVotes:         src.NegativeVotes,
		GameMode:              src.GameMode,
		RadiantCaptain:        src.RadiantCaptain,
		DireCaptain:           src.DireCaptain,
		Players:               []MatchPlayer{},
	}

	for _, srcPlayer := range src.Players {
		player := MatchPlayerFromSteamMatchPlayer(srcPlayer, users)
		ret.Players = append(ret.Players, player)
	}

	return ret
}

func MatchPlayerFromSteamMatchPlayer(src steam.MatchPlayer, users []User) MatchPlayer {
	ret := MatchPlayer{
		Slot:            src.Slot,
		HeroId:          src.HeroId,
		UserId:          src.UserIdAsString(),
		ItemIds:         []int{src.Item0, src.Item1, src.Item2, src.Item3, src.Item4, src.Item5},
		Kills:           src.Kills,
		Deaths:          src.Deaths,
		Assists:         src.Assists,
		LeaverStatus:    src.LeaverStatus,
		Gold:            src.Gold,
		LastHits:        src.LastHits,
		Denies:          src.Denies,
		GoldPerMin:      src.GoldPerMin,
		XpPerMin:        src.XpPerMin,
		GoldSpent:       src.GoldSpent,
		HeroDamage:      src.HeroDamage,
		TowerDamage:     src.TowerDamage,
		HeroHealing:     src.HeroHealing,
		Level:           src.Level,
		AbilityUpgrades: []AbilityUpgrade{},
	}

	ret.User = findUserFromSlice(users, ret.UserId)

	for _, item := range src.AbilityUpgrades {
		up := AbilityUpgrade{}
		copier.Copy(&up, &item)

		ret.AbilityUpgrades = append(ret.AbilityUpgrades, up)
	}

	return ret
}

func LeagueFromSteamLeague(src steam.League) League {
	ret := League{}
	copier.Copy(&ret, &src)
	return ret
}
