#!/bin/sh

# --batch to prevent interactive command --yes to assume "yes" for questions
gpg --quiet --batch --yes --decrypt --passphrase="$DECRYPT_KEY" \
--output properties.tar.gz properties.tar.gz.gpg
tar -xzvf properties.tar.gz
