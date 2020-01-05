package tables

import (
	"fmt"
	"github.com/go-redis/redis/v7"
	"strconv"
	"time"
)

// UserProjectSet is a sorted set's name format
const UserProjectSet = "user_proj:%d"

// FormatUserProjectSetName returns a formatted key name to a set
// example: "user_proj:project_id"
func FormatUserProjectSetName(userID int64) string {
	return fmt.Sprintf(UserProjectSet, userID)
}

// UserJoinProject allows user to join a project
func UserJoinProject(client *redis.Client, userID int64, projectID int64) error {
	if err := ExistProject(client, projectID); err != nil {
		return ErrProjectNotFound
	}
	key := FormatUserProjectSetName(userID)
	now := time.Now().Unix()
	if _, err := client.ZAddNX(key, &redis.Z{Member: projectID, Score: float64(now)}).Result(); err != nil {
		return err
	}
	return nil
}

// UserQuitProject out a project id out of user's project set.
func UserQuitProject(client *redis.Client, userID int64, projectID int64) error {
	key := FormatUserProjectSetName(userID)
	result, err := client.ZRem(key, projectID).Result()
	if err == redis.Nil {
		return ErrUserNotFound
	} else if err != nil {
		return err
	}
	if result <= 0 {
		return ErrProjectNotFound
	}
	return nil
}

// GetUserAllProject returns all project's ids in string type.
func GetUserAllProject(client *redis.Client, userID int64) ([]string, error) {
	key := FormatUserProjectSetName(userID)

	projects, err := client.ZRevRangeByScore(
		key,
		&redis.ZRangeBy{Min: strconv.FormatInt(0, 10), Max: strconv.FormatInt(time.Now().Unix(), 10)}).Result()
	if err != nil {
		return nil, err
	}
	return projects, nil
}
