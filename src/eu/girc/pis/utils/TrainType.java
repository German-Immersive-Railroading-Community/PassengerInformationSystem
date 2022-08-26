package eu.girc.pis.utils;

public enum TrainType {
	
	TRAM("T"),
	UNDERGROUND_TRAIN("U"),
	CITY_TRAIN("S"),
	
	REGIONAL_TRAIN("RB"),
	REGIONAL_EXPRESS("RE"),
	INTERREGIO_EXPRESS("IRE"),
	INTERREGIO("IR"),
	AIRPORT_EXPRESS("FEX"),
	INTERCITY("IC"),
	INTERCITY_EXPRESS("ICE"),
	EUROCITY("EC"),
	EUROCITY_EXPRESS("ECE"),
	EXPRESS("D"),
	
	REGIO_TRAIN("R"),
	REGIONAL_XPRESS("REX"),
	CITYJET_XPRESS("CJX"),
	CITY_AIRPORT_TRAIN("CAT"),
	REGIOJET("RGJ"),
	RAILJET("RJ"),
	RAILJET_XPRESS("RJX"),
	SUPER_CITY("SC"),
	
	EURONIGHT("EN"),
	NIGHTJET("NJ"),
	NIGHT_EXPRESS("DN"),
	
	RESEAU_EXPRESS_REGIONAL("RER"),
	TRANSPORT_EXPRESS_REGIONAL("TER"),
	EUROSTAR("ES"),
	THALYS("THA"),
	TRAIN_A_GRANDE_VITESSE("TGV"),
	
	FREIGHT_INTERNATIONAL_EUROPE("FE"),
	FREIGHT_INTERREGIONAL("FIR"),
	FREIGHT_REGIONAL("FR"),
	FREIGHT_SPECIAL("FS"),
	FREIGHT_OTHER("FX"),
	FREIGHT_FEEDER("FZ"),
	FREIGHT_FEEDER_SPECIAL("FZS"),
	FREIGHT_FEEDER_TRANSPORT_CHAIN("FZT"),
	
	COMPLETE_FREIGHT_AD_HOC_SPECIAL("CFA"),
	COMPLETE_FREIGHT_SPECIAL("CFN"),
	COMPLETE_FREIGHT_PRE_ORDERD("CFP"),
	COMPLETE_HEAVY_LOAD("CHL"),
	COMPLETE_INTER_LINE("CIL"),
	COMPLETE_SCEDULED("CS"),
	COMPLETE_SCEDULED_QUALITY("CSQ"),
	COMPLETE_TRUCKING_TRAIN("CT"),
	
	INTER_KOMBI_EXPRESS("IKE"),
	INTER_KOMBI_LOGISTIC("IKL"),
	INTER_KOMBI_SPECIAL("IKS");
	
	private final String token;
	
	TrainType(String token) {
		this.token = token;
	}

	public String getToken() {
		return token;
	}
	
	public String getName() {
		final StringBuilder name = new StringBuilder(name().toLowerCase());
		final String firstChar = Character.toString(name.charAt(0)).toUpperCase();
		name.setCharAt(0, firstChar.toCharArray()[0]);
		for (int i = 0; i < name.length(); i++) {
			if (name.charAt(i) == '_') {
				name.setCharAt(i, ' ');
				if (i + 1 < name.length()) {
					final String character = Character.toString(name.charAt(i + 1)).toUpperCase();
					name.setCharAt(i + 1, character.toCharArray()[0]);
				}
			}
		}
		return name.toString();
	}

}
