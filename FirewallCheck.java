import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class FirewallCheck {
    //Node class for ip address
    private class Node {
        int num;
        Node[] child;
        private Node (int num) {
            this.num = num;
            child = new Node[256];
        }
    }

    //Read the CSV file.
    private BufferedReader bufferedReader;

    //The tree to store the rules
    //Key: The direction and protocol string, ex: "inboundtcp"
    //value: Map<port, ip address Trie tree root> -> each port has its ip address tree.
    private Map<String, Map<Integer, Node>> map;

    public FirewallCheck(String fileName) throws IOException {
        map = new HashMap<>();
        String[] outerKey = new String[]{"inboundtcp", "outboundtcp", "inboundudp", "outboundudp"};
        //Initialize the key for the map
        for (String key : outerKey) {
            Map<Integer, Node> newMap = new HashMap<>();
            map.put(key, newMap);
        }
        try {
            URL url = FirewallCheck.class.getResource(fileName);
            File file = new File(url.getPath());
            bufferedReader = new BufferedReader(new FileReader(file));
        } catch (Exception e) {
            throw new IOException();
        }
        //load the rules into the map
        loadRule();
    }

    public boolean accept_packet(String direction, String protocol, int port, String IP) {
        String[] IPtoCheck = IP.split("\\.");
        String outerKey = direction + protocol;
        Node cur = map.get(outerKey).get(port);
        for (int i = 0; i < 4; i ++) {
            int partValue = Integer.valueOf(IPtoCheck[i]);
            if (cur == null || cur.child[partValue] == null) {
                return false;
            }
            cur = cur.child[partValue];
        }
        return true;
    }
    // read and store rules
    private void loadRule() throws IOException {
        String line = bufferedReader.readLine();
        while(line != null) {
            //split rules
            //parameter[0]: direction
            //parameter[1]: protocol
            //parameter[2]: port
            //parameter[3]: Ip address
            String[] parameters = line.split(",");

            //Deal with Outer key
            String outerKey = parameters[0] + parameters[1];

            //Parse port
            String[] portRange = parameters[2].split("-");
            int startPort = Integer.parseInt(portRange[0]);
            int endPort = Integer.parseInt(portRange[portRange.length - 1]);

            //Parse ipRange
            String[] ipRange = parameters[3].split("-");
            String startIP = ipRange[0];
            String endIP = ipRange[ipRange.length - 1];

            //Add port and ip into tree
            for (int p = startPort; p <= endPort; p++) {
                addPort(outerKey, p);
                addIP(outerKey, p, startIP, endIP);
            }
            line = bufferedReader.readLine();
        }
    }

    //Add port to  the map
    private void addPort(String key, int port) {
        Map<Integer, Node> resultMap = map.get(key);
        if (!resultMap.containsKey(port)) {
            resultMap.put(port, new Node(-1));
        }
    }

    //Add Ip address to the map
    private void addIP(String key, int port, String start, String end) {
        //Get the ipTree for the port
        Node dummyRoot = map.get(key).get(port);

        //parse the ip address to four parts
        String[] startIPstr = start.split("\\.");
        String[] endIPstr = end.split("\\.");

        //Using bfs to traverse the ip trie tree
        Queue<Node> bfs = new LinkedList<>();
        bfs.add(dummyRoot);
        for (int i = 0; i < 4; i++) {
            int size = bfs.size();

            //parse the ip start value and ip end value for each part
            int partValueStart = Integer.valueOf(startIPstr[i]);
            int partValueEnd = Integer.valueOf(endIPstr[i]);

            for (int j = 0; j < size; j++) {
                Node node = bfs.poll();

                //if this part is the first part of ip address, set the loopStart value to partValueStart
                int loopStart = j == 0 ? partValueStart : 0;
                //if this part is the last part of ip address, set the loopEnd value to partValueEnd
                int loopEnd = j == size - 1 ? partValueEnd : 255;
                //Build trie tree for each ip address
                for (int k = loopStart; k <= loopEnd; k++) {
                    if (node.child[k] == null) {
                        node.child[k] = new Node(k);
                    }
                    //i is not the last element, we add it back to queue
                    if (i != 3) {
                        bfs.add(node.child[k]);
                    }
                }
            }
        }
    }
}





