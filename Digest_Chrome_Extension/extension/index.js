
// document.getElementById('clickMe').addEventListener("click",function(){
// 	chrome.tabs.executeScript({
// 		file: 'redirect.js'
// 	});
// });

chrome.browserAction.onClicked.addListener(function(){
	chrome.tabs.executeScript({
		file: 'redirect.js'
	});
});