# coding=utf8

'''
version: December 05, 2019 05:31 PM
Last revision: December 09, 2019 07:25 PM

Author : Chao-Hsuan Ke

BeautifulSoup
https://blog.gtwang.org/programming/python-beautiful-soup-module-scrape-web-pages-tutorial/2/

lytic source
魔鏡歌詞
https://mojim.com/

'''

import requests
from bs4 import BeautifulSoup

url = 'https://mojim.com/twy100012x70x1.htm'	# 歌詞位址
name = '玫瑰少年'								# 歌詞標題 (歌名)
extension = '.txt'								# 副檔名

#output
outputFolder = ""               				# 歌詞儲存資料夾位置
outputFile = name                               # 歌詞儲存名稱 (由 name + extension 組成)
# open file
fp = open(outputFolder + name + extension, 'a', encoding='UTF-8')


r = requests.get(url)
html_str = r.text

# response state
#print(r.status_code)

# parsing data
soup = BeautifulSoup(html_str, 'html.parser')

link_tag = soup.find(id='fsZx3')

#encoding
link_tag.encode('UTF-8')

#ouptut
fp.writelines(link_tag.text)
fp.close()

print('finished')

