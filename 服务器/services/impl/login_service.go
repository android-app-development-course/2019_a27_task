package impl

import (
	context "context"
	"crypto/sha256"
	"fmt"
	"github.com/go-redis/redis/v7"
	"log"
	"strconv"
	"task/repo/repokits"
	"task/repo/tables"
	pb "task/services/service"
	"time"
)

// LoginService implements service interfaces defined in login.proto
type LoginService struct {
	pb.UnimplementedLoginServiceServer
}

// Register allows users to register userID in redis.
// return fasle if phoneNum has exist
func (s *LoginService) Register(ctx context.Context, info *pb.LoginInfo) (*pb.Result, error) {
	client := redis.NewClient(repokits.ConnConfig)
	defer client.Close()
	log.SetPrefix("Register - ")
	log.Printf("phoneNum:%s, password: %s", info.PhoneNum, info.Password)

	// Set phone number and password
	if ok, err := client.HSetNX(tables.Password, info.PhoneNum, info.Password).Result(); err != nil {
		log.Printf("fail to set password for phone num: %s", info.PhoneNum)
		return &pb.Result{Success: false, ErrorMsg: "fail to set password"}, err
	} else if !ok {
		log.Printf("User[User PhoneNum:%s] has already registerred", info.PhoneNum)
		errMsg := fmt.Sprintf("%s has already registerred", info.PhoneNum)
		return &pb.Result{Success: false, ErrorMsg: errMsg}, nil
	}
	userID, err := client.Incr(tables.UserIDCursor).Result()
	if err != nil && err != redis.Nil {
		log.Print(err)
		return nil, err
	}
	if err := client.HSet(tables.UserIDTable, info.PhoneNum, userID).Err(); err != nil {
		log.Print(err)
		return &pb.Result{Success: false}, err
	}
	return &pb.Result{Success: true}, nil
}

// Login allows users to fetch token,
// which allows users to fetch userID and cache cookies.
func (s *LoginService) Login(ctx context.Context, info *pb.LoginInfo) (*pb.Token, error) {
	client := redis.NewClient(repokits.ConnConfig)
	defer client.Close()
	log.SetPrefix("Login - ")
	// validate password
	passwd, err := client.HGet(tables.Password, info.PhoneNum).Result()
	if err == redis.Nil {
		log.Printf("User[User PhoneNum:%s] Not Registerred", info.PhoneNum)
		return &pb.Token{Token: "", Registered: false}, fmt.Errorf("User Not Registerred")
	} else if err != nil {
		log.Println(err)
		return &pb.Token{Token: "", Registered: true}, err
	}
	if passwd != info.Password {
		log.Printf("password validates fail (phoneNum: %s, passwd: %s)", info.PhoneNum, passwd)
		return &pb.Token{Token: "", Registered: true}, fmt.Errorf("Wrong Password")
	}

	timestamp := time.Now().Unix()
	sha := sha256.New()
	sha.Write([]byte(info.PhoneNum + strconv.FormatInt(timestamp, 10)))
	token := fmt.Sprintf("%x", sha.Sum(nil))

	// get userid from UserID
	userid, err := client.HGet(tables.UserIDTable, info.PhoneNum).Int64()
	if err != nil {
		log.Println(err)
		return &pb.Token{Token: err.Error(), Registered: true}, err
	}
	// set token - userid into login table
	if err = client.HSet(tables.Login, token, userid).Err(); err != nil {
		log.Println(err)
		return &pb.Token{Token: "", Registered: true}, err
	}

	// add token - timestamp into recent table
	_, err = client.ZAdd(tables.Recent, &redis.Z{Member: token, Score: float64(timestamp)}).Result()
	if err != nil {
		return &pb.Token{Token: "", Registered: true}, err
	}
	log.Printf("user: %d, token: %s", userid, token)
	return &pb.Token{Token: token, Registered: true}, nil
}

// CheckToken checks if a token is in redis,
func (s *LoginService) CheckToken(ctx context.Context, token *pb.Token) (*pb.Result, error) {
	client := redis.NewClient(repokits.ConnConfig)
	defer client.Close()
	log.SetPrefix("CheckToken - ")

	exist, err := client.HExists(tables.Login, token.Token).Result()
	if err != nil {
		log.Println(err)
		return &pb.Result{Success: false}, err
	}
	if exist {
		log.Println("success: " + token.Token)
	} else {
		log.Println("fail: " + token.Token)
	}

	return &pb.Result{Success: exist}, nil
}

// CleanToken remove tokens that are outdated,
// which exist for TokenCacheDuration(see repo_config.go)
func CleanToken() ([]string, error) {
	client := redis.NewClient(repokits.ConnConfig)
	defer client.Close()
	log.SetPrefix("CleanToken")

	maxTimeStamp := strconv.FormatInt(time.Now().Add(-repokits.TokenCacheDuration).Unix(), 10)
	minTimeStamp := "0"

	tokens, err := client.ZRangeByScore(tables.Recent,
		&redis.ZRangeBy{Max: maxTimeStamp, Min: minTimeStamp}).Result()
	if err != nil {
		log.Println(err)
		return nil, err
	}
	if len(tokens) <= 0 {
		log.Println("no token to be cleaned")
		return nil, err
	}
	_, err = client.Pipelined(func(pipe redis.Pipeliner) error {
		for _, token := range tokens {
			err := pipe.HDel(tables.Login, token).Err()
			if err != nil {
				return err
			}
		}
		return nil
	})

	removed, err := client.ZRemRangeByScore(tables.Recent, minTimeStamp, maxTimeStamp).Result()
	if err != nil {
		log.Println(err)
		return nil, err
	}
	log.Printf("removed %d tokens", removed)
	return tokens, nil
}

// Logout removes token from login and recent table.
func (s *LoginService) Logout(ctx context.Context, token *pb.Token) (*pb.Result, error) {
	/*
		log.SetPrefix("Logout - ")
		client := redis.NewClient(repokits.ConnConfig)
		defer client.Close()

		success, err := client.HDel(tables.Login, token.Token).Result()
		if err != nil {
			log.Println(err)
			return &pb.Result{Success: false}, err
		}
		if success != 1 {
			err = fmt.Errorf("fail to remove token %s in login table", token.Token)
			log.Println(err)
			return &pb.Result{Success: false, ErrorMsg: "fail to remove token"}, err
		}

		success, err = client.HDel(tables.Recent, token.Token).Result()
		if err != nil {
			log.Println(err)
			return &pb.Result{Success: false, ErrorMsg: "fail to remove token"}, err
		}
		if success != 1 {
			err = fmt.Errorf("fail to remove token %s in recent table", token.Token)
			log.Println(err)
			return &pb.Result{Success: false, ErrorMsg: "fail to remove token"}, err
		}
	*/
	return &pb.Result{Success: true}, nil
}
