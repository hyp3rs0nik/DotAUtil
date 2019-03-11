package steam

import (
	"strconv"
)

const PrivateUserId int64 = 4294967295
const PrivateUserIdString string = "4294967295"

type User struct {
	Id                       string `json:"steamid"`
	CommunityVisibilityState int8
	ProfileState             int8
	PersonaName              string
	LastLogoff               int32
	ProfileUrl               string
	AvatarUrl                string `json:"avatar"`
	PersonaState             int8
	PrimaryClanId            string
	TimeCreated              int32
	PersonaStateFlags        int16
}

type MatchCompact struct {
	Id        int `json:"match_id"`
	SeqNum    int `json:"match_seq_num"`
	StartTime int `json:"start_time"`
	LobbyType int `json:"lobby_type"`
	Players   []MatchPlayerCompact
}

type MatchPlayerCompact struct {
	Slot   int   `json:"player_slot"`
	HeroId int   `json:"hero_id"`
	UserId int64 `json:"account_id"`
}

func (self MatchPlayerCompact) UserIdAsString() string {
	return strconv.FormatInt(self.UserId, 10)
}

type Match struct {
	Id                    int  `json:"match_id"`
	SeqNum                int  `json:"match_seq_num"`
	StartTime             int  `json:"start_time"`
	LobbyType             int  `json:"lobby_type"`
	RadiantWin            bool `json:"radiant_win"`
	Duration              int  `json:"duration"`
	TowerStatusRadiant    int  `json:"tower_status_radiant"`
	TowerStatusDire       int  `json:"tower_status_dire"`
	BarracksStatusRadiant int  `json:"barracks_status_radiant"`
	BarracksStatusDire    int  `json:"barracks_status_dire"`
	Cluster               int  `json:"cluster"`
	FirstBloodTime        int  `json:"first_blood_time"`
	HumanPlayers          int  `json:"human_players"`
	LeagueId              int  `json:"league_id"`
	PositiveVotes         int  `json:"positive_votes"`
	NegativeVotes         int  `json:"negative_votes"`
	GameMode              int  `json:"game_mode"`
	RadiantCaptain        int  `json:"radiant_captain"`
	DireCaptain           int  `json:"dire_captain"`
	Players               []MatchPlayer
}

type MatchPlayer struct {
	Slot            int              `json:"player_slot"`
	HeroId          int              `json:"hero_id"`
	UserId          int64            `json:"account_id"`
	Item0           int              `json:"item_0"`
	Item1           int              `json:"item_1"`
	Item2           int              `json:"item_2"`
	Item3           int              `json:"item_3"`
	Item4           int              `json:"item_4"`
	Item5           int              `json:"item_5"`
	Kills           int              `json:"kills"`
	Deaths          int              `json:"deaths"`
	Assists         int              `json:"assists"`
	LeaverStatus    int              `json:"leaver_status"`
	Gold            int              `json:"gold"`
	LastHits        int              `json:"last_hits"`
	Denies          int              `json:"denies"`
	GoldPerMin      int              `json:"gold_per_min"`
	XpPerMin        int              `json:"xp_per_min"`
	GoldSpent       int              `json:"gold_spent"`
	HeroDamage      int              `json:"hero_damage"`
	TowerDamage     int              `json:"tower_damage"`
	HeroHealing     int              `json:"hero_healing"`
	Level           int              `json:"level"`
	AbilityUpgrades []AbilityUpgrade `json:"ability_upgrades"`
}

func (self MatchPlayer) UserIdAsString() string {
	return strconv.FormatInt(self.UserId, 10)
}

type AbilityUpgrade struct {
	Ability int `json:"ability"`
	Time    int `json:"time"`
	Level   int `json:"level"`
}

type League struct {
	Id            int32 `json:"leagueid"`
	Name          string
	Description   string
	TournamentUrl string `json:"tournament_url"`
	ItemDef       int32
}

type EconItem struct {
	Name          string
	DefIndex      int32
	ImageUrl      string `json:"image_url"`
	ImageUrlLarge string `json:"image_url_large"`
}
