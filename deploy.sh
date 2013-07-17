#!/bin/bash

die () {
  echo >&2 "$@"
  exit 1
}

if [ "$#" -eq 0 ]
then INVENTORY="dev"
else INVENTORY=$1
fi

[ -e infrastructure/ansible/$INVENTORY ] || die "Inventory file $INVENTORY not found"

./build.sh
cd infrastructure/ansible/
ansible-playbook -i $INVENTORY deploy.yml
