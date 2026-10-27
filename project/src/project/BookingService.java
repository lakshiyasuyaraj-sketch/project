package project;

import java.util.*;

	/** Manages rooms, bookings, cancellations, invoices and reports. */
	public class BookingService {
	    private final List<Room> rooms = new ArrayList<>();       // Polymorphic list
	    private final List<Booking> bookings = new ArrayList<>();
	    private final Map<String, Double> percentCoupons = new HashMap<>();
	    private final Map<String, Double> flatCoupons = new HashMap<>();
	    public static final double GST_RATE = 0.12;
	    public static final double BREAKFAST_PER_NIGHT = 350;
	    public static final double CANCELLATION_FEE_RATE = 0.10;

	    public BookingService() {
	        percentCoupons.put("WELCOME10", 0.10);
	        flatCoupons.put("FLAT500", 500.0);
	    }

	    public void addRoom(Room r) {
	        for (Room x : rooms)
	            if (x.getRoomNo() == r.getRoomNo()) {
	                System.out.println("Room " + r.getRoomNo() + " already exists. Not added.");
	                return;
	            }
	        rooms.add(r);
	    }

	    public Room findRoom(int roomNo) {
	        for (Room r : rooms) if (r.getRoomNo() == roomNo) return r;
	        return null;
	    }

	    public void viewAvailability() {
	        System.out.println("---- Room Availability ----");
	        for (Room r : rooms) System.out.println(r);
	        System.out.println("Available: " + countByStatus(RoomStatus.AVAILABLE) + " / " + rooms.size());
	    }

	    private int countByStatus(RoomStatus s) {
	        int c = 0;
	        for (Room r : rooms) if (r.getStatus() == s) c++;
	        return c;
	    }

	    // ---- Overloaded book() methods ----
	    public Booking book(int roomNo, String guest, int nights) {
	        return book(roomNo, guest, nights, false, null);
	    }

	    public Booking book(int roomNo, String guest, int nights, boolean breakfast) {
	        return book(roomNo, guest, nights, breakfast, null);
	    }

	    public Booking book(int roomNo, String guest, int nights, boolean breakfast, String coupon) {
	        Room room = findRoom(roomNo);
	        if (room == null) { System.out.println("Booking failed: room " + roomNo + " not found."); return null; }
	        if (nights <= 0) { System.out.println("Booking failed: nights must be > 0."); return null; }
	        if (!room.book()) {
	            System.out.println("Booking failed: room " + roomNo + " is not available (" + room.getStatus() + ").");
	            return null;
	        }
	        Booking b = new Booking(room, guest, nights, breakfast, coupon);
	        double roomCharge = room.computeBill(nights);                // dynamic dispatch
	        double bfast = (breakfast && !room.isBreakfastIncluded()) ? BREAKFAST_PER_NIGHT * nights : 0;
	        double gross = roomCharge + bfast;
	        double discount = 0;
	        if (coupon != null) {
	            String code = coupon.toUpperCase();
	            if (percentCoupons.containsKey(code)) discount = gross * percentCoupons.get(code);
	            else if (flatCoupons.containsKey(code)) discount = Math.min(flatCoupons.get(code), gross);
	            else System.out.println("Note: coupon '" + coupon + "' is invalid and was ignored.");
	        }
	        double tax = (gross - discount) * GST_RATE;
	        b.setCharges(roomCharge, bfast, discount, tax);
	        bookings.add(b);
	        System.out.println("Booking confirmed. ID " + b.getBookingId() + " for " + guest + ", room " + roomNo);
	        return b;
	    }

	    public boolean cancel(int bookingId) {
	        for (Booking b : bookings) {
	            if (b.getBookingId() == bookingId) {
	                if (!b.isActive()) { System.out.println("Booking " + bookingId + " already cancelled."); return false; }
	                b.getRoom().cancel();
	                double fee = b.getTotal() * CANCELLATION_FEE_RATE;
	                b.markCancelled(fee);
	                System.out.printf("Booking %d cancelled. Fee Rs.%.2f, refund Rs.%.2f%n",
	                        bookingId, fee, b.getTotal() - fee);
	                return true;
	            }
	        }
	        System.out.println("Cancel failed: booking " + bookingId + " not found.");
	        return false;
	    }

	    public void generateInvoice(int bookingId) {
	        for (Booking b : bookings) {
	            if (b.getBookingId() != bookingId) continue;
	            Room r = b.getRoom();
	            System.out.println("==================== INVOICE ====================");
	            System.out.println("Booking ID : " + b.getBookingId() + (b.isActive() ? "" : "  (CANCELLED)"));
	            System.out.println("Guest      : " + b.getGuestName());
	            System.out.println("Room       : " + r.getRoomNo() + " (" + r.getType() + "), " + b.getNights() + " night(s)");
	            System.out.printf("Room charge        : Rs.%10.2f%n", b.getRoomCharge());
	            String bf = r.isBreakfastIncluded() ? "complimentary" : (b.hasBreakfast() ? "" : "not opted");
	            System.out.printf("Breakfast          : Rs.%10.2f %s%n", b.getBreakfastCharge(), bf);
	            System.out.printf("Discount (%s) : -Rs.%9.2f%n", b.getCoupon() == null ? "none" : b.getCoupon(), b.getDiscount());
	            System.out.printf("GST @ %.0f%%          : Rs.%10.2f%n", GST_RATE * 100, b.getTax());
	            System.out.printf("TOTAL              : Rs.%10.2f%n", b.getTotal());
	            if (!b.isActive()) System.out.printf("Cancellation fee   : Rs.%10.2f%n", b.getCancellationFee());
	            System.out.println("=================================================");
	            return;
	        }
	        System.out.println("Invoice failed: booking " + bookingId + " not found.");
	    }

	    public void dailyReport() {
	        double revenue = 0, fees = 0;
	        int active = 0, cancelled = 0;
	        for (Booking b : bookings) {
	            if (b.isActive()) { revenue += b.getTotal(); active++; }
	            else { fees += b.getCancellationFee(); cancelled++; }
	        }
	        int booked = countByStatus(RoomStatus.BOOKED);
	        System.out.println("------------- DAILY REPORT -------------");
	        System.out.println("Total rooms        : " + rooms.size());
	        System.out.println("Occupied rooms     : " + booked);
	        System.out.println("Available rooms    : " + countByStatus(RoomStatus.AVAILABLE));
	        System.out.println("Under maintenance  : " + countByStatus(RoomStatus.MAINTENANCE));
	        System.out.printf("Occupancy          : %.1f%%%n", rooms.isEmpty() ? 0 : 100.0 * booked / rooms.size());
	        System.out.println("Active bookings    : " + active + ", cancelled: " + cancelled);
	        System.out.printf("Booking revenue    : Rs.%.2f%n", revenue);
	        System.out.printf("Cancellation fees  : Rs.%.2f%n", fees);
	        System.out.printf("Total revenue      : Rs.%.2f%n", revenue + fees);
	        System.out.println("----------------------------------------");
	    }

	    public List<Room> getRooms() { return Collections.unmodifiableList(rooms); }
	    public List<Booking> getBookings() { return Collections.unmodifiableList(bookings); }
	}



