# Digest
Digest is an application + chrome extension which lets you summarize long articles, audio & video files.

# Requirements
* Python 3.x
* nltk library
* Android Studio

# Working
For video summary, firstly mp4 file is converted to aac file through ffmpeg library and then text is extracted from audio through IBM Watson. Text is extracted from image through OCR. Then textrank algorithm is applied to text to make its summary.
