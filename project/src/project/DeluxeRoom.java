package project;

	public class DeluxeRoom extends Room {
	    private String extraAmenities;
	    private double amenityChargePerNight;
	    private static final int LONG_STAY_NIGHTS = 5;
	    private static final double LONG_STAY_DISCOUNT = 0.05;

	    public DeluxeRoom(int roomNo, double basePrice, String extraAmenities, double amenityChargePerNight) {
	        super(roomNo, "Deluxe", basePrice);
	        this.extraAmenities = extraAmenities;
	        this.amenityChargePerNight = amenityChargePerNight;
	    }

	    @Override
	    public double computeBill(int nights) {            // Overriding
	        double total = (getBasePrice() + amenityChargePerNight) * nights;
	        if (nights >= LONG_STAY_NIGHTS) total -= total * LONG_STAY_DISCOUNT;
	        return total;
	    }

	    @Override
	    public String getAmenities() { return super.getAmenities() + ", " + extraAmenities; }

	    public String getExtraAmenities() { return extraAmenities; }
	    public void setExtraAmenities(String extraAmenities) { this.extraAmenities = extraAmenities; }
	    public double getAmenityChargePerNight() { return amenityChargePerNight; }
	    public void setAmenityChargePerNight(double c) {
	        if (c < 0) throw new IllegalArgumentException("Charge cannot be negative");
	        this.amenityChargePerNight = c;
	    }
	}



