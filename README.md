# Digest
![Logo](https://github.com/nvrocks/Digest/blob/master/images/logo.png)

Digest is an android application + chrome extension which lets you summarize long articles, audio & video files.

# Requirements
* Python 3.x 
* nltk library   
* Android Studio

# Working of android app
* For summarizing video files, firstly the video file is converted to audio file through "ffmpeg library" and then text is extracted from audio through IBM Watson.This text then summarized using machine learning algorithm.
* For audio, it is first converted into text and then summarized.
* For images, text is extracted using OCR and then it is summarized.

# Chrome extension
It gives the summary of the selected text. In backend Django server is used which runs the python script to provide the needed functionality.
