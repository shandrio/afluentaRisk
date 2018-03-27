package com.afluenta;

import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import static com.afluenta.DoubleUtil.*;

public class Risk {

	public static void main(String[] args) throws InterruptedException {

		ClipboardScout thread = new ClipboardScout();
		thread.start();

		thread.join();
	}

	public static void printOutput(String data) {
		double plazo = lFindDoubleDelimitedBy(data, "meses\nPlazo", 2);
		double cuotaPromedio = rFindDoubleDelimitedBy(data, "\n$", "\nCuota");
		double ingresoInferido = rFindDoubleDelimitedBy(data, "Ingresoinferido$", "\nCompromiso");
		double compromisoMensual = findDoubleDelimitedBy(data, "Compromisomensual$", "\n");
		double riesgo = (plazo * cuotaPromedio) / (ingresoInferido - compromisoMensual);

		StringBuilder scriptCmd = new StringBuilder();
		scriptCmd.append("display notification \"Plazo: ");
		scriptCmd.append(formatLargeDouble(plazo));
		scriptCmd.append("\t\t\tCuota: ");
		scriptCmd.append(formatLargeDouble(cuotaPromedio));
		scriptCmd.append("\nIngreso: ");
		scriptCmd.append(formatLargeDouble(ingresoInferido));
		scriptCmd.append("\tCompromiso: ");
		scriptCmd.append(formatLargeDouble(compromisoMensual));
		scriptCmd.append("\" with title \"RIESGO AFLUENTA: ");
		scriptCmd.append(formatSmallDouble(riesgo));
		scriptCmd.append("\"");

		System.out.println(scriptCmd.toString());

		String [] cmd = {"osascript", "-e", scriptCmd.toString()};
		try {
			Runtime.getRuntime().exec(cmd);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

class ClipboardScout extends Thread {

	public static boolean go = true;

	@Override
	public void run() {
		String dataBefore = "";

		while (go) {
			try {
				sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
				go = false;
			}

			String data = "";
			try {
				data = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
			} catch (HeadlessException | UnsupportedFlavorException | IOException | IllegalStateException e) {
				e.printStackTrace();
			}

			if ("exit!Risk".equals(data)) {
				go = false;
				System.out.println("Exiting");
			}

			if (data.contains("meses\nPlazo") && !data.equals(dataBefore)) {
				dataBefore = data;
				data = data.replace(" ", "");
				data = data.replace("\t", "");
				data = data + "\n\n";
				
				Risk.printOutput(data);
			}

		}
	}
}