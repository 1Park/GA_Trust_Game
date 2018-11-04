package kr.hs.ts.scienkipia;

import java.util.Random;

public class Entity {
	
	public static final int INF = 1000000000;
	public static final double MUTANT_RATE=0.02; // 2% Ȯ���� �������� ����
	public static final int GENE_NUM=20; // ����ü ���� 
	public static final double SELECTION_PRESSURE = 3.5;
	protected int[] chromosome = new int [GENE_NUM];
	public double benefit;
	protected int playCount;
	public static Random random = new Random();
	
	Entity(){
		benefit = 0; playCount=0;
		for(int i=0;i<GENE_NUM;i++)
			if(random.nextInt(2)==0)
				chromosome[i]=random.nextInt(100);
			else
				chromosome[i]=50+random.nextInt(50);
	}
	
	Entity(Entity parent1, Entity parent2, int generation){
		benefit = 0; playCount=0;
		if(random.nextFloat()>MUTANT_RATE)
			this.chromosome = crossover(parent1.chromosome,parent2.chromosome);
		else
			this.chromosome = mutant(crossover(parent1.chromosome,parent2.chromosome), generation);
	}
	

	
	//����
	int[] crossover(int[] chr1, int[] chr2) { 
		int result[]= new int[GENE_NUM];
		
		// 1/4 ����
		/*
		boolean crossPoint=true; //���������� Ȯ��
		if(random.nextInt(2)==0)
			crossPoint=false;
		for(int i=0;i<GENE_NUM;i++) {
			if(random.nextFloat()<0.25) // 25% �󵵷� ���� �̷����
				crossPoint=!crossPoint;
			if(crossPoint)
				result[i]=chr1[i];
			else
				result[i]=chr2[i];
		}
		*/
		
		//uniform crossover(�յ� ����)
		for(int i=0;i<GENE_NUM;i++) {
			if(random.nextFloat()<0.5)
				result[i]=chr1[i];
			else
				result[i]=chr2[i];
		}
		
		//1 point crossover(1�� ����)
		/*
		int crossPoint = random.nextInt(GENE_NUM-1);
		for(int i=0;i<GENE_NUM;i++) {
			if(i<=crossPoint)
				result[i]=chr1[i];
			else
				result[i]=chr2[i];
				
		}
		*/
		return result;
	}
	
	//�������� - Swap Mutation ��� 
	/*
	int[] mutant(int[] chr) {
		int result[] = chr;
		int a,b,temp;
		for(int i=0;i<GENE_NUM/10;i++)
		{
			a=random.nextInt(GENE_NUM);
			b=random.nextInt(GENE_NUM);
			temp=result[a];
			result[a]=result[b];
			result[b]=temp;
		}
		return result;
	}
	*/
	
	
	//�������� - ��յ� ���� ����ȭ
	int[] mutant(int[] chr, int generation) { 
		int result[] = chr;
		int a,b,temp;
		for(int i=0;i<GENE_NUM;i++)
		{
			if(random.nextFloat()<0.8)
				continue;
			if(random.nextInt(2)==0) 
				result[i]=result[i]+random.nextInt(Math.min((51-generation/10) , 100-result[i]));
			else
				result[i]=result[i]-random.nextInt(Math.min((51-generation/10), result[i]+1));

		}
		return result;
	}
	
	public String toString() {
		String s = "";
		for(int i=0;i<GENE_NUM;i++) {
			s=s+chromosome[i]+" ";
			if(chromosome[i]<10)
				s+=" ";
		}
			
		return s; //+benefit
	}
	//���յ� �Լ�
	double fitness(Entity entity, Entity[] pool) {
		double Cw=INF,Cb=0;
		for(int i=0;i<pool.length;i++) {
			if(pool[i].benefit/pool[i].playCount<Cw)
				Cw=pool[i].benefit/pool[i].playCount;
			if(pool[i].benefit/pool[i].playCount>Cb)
				Cb=pool[i].benefit/pool[i].playCount;
		}
		return ( (entity.benefit/entity.playCount-Cw) + (Cb-Cw) ) / (SELECTION_PRESSURE-1);
	}
	
}
