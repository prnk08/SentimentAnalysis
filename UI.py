import wx
import os
import sys
def ask(parent=None,message='',default_value=''):
	dlg = wx.TextEntryDialog(parent,message,defaultValue=default_value)
	dlg.ShowModal()
	result=dlg.GetValue()
	dlg.Destroy()
	return result


app = wx.PySimpleApp()

x = ask(message = "Enter the Tweet to be analyzed")
while(x!=''):
	f = open('sample.txt','w')
	f.write("neutral\t"+x)
	f.close()
	os.system("bash newscript.sh")
	f = open('OUTPUT')
	f = f.readlines()
	print f[0][:-1]
	val = f[0][:-1]
	if val=='0':
		ans = "Neutral"
	elif val=='1':
		ans= "Positive"
	elif val=="-1":
		ans= "Negative"
	else:
		ans = "Couldn't decide"
	dlg = wx.MessageDialog(None,"Class that has Been Decided is "+ans+"\n\nDo you want to continue?","Twitter Analysis",wx.YES_NO | wx.ICON_QUESTION)
	ret = dlg.ShowModal()
	if(ret == wx.ID_NO):
		break
	dlg.Destroy()
	x = ask(message = "Enter the Tweet to be analyzed")

