package components

import (
	"encoding/json"
	"io/ioutil"
)

type Config struct {
	ResourcesPath      string
	DbConnectionString string
}

func GetConfig(path string) (*Config, error) {
	fileData, err := ioutil.ReadFile(path)
	if err != nil {
		return nil, err
	}

	config := Config{}
	err = json.Unmarshal(fileData, &config)
	return &config, err
}

func (self Config) GetResourcesPath() string {
	return self.ResourcesPath
}

func (self Config) GetDbConnectionString() string {
	return self.DbConnectionString
}
