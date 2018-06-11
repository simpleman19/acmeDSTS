pg_ctl init -D database
pg_ctl start -D database
createdb acme
createuser postgres
pg_ctl stop -D database
