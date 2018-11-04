package kr.hs.ts.scienkipia;

import java.io.File;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Main {

	public static final int MAX_PERSON=100;
	public static final int GAME_ROUNDS = 10000;
	
	public static void game(Entity[] giverPool, Entity[] receiverPool, int generation, int targetGeneration) {
		
		if(generation>targetGeneration)
			return;
		Entity[] nextGiverPool = new Entity[MAX_PERSON];
		Entity[] nextReceiverPool = new Entity[MAX_PERSON];
		double[] giverFitness = new double[MAX_PERSON];
		double[] receiverFitness = new double[MAX_PERSON];
		double sumOfGiverFitness=0;
		double sumOfReceiverFitness=0;
 		//System.out.println(generation+"세대: ");
		
		//게임 진행
		for(int i=0;i<GAME_ROUNDS;i++) {
			int a = Entity.random.nextInt(MAX_PERSON);
			int b = Entity.random.nextInt(MAX_PERSON);
			Giver giver; Receiver receiver;
			giver = (Giver) giverPool[a];
			receiver = (Receiver) receiverPool[b];
			double giverMoney = giver.give();
			double receiverMoney = receiver.give(giverMoney, giver);
			// 게임 로그 분석
			/*
			try {
				File file = new File("C:\\Users\\Samsung\\Desktop\\2-2 사키\\실행결과\\game_log.txt");
				BufferedWriter bw = new BufferedWriter(new FileWriter(file,true));
				PrintWriter pw = new PrintWriter(bw,true);
				if(file.isFile()&&file.canWrite()) {
					
					pw.write("Giver 처음에 준 금액: "+String.format("%.2f",giverMoney)+"   ");
					pw.write("Receiver 취한 금액: "+String.format("%.2f",receiverMoney)+"  ");
					pw.write("Giver 되받은 금액: "+String.format("%.2f",giverMoney*3-receiverMoney)+"  ");
					bw.newLine();
					pw.close();
				}
			}catch (IOException e) {
				System.out.println(e);
			}
			*/
		}
		
		//모든 개체에 대해 적합도 검사
		for(int i=0;i<MAX_PERSON;i++) {
			
			giverFitness[i]=giverPool[i].fitness(giverPool[i],giverPool);
			sumOfGiverFitness+=giverFitness[i];
			receiverFitness[i]=receiverPool[i].fitness(receiverPool[i],receiverPool );
			sumOfReceiverFitness+=receiverFitness[i];
		}
		
		//룰렛 함수
		//다음 세대 생성
		for(int i=0;i<MAX_PERSON;i++) {
			int[] parentIdx = new int[2];
			int j;
			for(int k=0;k<2;k++) {
				Integer s = (int) (long) Math.round(sumOfGiverFitness);
				if(s==0) s=1;
				int point = Entity.random.nextInt( s ), sum=0;
				for(j=0;j<MAX_PERSON;j++) {
					sum+= giverFitness[j] ;
					if(point <= sum ) break;
				}
				if(j==MAX_PERSON) j-=1;
				parentIdx[k]=j;
			}
			
			nextGiverPool[i] = new Giver(giverPool[parentIdx[0]],giverPool[parentIdx[1]],generation);
			
		}
		for(int i=0;i<MAX_PERSON;i++) {
			int[] parentIdx = new int[2];
			for(int k=0;k<2;k++) {
				Integer s = (int) (long) Math.round(sumOfReceiverFitness);
				if(s==0) s=1;
				int point = Entity.random.nextInt( s ), sum=0, j;
				for(j=0;j<MAX_PERSON;j++) {
					sum+= receiverFitness[j] ;
					if(point <= sum ) break;
				}
				if(j==MAX_PERSON) j-=1;
				parentIdx[k]=j;
			}
			nextReceiverPool[i] = new Receiver(receiverPool[parentIdx[0]],receiverPool[parentIdx[1]],generation);
			
		}
		
		double giverAvg=0, receiverAvg=0, giverBenefit=0, receiverBenefit=0, giver_2_avg=0, receiver_2_avg=0;
		//System.out.println("Giver: ");
		for(int i=0;i<MAX_PERSON;i++) {
			//System.out.println(giverPool[i]);
			for(int j=0;j<Entity.GENE_NUM;j++) {
				double a = (double)giverPool[i].chromosome[j];
				giverAvg+=a/(Entity.GENE_NUM*MAX_PERSON);
				giver_2_avg+=a*a/(Entity.GENE_NUM*MAX_PERSON);
			}
			giverBenefit+=giverPool[i].benefit;
		}
			
		//System.out.println("\nReciever: ");
		for(int i=0;i<MAX_PERSON;i++) {
			//System.out.println(receiverPool[i]);
			for(int j=0;j<Entity.GENE_NUM;j++) {
				double a = (double)receiverPool[i].chromosome[j];
				receiverAvg+=a/(Entity.GENE_NUM*MAX_PERSON);	
				receiver_2_avg+=a*a/(Entity.GENE_NUM*MAX_PERSON);
			}

			receiverBenefit+=receiverPool[i].benefit;
		}
					
		try {
			File file = new File("C:\\Users\\Samsung\\Desktop\\2-2 사키\\실행결과\\result.txt");//설정한 경로에 txt 파일로 실행결과 정리
			BufferedWriter bw = new BufferedWriter(new FileWriter(file,true));
			PrintWriter pw = new PrintWriter(bw,true);
			if(file.isFile()&&file.canWrite()) {
				
				pw.write("( "+generation+"세대 )");
				pw.write("Giver 평균: "+String.format("%.4f",giverAvg)+"%   ");
				pw.write("Receiver 평균: "+String.format("%.4f",receiverAvg)+"%  ");
				bw.newLine();
				pw.write("Giver 평균 이익: "+String.format("%.2f",giverBenefit/GAME_ROUNDS)+"  ");
				pw.write("Receiver 평균 이익: "+String.format("%.2f",receiverBenefit/GAME_ROUNDS)+"  ");
				bw.newLine();
				pw.close();
			}
		}catch (IOException e) {
			System.out.println(e);
		}
		
		game(nextGiverPool,nextReceiverPool,generation+1,targetGeneration);
		
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Entity[] giverPool = new Entity [MAX_PERSON];
		Entity[] receiverPool = new Entity [MAX_PERSON];
		int targetGeneration = 500;
		try {
			File file = new File("C:\\Users\\Samsung\\Desktop\\2-2 사키\\실행결과\\result.txt");
			BufferedWriter bw = new BufferedWriter(new FileWriter(file,true));
			PrintWriter pw = new PrintWriter(bw,true);
			if(file.isFile()&&file.canWrite()) {
				
				pw.write("개체 수: "+Integer.toString(Main.MAX_PERSON)+"\n");bw.newLine();
				pw.write("세대 당 게임 횟수: "+Integer.toString(Main.GAME_ROUNDS)+"\n");bw.newLine();
				pw.write("최대 세대전환 횟수: "+Integer.toString(targetGeneration)+"\n");bw.newLine();
				pw.write("돌연변이율: "+Double.toString(Entity.MUTANT_RATE)+"\n");bw.newLine();
				pw.write("선택압: "+Double.toString(Entity.SELECTION_PRESSURE)+"\n");bw.newLine();
				pw.write("유전자 길이: "+Integer.toString(Entity.GENE_NUM)+"\n");bw.newLine();
				pw.write("교배 방법: "+"Uniform Crossover"+"\n");bw.newLine();
				pw.write("변이 생성 방법: "+"난수 변형 (비균등 변이 최적화 적용)"+"\n");bw.newLine();
				pw.close();
				
			}
		}catch (IOException e) {
			System.out.println(e);
		}
		
		for(int i=0;i<MAX_PERSON;i++)
		{
			giverPool[i]=new Giver();
			receiverPool[i]=new Receiver();
		}
		game(giverPool,receiverPool,1,targetGeneration);
		System.out.println("done");
	}

}

//300세대 실행 Giver 5.2845% Receiver 29.187%