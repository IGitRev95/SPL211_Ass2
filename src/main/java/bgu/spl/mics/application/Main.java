package bgu.spl.mics.application;

import bgu.spl.mics.application.passiveObjects.Attack;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.Ewoks;
import bgu.spl.mics.application.passiveObjects.TimeDetailOf;
import bgu.spl.mics.application.services.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.util.concurrent.CountDownLatch;

/** This is the Main class of the application. You should parse the input file,
 * create the different components of the application, and run the system.
 * In the end, you should output a JSON.
 */
public class Main {
	public static CountDownLatch  threadInitCounter;

	public static void main(String[] args) throws FileNotFoundException {
		threadInitCounter = new CountDownLatch(4);

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		ParseJson inputJson = gson.fromJson(new FileReader(args[0]),ParseJson.class);

		Ewoks.init(inputJson.Ewoks);
		LeiaMicroservice pLeia = new LeiaMicroservice(inputJson.attacks);
		R2D2Microservice r2D2 = new R2D2Microservice(inputJson.R2D2);
		LandoMicroservice lando = new LandoMicroservice(inputJson.Lando);

		HanSoloMicroservice hSolo = new HanSoloMicroservice();
		C3POMicroservice c3po = new C3POMicroservice();

		Thread[] threads ={new Thread(lando),new Thread(r2D2),new Thread(hSolo),new Thread(c3po)};
		for (Thread th:threads ) { th.start();}
		try {
			threadInitCounter.await();
			Thread pL = new Thread(pLeia);
			pL.start();
			pL.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		for (Thread th:threads ) {
			try {
				th.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}


		try {
			objectToOutputJsonFile(gson, Diary.getInstance() , args[1]);
		}catch (IOException exp){ exp.printStackTrace();}

		System.out.println("Finish");
	}

	/** This is a class that provide a conviniant way
	 * accessing the input data parsed from the input json file
	 */
	public class ParseJson{
		private Attack[] attacks=null;
		private long R2D2=0;
		private long Lando=0;
		private int Ewoks=0;
	}


	/** This method creates an output json file from an object at defined path given by user
	 * @param gson - Gson parser
	 * @param toOutputFile - Object to converted to json file
	 * @param outPutPath - output json file path
	 */
	public static void objectToOutputJsonFile(Gson gson, Object toOutputFile,String outPutPath) throws IOException {
		FileWriter outputJson = new FileWriter(new File(outPutPath+"/output.json"));//TODO: Before submission remove "+"/output.json""
		gson.toJson(toOutputFile,outputJson);
		outputJson.close();
	}
}
