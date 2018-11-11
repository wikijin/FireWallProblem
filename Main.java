
public class Main {

    public static void main(String[] args) {
        try {
            FirewallCheck fw = new FirewallCheck("rules.csv");
            //Sample TestCase
            System.out.println("Actual Output: " + fw.accept_packet("inbound", "udp", 53, "192.168.2.1") + ", Expected output: true");
            System.out.println("Actual Output: " + fw.accept_packet("inbound", "tcp", 80, "192.168.1.2") + ", Expected output: true");
            System.out.println("Actual Output: " + fw.accept_packet("outbound", "tcp", 10234, "192.168.10.11") + ", Expected output: true");
            System.out.println("Actual Output: " +fw.accept_packet("inbound", "tcp", 81, "192.168.1.2") + ", Expected output: false");
            System.out.println("Actual Output: " +fw.accept_packet("inbound", "udp", 24, "52.12.48.92") + ", Expected output: false");

            //Common Cases and Edge Testcases
            System.out.println("Actual Output: " + fw.accept_packet("outbound", "tcp", 10000, "192.168.10.11") + ", Expected output: true");
            System.out.println("Actual Output: " + fw.accept_packet("outbound", "tcp", 20000, "192.168.10.11") + ", Expected output: true");
            System.out.println("Actual Output: " + fw.accept_packet("inbound", "udp", 53, "192.168.1.255") + ", Expected output: true");
            System.out.println("Actual Output: " +fw.accept_packet("inbound", "udp", 81, "192.168.1.2") + ", Expected output: false");
            System.out.println("Actual Output: " +fw.accept_packet("inbound", "tcp", 1002, "52.12.48.92") + ", Expected output: false");



        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}