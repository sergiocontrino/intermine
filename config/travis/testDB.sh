echo 'tesdtDD....'
export PGPASSWORD=postgres
echo "${PGPASSWORD}"
psql -h localhost -U postgres -d postgres -c "create database imtest;"
