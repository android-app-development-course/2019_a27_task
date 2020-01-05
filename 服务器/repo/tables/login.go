package tables

import (
	"errors"
	"github.com/go-redis/redis/v7"
)

// Login is a hash table storing tokens and userIDs
const Login string = "login:"

// Password is a hash table storing phoneNums and passsword
const Password string = "passwd:"

// Recent is a sorted set storing token sorted by time stamp
const Recent string = "recent:"

// ErrUserNotLogin is returned when token is not found.
var ErrUserNotLogin error = errors.New("User Must Login")

// CheckToken returns whenther a token exist in Login table
func CheckToken(client *redis.Client, token string) (bool, error) {
	exist, err := client.HExists(Login, token).Result()
	if err != nil {
		return false, err
	}
	return exist, nil
}
