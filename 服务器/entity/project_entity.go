package entity

// Project is an entity representing a project.
// Members array is actually members' phoneNum,
// which is the key of member's profile.
// Members array's len of a project must larger than 0,
// and the first one is project's leader
type Project struct {
	Name           string // project's name
	CreateDate     int64  `key:"create_date"`     //  project's created date
	LeaderPhoneNum string `key:"leader_phone_num` // leader's userid
}
