package behaviours;

import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class DFBehaviour extends OneShotBehaviour {

    private String type;
    private String name;

    public DFBehaviour(String type, String name) {
        this.type = type;
        this.name = name;
    }

    /**
     * This behaviour is needed to register Agents in the DirectoryFacilitator (DF) and make them "searchable".
     * To register an agent, an Agent Description and a Service Description must be provided.
     */

    @Override
    public void action() {
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAgent().getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType(this.type);
        sd.setName(this.name);
        dfd.addServices(sd);
        try {
            DFService.register(getAgent(), dfd);
            System.out.println(this.type + " " + getAgent().getAID().getName() + " registered in DF.");
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }

    }
}
