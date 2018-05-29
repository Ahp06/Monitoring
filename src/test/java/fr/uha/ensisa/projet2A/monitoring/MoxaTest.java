package fr.uha.ensisa.projet2A.monitoring;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import net.wimpi.modbus.Modbus;
import net.wimpi.modbus.net.ModbusTCPListener;
import net.wimpi.modbus.net.TCPMasterConnection;

public class MoxaTest {

	private Moxa sut;

	private int moxaPort = Modbus.DEFAULT_PORT;
	private String[] machineNames = { "default" };
	private ModbusTCPListener listener ; 

	@Mock
	private TCPMasterConnection mockConnection;

	@Before
	public void create() throws UnknownHostException {
		sut = new Moxa();
		listener = new ModbusTCPListener(1, InetAddress.getLocalHost());
		listener.setPort(moxaPort);
		listener.start();
	}

	@Test
	public void poolingTest() throws Exception {

		String[] IPs = { InetAddress.getByName("10.10.10.10").getHostAddress() }; // Why localhost doesn't work ? 

		sut.pooling(IPs, machineNames, moxaPort);
	}

}
