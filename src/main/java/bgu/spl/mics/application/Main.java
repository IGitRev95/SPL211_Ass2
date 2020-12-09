package bgu.spl.mics.application;

import bgu.spl.mics.application.passiveObjects.*;
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
	public static CountDownLatch  threadInitCounter;//verifying that all the MicroServices except of Leia finished initialized

	public static void main(String[] args) throws FileNotFoundException {
		threadInitCounter = new CountDownLatch(4);

		//Json Parsing
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		ParseJson inputJson = gson.fromJson(new FileReader( args[0] ), ParseJson.class);

		//init objects with input data json related
		Ewoks.init( inputJson.Ewoks );
		LeiaMicroservice pLeia = new LeiaMicroservice( inputJson.attacks );
		R2D2Microservice r2D2 = new R2D2Microservice( inputJson.R2D2 );
		LandoMicroservice lando = new LandoMicroservice( inputJson.Lando );

		HanSoloMicroservice hSolo = new HanSoloMicroservice();
		C3POMicroservice c3po = new C3POMicroservice();

		//Threads init
		Thread[] threads ={new Thread(lando),new Thread(r2D2),new Thread(hSolo),new Thread(c3po)};
		for (Thread th:threads ) { th.start();}

		//Leia waiting for all other micro services to initialize
		try {
			threadInitCounter.await();
			Thread pL = new Thread(pLeia);
			pL.start();

			//waiting for all threads to terminate
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

		//Generating Output Json
		try {
			objectToOutputJsonFile(gson, Diary.getInstance() , args[1]);
		}catch (IOException exp){ exp.printStackTrace(); }

		System.out.println("To Be Continue... The End?");
	}

	/** This is a class that provide a convenient way
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
		FileWriter outputJson = new FileWriter(new File( outPutPath ));
		gson.toJson( toOutputFile, outputJson);
		outputJson.close();
	}
}
