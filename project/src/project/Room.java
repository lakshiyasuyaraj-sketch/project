package project;

	public class Room {
	    private final int roomNo;
	    private String type;
	    private double basePrice;          
	    private RoomStatus status;
	    private static int totalRooms = 0; 
	    public Room(int roomNo, String type, double basePrice) {
	        if (basePrice <= 0) throw new IllegalArgumentException("Price must be positive");
	        this.roomNo = roomNo;
	        this.type = type;
	        this.basePrice = basePrice;
	        this.status = RoomStatus.AVAILABLE;
	        totalRooms++;
	    }

	    // ---- core behaviour ----
	    public boolean checkAvailability() { return status == RoomStatus.AVAILABLE; }

	    public boolean book() {
	        if (!checkAvailability()) return false;
	        status = RoomStatus.BOOKED;
	        return true;
	    }

	    public boolean cancel() {
	        if (status != RoomStatus.BOOKED) return false;
	        status = RoomStatus.AVAILABLE;
	        return true;
	    }

	    public void sendForMaintenance() {
	        if (status == RoomStatus.BOOKED) throw new IllegalStateException("Room is occupied");
	        status = RoomStatus.MAINTENANCE;
	    }

	    public void completeMaintenance() {
	        if (status == RoomStatus.MAINTENANCE) status = RoomStatus.AVAILABLE;
	    }

	    /** Room charge for the stay, before breakfast, discount and tax. Overridden by subclasses. */
	    public double computeBill(int nights) { return basePrice * nights; }

	    public String getAmenities() { return "TV, Wi-Fi, AC"; }

	    /** True if breakfast is complimentary for this room type. */
	    public boolean isBreakfastIncluded() { return false; }

	    // ---- getters / setters ----
	    public int getRoomNo() { return roomNo; }
	    public String getType() { return type; }
	    public void setType(String type) { this.type = type; }
	    public double getBasePrice() { return basePrice; }
	    public void setBasePrice(double basePrice) {
	        if (basePrice <= 0) throw new IllegalArgumentException("Price must be positive");
	        this.basePrice = basePrice;
	    }
	    public RoomStatus getStatus() { return status; }
	    public static int getTotalRooms() { return totalRooms; }

	    @Override
	    public String toString() {
	        return String.format("Room %-4d %-8s Rs.%-9.2f %-12s [%s]",
	                roomNo, type, basePrice, status, getAmenities());
	    }
	}




