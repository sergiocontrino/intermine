echo 'tesdtDD....'
export PGPASSWORD=postgres
echo "${PGPASSWORD}"
psql -U postgres -d postgres -c "create database imtest"
