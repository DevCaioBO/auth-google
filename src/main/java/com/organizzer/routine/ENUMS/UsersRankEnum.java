package com.organizzer.routine.ENUMS;

public enum UsersRankEnum { // enum teste = {bronze =0, silver = 50, iron = 500, ...}
	
	BRONZE(0),
	PRATA(50),
	FERRO(500),
	OURO(1500),
	ESMERALDA(2000),
	DIAMANTE(5000),
	AMETISTA(15000),
	RADIANTE(30000)
	;
	private final Integer xpRequire;

	 UsersRankEnum(Integer xpRequire) {
		this.xpRequire = xpRequire;
	}
	 
	 
	
	public Integer getXpRequire() {
		return xpRequire;
	}



	public static UsersRankEnum getRankForXp(Integer xp) {
		UsersRankEnum lastRank = BRONZE;
		for(UsersRankEnum rank: UsersRankEnum.values()) {
			if(xp >= rank.getXpRequire()) {
				lastRank = rank;
			}
			else {
				break;
			}
			
		}
		return lastRank;
	}
	
	

	
}
