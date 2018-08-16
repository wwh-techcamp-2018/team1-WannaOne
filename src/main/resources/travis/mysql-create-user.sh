#!/usr/bin/env bash

echo "Create woowanote team1 user"
mysql -u root -e "CREATE USER 'team1'@'%' IDENTIFIED BY 'team1;GRANT ALL PRIVILEGES ON woowanote.* TO 'team1'@'%';FLUSH PRIVILEGES;"