package test

import (
	"context"
	"google.golang.org/grpc"
	"io"
	"strconv"
	"task/repo/tables"
	pb "task/services/service"
	"testing"
)

var phoneNums = []string{"111", "222", "333"}

func CreateProjectAndInvite(t *testing.T, conn *grpc.ClientConn)  string {
	loginClient := pb.NewLoginServiceClient(conn)

	for _, num := range phoneNums {
		if result, err := loginClient.Register(context.Background(), &pb.LoginInfo{
			PhoneNum: num,
			Password: password}); err != nil {
			t.Fatal(err)
		} else if !result.Success {
			t.Fatal(result.ErrorMsg)
		}
	}

	projClient := pb.NewProjectServiceClient(conn)
	tk, err := loginClient.Login(context.Background(), &pb.LoginInfo{PhoneNum:phoneNums[0], Password:password})
	if err != nil {
		t.Fatal(err)
	}
	result, err := projClient.CreateProject(context.Background(), &pb.Project{
		Token:          tk.Token,
		Name:           projectName,
		LeaderPhoneNum: phoneNums[0]})
	if err != nil {
		t.Fatal(err)
	}
	if !result.Success {
		t.Fatal(result.ErrorMsg)
	}

	projectID, _ := strconv.ParseInt(result.ErrorMsg, 10, 64)
	for _, num := range phoneNums[1:] {
		if res, err := projClient.InviteMember(context.Background(), &pb.Member{
			Token:     tk.Token,
			ProjectID: projectID,
			PhoneNum:  num}); err != nil {
			t.Fatal(err)
		} else if !res.Success {
			t.Fail()
		}
	}
	return tk.Token
}

func Test_AskMessage(t *testing.T) {
	conn, err := grpc.Dial(host+":"+port, grpc.WithInsecure())
	defer conn.Close()
	defer cleanUp("project:1", "members:1", "user_proj:1", tables.ProjectIDCursor)

	if err != nil {
		t.Fatal(err)
	}

	CreateProjectAndInvite(t, conn)
	msgCli := pb.NewMessageServiceClient(conn)
	loginCli := pb.NewLoginServiceClient(conn)

	token, err := loginCli.Login(context.Background(), &pb.LoginInfo{PhoneNum:phoneNums[2], Password:password})
	if err != nil {
		t.Fatal(err)
	}

	srv, err := msgCli.AskMessage(context.Background(), &pb.Token{Token:token.Token})
	if err != nil {
		t.Fatal(srv)
	}
	for {
		if msg, err := srv.Recv(); err == io.EOF {
			break
		} else if err != nil {
			t.Fatal(err)
		} else {
			if msg.Content != "" {
				continue
			}
		}
	}
}