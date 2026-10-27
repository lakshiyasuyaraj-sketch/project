package project;

	public class HotelAppMain {
	    public static void main(String[] args) {
	        BookingService svc = new BookingService();

	        System.out.println(">>> 1. Adding rooms");
	        svc.addRoom(new Room(101, "Standard", 2000));
	        svc.addRoom(new Room(102, "Standard", 2200));
	        svc.addRoom(new DeluxeRoom(201, 3500, "Mini-bar, Balcony", 400));
	        svc.addRoom(new DeluxeRoom(202, 3800, "Mini-bar, Sea view", 450));
	        svc.addRoom(new SuiteRoom(301, 8000, true, 1000));
	        svc.addRoom(new SuiteRoom(302, 9500, false, 0));
	        svc.addRoom(new Room(101, "Standard", 2000));   // duplicate
	        System.out.println("Room objects created (incl. rejected duplicate): " + Room.getTotalRooms());
	        svc.viewAvailability();

	        System.out.println("\n>>> 2. Bookings (overloaded book())");
	        Booking b1 = svc.book(101, "Arun Kumar", 2);
	        Booking b2 = svc.book(201, "Priya Nair", 3, true);
	        Booking b3 = svc.book(301, "Rahul Sharma", 7, true, "WELCOME10");
	        Booking b4 = svc.book(202, "Meena Iyer", 5, true, "FLAT500");
	        svc.book(101, "Late Guest", 1);               // already booked
	        svc.book(999, "Nobody", 1);                   // invalid room
	        svc.book(102, "Sam", 2, false, "BADCODE");    // invalid coupon
	        svc.viewAvailability();

	        System.out.println("\n>>> 3. Cancellation");
	        svc.cancel(b1.getBookingId());
	        svc.cancel(b1.getBookingId());                // repeat
	        svc.cancel(5555);                             // unknown id

	        System.out.println("\n>>> 4. Invoices (polymorphic computeBill)");
	        svc.generateInvoice(b2.getBookingId());
	        svc.generateInvoice(b3.getBookingId());
	        svc.generateInvoice(b4.getBookingId());
	        svc.generateInvoice(b1.getBookingId());

	        System.out.println("\n>>> 5. Polymorphism: 4-night bill for every room");
	        for (Room r : svc.getRooms())
	            System.out.printf("Room %d (%s): Rs.%.2f%n", r.getRoomNo(), r.getType(), r.computeBill(4));

	        System.out.println("\n>>> 6. Encapsulation: invalid price rejected");
	        try { svc.findRoom(102).setBasePrice(-5); }
	        catch (IllegalArgumentException e) { System.out.println("Rejected: " + e.getMessage()); }

	        System.out.println("\n>>> 7. Daily occupancy & revenue report");
	        svc.dailyReport();
	    }
	}



