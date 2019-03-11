package testing

import (
	"encoding/json"
	"fmt"
	"io/ioutil"
	"os"
)

type Config struct {
	ResourcesPath      string
	DbConnectionString string
}

func NewConfig() Config {
	configFilePath := os.Getenv("MYDOTA_CONFIG")
	fileData, err := ioutil.ReadFile(configFilePath)
	if err != nil {
		fmt.Println(err)
	}

	config := Config{}
	err = json.Unmarshal(fileData, &config)
	if err != nil {
		fmt.Println(err)
	}

	return config
}

func (self Config) GetResourcesPath() string {
	return self.ResourcesPath
}

func (self Config) GetDbConnectionString() string {
	return self.DbConnectionString
}

func (self *Config) GetFixturesPath() string {
	return self.ResourcesPath + "/fixtures"
}
