#!/bin/bash
if [ "$#" -eq 0 ]
then INVENTORY="dev"
else INVENTORY=$1
fi
  
./build.sh
cd infrastructure/ansible/
ansible-playbook -i $INVENTORY deploy.yml
