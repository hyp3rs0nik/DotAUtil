#!/bin/bash -x

GOPATH='/var/apps/mydota/gopath'
APP_PATH='/var/apps/mydota/current/web/gopath'

export GOPATH=$GOPATH
export PATH=$GOPATH/bin:$PATH

echo `rm -rf $GOPATH/src/hamak`
echo `cp -rf $APP_PATH/src/* $GOPATH/src`
echo `go get github.com/theplant/pak`
cd $GOPATH/src/hamak/mydota
echo `pak get`
echo `go install`
echo `cp -rf $APP_PATH/src/hamak/mydota/resources $GOPATH/bin`
