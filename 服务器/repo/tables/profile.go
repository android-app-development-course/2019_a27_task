package tables

import (
	"fmt"
	"github.com/go-redis/redis/v7"
	"task/entity"
	"task/repo/repokits"
)

var ErrUserProfileNotFound = fmt.Errorf("User Profile Not Found")
// FormatProfileKey return a key formated into "profile:userid"
// id refer to UserID.
func FormatProfileKey(userid int64) string {
	return fmt.Sprintf("profile:%d", userid)
}

// SaveProfiles saves an user's profile into hash table and return userID
// Note: SaveProfiles will override an existed profile.
func SaveProfiles(client *redis.Client, profile *entity.Profile) error {
	id, err := client.HGet(UserIDTable, profile.PhoneNum).Int64()
	if err == redis.Nil {
		return ErrUserNotFound
	} else if err != nil {
		return err
	}

	key := FormatProfileKey(id)
	client.HMSet(key, repokits.Struct2Map(*profile))
	return nil
}

// DeleteProfile delete an user's profile by userid
func DeleteProfile(client *redis.Client, userid int64) error {
	key := FormatProfileKey(userid)
	err := client.Del(key).Err()
	if err == redis.Nil {
		return ErrUserProfileNotFound
	} else if err != nil {
		return fmt.Errorf("fail to delete %s: %e ", key, err)
	}
	return nil
}

// GetProfile returns an user's profile by its userid.
func GetProfile(client *redis.Client, userid int64) (*entity.Profile, error) {
	key := FormatProfileKey(userid)

	m, err := client.HGetAll(key).Result()
	if err == redis.Nil {
		return nil, ErrUserProfileNotFound
	}else if err != nil {
		return nil, err
	}
	return &entity.Profile{
		Name:        m["name"],
		Email:       m["email"],
		PhoneNum:    m["phonenum"],
		Workplace:   m["workplace"],
		Description: m["description"]}, nil
}
