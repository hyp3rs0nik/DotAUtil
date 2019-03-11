package steam

import (
	"strconv"
)

type MatchesQueryParams struct {
	UserId        string
	BeforeMatchId int
	LeagueId      int
	HeroId        int
	Count         int
}

func NewMatchesQueryParams() MatchesQueryParams {
	ret := MatchesQueryParams{}
	ret.Count = 20
	return ret
}

func (self MatchesQueryParams) CountAsString() string {
	return strconv.FormatInt(int64(self.Count), 10)
}

func (self MatchesQueryParams) BeforeMatchIdAsString() string {
	return strconv.FormatInt(int64(self.BeforeMatchId), 10)
}

func (self MatchesQueryParams) LeagueIdAsString() string {
	return strconv.FormatInt(int64(self.LeagueId), 10)
}

func (self MatchesQueryParams) HeroIdAsString() string {
	return strconv.FormatInt(int64(self.HeroId), 10)
}
