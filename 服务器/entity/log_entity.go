package entity

// LogItem is an entity recoding every action in a project.
// It's stored as a sorted set , indexed by projectID.
// Each project has an unique logItem set sorted by timestamp.
type LogItem struct {
	CommitedBy  string `key:"committer"`
	CreatedDate int64  `key:"date"`
	Content     string `key:"content"`
	Done 		bool `key:"done"`
}
