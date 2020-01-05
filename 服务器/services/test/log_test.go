package test

import (
	"context"
	"google.golang.org/grpc"
	"io"
	pb "task/services/service"
	"testing"
	"time"
)

func Test_AddLog(t *testing.T) {
	conn, err := grpc.Dial(host+":"+port, grpc.WithInsecure())
	defer conn.Close()
	defer cleanUp()
	if err != nil {
		t.Fatal(err)
	}
	loginCli := pb.NewLoginServiceClient(conn)
	token := RegisterAndLogin(t, loginCli, phoneNum)
	
	logCli := pb.NewLogServiceClient(conn)

	projClient := pb.NewProjectServiceClient(conn)
	result, err := projClient.CreateProject(context.Background(), &pb.Project{
		Token:          token,
		Name:           projectName,
		ID:             0,
		LeaderPhoneNum: phoneNum,
		CreateDate: time.Now().Unix()})
	if err != nil {
		t.Fatal(err)
	}
	if !result.Success {
		t.Fatal(result.ErrorMsg)
	}


	result, err = logCli.AddLog(context.Background(), &pb.Log{
		Token:     token,
		ProjectID: 1,
		Name:      "admin",
		Date:      time.Now().Unix(),
		Done: false,
	})

	if err != nil {
		t.Fatal(err)
	}
	if ! result.Success {
		t.FailNow()
	}
	
	rsv ,err := logCli.PullLogs(context.Background(), &pb.ProjectQuery{
		Token: token,
		ID:    1,
	})
	for {
		if l, err := rsv.Recv(); err == io.EOF {
			break
		} else if err != nil {
			t.Fatal(err)
		} else if l == nil {
			t.Fatal(l)
		}
	}

	result, err = logCli.SetStatus(context.Background(), &pb.LogStatus{
		Token:                token,
		ProjectID:            1,
		Index:                0,
		Done:                 true})
	if err != nil {
		t.Fatal(err)
	}
}

