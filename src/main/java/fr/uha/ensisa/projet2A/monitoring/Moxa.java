package fr.uha.ensisa.projet2A.monitoring;

import java.net.InetAddress;
import java.util.Date;

import net.wimpi.modbus.io.ModbusTCPTransaction;
import net.wimpi.modbus.msg.ReadInputDiscretesRequest;
import net.wimpi.modbus.msg.ReadInputDiscretesResponse;
import net.wimpi.modbus.net.TCPMasterConnection;

public class Moxa {

	private TCPMasterConnection connection = null;
	private ModbusTCPTransaction transaction = null;
	private ReadInputDiscretesRequest rreq = null;
	private ReadInputDiscretesResponse rres = null;
	
	private String[] machines = { "Demeter" , "HAAS_VF2_5AXES" , "HAAS_VF2_3AXES", "HAAS_SL20"};
	

	public int getIDByName(String machineName) {
		// ID 1 is for DMG_CTX
		if (machineName.equals("Demeter")) {
			return 2;
		} else if (machineName.equals("HAAS_VF2_5AXES")) {
			return 3;
		} else if (machineName.equals("HAAS_VF2_3AXES")) {
			return 4;
		} else if (machineName.equals("HAAS_SL20")) {
			return 5;
		}

		return -1;
	}

	public MachineUpdate readTransaction(String IP, int port) throws Exception {

		InetAddress inet = InetAddress.getByName(IP);
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
		//update.setMachineID();
		//update.setMachineName(machineName);
		update.setState(state);
		update.setStateLabel(ElasticSearchUtil.getStateLabel(update.getState()));
		update.setTime(new Date());

		connection.close();

		return null;

	}
}
