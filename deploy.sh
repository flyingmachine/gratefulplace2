#!/bin/bash

./build.sh
cd infrastructure/ansible/
ansible-playbook -i dev deploy.yml
