package behaviours;

import agents.AgentClient;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.domain.RequestFIPAServiceBehaviour;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;

public class PayProducts extends AchieveREInitiator {

    public PayProducts(Agent a, ACLMessage msg) {
        super(a, msg);
    }

    @Override
    protected void handleRefuse(ACLMessage refuse) {
        System.out.println(myAgent.getLocalName() + ": Agent " + refuse.getSender().getLocalName() + " refused");
    }

    @Override
    protected void handleAgree(ACLMessage agree) {
        System.out.println(myAgent.getLocalName() + ": Agent " + agree.getSender().getLocalName() + " agreed");
    }

    @Override
    protected void handleInform(ACLMessage inform) {
        System.out.println(myAgent.getLocalName() + ": Agent " + inform.getSender().getLocalName() + " completed the payment");
    }

    @Override
    protected void handleFailure(ACLMessage failure) {
        System.out.println(myAgent.getLocalName() + ": A problem occurred with the payment");

        ACLMessage msgPay = new ACLMessage(ACLMessage.REQUEST);
        msgPay.addReceiver(new AID("Cashier", AID.ISLOCALNAME));
        msgPay.setContent("pay-" + ((AgentClient) myAgent).getProductsToBuy());
        myAgent.addBehaviour(new CNPTakeProducts(myAgent, msgPay));
        System.out.println(myAgent.getLocalName() + ": Retring to pay products");
    }

    // TODO implementare il Request Interaction Protocol, o in alternativa fare uno scambio di messaggi con
    //  Client:voglio pagare n prodotti,
    //  Cashier: ok...fatto,
    //  Client:grazie, ciao
    // protected void handle(RunnableChangedEvent rce) {}
}
