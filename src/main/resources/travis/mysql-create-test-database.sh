#!/usr/bin/env bash

echo "Create woowanote DB"
mysql -u root -e 'CREATE DATABASE IF NOT EXISTS woowanote;'