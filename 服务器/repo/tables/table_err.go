package tables

import (
	"fmt"
)

// KeyNotFoundErr is throws when key can't be found
type KeyNotFoundErr struct {
	TName string
	Key   string
}

func (k *KeyNotFoundErr) Error() string {
	return fmt.Sprintf("[%s] is not found in %s", k.Key, k.TName)
}

// EmptyStringErr is throws when key or value's empty string.
type EmptyStringErr struct {
	TName string
	Str   string
}

func (e *EmptyStringErr) Error() string {
	return fmt.Sprintf("|%s|'s field [%s] can't be empty", e.TName, e.Str)
}
