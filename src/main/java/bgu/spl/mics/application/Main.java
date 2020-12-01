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

/** This is the Main class of the application. You should parse the input file,
 * create the different components of the application, and run the system.
 * In the end, you should output a JSON.
 */
public class Main {
	public static void main(String[] args) throws FileNotFoundException {
		/*

		TODO: maybe init thread pool
		TODO: deliver microservices to threads and run all
		TODO: set a gracefully termination process with interrupts, joins, and closing al microservices
		TODO: construct Json from diary for output file
		 */
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		ParseJson inputJson = gson.fromJson(new FileReader(args[0]),ParseJson.class);

		MessageBus messageBus = new MessageBusImpl();
		Diary battleLog = new Diary();
		Ewoks ewoksPoll = new Ewoks(inputJson.Ewoks);
		LeiaMicroservice pLeia = new LeiaMicroservice(inputJson.attacks);
		R2D2Microservice r2D2 = new R2D2Microservice(inputJson.R2D2);
		LandoMicroservice lando = new LandoMicroservice(inputJson.Lando);

		HanSoloMicroservice hSolo = new HanSoloMicroservice();
		C3POMicroservice c3po = new C3POMicroservice();


//		try {
//			objectToOutputJsonFile(gson, inputJson, args[1]);
//		}catch (IOException exp){ exp.printStackTrace();}

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
		FileWriter outputJson = new FileWriter(outPutPath); //TODO: complete method (Ido)
		gson.toJson(toOutputFile,outputJson);
		outputJson.close();
	}
}
