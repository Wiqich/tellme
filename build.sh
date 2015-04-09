#!/bin/bash
cur=`cd $(dirname $0) ; pwd`
rm -f $cur/tellme-*-jar-with-dependencies.jar
mvn clean package
mv $cur/target/tellme-*-jar-with-dependencies.jar $cur
mvn clean
