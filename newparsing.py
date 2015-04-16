import subprocess
import os
import sys
import re
from string import ascii_lowercase
farg = open(sys.argv[1])
f2 = open("temp",'w')
f = farg.readlines()
count = 0
count2 = 0
##print len(f)
for line in f:
		temp = line.split("\t")
		if(temp[1][-1] != '\n'):
			temp[1] = temp[1] + '\n'
		f2.write(temp[1])
		count+=1
#print count
#print count2
#print 
farg.close()
f2.close()
#os.system("wc -l temp")
#os.system("wc -l temp2")
os.system("./ark-tweet-nlp-0.3.2/runTagger.sh temp > temp2")
#subprocess.check_call(["./ark-tweet-nlp-0.3.2/twokenize.sh","temp","> temp2"])

STOPWORDS = ['a','able','about','across','after','all','almost','also','am','among','an','and','any','are','as','at','be','because','been','but','by','can','cannot','could','dear','did','do','does','either','else','ever','every','for','from','get','got','had','has','have','he','her','hers','him','his','how','however','i','if','in','into','is','it','its','just','least','let','like','likely','may','me','might','most','must','my','neither','no','nor','not','of','off','often','on','only','or','other','our','own','rather','said','say','says','she','should','since','so','some','than','that','the','their','them','then','there','these','they','this','tis','to','too','twas','us','wants','was','we','were','what','when','where','which','while','who','whom','why','will','with','would','yet','you','your']

acronymDict = {}
for c in ascii_lowercase:
	af = open("./acronyms/"+c)
	af = af.readlines()
	acronymDict[c] = {}
	for l in af:
		l = l[:-1]
		l = l.split(' - ')
		acronymDict[c][l[0]] = l[1].split()


f4 = open("temp4",'w')
f2 = open("temp2")
if2 = f2.readlines()
#print
#print len(if2)
for i in range(len(if2)):
	if2[i] = if2[i].split('\t')
	token = if2[i][0].split()
	finaltoken = []
	tweet = ''
	for j in token:
		j = j.lower()
		try:
			if j in acronymDict[j[0]].keys():
				for k in acronymDict[j[0]][j]:
					tweet = tweet + ' ' + k
			else:
				tweet = tweet + ' ' + j
		except:
			tweet = tweet + ' ' + j
	tweet = tweet[1:]
	f4.write(tweet+'\n')

f2.close()
f4.close()
os.system("mv temp2 wtf")
os.system("./ark-tweet-nlp-0.3.2/runTagger.sh temp4 > temp2")
	
f = open(sys.argv[1])
f = f.readlines()
f2 = open("temp2")
f2 = f2.readlines()
f3 = open("final_out",'w')
for i in range(len(f2)):
	f[i] = f[i].split('\t')
	f2[i] = f2[i].split('\t')
	tokens = f2[i][0].split()
	tags = f2[i][1].split()
	finaltoken = []
	finaltags = []
	for j in range(len(tokens)):
		if tags[j] != 'U'and tags[j] != '@':
			if tags[j] == '#':
				tokens[j] = tokens[j][1:]
			tokens[j] = re.sub(r'(.)\1+', r'\1\1',tokens[j].lower())
			finaltoken.append(tokens[j].lower())
			finaltags.append(tags[j])
	f3.write(str(f[i][0])+' '+str(finaltoken)+' '+str(finaltags)+'\n')
f3.close()


#os.system("rm -rf temp temp2 temp4")
