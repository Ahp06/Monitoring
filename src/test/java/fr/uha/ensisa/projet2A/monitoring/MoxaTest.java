package fr.uha.ensisa.projet2A.monitoring;

import java.io.IOException;
import java.net.InetAddress;
import org.junit.Before;
import org.junit.Test;
import net.wimpi.modbus.Modbus;
import net.wimpi.modbus.net.ModbusTCPListener;
import net.wimpi.modbus.net.TCPMasterConnection;

public class MoxaTest {

	/*private Moxa sut;

	private int moxaPort = Modbus.DEFAULT_PORT;
	private String[] machineNames = { "default" };

	@Mock
	private TCPMasterConnection mockConnection;

	@Before
	public void create() throws IOException {
		sut = new Moxa();
		/*InetAddress addr = Mockito.mock(InetAddress.class); 
		Mockito.when(addr.isReachable(3000)).thenReturn(Boolean.TRUE); 
	}

	@Test
	public void poolingTest() throws Exception {

		String[] IPs = { InetAddress.getByName("10.10.10.10").getHostAddress() }; // Why localhost doesn't work ? 

		sut.pooling(IPs, machineNames, moxaPort);
	}*/

}
