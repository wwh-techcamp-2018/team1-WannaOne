#!/usr/bin/env bash

echo "upgrade mysql version"

echo mysql-apt-config mysql-apt-config/select-server select mysql-5.7 | sudo debconf-set-selections
wget http://dev.mysql.com/get/mysql-apt-config_0.8.1-1_all.deb
sudo DEBIAN_FRONTEND=noninteractive dpkg -i mysql-apt-config_0.8.1-1_all.deb
sudo apt-get update -q
sudo apt-get install -q --force-yes -o Dpkg::Options::=--force-confnew mysql-server
sudo mysql_upgrade --force