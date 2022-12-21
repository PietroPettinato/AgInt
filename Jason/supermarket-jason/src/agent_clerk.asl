// Agent agent_clerk in project supermarket.mas2j

/* Initial beliefs and rules */

itemsOnShelf(0).



/* Initial goals */

!start.

!register.

/* Plans */

+!start : itemsOnShelf(N) <- 
	.print("hello world."); 
	.print("There are ", N, " items on the shelf");
	!refill.

+!refill : 	itemsOnShelf(Old) <-
	.print("Refilling shelf...");
	New = Old + 10;
	// New = Old + 1; // to fail switch to this
	-itemsOnShelf(Old);
	+itemsOnShelf(New);
	.print("There are ", New, " items on the shelf").

+!register <- .df_register("participant");
              .df_subscribe("initiator").

// answer to Call For Proposal
@c1 +cfp(CNPId,Task)[source(A)]
   :  provider(A,"initiator") & 
	  itemsOnShelf(Y) &
	  Y >= Task
   <- +proposal(CNPId,Task,true); // remember my proposal
      .send(A,tell,propose(CNPId,true)).

@r1 +accept_proposal(CNPId)
   :  proposal(CNPId,Task,Offer)
   <- .print("My proposal '",Offer,"' won CNP ",CNPId, " for ",Task,"!");
      // do the task and report to initiator
	  !update_shelf(Task).

+!update_shelf(N)
	: itemsOnShelf(Y)
	<-	New = Y - N;
		-itemsOnShelf(Y);
	  	+itemsOnShelf(New);
		.print("There are ", New, " items on the shelf").

@r2 +reject_proposal(CNPId)
   <- .print("I lost CNP ",CNPId, ".");
      -proposal(CNPId,_,_). // clear memory

