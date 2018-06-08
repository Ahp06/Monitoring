package fr.uha.ensisa.projet2A.monitoring;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.net.InetAddress;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import net.wimpi.modbus.Modbus;
import net.wimpi.modbus.net.TCPMasterConnection;

public class MoxaTest extends TCPMasterConnectionWrapper {

	@Test
	public void testConnectAndClose() throws Exception {
		final TCPMasterConnection mockConnection = Mockito.mock(TCPMasterConnection.class);
		Mockito.doAnswer(new Answer() {

			public Object answer(InvocationOnMock invocation) throws Throwable {
				Mockito.when(mockConnection.isConnected()).thenReturn(Boolean.TRUE);
				return null;
			}
		}).when(mockConnection).connect();
		Mockito.doAnswer(new Answer() {

			public Object answer(InvocationOnMock invocation) throws Throwable {
				Mockito.when(mockConnection.isConnected()).thenReturn(Boolean.FALSE);
				return null;
			}
		}).when(mockConnection).close();
		assertFalse(mockConnection.isConnected());
		mockConnection.connect();
		assertTrue(mockConnection.isConnected());
		mockConnection.close();
		assertFalse(mockConnection.isConnected());
	}

	@Test
	public void testIsConnected() {
		TCPMasterConnection mockConnection = Mockito.mock(TCPMasterConnection.class);
		Mockito.when(mockConnection.isConnected()).thenReturn(Boolean.TRUE, Boolean.FALSE);
		assertTrue(mockConnection.isConnected());
		assertFalse(mockConnection.isConnected());
	}

	@Test 
	public void poolingTest() throws Exception{
		String[] IPs = { InetAddress.getLocalHost().getHostAddress() };
		String[] machineNames = { "local" }; 
		int port = Modbus.DEFAULT_PORT; 
		
		/*moxa.getRres().setDiscreteStatus(0, false);
		moxa.getRres().setDiscreteStatus(1, true);
		moxa.pooling(IPs, machineNames, port); */
	}

}
