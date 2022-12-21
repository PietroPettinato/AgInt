package agents;

import behaviours.DFBehaviour;
import behaviours.CNPServeClient;
import jade.core.Agent;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class AgentClerk extends Agent{

    private int itemsOnShelf = 0;


    protected void setup () {
        // Printout a welcome message
        System.out.println(getLocalName() + " : Hello World. I'am an AgentClerk!");
        System.out.println(getLocalName() + " : There are " + itemsOnShelf + " items on the shelf");

        MessageTemplate templateCNP = MessageTemplate.and(
                MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET),
                MessageTemplate.MatchPerformative(ACLMessage.CFP)
        );

        addBehaviour(new DFBehaviour("clerk", "Clerk"));
        addBehaviour(new CNPServeClient(this, templateCNP));

    }

    public int getItemsOnShelf() {
        return itemsOnShelf;
    }

    public void setItemsOnShelf(int itemsOnShelf) {
        this.itemsOnShelf = itemsOnShelf;
    }

    @Override
    protected void takeDown() {
        System.out.println(this.getLocalName() + " : Cleaning...");
        doWait(3000);
        System.out.println(this.getLocalName() + " : Cleaned!");
        System.out.println(this.getLocalName() + " : Time to go home!");
    }


}

