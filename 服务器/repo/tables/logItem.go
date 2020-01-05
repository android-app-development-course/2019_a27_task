package tables

import (
	"fmt"
)

// FormatLogKey return a formatted key
// Logs is a sorted set of log item indexed by project id and scoring its timestamp
// stores how many log is added in project
// Log item's key format is like "log:project_id:index"
// Note: log item is encoded as json
func FormatLogKey(projectID int64) string {
	return fmt.Sprintf("logs:%d", projectID)
}


