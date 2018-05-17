package fr.uha.ensisa.projet2A.monitoring;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

import net.wimpi.modbus.ModbusException;
import net.wimpi.modbus.ModbusIOException;
import net.wimpi.modbus.ModbusSlaveException;
import net.wimpi.modbus.io.ModbusTCPTransaction;
import net.wimpi.modbus.msg.ReadInputDiscretesRequest;
import net.wimpi.modbus.msg.ReadInputDiscretesResponse;
import net.wimpi.modbus.net.TCPMasterConnection;

public class Moxa {

	private TCPMasterConnection connection = null;
	private ModbusTCPTransaction transaction = null;
	private ReadInputDiscretesRequest rreq = null;
	private ReadInputDiscretesResponse rres = null;
	
	public String getMachineName(int IP_number) {
		switch(IP_number) {
			case 0 :
				return "Demeter"; 
			case 1:
				return "HAAS_VF2_5AXES"; 
			case 2: 
				return "HAAS_VF2_3AXES"; 
			case 3: 
				return "HAAS_SL20"; 
		}
		return null;
	}

	public MachineUpdate readTransaction(String IPs[], int port) throws Exception {

		for (int i = 0; i < IPs.length; i++) {
			InetAddress inet = InetAddress.getByName(IPs[i]);
			if (inet.isReachable(3000)) {

				connection = new TCPMasterConnection(inet);
				connection.setPort(port);
				connection.connect();

			} else {
				System.out.println("***** The machine : " + inet.getHostAddress() + " is off ***** ");
			}

			this.rreq = new ReadInputDiscretesRequest(0, 2);
			this.transaction = new ModbusTCPTransaction(connection);
			this.transaction.setRequest(rreq);
			this.transaction.execute();
			this.rres = (ReadInputDiscretesResponse) transaction.getResponse();
			int state = -1;

			if (rres.getDiscreteStatus(0) == false && rres.getDiscreteStatus(1) == false) {
				state = 1; // Stop
			} else if (rres.getDiscreteStatus(0) == true && rres.getDiscreteStatus(1) == true) {
				state = 3; // Off
			} else if (rres.getDiscreteStatus(0) == true && rres.getDiscreteStatus(1) == false) {
				state = 2; // Run
			} else if (rres.getDiscreteStatus(0) == false && rres.getDiscreteStatus(1) == true) {
				state = 3; // Off
			}
			
			MachineUpdate update = new MachineUpdate(); 
			update.setMachineID(i+1); //Because DMG_CTX id is 1
			update.setMachineName(this.getMachineName(i));
			update.setState(state);
			update.setStateLabel(ElasticSearchUtil.getStateLabel(update.getState()));
			update.setTime(new Date());

			connection.close();

		}

		return null;

	}
}
