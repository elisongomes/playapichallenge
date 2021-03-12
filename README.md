# CRM Challenge

## _Play Framework with REST API + Ebean ORM + H2 + Pipedrive_

The purpose of this project is to demonstrate how use the [Play Framework](https://www.playframework.com/) with [H2 Database](https://www.h2database.com) and [EBean ORM](https://ebean.io/) integrating on [Pipedrive CRM](https://www.pipedrive.com).

## Configuring application

Change the `application.conf` file located in `conf` folder:

```
crm.api.token="You pipedrive token"

db.default.driver=org.h2.Driver
db.default.url="jdbc:h2:mem:[database_name]"
db.default.user=[user]
db.default.password=[password]
```

## Run application


```
sbt run
```

## Exploring the REST APIs

The application contains the following REST APIs:

Method | Target | Description
-------|--------|------------
`GET` | `/users` | List all users stored in database
`GET` | `/users/{id}` | Get an user by the `id`
`POST` | `/users` | Insert an user to database. Fields: `name` (string), `email` (string).
`GET` | `/leads` | List all leads stored in database
`POST` | `/leads` | Insert an lead to database. Fields: `name` (string), `email` (string), `company` (string), `site` (string), `notes` (string), `phones` (array), `user_id` (int).
`POST` | `/leads/{id}/finish` | Finish an lead and save on Pipedrive. Fields: `status` (string - WON or LOST).