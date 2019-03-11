package testing

import (
	"github.com/jinzhu/gorm"
	"github.com/lann/squirrel"
	"gopkg.in/yaml.v1"
	"hamak/mydota/utils"
	"io/ioutil"
)

func ClearDbTables(config Config) {
	db, err := gorm.Open("postgres", config.GetDbConnectionString())
	utils.PanicIfErr(err)

	db.Exec("DELETE FROM leagues;")
	db.Exec("DELETE FROM econ_items;")
	db.Exec("DELETE FROM users;")

	path := config.GetFixturesPath() + "/default.yml"
	fileData, err := ioutil.ReadFile(path)
	utils.PanicIfErr(err)

	data := make(map[interface{}]interface{})
	err = yaml.Unmarshal(fileData, &data)
	utils.PanicIfErr(err)

	for key, value := range data {
		table := key.(string)
		rows := value.([]interface{})
		for _, value := range rows {
			row := value.(map[interface{}]interface{})

			fields := []string{}
			values := make([]interface{}, 0)
			for key, value := range row {
				field := key.(string)
				fields = append(fields, field)
				values = append(values, value)
			}

			sql, args, err := squirrel.Insert(table).Columns(fields...).Values(values...).ToSql()
			utils.PanicIfErr(err)

			db.Exec(sql, args...)
		}
	}
}
