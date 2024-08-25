#!/bin/bash

docker pull zaproxy/zap-stable
docker run -i zaproxy/zap-stable zap-baseline.py -t "https://github.com/sayoungestguy/scaleup" -l PASS > zap_baseline_report.html

echo $? > /dev/null
