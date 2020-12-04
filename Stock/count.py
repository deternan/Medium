
# coding=utf8

'''
Created on December 02, 2020   07:30 PM
Last revision: December 04, 2020   08:36 PM
author: Chao-Hsuan Ke

'''

from collections import Counter

'''
Read test
'''

#filePath = "D:\\Phelps\\GitHub\\Medium\\Stock\\";        # file path
filePath = '/Users/phelps/Documents/github/Medium/Stock/';       # mac file path
fileName = "TWSETPEX.txt";        # file name

f = open(filePath + fileName, 'r', encoding="utf-8")


# read each lines
count = 0
idList = []
nameList = []
removeWord = ''
for line in f:
    #print(line[0])
    sep = line.split('\t')
#    print(count, sep[0], sep[1])
    idList.append(sep[0])

    '''
    remove -KY
    '''
    if 'KY' in sep[1]:
        removeWord = sep[1].replace('-KY', '')
    else:
        removeWord = sep[1]

    removeWord.replace("\n", '')
    removeWord.replace("\r", '')
    nameList.append(removeWord)
    #print(sep[0], len(removeWord.strip()), removeWord.strip()[-1])
    count += 1


#print(len(idList))
#print(len(nameList))

f.close()

'''
get first work (Chinese work) 
'''

count = 0
firstWord = []
for index in idList:
    #print(index, nameList[count])
    firstWord.append(nameList[count][0])
    count+=1

'''
repeat word count
'''

#print(Counter(firstWord))

'''
get last work (Chinese work) 
'''

count = 0
lastWord = []
for index in idList:
    lastWord.append(nameList[count].strip()[-1])
    count+=1


'''
repeat word count
'''
#print(Counter(lastWord))

'''
convert to dict
'''
# first word
dstart = {}
cnt = Counter(Counter(lastWord))
for key, value in cnt.items():
    dstart[key] = value

# last word
dlast = {}
cnt = Counter(Counter(lastWord))
for key, value in cnt.items():
    dlast[key] = value
    #print(d[key], key)

'''
dict sort
'''
dictItem = {}
dictItem = dstart
sort_orders = sorted(dictItem.items(), key=lambda x: x[1], reverse=True)
# for i in sort_orders:
# 	print(i[0], i[1])

'''
search & list
'''
inputStr = '台'
indexCount = 0
printStr = ''
for index in nameList:
    #print(index, index[:1])
    if inputStr == index[:1]:
        printStr = "%s (%s)" %(index, idList[indexCount])
        print(printStr, end='')
        printStr = ''
        #print(index, idList[indexCount])
    indexCount += 1

