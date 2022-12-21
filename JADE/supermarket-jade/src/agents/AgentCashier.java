package agents;

import behaviours.DFBehaviour;
import behaviours.MakePayment;
import jade.core.Agent;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class AgentCashier extends Agent {

    protected void setup () {
        // Printout a welcome message
        System.out.println(getLocalName() + " : Hello World. I'am an AgentCashier!");

        MessageTemplate templateReq = MessageTemplate.and(
                MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST),
                MessageTemplate.MatchPerformative(ACLMessage.REQUEST)
        );
        addBehaviour(new DFBehaviour("cashier", "Cashier"));
        addBehaviour(new MakePayment(this, templateReq));

    }

    @Override
    protected void takeDown() {
        System.out.println(this.getLocalName() + " : Time to close!");
    }
}
