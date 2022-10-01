#!/bin/bash

set -o xtrace
set -e

if grep -E 'SwapTotal:\s+0+\s+kB' /proc/meminfo; then
    echo "Positively identified no swap space, creating some."
    dd if=/dev/zero of=/var/swapfile bs=1M count=512
    /sbin/mkswap /var/swapfile
    chmod 000 /var/swapfile
    /sbin/swapon /var/swapfile
else
    echo "Did not confirm zero swap space, doing nothing."
fi