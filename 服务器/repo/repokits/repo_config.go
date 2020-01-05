package repokits

import (
	"github.com/go-redis/redis/v7"
	"time"
)

// ConnConfig is redis's configuration
// add your redis connect config here.
var ConnConfig *redis.Options = &redis.Options{
	Addr:       "localhost:6379",
	Password:   "",
	DB:         0,
	MaxRetries: 3,
}

// TokenCacheDuration set token's expire time.
// The default value is a week.
var TokenCacheDuration time.Duration = time.Second * 5 // time.Hour * 24 * 7
