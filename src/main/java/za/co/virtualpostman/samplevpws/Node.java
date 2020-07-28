/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.co.virtualpostman.samplevpws;

import java.util.LinkedList;
import java.util.List;
import za.co.virtualpostman.samplevpws.wsdl.authenticate.AuthorizationFault;
import za.co.virtualpostman.samplevpws.wsdl.node.AuthenticationFault;
import za.co.virtualpostman.samplevpws.wsdl.node.DuplicateRecordFault;
import za.co.virtualpostman.samplevpws.wsdl.node.InvalidArgumentFault;
import za.co.virtualpostman.samplevpws.wsdl.node.NodeIndex;
import za.co.virtualpostman.samplevpws.wsdl.node.NodeIndexType;
import za.co.virtualpostman.samplews.wsclient.VirtualPostmanClient;

/**
 *
 * @author eolykab
 */
public class Node {

    public static void main(String[] args) throws AuthorizationFault, InvalidArgumentFault, AuthenticationFault, DuplicateRecordFault, za.co.virtualpostman.samplevpws.wsdl.node.AuthorizationFault, za.co.virtualpostman.samplevpws.wsdl.authenticate.AuthenticationFault {
        VirtualPostmanClient vpc = new VirtualPostmanClient("https://poc.virtualpostman.co.za/external", "admin", "admin");
        vpc.getNode().createNodeCategory(vpc.getSessionId(), "Category");

        List<NodeIndex> indexes = new LinkedList<>();
        NodeIndex index = new NodeIndex();
        index.setName("Invoice");
        index.setHidden(true);
        index.setPartOfKey(true);
        index.setRequired(true);
        index.setType(NodeIndexType.STRING);
        indexes.add(index);
        vpc.getNode().createNode(vpc.getSessionId(), "Category", "Node name", indexes);
        System.out.println("Done");

    }

}
