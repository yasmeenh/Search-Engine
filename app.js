var express = require('express'),
    app = express(),
    url = require('url'),
    rooms = require('roomsjs'),
    roomdb = require('rooms.db'),
    server = require('http').createServer(app),
    io = require('socket.io').listen(server),
    mongoose = require('mongoose'),
    users = {},
    path = require('path'),
    fs = require('fs');
var request = require("request");
 var mysql = require("mysql");
// Run some jQuery on a html fragment
//var jsdom = require("jsdom/lib/old-api.js");
const jsdom = require("jsdom");
const { JSDOM } = jsdom;

server.listen(3000);




app.use(express.static(path.join(__dirname, 'public')));
app.use(express.static(path.join('C:/Users/egypt2/workspace/', 'SearchEngine/')));
//app.use(express.static(path.join(__dirname, 'rooms/room')));

app.get('/', function(req, res) {
    res.sendFile(__dirname + '/index.html');
//res.end();

	});
	var html;

io.sockets.on('connection', function(socket) {
socket.on("getfile",function(data,callback){
	
	fs.readFile(__dirname + '/public/'+data.name, 'utf8', function(err, data1) {
    console.log("readed");
	if (err) {
        return console.log(err);
    }
    html = data1.toString();
	//console.log(html);


// console.log(html);
 if(html!="undefined")
 {
const dom = new JSDOM(html);
var textt;
 textt=dom.window.document.querySelector("p");
 if(textt!=null){
	textt=textt.textContent;
var title=dom.window.document.querySelector("title").textContent;
//console.log(textt);
var index;
index=textt.split(" ");
	var place=index.indexOf(data.word);
	var sentence="";
	if(place>-1)
	{
	if(h>20){
		for(var h=place-20;h<place+index.length;h++)
		{
			sentence+=" "+index[h];
			if(h>place+20)
				break;
		}
	}
	else{
		for(var h=0;h<place+index.length;h++)
		{
			sentence+=" "+index[h];
			if(h>40)
				break;
		}
	}
	console.log(sentence);
	console.log(place);
	console.log(data.type);
	socket.emit("files",{title:title,words:sentence,url:data.url});
	}
	else{
		for(var h=1;h<index.length;h++)
		{
			sentence+=" "+index[h];
			if(h>40)
				break;
		}
		socket.emit("files",{title:title,words:sentence,url:data.url});
	}
	}
 }
});
	
});	

var pool = mysql.createPool({
host : "localhost",
user : "root",
password : "1",
database : "crawler"
}
);

pool.getConnection(function(error,conn){
socket.on("phrase",function(data){
	var word=data.replace('"','');
	word=word.split(' ');
	var isfound=false;
	var queryString = "SELECT * FROM `indexer`,`webpage` WHERE indexer.word='"+word[0]+"' AND indexer.url=webpage.PageID";
conn.query(queryString, function (error,results){
if(error)
           throw error;
        else
		{
	for(var i=0;i<results.length;i++){
		console.log(results[i].url);
		socket.emit("check",{id:results[i].url,word:results[i].word, url: results[i].URL, repeated: results[i].repeated,pagerank:results[i].Inlink,type:results[i].type,phrase:data,title:results[i].Title});
	}
		}
	});
});
socket.on('find',function(data){
	fs.readFile(__dirname + '/public/'+data.id, 'utf8', function(err, data1) {
    console.log("readed");
	if (err) {
        return console.log(err);
    }
    html = data1.toString();
var p=	data.phrase.replace('"','');
 p=	p.replace('"','');
console.log(p+"			"+"			"+html.includes(p));
	if(html.includes(p)>-1)
	{
		isfound=true;
	}
	if(isfound){
		console.log(data);
socket.emit("found",data);
	}
	
});

	
});


	
	
socket.on("getresult",function(data,callback){
	  // connect to your database
var finalresult=[{}];
var query;
var url;
var rep;
var type;
var rank;
var urlid;

var mapidrank=new Map();
var mapwordsrank=new Map;
var urlsarray=[];
var allwords=data.split(" ");
var phrase=false;
var sentence=false;
var allids=[];
console.log("All Words :	"+allwords+"		"+allwords.length);


	var allurlsarray=[];
for(var k=0;k<allwords.length;k++)
{
var queryString = "SELECT * FROM `indexer`,`webpage` WHERE indexer.word='"+allwords[k]+"' AND indexer.url=webpage.PageID";
conn.query(queryString, function (error,results){
if(error)
           throw error;
        else
		{
			callback(results.length);
	for(var i=0;i<results.length;i++)
	socket.emit("done",{id:results[i].url,word:results[i].word, url: results[i].URL, repeated: results[i].repeated,pagerank:results[i].Inlink,type:results[i].type});
 }



	});
}



});	

});
});


/* 	function allurlsbyrank(data){
var query;
var url;
var rep;
var type;
var rank;
var urlid;
var urlsarray=[{}];
var allurlsarray=[{}];
var mapidrank=new Map();
var mapwordsrank=new Map;
var pool = mysql.createPool({
host : "localhost",
user : "root",
password : "1",
database : "crawler"
}
);
var allwords=data.split(" ");
var phrase=false;
var sentence=false;
var allids=[];
console.log("All Words :	"+allwords+"		"+allwords.length);
for(var k=0;k<allwords.length;k++)
{
	console.log("word index number 			"+k+"			"+allwords[k]);
   pool.getConnection(function(error,conn){
var queryString = "SELECT * FROM `indexer`,`webpage` WHERE indexer.word='"+allwords[k]+"' AND indexer.url=webpage.PageID";
console.log("inside the pool");
conn.query(queryString, function (error,results)
{
if(error)
{
throw error;
}
else {
	console.log("inside the connection");

console.log(results.length);

for(var i=0;i<results.length;i++)
{
	if(results[i].type=="head")
	{
		type=0.6;
	}
	else if(results[i].type=="bold")
	{
		type=0.3;
	}
	else{
		type=0.1;
	}
	rank=0.4*(results[i].pageRank/1000)+0.1*(results[i].repeated/100)+0.6*type;
	url=results[i].URL;
	urlid=results[i].PageID;
	
	urlsarray.push({id:urlid,rank:rank});
	allids.push(urlid);

}
console.log("urlsarray of Word :	"+urlsarray);
allurlsarray.push({word:allwords[k],urlsrank:urlsarray});
}
console.log("All urlsarray Words :	"+allurlsarray);
});
   });
}

console.log("All urlsarray Words :	"+allurlsarray);
allids.sort(sortNumber);
allurlsarray.sort(function(a,b){
	return b.urlsrank.rank - a.urlsrank.rank;
});
var count=1;
var countsurl=[{}];
for(var m=0;m<allids.length;m++)
{
	if(m==allids.length)
	{
		countsurl.push({id:allids[m],count:count});
		count=1;
	}
	else{
	if(allids[m]==allids[m+1])
		count++;
	else{
		countsurl.push({id:allids[m],count:count});
		count=1;
	}
	}
}
console.log(allurlsarray);
var tempcountorder=countsurl.sort(function(a,b){return b.count-a.count});
var idswithranks=[];
for(var r=0;r<tempcountorder.length;r++)
{
	idswithranks.push(allurlsarray.urlsarray.id===tempcountorder[r].id);
}

function sortNumber(a,b) {
    return a - b;
}
return(tempcountorder);
	} */
/* var mapAsc = new Map([...map.entries()].sort());
console.log((arrayurl.map(function(item){
	return parseInt(item,10);
}).sort(sortNumber)));
}
var a=map.sort(sortNumber);
console.log(a);
allarray.push({array:a,max:maxMap});
conn.release();
});       
           
    });
}
if(allwords.length>1)
{
	var finalresult=[{}];
	for(var m=0;m<allarray.length;m++){
		if(allarray[m].url==allarray[m+1].url)
		{
			finalresult.push({url:allarray[m].url})
		}
	}
	var arr=allarray.map(function(data){
		return Math.max(data.array.rank);
	});
	
}

	  }



 */
