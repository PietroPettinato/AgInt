package behaviours;


import agents.AgentClerk;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.*;
import jade.domain.FIPAException;
import jade.lang.acl.MessageTemplate;
import jade.proto.ContractNetResponder;
import jade.lang.acl.ACLMessage;


public class CNPServeClient extends ContractNetResponder {

    public CNPServeClient(Agent a, MessageTemplate mt) {
        super(a, mt);
    }

    @Override
    protected ACLMessage handleCfp(ACLMessage cfp) throws NotUnderstoodException, RefuseException {
        System.out.println(myAgent.getLocalName() + ": CFP received from " + cfp.getSender().getLocalName() + ". Action is " + cfp.getContent());
        String n = cfp.getContent().split("-")[1];
        boolean proposal = evaluateAction(Integer.parseInt(n));
        if (proposal) {
            // We provide a proposal
            ACLMessage propose = cfp.createReply();
            propose.setPerformative(ACLMessage.PROPOSE);
            propose.setContent(String.valueOf(proposal));
            // System.out.println("MESSAGGIO CLERK:" + propose);

            return propose;
        } else {
            // We refuse to provide a proposal
            System.out.println(myAgent.getLocalName() + ": Refused, not enough products on the shelf");
            this.refillShelf();
            throw new RefuseException("evaluation-failed");
        }
    }

    @Override
    protected ACLMessage handleAcceptProposal(ACLMessage cfp, ACLMessage propose, ACLMessage accept) throws FailureException {
        System.out.println(myAgent.getLocalName() + ": Proposal accepted");
        String n = cfp.getContent().split("-")[1];
        if (performAction(Integer.parseInt(n))) {
            System.out.println(myAgent.getLocalName() + ": Action successfully performed");
            System.out.println(myAgent.getLocalName() + ": Items on the shelf: " + ((AgentClerk) myAgent).getItemsOnShelf());
            ACLMessage inform = accept.createReply();
            inform.setPerformative(ACLMessage.INFORM);
            // myAgent.send(inform);
            // System.out.println("MESSAGGIO CLERK:" + inform);
            return inform;

            /*
            DFAgentDescription templateArbiter = new DFAgentDescription();
            ServiceDescription sd = new ServiceDescription();
            sd.setType("client");
            templateArbiter.addServices(sd);

            try {
                DFAgentDescription[] result = DFService.search(getAgent(), templateArbiter);
                inform.addReceiver(result[0].getName());
            } catch (FIPAException e) {
                System.out.println("ERRRRRRRRRRRRRRROOOOOOOOOOOOOOOOOORRRRRRRRRRRRRRRRE");
                throw new RuntimeException(e);
            }
            */
        } else {
            System.out.println(myAgent.getLocalName() + ": Action execution failed");
            throw new FailureException("unexpected-error");
        }
    }


    protected void handleRejectProposal(ACLMessage cfp, ACLMessage propose, ACLMessage reject) {
        System.out.println(myAgent.getLocalName() + ": Proposal rejected");
    }

    private boolean evaluateAction(int request) {
        int items = ((AgentClerk) myAgent).getItemsOnShelf();
        return items >= request;
    }

    private boolean performAction(int request) {
        try {
            int items = ((AgentClerk) myAgent).getItemsOnShelf();
            items = items - request;
            ((AgentClerk) getAgent()).setItemsOnShelf(items);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void refillShelf() {
        System.out.println(myAgent.getLocalName() + " : Refilling shelf");
        ((AgentClerk) myAgent).setItemsOnShelf(((AgentClerk) myAgent).getItemsOnShelf() + 10);
        System.out.println(myAgent.getLocalName() + " : Now there are " + ((AgentClerk) getAgent()).getItemsOnShelf() + " items on the shelf");
    }

}
