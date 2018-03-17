import pyperclip,re
from django.http import HttpResponse
from django.shortcuts import render
import nltk
# nltk.download('stopwords')
# nltk.download('punkt')
from nltk.tokenize import sent_tokenize,word_tokenize
from nltk.corpus import stopwords
from collections import defaultdict
from string import punctuation
from heapq import nlargest



class FrequencySummarizer:

	def __init__(self, min_cut=0.1, max_cut=0.9):
		self._min_cut = min_cut
		self._max_cut = max_cut
		self._stopwords = set(stopwords.words('english') + list(punctuation))

	def _compute_frequencies(self, word_sent):
		freq = defaultdict(int)
		for s in word_sent:
			for word in s:
				if word not in self._stopwords:
					freq[word] += 1
	    # frequencies normalization and fitering
		m = float(max(freq.values()))
		for w in freq.keys():
			freq[w] = freq[w]/m
		if freq[w] >= self._max_cut or freq[w] <= self._min_cut:
			freq[w] = 0
		return freq
	def summarize(self, text, n):
		"""
			Return a list of n sentences 
			which represent the summary of text.
		"""
	
		sents = sent_tokenize(text)
		assert n <= len(sents)
		word_sent = [word_tokenize(s.lower()) for s in sents]
		self._freq = self._compute_frequencies(word_sent)
		ranking = defaultdict(int)
		for i,sent in enumerate(word_sent):
			for w in sent:
				if w in self._freq:
					if self._freq[w] == 0:
						continue
				ranking[i] += self._freq[w]
		sents_idx = self._rank(ranking, n)    
		return [sents[j] for j in sents_idx]


	def _rank(self, ranking, n):
		""" return the first n sentences with highest ranking """
		return nlargest(n, ranking, key=ranking.get)



def summarize_ext(request):

	prep_list = ["is", "The","the", "A", "to", "be", "are", "Really", "really", "Very", "its","Its","also","very", "Just", "just", "That", "that", "Then", "then", "Totally", "totally", "Completely", "completely", "Absolutely", "absolutely", "literally", "Literally", "Definitely", "definitely", "certainly", "Certainly", "actually", "Actually", "Basically", "basically", "virtually", "Virtually", "Rather", "rather", "quite", "Quite", "Somehow", "somehow", "somewhat", "Somewhat"]
	summ = []
	summ1 = []
	summ2 = []
	new_list = []
	list1 = []
	fs = FrequencySummarizer()
	# text = "A purely peer-to-peer version of electronic cash would allow online payments to be sent directly from one party to another without going through a financial institution. Digital signatures provide part of the solution, but the main benefits are lost if a trusted third party is still required to prevent double-spending. We propose a solution to the double-spending problem using a peer-to-peer network. The network timestamps transactions by hashing them into an ongoing chain of hash-based proof-of-work, forming a record that cannot be changed without redoing the proof-of-work. The longest chain not only serves as proof of the sequence of events witnessed, but proof that it came from the largest pool of CPU power. As long as a majority of CPU power is controlled by nodes that are not cooperating to attack the network, they'll generate the longest chain and outpace attackers. The network itself requires minimal structure. Messages are broadcast on a best effort basis, and nodes can leave and rejoin the network at will, accepting the longest proof-of-work chain as proof of what happened while they were gone."
	text = str(pyperclip.paste())
	sentence = 0
	for c in text:
		if c is '.':
			sentence+=1

# print("*",sentence);
	sentence = sentence//3
# print("*",sentence);

	for s in fs.summarize(text, sentence):
		summ.append(s)

	for s in summ:
		for word in s.split():
			list1.append(word)
			if word in prep_list:
				new_list.append(word)
				s = s.replace(word,'')
		summ1.append(s)

	for s in summ1:
		s = s.replace('  ', ' ')
		summ2.append(s)
	summary1 = ""
	for s in summ2:
		summary1 += s

	summary = re.sub(r'\[\d+\]',"",summary1)
	print(summary)

	return render(request,'extension.html',{'data':summary,'list':new_list,'list1':list1})






def processit(request):
	data = request.GET['str']
	return render(request,'process.html',{'data':data})
	# return (request,'templates/process.html')
