package steam

import (
	"encoding/json"
	"github.com/jinzhu/copier"
	"github.com/jmcvetta/napping"
	"log"
	"net/url"
	"strings"
)

type Client struct {
	baseUrl string
	apiKey  string
}

func NewClient() *Client {
	ret := Client{}
	ret.baseUrl = "https://api.steampowered.com"
	ret.apiKey = "1D590860D494B74F01AF6B49D4D1552B"
	return &ret
}

func (self Client) GetUserById(id string) (*User, error) {
	users, err := self.GetUsers([]string{id})
	if err == nil && len(users) > 0 {
		return &users[0], nil
	} else {
		return nil, err
	}
}

func (self Client) GetUsers(ids []string) ([]User, error) {
	if len(ids) == 0 {
		return []User{}, nil
	}

	ids, err := Convert32BitIdsTo64(ids)
	if err != nil {
		return nil, err
	}

	endpoint := self.baseUrl + "/ISteamUser/GetPlayerSummaries/v0002/?"
	params := url.Values{}
	params.Add("key", self.apiKey)
	params.Add("steamids", strings.Join(ids, ","))
	endpoint += params.Encode()
	log.Printf("API: %+v", endpoint)

	response, err := napping.Get(endpoint, nil, nil, nil)
	if err != nil {
		return nil, err
	}

	var data = new(struct {
		Response struct {
			Players []User
		}
	})

	err = json.Unmarshal([]byte(response.RawText()), &data)
	if err != nil {
		return nil, err
	}

	users := []User{}
	for _, value := range data.Response.Players {
		user := User{}
		copier.Copy(&user, &value)
		user.Id, err = Convert64BitIdTo32(user.Id)
		if err != nil {
			return nil, err
		}
		users = append(users, user)
	}

	return users, nil
}

func (self Client) GetMatches(queryParams MatchesQueryParams) ([]MatchCompact, error) {
	endpoint := self.baseUrl + "/IDOTA2Match_570/GetMatchHistory/v001/?"
	params := url.Values{}
	params.Add("key", self.apiKey)
	if queryParams.UserId != "" {
		params.Add("account_id", queryParams.UserId)
	}
	if queryParams.BeforeMatchId != 0 {
		params.Add("start_at_match_id", queryParams.BeforeMatchIdAsString())
	}
	if queryParams.HeroId != 0 {
		params.Add("hero_id", queryParams.HeroIdAsString())
	}
	if queryParams.LeagueId != 0 {
		params.Add("league_id", queryParams.LeagueIdAsString())
	}
	params.Add("matches_requested", queryParams.CountAsString())
	endpoint += params.Encode()

	response, err := napping.Get(endpoint, nil, nil, nil)
	if err != nil {
		return nil, err
	}

	var data = new(struct {
		Result struct {
			Matches []MatchCompact
		}
	})

	err = json.Unmarshal([]byte(response.RawText()), &data)
	if err != nil {
		return nil, err
	}

	return data.Result.Matches, nil
}

func (self Client) GetMatch(matchId string) (*Match, error) {
	endpoint := self.baseUrl + "/IDOTA2Match_570/GetMatchDetails/V001?"
	params := url.Values{}
	params.Add("key", self.apiKey)
	params.Add("match_id", matchId)
	endpoint += params.Encode()

	response, err := napping.Get(endpoint, nil, nil, nil)
	if err != nil {
		return nil, err
	}

	var data = new(struct{ Result Match })
	err = json.Unmarshal([]byte(response.RawText()), &data)
	if err != nil {
		return nil, err
	}
	if data.Result.Id == 0 {
		return nil, nil
	}

	return &data.Result, nil
}

func (self Client) GetLeagues() ([]League, error) {
	endpoint := self.baseUrl + "/IDOTA2Match_570/GetLeagueListing/v0001?"
	params := url.Values{}
	params.Add("key", self.apiKey)
	params.Add("language", "en_us")
	endpoint += params.Encode()

	response, err := napping.Get(endpoint, nil, nil, nil)
	if err != nil {
		return nil, err
	}

	var data = new(struct {
		Result struct{ Leagues []League }
	})
	if err = json.Unmarshal([]byte(response.RawText()), &data); err != nil {
		return nil, err
	}

	return data.Result.Leagues, nil
}

func (self Client) GetEconItems() ([]EconItem, error) {
	endpoint := self.baseUrl + "/IEconItems_570/GetSchema/v0001/?"
	params := url.Values{}
	params.Add("key", self.apiKey)
	params.Add("language", "en_us")
	endpoint += params.Encode()

	response, err := napping.Get(endpoint, nil, nil, nil)
	if err != nil {
		return nil, err
	}

	var data = new(struct {
		Result struct {
			Items []EconItem
		}
	})
	if err = json.Unmarshal([]byte(response.RawText()), &data); err != nil {
		return nil, err
	}

	return data.Result.Items, nil
}
