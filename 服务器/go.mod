module task

go 1.13

require (
	github.com/eclipse/paho.mqtt.golang v1.2.0
	github.com/go-redis/redis/v7 v7.0.0-beta.4
	github.com/golang/protobuf v1.3.2
	golang.org/x/net v0.0.0-20190620200207-3b0461eec859 // indirect
	google.golang.org/grpc v1.23.0
)

replace google.golang.org/grpc => github.com/grpc/grpc-go v1.25.1
