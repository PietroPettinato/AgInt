package behaviours;

import agents.AgentClerk;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;

public class MakePayment extends AchieveREResponder {
    public MakePayment(Agent agent, MessageTemplate template) {
        super(agent, template);
    }

    @Override
    protected ACLMessage handleRequest(ACLMessage request) throws NotUnderstoodException, RefuseException {
        System.out.println(myAgent.getLocalName() + ": REQUEST received from " + request.getSender().getLocalName() + ". Action is " + request.getContent());
        try {
            String n = request.getContent().split("-")[1];
            ACLMessage agree = request.createReply();
            agree.setPerformative(ACLMessage.AGREE);
            // System.out.println("MESSAGGIO CLERK:" + agree);
            return agree;
        } catch (Exception e){
            // We refuse to provide a proposal
            System.out.println(myAgent.getLocalName() + ": An error occured");
            throw new RefuseException("evaluation-failed");
        }
    }

    @Override
    protected ACLMessage prepareResultNotification(ACLMessage request, ACLMessage response) throws FailureException {
        System.out.println(myAgent.getLocalName() + ": Trying payment...");
        try {
            String n = request.getContent().split("-")[1];
            System.out.println(myAgent.getLocalName() + ": Payment succesfully completed");
            ACLMessage inform = request.createReply();
            inform.setPerformative(ACLMessage.INFORM);
            myAgent.send(inform);
            // System.out.println("MESSAGGIO CLERK:" + inform);
            return inform;

        } catch (Exception e) {
            System.out.println(myAgent.getLocalName() + ": Payment failed");
            throw new FailureException("unexpected-error");
        }

    }
}
