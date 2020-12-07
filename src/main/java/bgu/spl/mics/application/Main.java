package bgu.spl.mics.application;

import bgu.spl.mics.MessageBus;
import bgu.spl.mics.MessageBusImpl;
import bgu.spl.mics.application.passiveObjects.Attack;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.Ewoks;
import bgu.spl.mics.application.passiveObjects.TimeDetailOf;
import bgu.spl.mics.application.services.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/** This is the Main class of the application. You should parse the input file,
 * create the different components of the application, and run the system.
 * In the end, you should output a JSON.
 */
public class Main {
	public static void main(String[] args) throws FileNotFoundException {

		Diary battleLog = Diary.getInstance();// manage Time stamps relatively to its creation

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		ParseJson inputJson = gson.fromJson(new FileReader(args[0]),ParseJson.class);

		CountDownLatch threadInitCounter = new CountDownLatch(4);

		Ewoks ewoksPool = Ewoks.init(inputJson.Ewoks);
		MessageBus messageBus = new MessageBusImpl();
		LeiaMicroservice pLeia = new LeiaMicroservice(inputJson.attacks,threadInitCounter);
		R2D2Microservice r2D2 = new R2D2Microservice(inputJson.R2D2,threadInitCounter);
		LandoMicroservice lando = new LandoMicroservice(inputJson.Lando,threadInitCounter);

		HanSoloMicroservice hSolo = new HanSoloMicroservice(threadInitCounter);
		C3POMicroservice c3po = new C3POMicroservice(threadInitCounter);

		Thread[] threads ={new Thread(pLeia),new Thread(lando),new Thread(r2D2),new Thread(hSolo),new Thread(c3po)};
		for (Thread th:threads ) { th.start();}

		for (Thread th:threads ) {
			try {
				th.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}


		try {
			objectToOutputJsonFile(gson, new OutputDiaryFormat() , args[1]);
		}catch (IOException exp){ exp.printStackTrace();}

		System.out.println("Finish");
	}

	/** This is a class that provide a conviniant way
	 * accessing the input data parsed from the input json file
	 */
	public class ParseJson{
		public Attack[] attacks=null;
		public long R2D2=0;
		public long Lando=0;
		public int Ewoks=0;
	}

	public static class OutputDiaryFormat {
		public int totalAttacks;
		public long HanSoloFinish;
		public long C3POFinish;
		public long R2D2Deactivate;
		public long LeiaTerminate;
		public long HanSoloTerminate;
		public long C3POTerminate;
		public long R2D2Terminate;
		public long LandoTerminate;

		public OutputDiaryFormat(){
			Diary diary = Diary.getInstance();
			totalAttacks=diary.getTotalAttacks().get();
			HanSoloFinish= diary.getTimeOF(TimeDetailOf.HanSoloFinish);
			C3POFinish= diary.getTimeOF(TimeDetailOf.C3POFinish);
			R2D2Deactivate= diary.getTimeOF(TimeDetailOf.R2D2Deactivate);
			LeiaTerminate= diary.getTimeOF(TimeDetailOf.LeiaTerminate);
			HanSoloTerminate= diary.getTimeOF(TimeDetailOf.HanSoloTerminate);
			C3POTerminate= diary.getTimeOF(TimeDetailOf.C3POTerminate);
			R2D2Terminate= diary.getTimeOF(TimeDetailOf.R2D2Terminate);
			LandoTerminate= diary.getTimeOF(TimeDetailOf.LandoTerminate);
		}
	}

	/** This method creates an output json file from an object at defined path given by user
	 * @param gson - Gson parser
	 * @param toOutputFile - Object to converted to json file
	 * @param outPutPath - output json file path
	 */
	public static void objectToOutputJsonFile(Gson gson, Object toOutputFile,String outPutPath) throws IOException {
		FileWriter outputJson = new FileWriter("output.json");
		gson.toJson(toOutputFile,outputJson);
		outputJson.close();
	}
}
