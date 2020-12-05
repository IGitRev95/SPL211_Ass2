package bgu.spl.mics.application;

import bgu.spl.mics.MessageBus;
import bgu.spl.mics.MessageBusImpl;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.passiveObjects.Attack;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.Ewoks;
import bgu.spl.mics.application.services.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

import java.io.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/** This is the Main class of the application. You should parse the input file,
 * create the different components of the application, and run the system.
 * In the end, you should output a JSON.
 */
public class Main {
	public static void main(String[] args) throws FileNotFoundException {
		/*
		TODO: construct Json from diary for output file
		 */
		Diary battleLog = Diary.getInstance();// manage Time stamps relatively to its creation

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		ParseJson inputJson = gson.fromJson(new FileReader(args[0]),ParseJson.class);

		CountDownLatch threadInitCounter = new CountDownLatch(4);
		AtomicInteger attackCounter = battleLog.getTotalAttacks();

		Ewoks ewoksPool = Ewoks.init(inputJson.Ewoks);
		MessageBus messageBus = new MessageBusImpl();
		LeiaMicroservice pLeia = new LeiaMicroservice(inputJson.attacks,threadInitCounter);//,messageBus,battleLog);
		R2D2Microservice r2D2 = new R2D2Microservice(inputJson.R2D2,threadInitCounter);//,messageBus,battleLog);
		LandoMicroservice lando = new LandoMicroservice(inputJson.Lando,threadInitCounter);//,messageBus,battleLog);

		HanSoloMicroservice hSolo = new HanSoloMicroservice(threadInitCounter,attackCounter);//,messageBus,ewoksPool,battleLog);
		C3POMicroservice c3po = new C3POMicroservice(threadInitCounter,attackCounter);//,messageBus,ewoksPool,battleLog);
/*
		Thread pL = new Thread(pLeia);
		Thread r2 = new Thread(r2D2);
		Thread lN = new Thread(lando);
		Thread hS = new Thread(hSolo);
		Thread c3 = new Thread(c3po);
		Thread[] threads ={pL,r2,lN,hS,c3};
*/
		Thread[] threads ={new Thread(pLeia),new Thread(lando),new Thread(r2D2),new Thread(hSolo),new Thread(c3po)};
//		for (Thread th:threads ) { th.start();}
//
//		for (Thread th:threads ) {
//			try {
//				th.join();
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}

		try {
			objectToOutputJsonFile(gson, inputJson, args[1]);
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
	/** This method creates an output json file from an object at defined path given by user
	 * @param gson - Gson parser
	 * @param toOutputFile - Object to converted to json file
	 * @param outPutPath - output json file path
	 */
	public static void objectToOutputJsonFile(Gson gson, Object toOutputFile,String outPutPath) throws IOException {
		FileWriter outputJson = new FileWriter("output.json"); //TODO: complete method (Ido)
		gson.toJson(toOutputFile,outputJson);
		outputJson.close();
	}
}
