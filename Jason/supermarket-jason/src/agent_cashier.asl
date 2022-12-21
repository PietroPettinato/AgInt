// Agent agent_cashier in project supermarket.mas2j

/* Initial beliefs and rules */

client(agent_client).

/* Initial goals */

!start.

/* Plans */

+!start : true <- .print("hello world.").

+pay(Msg)[source(C)]
	: client(C)
	<-	.print("Client ", C, " wants to pay ", Msg, " products");
		!makePayment.

+!makePayment
	: true
	<- 	.print("Processing the payment...");
		.wait(1000);
		!paymentDone.
		//!paymentNotDone. // to fail switch to this

+!paymentDone
	: client(C)
	<-	.print("... Payment executed!");
		.send(C, tell, payment_done("true"));
		.print("Sent to ", C, " message ", payment_done("true")).

+!paymentNotDone
	: client(C)
	<-	.print("... Payment failed!");
		.send(C, tell, payment_done("false"));
		.print("Sent to ", C, " message ", payment_done("false")).

