#!/bin/bash
cname=ch$(seq -w 01 $1 | tail -n 1)
mkdir -p $cname
(cd $cname; mkdir htdocs webapps; echo "# ${1}章" > Readme.md)
