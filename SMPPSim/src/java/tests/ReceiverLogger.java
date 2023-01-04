package tests;

import com.logica.smpp.Connection;
import com.logica.smpp.NotSynchronousException;
import com.logica.smpp.Session;
import com.logica.smpp.TCPIPConnection;
import com.logica.smpp.TimeoutException;
import com.logica.smpp.WrongSessionStateException;
import com.logica.smpp.pdu.BindReceiver;
import com.logica.smpp.pdu.BindRequest;
import com.logica.smpp.pdu.DeliverSM;
import com.logica.smpp.pdu.EnquireLinkResp;
import com.logica.smpp.pdu.PDU;
import com.logica.smpp.pdu.PDUException;
import com.logica.smpp.pdu.Request;
import com.logica.smpp.pdu.Response;
import tests.exceptions.BindReceiverException;

import java.io.IOException;
import java.net.SocketException;

public class ReceiverLogger {

	public static void main(String [] args)
		throws Exception {
		System.out.println("Attempting to establish receiver session");
		Response resp = null;
		Connection conn;
		Session session;
		// get a receiver session
		try {
			conn = new TCPIPConnection("localhost", 2775);
			session = new Session(conn);
			BindRequest breq = new BindReceiver();
			breq.setSystemId("smppclient");
			breq.setPassword("password");
			resp = session.bind(breq);
		} catch (Exception e) {
			System.out.println("Exception: " + e.getMessage());
			System.out.println(
				"Exception whilst setting up or executing bind receiver. "
					+ e.getMessage());
			throw new BindReceiverException(
				"Exception whilst setting up or executing bind receiver. "
					+ e.getMessage());
		}
		System.out.println("Established receiver session successfully");

		// now wait for messages and log them as they are received
		PDU pdu = null;
		DeliverSM deliversm;
		Response response;
		int count=0;
		boolean gotDeliverSM = false;
		while (true) {
			try {
				// wait for a PDU
				pdu = session.receive();
				if (pdu != null) {
					if (pdu instanceof DeliverSM) {
						count++;
						deliversm = (DeliverSM) pdu;
						gotDeliverSM = true;
						System.out.println("Received DELIVER_SM #:"+count+" : "+pdu.toString());
						response = ((Request)pdu).getResponse();
						session.respond(response);

					} else {
						if (pdu instanceof EnquireLinkResp) {
							System.out.println("EnquireLinkResp received");
						} else {
							System.out.println(
								"Unexpected PDU of type: "
									+ pdu.getClass().getName()
									+ " received - discarding");
						}
					}
				}
			} catch (SocketException e) {
				System.out.println("Connection has dropped for some reason");
			} catch (IOException ioe) {
				System.out.println("IOException: " + ioe.getMessage());
			} catch (NotSynchronousException nse) {
				System.out.println("NotSynchronousException: " + nse.getMessage());
			} catch (PDUException pdue) {
				System.out.println("PDUException: " + pdue.getMessage());
			} catch (TimeoutException toe) {
				System.out.println("TimeoutException: " + toe.getMessage());
			} catch (WrongSessionStateException wsse) {
				System.out.println("WrongSessionStateException: " + wsse.getMessage());
			}
		}
	}

}