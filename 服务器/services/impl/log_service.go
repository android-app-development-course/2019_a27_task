package impl

import (
	"context"
	"encoding/json"
	"github.com/go-redis/redis/v7"
	"google.golang.org/grpc/codes"
	"google.golang.org/grpc/status"
	"log"
	"task/entity"
	"task/repo/repokits"
	"task/repo/tables"
	pb "task/services/service"
)

type LogService struct {
	pb.UnimplementedLogServiceServer
}

func (*LogService) AddLog(ctx context.Context, req *pb.Log) (*pb.Result, error) {
	rdb := redis.NewClient(repokits.ConnConfig)
	defer rdb.Close()
	log.SetPrefix("AddLog - ")

	// check token
	logined, err := rdb.HExists(tables.Login, req.Token).Result()
	if err != nil {
		log.Println(err)
		return &pb.Result{Success:false, ErrorMsg: err.Error() },status.Error(codes.Internal, ErrInternalFailure.Error())
	} else if !logined {
		log.Println("User Not Login")
		return &pb.Result{Success:false, ErrorMsg: "User Not Login"}, status.Error(codes.Unauthenticated, "User Not Login")
	}

	key := tables.FormatLogKey(req.ProjectID)
	l := entity.LogItem{
		CommitedBy:  req.Name,
		CreatedDate: req.Date,
		Content:     req.Content,
		Done:        req.Done }
	jstr, err := json.Marshal(l)
	if err != nil {
		log.Printf("fail to marshal %v", l)
		return &pb.Result{Success:false, ErrorMsg: ErrInternalFailure.Error()}, status.Error(codes.Internal, ErrInternalFailure.Error())
	}
	if _, err := rdb.LPush(key, jstr).Result(); err != nil {
		log.Printf("fail to add Log[%v]", l)
		return &pb.Result{Success:false, ErrorMsg: ErrInternalFailure.Error()}, status.Error(codes.Internal, ErrInternalFailure.Error())
	}
	log.Printf("Success to Add Log: %v", l)
	return &pb.Result{Success:true}, nil
}

func (*LogService) PullLogs(req *pb.ProjectQuery, srv pb.LogService_PullLogsServer) error {
	rdb := redis.NewClient(repokits.ConnConfig)
	defer rdb.Close()
	log.SetPrefix("PullLogs - ")

	// check token
	logined, err := rdb.HExists(tables.Login, req.Token).Result()
	if err != nil {
		log.Println(err)
		return status.Error(codes.Internal, ErrInternalFailure.Error())
	} else if !logined {
		log.Println("User Not Login")
		return status.Error(codes.Unauthenticated, "User Not Login")
	}

	key := tables.FormatLogKey(req.ID)
	length, err := rdb.LLen(key).Result()
	if err == redis.Nil {
		length = 0
	} else if err != nil {
		log.Println(err)
		return status.Error(codes.Internal, ErrInternalFailure.Error())
	}
	logs, err := rdb.LRange(key, 0, length).Result()
	if err != nil {
		return status.Error(codes.Internal, ErrInternalFailure.Error())
	}
	for _, l := range logs {
		var e entity.LogItem
		if err := json.Unmarshal([]byte(l), &e); err != nil {
			log.Println(err)
			return status.Error(codes.Internal, ErrInternalFailure.Error())
		}
		if err := srv.Send(&pb.Log{
			Token:                req.Token,
			ProjectID:            req.ID,
			Name:                 e.CommitedBy,
			Date:                 e.CreatedDate,
			Done:                 e.Done,
			Content:              e.CommitedBy }); err != nil {
			return status.Error(codes.Internal, ErrInternalFailure.Error())
		}
	}
	log.Printf("Pull %d logs successfully", length)
	return nil
}

func (*LogService) SetStatus(ctx context.Context, req *pb.LogStatus) (*pb.Result, error) {
	rdb := redis.NewClient(repokits.ConnConfig)
	defer rdb.Close()
	log.SetPrefix("PullLogs - ")

	// check token
	logined, err := rdb.HExists(tables.Login, req.Token).Result()
	if err != nil {
		log.Println(err)
		return &pb.Result{Success:false, ErrorMsg: err.Error() },status.Error(codes.Internal, ErrInternalFailure.Error())
	} else if !logined {
		log.Println("User Not Login")
		return &pb.Result{Success:false, ErrorMsg: "User Not Login"}, status.Error(codes.Unauthenticated, "User Not Login")
	}

	key := tables.FormatLogKey(req.ProjectID)
	if exist, err := rdb.Exists(tables.FormatProjectKey(req.ProjectID)).Result(); err != nil {
		log.Println(err)
		return &pb.Result{Success:false, ErrorMsg: err.Error() },status.Error(codes.Internal, ErrInternalFailure.Error())
	} else if exist == 0 {
		log.Printf("Project[ID: %s] Not Found", req.ProjectID)
		return &pb.Result{Success:false, ErrorMsg: "Project Not Found" },status.Error(codes.InvalidArgument, "Project Not Found")
	}

	length, err := rdb.LLen(key).Result()
	if err != nil {
		log.Println(err)
		return &pb.Result{Success:false, ErrorMsg: err.Error() },status.Error(codes.Internal, ErrInternalFailure.Error())
	} else if length <= req.Index {
		log.Printf("Log[Index: %d] out of range", req.Index)
		return &pb.Result{Success:false, ErrorMsg: "Log Index Out of Range"}, status.Error(codes.OutOfRange, "Log Index Out of Range")
	}

	before, _ := rdb.LIndex(key, req.Index).Result()
	var item entity.LogItem
	json.Unmarshal([]byte(before), &item)
	item.Done = req.Done

	if after, err := json.Marshal(item); err != nil {
		log.Printf("fail to marshal LogItem[%v]", item)
		return &pb.Result{Success: false, ErrorMsg: ErrInternalFailure.Error()}, status.Error(codes.Internal, ErrInternalFailure.Error())
	} else {
		if err := rdb.LSet(key, req.Index, after).Err(); err != nil {
			log.Printf("fail to marshal LogItem[%s]", after)
			return &pb.Result{Success: false, ErrorMsg: ErrInternalFailure.Error()}, status.Error(codes.Internal, ErrInternalFailure.Error())
		}
	}
	return &pb.Result{Success:true}, nil
}
