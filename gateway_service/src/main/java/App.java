public class App {


    public static void main(String[] args) throws Exception {

        homepage_servlet.display();

    }
}
/*magic
* mvn --projects gateway_service,signal_service,graph_service --also-make clean install*/