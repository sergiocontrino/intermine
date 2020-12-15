echo 'tesdtDD....'
export PGPASSWORD=postgres
echo "${PGPASSWORD}"
echo "update pg_hba.conf"
echo "host all all all md5" >> pg_hba.conf
psql -U postgres -c "create database imtest;"
