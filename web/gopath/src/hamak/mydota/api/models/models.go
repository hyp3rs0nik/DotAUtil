// Models for API JSON outputs
package models

type User struct {
	Id                       string `json:"id"`
	CommunityVisibilityState int8   `json:"communityVisibilityState"`
	ProfileState             int8   `json:"profileState"`
	PersonaName              string `json:"personaName"`
	LastLogoff               int32  `json:"lastLogoff,omitempty"`
	ProfileUrl               string `json:"profileUrl"`
	AvatarUrl                string `json:"avatarUrl"`
	PersonaState             int8   `json:"personaState"`
	PrimaryClanId            string `json:"primaryClanId,omitempty"`
	TimeCreated              int32  `json:"timeCreated,omitempty"`
	PersonaStateFlags        int16  `json:"personaStateFlags"`
}

type MatchCompact struct {
	Id        int                  `json:"id"`
	SeqNum    int                  `json:"seqNum"`
	StartTime int                  `json:"startTime"`
	LobbyType int                  `json:"lobbyType"`
	Players   []MatchPlayerCompact `json:"players"`
}

type MatchPlayerCompact struct {
	Slot   int    `json:"slot"`
	HeroId int    `json:"heroId"`
	UserId string `json:"userId"`
	User   *User  `json:"user"`
}

type Match struct {
	Id                    int           `json:"id"`
	SeqNum                int           `json:"seqNum"`
	StartTime             int           `json:"startTime"`
	LobbyType             int           `json:"lobbyType"`
	RadiantWin            bool          `json:"radiantWin"`
	Duration              int           `json:"duration"`
	TowerStatusRadiant    int           `json:"towerStatusRadiant"`
	TowerStatusDire       int           `json:"towerStatusDire"`
	BarracksStatusRadiant int           `json:"barracksStatusRadiant"`
	BarracksStatusDire    int           `json:"barracksStatusDire"`
	Cluster               int           `json:"cluster"`
	FirstBloodTime        int           `json:"firstBloodTime"`
	HumanPlayers          int           `json:"humanPlayers"`
	LeagueId              int           `json:"leagueId"`
	PositiveVotes         int           `json:"positiveVotes"`
	NegativeVotes         int           `json:"negativeVotes"`
	GameMode              int           `json:"gameMode"`
	RadiantCaptain        int           `json:"radiantCaptain"`
	DireCaptain           int           `json:"direCaptain"`
	Players               []MatchPlayer `json:"players"`
}

type MatchPlayer struct {
	Slot            int              `json:"slot"`
	HeroId          int              `json:"heroId"`
	UserId          string           `json:"userId"`
	User            *User            `json:"user"`
	ItemIds         []int            `json:"itemIds"`
	Kills           int              `json:"kills"`
	Deaths          int              `json:"deaths"`
	Assists         int              `json:"assists"`
	LeaverStatus    int              `json:"leaverStatus"`
	Gold            int              `json:"gold"`
	LastHits        int              `json:"lastHits"`
	Denies          int              `json:"denies"`
	GoldPerMin      int              `json:"goldPerMin"`
	XpPerMin        int              `json:"xpPerMin"`
	GoldSpent       int              `json:"goldSpent"`
	HeroDamage      int              `json:"heroDamage"`
	TowerDamage     int              `json:"towerDamage"`
	HeroHealing     int              `json:"heroHealing"`
	Level           int              `json:"level"`
	AbilityUpgrades []AbilityUpgrade `json:"abilityUpgrades"`
}

type AbilityUpgrade struct {
	Ability int `json:"ability"`
	Time    int `json:"time"`
	Level   int `json:"level"`
}

type League struct {
	Id            int32  `json:"id"`
	Name          string `json:"name"`
	Description   string `json:"description"`
	TournamentUrl string `json:"tournamentUrl"`
	ImageUrl      string `json:"imageUrl,omitempty"`
	ImageUrlLarge string `json:"imageUrlLarge,omitempty"`
}
