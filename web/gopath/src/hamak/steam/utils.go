package steam

import (
	"gopkg.in/fatih/set.v0"
	"strconv"
)

const userId64BitConversionAddend int64 = 76561197960265728

func Convert64BitIdTo32(id64 string) (string, error) {
	ret, err := strconv.ParseInt(id64, 10, 64)
	if err != nil {
		return "", err
	}
	ret = ret - userId64BitConversionAddend
	return strconv.FormatInt(ret, 10), nil
}

func Convert32BitIdTo64(id32 string) (string, error) {
	ret, err := strconv.ParseInt(id32, 10, 64)
	if err != nil {
		return "", err
	}
	ret = ret + userId64BitConversionAddend
	return strconv.FormatInt(ret, 10), nil
}

func Convert32BitIdsTo64(id32s []string) ([]string, error) {
	ret := make([]string, len(id32s))
	for i, value := range id32s {
		id64, err := Convert32BitIdTo64(value)
		if err != nil {
			return nil, err
		} else {
			ret[i] = id64
		}
	}

	return ret, nil
}

func GetUserIdsFromMatchCompacts(matches []MatchCompact) []string {
	userIdsSet := set.NewNonTS()
	for _, stMatchCompact := range matches {
		for _, stPlayerCompact := range stMatchCompact.Players {
			//log.Printf("%+v", stPlayerCompact)
			userId := stPlayerCompact.UserIdAsString()
			if userId != PrivateUserIdString {
				userIdsSet.Add(userId)
			}
		}
	}
	userIds := set.StringSlice(userIdsSet)
	return userIds
}

func GetUserIdsFromMatch(match Match) []string {
	userIdsSet := set.NewNonTS()
	for _, stMatchPlayer := range match.Players {
		if stMatchPlayer.UserIdAsString() != PrivateUserIdString {
			userIdsSet.Add(stMatchPlayer.UserIdAsString())
		}
	}
	return set.StringSlice(userIdsSet)
}
