package impl

import (
	"context"
	"fmt"
	"github.com/go-redis/redis/v7"
	"google.golang.org/grpc/codes"
	"google.golang.org/grpc/status"
	"log"
	"task/repo/repokits"
	"task/repo/tables"
	pb "task/services/service"
)

var ErrInternalFailure = status.Error(codes.Internal, "Server Internal Failure")

type FriendService struct {
	pb.UnimplementedFriendServiceServer
}

func (*FriendService) AddFriend(ctx context.Context, req *pb.Profile) (*pb.Result, error) {
	rdb := redis.NewClient(repokits.ConnConfig)
	defer rdb.Close()
	log.SetPrefix("AddFriend - ")

	userID, err := rdb.HGet(tables.Login, req.Token).Int64()
	if err == redis.Nil {
		log.Println("User Not Login")
		return &pb.Result{Success: false, ErrorMsg:"User Not Login"}, nil
	} else if err != nil {
		log.Println(err)
		return &pb.Result{Success: false, ErrorMsg:"Server Internal Failure"}, nil
	}

	if exist, err := rdb.HExists(tables.UserIDTable, req.PhoneNum).Result(); err != nil {
		log.Println(ErrInternalFailure)
		return &pb.Result{Success:false, ErrorMsg:ErrInternalFailure.Error()}, status.Error(codes.Internal, ErrInternalFailure.Error())
	} else if !exist {
		f := fmt.Sprintf("Friend[PhoneNum: %s] Not Found", req.PhoneNum)
		log.Println(f)
		return &pb.Result{Success:false, ErrorMsg:ErrInternalFailure.Error()}, status.Error(codes.Internal, ErrInternalFailure.Error())
	}
	key := tables.FormatFriendSetKey(userID)
	count, err := rdb.SAdd(key, req.PhoneNum).Result()
	if err != nil {
		log.Println(fmt.Errorf("%w [UserID: %d]", err, userID))
		return &pb.Result{Success: false, ErrorMsg:"Server Internal Failure"}, nil
	} else if count == 0 {
		log.Printf("Fail to add friend[User PhoneNum: %s]", req.PhoneNum)
		return &pb.Result{Success: false, ErrorMsg:"Friend already Exists"}, nil
	} else {
		otherID, err := rdb.HGet(tables.UserIDTable,req.PhoneNum).Int64()
		if err != nil {
			log.Println(fmt.Errorf("%w [UserID: %d]", err, userID))
			return &pb.Result{Success: false, ErrorMsg:"Server Internal Failure"}, nil
		}
		key := tables.FormatFriendSetKey(otherID)
		myPhone, err := rdb.HGet(tables.FormatProfileKey(userID), "phonenum").Result()
		count, err := rdb.SAdd(key, myPhone).Result()
		if err != nil {
			log.Println(fmt.Errorf("%w [UserID: %d]", err, otherID))
			return &pb.Result{Success: false, ErrorMsg:"Server Internal Failure"}, nil
		} else if count == 0 {
			log.Printf("Fail to add friend[User PhoneNum: %s]", myPhone)
			return &pb.Result{Success: false, ErrorMsg: "Friend already Exists"}, nil
		}
	}

	log.Printf("Success Add Friend[User PhoneNum: %d] to User[UserID:%d]", req.PhoneNum, userID)
	return &pb.Result{Success:true, ErrorMsg: ""}, nil
}

func (*FriendService) DeleteFriend(ctx context.Context, req *pb.Profile) (*pb.Result, error) {
	rdb := redis.NewClient(repokits.ConnConfig)
	defer rdb.Close()
	log.SetPrefix("DeleteFriend - ")

	// check whether user logins
	userID, err := rdb.HGet(tables.Login, req.Token).Int64()
	if err == redis.Nil {
		log.Println("User Not Login")
		return &pb.Result{Success: false, ErrorMsg:"User Not Login"}, nil
	} else if err != nil {
		log.Println(err)
		return &pb.Result{Success: false, ErrorMsg:"Server Internal Failure"}, nil
	}

	// check friend's existence
	if exist, err := rdb.HExists(tables.UserIDTable, req.PhoneNum).Result(); err != nil {
		log.Println(ErrInternalFailure)
		return &pb.Result{Success:false, ErrorMsg:ErrInternalFailure.Error()}, status.Error(codes.Internal, ErrInternalFailure.Error())
	} else if !exist {
		f := fmt.Sprintf("Friend[PhoneNum: %s] Not Found", req.PhoneNum)
		log.Println(fmt.Errorf("%w %s", err, f ))
		return &pb.Result{Success:false, ErrorMsg:ErrInternalFailure.Error()}, status.Error(codes.Internal, ErrInternalFailure.Error())
	}


	key := tables.FormatFriendSetKey(userID)
	count, err := rdb.SRem(key, req.PhoneNum).Result()
	if err != nil {
		log.Println(fmt.Errorf("%w [UserID: %d, FriendPhoneNum: %s]", err, userID, req.PhoneNum))
		return &pb.Result{Success:false, ErrorMsg:"Server Internal Failure"}, err
	} else if count == 0 {
		log.Printf("User Not Found[UserID: %d, FriendPhoneNum: %s]", userID, req.PhoneNum)
		return &pb.Result{Success:false, ErrorMsg:"User Not Found"}, err
	}
	log.Printf("User[UserID: %d] delete Friend[PhoneNum: %s]", userID, req.PhoneNum)
	return &pb.Result{Success:true}, nil
}

func (*FriendService) GetFriends(req *pb.Token, srv pb.FriendService_GetFriendsServer) error {
	rdb := redis.NewClient(repokits.ConnConfig)
	defer rdb.Close()
	log.SetPrefix("DeleteFriend - ")

	userID, err := rdb.HGet(tables.Login, req.Token).Int64()
	if err == redis.Nil{
		log.Println("User Not Login")
		return  status.Error(codes.NotFound, tables.ErrUserNotFound.Error())
	} else if err != nil {
		log.Println(err)
		return ErrInternalFailure
	}

	key := tables.FormatFriendSetKey(userID)
	friends, err := rdb.SMembers(key).Result()
	if err != nil {
		log.Println(fmt.Errorf("%w [UserID: %d]", err, userID))
	}

	for _, phoneNum := range friends {
		uID, err := rdb.HGet(tables.UserIDTable, phoneNum).Int64()
		if  err != nil {
			return status.Error(codes.NotFound, "Member Not Found")
		}
		profile, err := rdb.HGetAll(tables.FormatProfileKey(uID)).Result()
		if err != nil {
			log.Println(err)
			return status.Error(codes.Internal, "Member's Profile Not Found")
		}
		if err := srv.Send(&pb.Profile{
			Token:                req.Token,
			Name:                 profile["name"],
			PhoneNum:             profile["phonenum"],
			Desription:           profile["description"],
			Email:                profile["email"],
			Workplace:            profile["workplace"]}); err != nil {
				err = status.Error(codes.DataLoss, err.Error())
				log.Println(err)
				return err
		}
	}
	return nil
}

