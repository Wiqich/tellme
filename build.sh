#!/bin/bash
cur=`cd $(dirname $0) ; pwd`
rm -f $cur/tellme-*.jar
mvn clean package
mv $cur/target/tellme-*.jar $cur
mvn clean
