package project;

	public class Booking {
	    private final int bookingId;
	    private final Room room;
	    private final String guestName;
	    private final int nights;
	    private final boolean breakfast;
	    private final String coupon;
	    private double roomCharge, breakfastCharge, discount, tax, total;
	    private boolean active = true;
	    private double cancellationFee = 0;
	    private static int counter = 1000;

	    public Booking(Room room, String guestName, int nights, boolean breakfast, String coupon) {
	        this.bookingId = ++counter;
	        this.room = room;
	        this.guestName = guestName;
	        this.nights = nights;
	        this.breakfast = breakfast;
	        this.coupon = coupon;
	    }

	    public void setCharges(double roomCharge, double breakfastCharge, double discount, double tax) {
	        this.roomCharge = roomCharge;
	        this.breakfastCharge = breakfastCharge;
	        this.discount = discount;
	        this.tax = tax;
	        this.total = roomCharge + breakfastCharge - discount + tax;
	    }

	    public void markCancelled(double fee) { active = false; cancellationFee = fee; }

	    public int getBookingId() { return bookingId; }
	    public Room getRoom() { return room; }
	    public String getGuestName() { return guestName; }
	    public int getNights() { return nights; }
	    public boolean hasBreakfast() { return breakfast; }
	    public String getCoupon() { return coupon; }
	    public double getRoomCharge() { return roomCharge; }
	    public double getBreakfastCharge() { return breakfastCharge; }
	    public double getDiscount() { return discount; }
	    public double getTax() { return tax; }
	    public double getTotal() { return total; }
	    public boolean isActive() { return active; }
	    public double getCancellationFee() { return cancellationFee; }
	}



