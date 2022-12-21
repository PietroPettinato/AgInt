package behaviours;

import agents.AgentClient;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.ContractNetInitiator;

import java.util.Vector;


public class CNPTakeProducts extends ContractNetInitiator {


    public CNPTakeProducts(Agent a, ACLMessage cfp) {
        super(a, cfp);
    }



    @Override
    protected boolean checkInSequence(ACLMessage reply) {
        return true;
    }


    protected void handleRefuse(ACLMessage refuse) {
        System.out.println(myAgent.getLocalName() + ": Agent " + refuse.getSender().getLocalName() + " refused");

        ACLMessage msg = new ACLMessage(ACLMessage.CFP);
        msg.addReceiver(new AID("Clerk", AID.ISLOCALNAME));
        msg.setProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);
        msg.setContent("buy-" + ((AgentClient) myAgent).getProductsToBuy());
        myAgent.addBehaviour(new CNPTakeProducts(myAgent, msg));
        System.out.println(myAgent.getLocalName() + ": Retring to buy products");

    }

    @Override
    protected void handlePropose(ACLMessage propose, Vector v) {
        System.out.println(myAgent.getLocalName() + ": Agent " + propose.getSender().getLocalName() + " proposed " + propose.getContent());

        ACLMessage accept = propose.createReply();
        accept.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
        myAgent.send(accept);
        /*
        for (int i=0 ; i < v.size(); i++) {
            v.set(i, ACLMessage.ACCEPT_PROPOSAL);
        }
         */

        System.out.println(myAgent.getLocalName() + ": Proposal of agent " + propose.getSender().getLocalName() + " accepted");

        // ACLMessage inform = propose.createReply();
        // inform.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
    }


    @Override
    protected void handleAllResponses(Vector responses, Vector acceptances) {
        /*
        ACLMessage propose = (ACLMessage) responses.elements().nextElement();
        ACLMessage accept = propose.createReply();
        accept.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
        myAgent.send(accept);
        for (int i=0 ; i < acceptances.size(); i++) {
            acceptances.set(i, ACLMessage.ACCEPT_PROPOSAL);
        }

        System.out.println(myAgent.getLocalName() + ": Proposal of agent " + propose.getSender().getLocalName() + " accepted");
        if (isFirst) {
            isFirst = false;
            ACLMessage propose = (ACLMessage) responses.elements().nextElement();
            ACLMessage accept = propose.createReply();
            accept.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
            System.out.println(myAgent.getLocalName() + ": Proposal of agent " + propose.getSender().getLocalName() + " accepted");
        } else {
            ACLMessage propose = (ACLMessage) responses.elements().nextElement();
            ACLMessage reject = propose.createReply();
            reject.setPerformative(ACLMessage.REJECT_PROPOSAL);
            System.out.println(myAgent.getLocalName() + ": Proposal of agent " + propose.getSender().getLocalName() + " refused");
        }
         */
    }

    @Override
    protected void handleInform(ACLMessage inform) {
        System.out.println(myAgent.getLocalName() + " : Agent " + inform.getSender().getLocalName() + " accepted. Items taken, bye!");
        ACLMessage msgPay = new ACLMessage(ACLMessage.REQUEST);
        msgPay.addReceiver(new AID("Cashier", AID.ISLOCALNAME));
        msgPay.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
        msgPay.setContent("pay-" + ((AgentClient) myAgent).getProductsToBuy());
        myAgent.addBehaviour(new PayProducts(myAgent, msgPay));
    }

    protected void handleFailure(ACLMessage failure) {
        if (failure.getSender().equals(myAgent.getAMS())) {
            // FAILURE notification from the JADE runtime: the receiver
            // does not exist
            System.out.println("Responder does not exist");
        }
        else {
            System.out.println("Agent " + failure.getSender().getName() + " failed");
        }
        // Immediate failure --> we will not receive a response from this agent
    }
}
