package test

import (
	"context"
	"google.golang.org/grpc"
	"io"
	pb "task/services/service"
	"testing"
)

func Test_AddFriend(t *testing.T) {
	conn, err := grpc.Dial(host+":"+port, grpc.WithInsecure())
	defer conn.Close()
	defer cleanUp()
	if err != nil {
		t.Fatal(err)
	}
	client := pb.NewLoginServiceClient(conn)
	friend := pb.NewFriendServiceClient(conn)
	phoneNums := []string{"123", "1234", "12345"}
	for _, num := range phoneNums {
		result, err := client.Register(context.Background(), &pb.LoginInfo{PhoneNum: num, Password: password})
		if err != nil {
			t.Fatal(err)
		}
		if !result.Success {
			t.Fatal(result.ErrorMsg)
		}
	}
	token, err := client.Login(context.Background(), &pb.LoginInfo{PhoneNum: "123", Password:password})
	if err != nil {
		t.Fatal(err)
	}

	result, err := friend.AddFriend(context.Background(), &pb.Profile{Token: token.Token, PhoneNum:"12345"})
	if err != nil {
		t.Fatal(err)
	}
	if !result.Success {
		t.Fatal(result.ErrorMsg)
	}
}

func Test_DeleteFriend(t *testing.T) {
	conn, err := grpc.Dial(host+":"+port, grpc.WithInsecure())
	defer conn.Close()
	defer cleanUp("friend:1")
	if err != nil {
		t.Fatal(err)
	}
	client := pb.NewLoginServiceClient(conn)
	friend := pb.NewFriendServiceClient(conn)
	phoneNums := []string{"123", "1234", "12345"}
	for _, num := range phoneNums {
		result, err := client.Register(context.Background(), &pb.LoginInfo{PhoneNum: num, Password: password})
		if err != nil {
			t.Fatal(err)
		}
		if !result.Success {
			t.Fatal(result.ErrorMsg)
		}
	}
	token, err := client.Login(context.Background(), &pb.LoginInfo{PhoneNum: "123", Password:password})
	if err != nil {
		t.Fatal(err)
	}

	result, err := friend.AddFriend(context.Background(), &pb.Profile{Token: token.Token, PhoneNum:"12345"})
	if err != nil {
		t.Fatal(err)
	}
	if !result.Success {
		t.Fatal(result.ErrorMsg)

	}

	result, err = friend.DeleteFriend(context.Background(), &pb.Profile{Token:token.Token, PhoneNum:"12345"})
	if err != nil {
		t.Fatal(err)
	}
	if !result.Success {
		t.Fatal(result.ErrorMsg)
	}
}


func Test_GetFriends(t *testing.T) {
	conn, err := grpc.Dial(host+":"+port, grpc.WithInsecure())
	defer conn.Close()
	defer cleanUp("profile:1", "profile:2", "profile:3", "friend:1", "friend:2", "friend:3")
	if err != nil {
		t.Fatal(err)
	}
	client := pb.NewLoginServiceClient(conn)
	friend := pb.NewFriendServiceClient(conn)
	profile := pb.NewProfileServiceClient(conn)
	phoneNums := []string{"123", "1234", "12345"}
	for _, num := range phoneNums {
		token := RegisterAndLogin(t, client, num)
		result, err := profile.SetProfile(context.Background(), &pb.Profile{Token: token, PhoneNum:num})
		if err != nil {
			t.Fatal(err)
		}
		if !result.Success {
			t.Fatal(result.ErrorMsg)
		}
	}
	token, err := client.Login(context.Background(), &pb.LoginInfo{PhoneNum: "123", Password:password})
	if err != nil {
		t.Fatal(err)
	}

	result, err := friend.AddFriend(context.Background(), &pb.Profile{Token: token.Token, PhoneNum:phoneNums[1]})
	if err != nil {
		t.Fatal(err)
	}
	if !result.Success {
		t.Fatal(result.ErrorMsg)
	}

	result, err = friend.AddFriend(context.Background(), &pb.Profile{Token: token.Token, PhoneNum:phoneNums[2]})
	if err != nil {
		t.Fatal(err)
	}
	if !result.Success {
		t.Fatal(result.ErrorMsg)
	}

	rs, err := friend.GetFriends(context.Background(), token)
	if err != nil {
		t.Fatal(err)
	}
	for {
		profile, err := rs.Recv()
		if err == io.EOF {
			break
		}
		if !(profile.PhoneNum == phoneNums[1] || profile.PhoneNum == phoneNums[2]) {
			t.FailNow()
		}
	}
}