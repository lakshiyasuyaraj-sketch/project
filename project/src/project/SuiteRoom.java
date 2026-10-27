package project;

	public class SuiteRoom extends Room {
	    private boolean loungeAccess;
	    private double loungeChargePerNight;
	    private static final double LUXURY_SURCHARGE = 0.10;
	    private static final int FREE_NIGHT_AFTER = 7;

	    public SuiteRoom(int roomNo, double basePrice, boolean loungeAccess, double loungeChargePerNight) {
	        super(roomNo, "Suite", basePrice);
	        this.loungeAccess = loungeAccess;
	        this.loungeChargePerNight = loungeChargePerNight;
	    }

	    @Override
	    public double computeBill(int nights) {            // Overriding
	        double perNight = getBasePrice() + (loungeAccess ? loungeChargePerNight : 0);
	        int payableNights = nights >= FREE_NIGHT_AFTER ? nights - 1 : nights;
	        double subtotal = perNight * payableNights;
	        return subtotal + subtotal * LUXURY_SURCHARGE;
	    }

	    @Override
	    public String getAmenities() {
	        return super.getAmenities() + ", Jacuzzi, Living area" + (loungeAccess ? ", Lounge" : "");
	    }

	    @Override
	    public boolean isBreakfastIncluded() { return true; }

	    public boolean hasLoungeAccess() { return loungeAccess; }
	    public void setLoungeAccess(boolean loungeAccess) { this.loungeAccess = loungeAccess; }
	    public double getLoungeChargePerNight() { return loungeChargePerNight; }
	    public void setLoungeChargePerNight(double c) {
	        if (c < 0) throw new IllegalArgumentException("Charge cannot be negative");
	        this.loungeChargePerNight = c;
	    }
	}



