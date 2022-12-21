/* Initial beliefs and rules */

products(5).

all_proposals_received(CNPId)
  :- nb_participants(CNPId,NP) &                 // number of participants
     .count(propose(CNPId,_)[source(_)], NO) &   // number of proposes received
     .count(refuse(CNPId)[source(_)], NR) &      // number of refusals received
     NP = NO + NR.

cashier(agent_cashier).

/* Initial goals */

!start.
!register.
!startCNP(1,products(N)).

/* Plans */

+!start : products(N) <- .print("hello world."); .print("I want to buy ", N, " items").

+!register <- .df_register(initiator).


// Take the products
+!startCNP(Id,Task):
	products(N)
   <- .print("Waiting participants for task ",Task,"...");
      .wait(500);  // wait participants introduction
      +cnp_state(Id,propose);   // remember the state of the CNP
      .df_search("participant",LP);
      .print("Sending CFP to ",LP);
      +nb_participants(Id,.length(LP));
      .send(LP,tell,cfp(Id,N));
      // the deadline of the CNP is now + 4 seconds (or all proposals were received)
      .wait(all_proposals_received(Id), 4000, _);
      !contract(Id).

// this plan needs to be atomic so as not to accept
// proposals or refusals while contracting
@lc1[atomic]
+!contract(CNPId)
   :  cnp_state(CNPId,propose) &
   products(N)
   <- -cnp_state(CNPId,_);
      +cnp_state(CNPId,contract);
      .findall(productsAvailability(O,A),propose(CNPId,O)[source(A)],L);
      .print("Response is ",L);
      L \== []; // constraint the plan execution to at least one offer
      .print("Taken ", N," products, bye!");
	  +itemsTaken;
      !announce_result(CNPId,L,WAg);
      -+cnp_state(CNPId,finished);
	  !pay_products.

// nothing todo, the current phase is not 'propose'
@lc2 +!contract(_).

-!contract(CNPId)
   <- .print("CNP ",CNPId," has failed!").

+!announce_result(_,[],_).
// announce to the winner
+!announce_result(CNPId,[productsAvailability(_,WAg)|T],WAg)
   <- .send(WAg,tell,accept_proposal(CNPId));
      !announce_result(CNPId,T,WAg).
// announce to others
+!announce_result(CNPId,[productsAvailability(_,LAg)|T],WAg)
   <- .send(LAg,tell,reject_proposal(CNPId));
      !announce_result(CNPId,T,WAg).


// Pay the products
+!pay_products
	: itemsTaken &
	cashier(A) &
	products(N)
	<- 	.print("Let's pay ", N, " products to ", A);
		.send(A, tell, pay(N));
		.print("Sent to ", A, " message ", pay(N)).

+payment_done(Msg)[source(A)]
	: cashier(A) &
	Msg == "true"
	<- .print("Payment done, bye!").

+payment_done(Msg)[source(A)]
	: cashier(A) &
	Msg == "false"
	<- .print("Payment not done... Yee free products :)").



