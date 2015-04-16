#!/bin/bash

python newparsing.py sample.txt 2&>1 >/dev/null

java -jar NewPreProcessing.jar

java -jar NewFeatureVector.jar

./svm-predict "/home/priyanka/Desktop/IRE/MajorProject/Data/newfeatures_processed_final_out" "newfeatures_processed_TRAINDATA.model" "OUTPUT" 2&>1 >/dev/null

