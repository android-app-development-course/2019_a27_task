package entity

// Profile is an entity representing user.
// It can be both leader and normal members.
// Profile's got no phone number,
// because phone number is use for key in redis.
type Profile struct {
	Name        string // User's name
	PhoneNum    string // User's phone number
	Description string // USer's self description
	Email       string // User's email
	Workplace   string // User's workplace
}
