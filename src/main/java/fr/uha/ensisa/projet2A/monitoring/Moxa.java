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

	/**
	 * Retrieve data and return a list of update object
	 * 
	 * @param IPs
	 * @param port
	 * @return
	 * @throws Exception
	 */
	public ArrayList<MachineUpdate> pooling(String[] IPs, String[] machineNames, int port) throws Exception {

		ArrayList<MachineUpdate> updates = new ArrayList<MachineUpdate>();
		for (int i = 0; i < IPs.length; i++) {
			InetAddress inet = InetAddress.getByName(IPs[i]);

			if (inet.isReachable(3000)) {

				connection = new TCPMasterConnection(inet);
				connection.setPort(port);
				connection.connect();
				System.out.println("The machine : " + inet.getHostAddress() + " is on ( " + machineNames[i] + " )");

				this.rreq = new ReadInputDiscretesRequest(0, 2);
				this.transaction = new ModbusTCPTransaction(connection);
				this.transaction.setRequest(rreq);
				this.transaction.execute();
				this.rres = (ReadInputDiscretesResponse) transaction.getResponse();
				int state = -1;

				/*System.out.println("Sortie 0 :" + rres.getDiscreteStatus(0));
				System.out.println("Sortie 1 :" + rres.getDiscreteStatus(1));
				System.out.println("Sortie 2 :" + rres.getDiscreteStatus(2));
				System.out.println("Sortie 3 :" + rres.getDiscreteStatus(3));
				*/
				
				if (rres.getDiscreteStatus(0) == false && rres.getDiscreteStatus(1) == false) {
					state = 3 ; //Réglage 
				} else if (rres.getDiscreteStatus(0) == false && rres.getDiscreteStatus(1) == true) {
					state = 2;  //Production
				} else {
					state = 1 ; //Arrêt 
				}

				if (ElasticSearchUtil.getLastStateByMachineID(i + 2) != -1 && state == ElasticSearchUtil.getLastStateByMachineID(i + 2)) {
					state = -1;
				}

				if (state != -1) {
					MachineUpdate update = new MachineUpdate();
					update.setMachineName(machineNames[i]);
					update.setMachineID(i + 2); // ID = 1 is for DMG_CTX and ID = 0 isn't attributed
					update.setState(state);
					update.setStateLabel(ElasticSearchUtil.getStateLabel(update.getState()));
					update.setTime(new Timestamp(System.currentTimeMillis()));
					updates.add(update);
				}

				connection.close();

			} else {
				System.out.println("The machine : " + inet.getHostAddress() + " is off ");
			}
		}

		return updates;

	}

	public ModbusTCPTransaction getTransaction() {
		return transaction;
	}

	public void setTransaction(ModbusTCPTransaction transaction) {
		this.transaction = transaction;
	}

	public ReadInputDiscretesRequest getRreq() {
		return rreq;
	}

	public void setRreq(ReadInputDiscretesRequest rreq) {
		this.rreq = rreq;
	}

	public ReadInputDiscretesResponse getRres() {
		return rres;
	}

	public void setRres(ReadInputDiscretesResponse rres) {
		this.rres = rres;
	}

}
