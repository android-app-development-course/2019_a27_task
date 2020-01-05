package tables

import (
	"errors"
	"github.com/go-redis/redis/v7"
)

// UserIDTable is a hash table storing user's phone number and userID.
// An userID is automatically increase.
// Note: DO NOT provide any interface to user to get access of this table.
const UserIDTable = "userid:"

// UserIDCursor is an auto Increment id counter
// and it will never reduce.
const UserIDCursor = "user_id_cursor"

// ErrUserNotFound is returned when a user is not registered
var ErrUserNotFound error = errors.New("User Not Found")

// ExistPhoneNum return whether a user is in userid table.
// UserID is
func ExistPhoneNum(client *redis.Client, phoneNum string) bool {
	exist, _ := client.HExists(UserIDTable, phoneNum).Result()
	return exist
}

// GetUserID return user's ID using phone number
// ErrUserNotFound when userID is not found in UserIDTable
func GetUserID(client *redis.Client, phoneNum string) (int64, error) {
	id, err := client.HGet(UserIDTable, phoneNum).Int64()
	if err == redis.Nil {
		return 0, ErrUserNotFound
	} else if err != nil {
		return 0, err
	}
	return id, nil
}
