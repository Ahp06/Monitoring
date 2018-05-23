package fr.uha.ensisa.projet2A.monitoring;

import java.net.InetAddress;
import java.sql.Timestamp;
import java.util.ArrayList;
import net.wimpi.modbus.io.ModbusTCPTransaction;
import net.wimpi.modbus.msg.ReadInputDiscretesRequest;
import net.wimpi.modbus.msg.ReadInputDiscretesResponse;
import net.wimpi.modbus.net.TCPMasterConnection;

public class Moxa {

	private TCPMasterConnection connection = null;
	private ModbusTCPTransaction transaction = null;
	private ReadInputDiscretesRequest rreq = null;
	private ReadInputDiscretesResponse rres = null;

	private String[] machines = { "Demeter", "HAAS_VF2_5AXES", "HAAS_VF2_3AXES", "HAAS_SL20" };

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
	
	/**
	 * Retrieve data and return a list of update object 
	 * @param IPs
	 * @param port
	 * @return
	 * @throws Exception
	 */
	public ArrayList<MachineUpdate> readTransaction(String[] IPs, int port) throws Exception {

		ArrayList<MachineUpdate> updates = new ArrayList<MachineUpdate>();
		for (int i = 0; i < IPs.length; i++) {
			InetAddress inet = InetAddress.getByName(IPs[i]);

			if (inet.isReachable(3000)) {

				connection = new TCPMasterConnection(inet);
				connection.setPort(port);
				connection.connect();

			} else {
				System.out.println("***** The machine : " + inet.getHostAddress() + " is off ***** ");
			}

			/*this.rreq = new ReadInputDiscretesRequest(0, 2);
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

			if (state != -1) {
				MachineUpdate update = new MachineUpdate();
				update.setMachineName(this.machines[i]);
				update.setMachineID(this.getIDByName(this.machines[i]));
				update.setState(state);
				update.setStateLabel(ElasticSearchUtil.getStateLabel(update.getState()));
				update.setTime(new Timestamp(System.currentTimeMillis()));

				updates.add(update);
			}
			*/
			connection.close();
		}
		
		return updates;

	}
}
