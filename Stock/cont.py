
# coding=utf8

'''
Created on December 02, 2020   07:30 PM
Last revision: December 02, 2020   09:30 PM
author: Chao-Hsuan Ke

'''

from collections import Counter

'''
Read test
'''

filePath = "D:\\Phelps\\GitHub\\Medium\\Stock\\";        # file path
fileName = "TWSETPEX.txt";        # file name

f = open(filePath + fileName, 'r', encoding="utf-8")
#print(f.read())                 # print all at once time
#print(len(f.readlines()))      # all line number

# read each lines
count = 0
idList = []
nameList = []
for line in f:
    #print(line[0])
    sep = line.split('\t')
#    print(count, sep[0], sep[1])
    idList.append(sep[0])
    nameList.append(sep[1])
    count+=1

#print(len(idList))
#print(len(nameList))

f.close()

'''
get first work (Chinese work) 
'''

count = 0
firstWork = []
for index in idList:
    print(index, nameList[count])
    firstWork.append(nameList[count][0])
    count+=1

#print(firstWork)

'''
repeat word count
'''

print(Counter(firstWork))


