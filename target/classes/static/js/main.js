var usernamePage = document.querySelector('#username-page');
var chatPage = document.querySelector('#chat-page');
var usernameForm = document.querySelector('#usernameForm');
var messageForm = document.querySelector('#messageForm');
var messageInput = document.querySelector('#message');
var messageArea = document.querySelector('#messageArea');
var connectingElement = document.querySelector('.connecting');


var stompClient = null;
var username = null;




/*window.onbeforeunload = function() {
	alert("b");
	}*/

    function myFunction(){
	var xhr = new XMLHttpRequest();
	xhr.responseType = 'json';
	xhr.open('GET', "http://localhost:8080/getonlineusers", true);
	xhr.send();
	xhr.onreadystatechange = function() {
		    if (this.readyState == 4 && this.status == 200) {
		    	var jsonResponse = xhr.response;
		    	var select = document.getElementById("list_users_dropdown");
		    	console.log("??????????????????????"+select);
		    	console.log(select.options.length);
		    	for (var j = select.options.length; j >= 0; j--) {
	    			select.options[j] = null; // for clearing
				}
		    	console.log(select.options.length); //for printing in console
		    	for(var i = 0;i < jsonResponse.length; i++) {
		    		
		    		console.log(jsonResponse[i]);
		        opt = document.createElement("option");
		    opt.value = jsonResponse[i];
		    opt.textContent = jsonResponse[i];
		    select.appendChild(opt);
		    	}
		    }
		  };
	
}





function selectOption(value) {
	var x = document.getElementById("list_users_dropdown").value;
    console.log(x);
	var xhr = new XMLHttpRequest();
       xhr.open('POST', "http://localhost:8080/getselecteduser", true);
	xhr.send(x);
	
  
}


function connect(event) {
    username = document.querySelector('#name').value.trim();

    if(username) {
     usernamePage.classList.add('hidden');
     chatPage.classList.remove('hidden');

     var socket = new SockJS('/group-chat');  
     var socket = new SockJS('/single-chat');   //The connect() function uses SockJS and stomp.js to open a connection to "/ws",which is where our SockJS server is waiting for connections.
     stompClient = Stomp.over(socket);

        stompClient.connect({}, onConnected, onError);
             }
    event.preventDefault();
  }




function onConnected() {
   
    stompClient.subscribe('/topic/public',onMessageReceived); //To receive messages in the browser, the STOMP client must first subscribe to a destination. we are sending data to /topic/public..and here all are subscribed to topic/public..so all can get messages.
    stompClient.subscribe("/user/{username}/queue/messages", onMessageReceived);


    
    stompClient.send("/app/chat.addUser",  {},
            JSON.stringify({sender: username, type: 'JOIN'})) //javascript object to a string
            
       

    connectingElement.classList.add('hidden');
         }


function onError(error) {
    connectingElement.textContent = 'Could not connect to WebSocket server, try again!';
    connectingElement.style.color = 'red';
           }


function sendMessage(event) {
	
    var messageContent = messageInput.value.trim();

    if(messageContent && stompClient) {
        var chatMessage = {sender: username, content: messageInput.value,type: 'CHAT' };
               
        stompClient.send("/app/group-chat", {}, JSON.stringify(chatMessage));//when stompclients sends,it includes destination,header and body..if no header to pass use an empty javascript literal
        console.log("are u here for group chat");
        messageInput.value = '';
     }
    event.preventDefault();
     }


  function sendPrivateMessage(event){
	  console.log("button is clicked");
	 var messageContent = messageInput.value.trim();
	 if(messageContent && stompClient) {
	        var chatMessage = {sender: username, content: messageInput.value,type: 'CHAT' };
	               
	        stompClient.send("/app/single-chat", {}, JSON.stringify(chatMessage));//when stompclients sends,it includes destination,header and body..if no header to pass use an empty javascript literal
	       
	        console.log("reached");
	        messageInput.value = '';
        
	       }
	    event.preventDefault();
	     }
	



  function onMessageReceived(payload) { //when the client receives a STOMP message from the server
    var message = JSON.parse(payload.body);  //from browser we get string data it is parsed to a javascript object
   
                                           

    var messageElement = document.createElement('li');

    if(message.type === 'JOIN') {
        messageElement.classList.add('event-message');
        message.content = message.sender + ' joined!';
    } else if (message.type === 'LEAVE') {
        messageElement.classList.add('event-message');
        message.content = message.sender + ' left!';
    } else {
        messageElement.classList.add('chat-message');

       var element = document.createElement('i');
        var text = document.createTextNode(message.sender);
       element.appendChild(text);
      

        messageElement.appendChild(element);

        var usernameElement = document.createElement('a');
        var usernameText = document.createTextNode(message.sender);
        usernameElement.appendChild(usernameText);
        messageElement.appendChild(usernameElement);
    }

    var textElement = document.createElement('p');
    var messageText = document.createTextNode(message.content);
    textElement.appendChild(messageText);

    messageElement.appendChild(textElement);

    messageArea.appendChild(messageElement);
   
}



usernameForm.addEventListener('submit', connect, true)
messageForm.addEventListener('submit', sendMessage, true)
messageForm.addEventListener('submit',sendPrivateMessage,true)
