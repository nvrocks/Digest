![Logo](https://github.com/nvrocks/Digest/blob/master/images/logo.png)

# Digest
Digest is an android application + chrome extension which lets you summarize long articles, audio & video files.

# Requirements
* Python 3.x  ![Logo](https://github.com/nvrocks/Digest/blob/master/images/python_nltk.png)
* nltk library   
* Android Studio

# Working
For video summary, firstly mp4 file is converted to aac file through ffmpeg library and then text is extracted from audio through IBM Watson. Text is extracted from image through OCR. Then textrank algorithm is applied to text to make its summary.
