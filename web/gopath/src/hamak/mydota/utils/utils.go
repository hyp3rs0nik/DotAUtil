package utils

import (
	"database/sql"
	"github.com/lib/pq"
	"net/url"
	"strconv"
	"time"
)

func GetIntFromValues(v url.Values, key string, defaultValue int) int {
	value := v.Get(key)
	if len(value) == 0 {
		return defaultValue
	} else {
		ret, err := strconv.ParseInt(value, 10, 32)
		if err != nil {
			panic(err)
		}
		return int(ret)
	}
}

func GetIntFromURLParams(params map[string]string, key string, defaultValue int) int {
	value := params[key]
	if len(value) == 0 {
		return defaultValue
	} else {
		ret, err := strconv.ParseInt(value, 10, 32)
		if err != nil {
			panic(err)
		}
		return int(ret)
	}
}

func StringToInt64(v string) int64 {
	if len(v) == 0 {
		return 0
	}
	ret, err := strconv.ParseInt(v, 10, 64)
	if err != nil {
		panic(err)
	} else {
		return ret
	}
}

// Returns time.Time's Unix() value as int32. If the time.Time is not valid, this returns 0.
// Note that `0` in unix/epoch time still has a different meaning (1970-1-1).
func TimeToInt32(t time.Time) int32 {
	if t.IsZero() {
		return 0
	} else {
		return int32(t.Unix())
	}
}

// Converts an int32 unix time to pq.NullTime. If the int32 value is zero, this will assume that
// it is a null time.
func Int32ToPQTime(i int32) pq.NullTime {
	if i == 0 {
		return pq.NullTime{Valid: false}
	} else {
		return pq.NullTime{Time: time.Unix(int64(i), 32), Valid: true}
	}
}

func StringToSqlNullString(s string) sql.NullString {
	return sql.NullString{s, len(s) != 0}
}
