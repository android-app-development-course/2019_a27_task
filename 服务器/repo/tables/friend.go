package tables

import (
	"fmt"
)

// friendSetKey stores an user's friend's phone number.
const friendSetKey = "friend:%d"

func FormatFriendSetKey(userID int64) string {
	return fmt.Sprintf(friendSetKey, userID)
}

