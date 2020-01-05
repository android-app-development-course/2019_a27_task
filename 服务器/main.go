package main

import (
	"fmt"
	"google.golang.org/grpc"
	"log"
	"net"
	"task/services/impl"
	pb "task/services/service"
)

func main() {
	lis, err := net.Listen("tcp", ":50050")
	if err != nil {
		log.Fatalf("fail to listen: %+v", err)
	}
	s := grpc.NewServer()
	pb.RegisterLoginServiceServer(s, &impl.LoginService{})
	pb.RegisterProfileServiceServer(s, &impl.ProfileService{})
	pb.RegisterProjectServiceServer(s, &impl.ProjectService{})
	pb.RegisterMessageServiceServer(s, &impl.MessageService{})
	pb.RegisterFriendServiceServer(s, &impl.FriendService{})
	pb.RegisterLogServiceServer(s, &impl.LogService{})
	fmt.Printf("start to serve at %v\n", lis.Addr())
	if err := s.Serve(lis); err != nil {
		log.Fatalf("failed to serve: %v", err)
	}

}
