pg_ctl init -D db
pg_ctl start -D db
createdb acme
createuser postgres
pg_ctl stop -D db
