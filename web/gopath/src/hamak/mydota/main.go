package main

import (
	"hamak/mydota/api"
	"hamak/mydota/components"
	"hamak/mydota/cron"
	"hamak/mydota/db"
	"log"
	"os"
)

func main() {
	configFilePath := os.Getenv("MYDOTA_CONFIG")
	if _, err := os.Stat(configFilePath); os.IsNotExist(err) {
		log.Fatal("Could not find or access configuration file")
	}

	config, err := components.GetConfig(configFilePath)
	if err != nil {
		log.Fatal(err)
	}

	conn, err := db.NewConnection(*config)
	if err != nil {
		log.Fatal(err)
	}

	cron := cron.NewCron(conn)
	cron.Start()

	api := api.NewApi(config, conn)
	api.Start()
}
