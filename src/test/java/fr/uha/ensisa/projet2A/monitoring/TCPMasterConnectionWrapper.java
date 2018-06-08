package fr.uha.ensisa.projet2A.monitoring;

import java.net.InetAddress;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import net.wimpi.modbus.Modbus;
import net.wimpi.modbus.net.ModbusTCPListener;

public class TCPMasterConnectionWrapper {

	protected static Moxa moxa; 
	private static ModbusTCPListener listener = null;
	private static int port = Modbus.DEFAULT_PORT;

	@BeforeClass
	public static void setUpClass() throws Exception {
		moxa = new Moxa(); 
		listener = new ModbusTCPListener(1, InetAddress.getLocalHost());
		listener.setPort(port);
		listener.start();
	}


	@AfterClass
	public static void tearDownClass() throws Exception {
		listener.stop();
		listener = null;
	}
}
