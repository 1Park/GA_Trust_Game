package kr.hs.ts.scienkipia;

public class Giver extends Entity{
	
	Giver(){}
	Giver(Entity giver1, Entity giver2,int generation){
		super(giver1,giver2,generation);
	}
	// 상대에게 줄 금액 결정하는 부분
	double donateRate() {
	
		int result=0;
		
		for(int i=0;i<GENE_NUM;i++)
			result+=chromosome[i];
		return 0.01*result/GENE_NUM;
	}

	double give() {
		benefit+=10000*(1-donateRate());
		playCount+=1;
		return 10000*donateRate();
	}
}
