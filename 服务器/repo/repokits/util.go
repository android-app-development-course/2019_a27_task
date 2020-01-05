package repokits

import (
	"errors"
	"fmt"
	"github.com/go-redis/redis/v7"
	"log"
	"reflect"
	"strings"
	"time"
)

func init() {
	if TokenCacheDuration <= 0 {
		panic("illegal TokenCacheDuration")
	}
	log.Printf("start with options: %v", *ConnConfig)
}

// Struct2Map convert struct to map[string]interface{}.
// Therefore, stru must be struct kind.
// Note: DO NOT PASS pointer
// Since redis can't store slice or array as hash value,
// stru can't have array or slice field.
// A struct's field can use tag to define it's key
// example:
// 	   type MyStruct struct {
//			a int `key:"key name"`
//			b int `key:"key name"`
//			c int `key:~` // delete this field
// 	   }
func Struct2Map(stru interface{}) map[string]interface{} {
	t := reflect.TypeOf(stru)
	v := reflect.ValueOf(stru)
	if k := t.Kind(); k != reflect.Struct {
		panic("Struct2Map can only convert struct")
	}
	res := make(map[string]interface{})
	for i := 0; i < t.NumField(); i++ {
		var key string
		if k, ok := t.Field(i).Tag.Lookup("key"); !ok {
			key = strings.ToLower(t.Field(i).Name)
		} else {
			key = k
		}
		if key == "~" {
			continue
		}
		if kind := v.Field(i).Kind(); kind == reflect.Slice ||
			kind == reflect.Array {
			panic("you can't format a struct with slice or array field")
		}
		if _, ok := res[key]; ok {
			panic("have duplicated keys")
		} else {
			res[key] = v.Field(i).Interface()
		}
	}
	return res
}

// KeyIncrementTx use redis transaction to increment a key
func KeyIncrementTx(client *redis.Client, key string, maxretries int) error {
	// Transactionally increments key using GET and SET commands.
	increment := func(key string) error {
		txf := func(tx *redis.Tx) error {
			// get current value or zero
			n, err := tx.Get(key).Int64()
			if err != nil && err != redis.Nil {
				return err
			}
			// actual opperation (local in optimistic lock)
			n++
			// runs only if the watched keys remain unchanged
			_, err = tx.Pipelined(func(pipe redis.Pipeliner) error {
				// pipe handles the error case
				pipe.Set(key, n, 0)
				return nil
			})
			return err
		}

		for retries := maxretries; retries > 0; retries-- {
			err := client.Watch(txf, key)
			if err != redis.TxFailedErr {
				return err
			}
			// optimistic lock lost
		}
		return errors.New("increment reached maximum number of retries")
	}
	if err := increment(key); err != nil {
		return fmt.Errorf("increment error: %w", err)
	}
	return nil
}

// ToSec parse duration into seconds
func ToSec(hour int, min int, sec int) (float64, error) {
	dur, err := time.ParseDuration(fmt.Sprintf("%dh%dm%d", hour, min, sec))
	return dur.Seconds(), err
}
