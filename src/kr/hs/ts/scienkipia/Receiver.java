package kr.hs.ts.scienkipia;

public class Receiver extends Entity{

	Receiver(){}
	Receiver(Entity receiver1, Entity receiver2,int generation){
		
		super(receiver1,receiver2,generation);
	}
	// 상대에게 줄 금액 결정하는 부분
	double donateRate(double money) {
	
		double result=0;
		
		for(int i=1;i<=GENE_NUM;i++)
			if( (double)i*10000/GENE_NUM >= money ) {
				result=(double)chromosome[i-1]/100;
				break;
			}
		return result;
	}
	
	double give(double money, Giver giver) {
		benefit+=3*money*(1-donateRate(money));
		giver.benefit+=3*money*donateRate(money);
		playCount+=1;
		return 3*money*(1-donateRate(money));
	}
}
