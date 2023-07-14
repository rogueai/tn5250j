from org.tn5250j.tools.encoder import EncodeComponent

from java.text import SimpleDateFormat
from java.util import Date
from java.lang import String
from java.lang import StringBuffer
from java.lang import System
from java.io import File

def fillBuffer(screen, sb):
    chars = screen.getScreenAsChars()
    c = screen.getColumns()
    l = screen.getRows() * c

    x = 0
    y = 0
    while x < l:
        sb.append(chars[x])
        if y == c - 1:
            sb.append('\n')
            y = 0
        else :
            y += 1
        x += 1

print "--------------- tn5250j printScreen script start ------------"

home = System.getProperty("user.home")
filename = 'screen-' + SimpleDateFormat("yyyyMMdd-HHmmss").format(Date())
filepath = home + '/Documents/as400/screens/' + filename

screen = _session.getScreen()
sb = StringBuffer()

fillBuffer(screen, sb)

screen_txt = open(filepath + '.txt','w')
screen_txt.write(sb.toString())
screen_txt.close()

screen_png = open(filepath + '.png','w')
EncodeComponent.encode(EncodeComponent.PNG, _session, screen_png)
screen_png.close()

print "---------------- tn5250j printScreen script end -------------"
