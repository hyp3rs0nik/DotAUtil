package models

func findUserFromSlice(users []User, userId string) *User {
	for _, user := range users {
		if user.Id == userId {
			//log.Printf("found = %+v", user)
			return &user
		}
	}
	return nil
}
