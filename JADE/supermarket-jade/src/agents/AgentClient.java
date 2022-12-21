package agents;

import behaviours.DFBehaviour;
import behaviours.CNPTakeProducts;
import behaviours.PayProducts;
import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.domain.FIPANames;
import jade.lang.acl.MessageTemplate;


public class AgentClient extends Agent {

    private int productsToBuy;


    protected void setup () {
        // Printout a welcome message
        System.out.println(getLocalName() + " : Hello World. I'am an AgentClient!");

        Object[] args = getArguments();
        if (args != null) {
            this.productsToBuy = Integer.parseInt((String) args[0]);
        }

        System.out.println(getLocalName() + " : I want to buy " + productsToBuy + " products");

        ACLMessage msgTake = new ACLMessage(ACLMessage.CFP);
        msgTake.addReceiver(new AID("Clerk", AID.ISLOCALNAME));
        msgTake.setProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);
        msgTake.setContent("buy-" + productsToBuy);

        /*
        ACLMessage msgPay = new ACLMessage(ACLMessage.REQUEST);
        msgPay.addReceiver(new AID("Cashier", AID.ISLOCALNAME));
        msgPay.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
        msgPay.setContent("pay-" + productsToBuy);
        addBehaviour(new PayProducts(this, msgPay));
         */

        addBehaviour(new DFBehaviour("client", "Client"));
        addBehaviour(new CNPTakeProducts(this, msgTake));

    }

    @Override
    protected void takeDown() {
        System.out.println(this.getLocalName() + " : All done, bye!");
    }

    public int getProductsToBuy() {
        return productsToBuy;
    }

}
