
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JOptionPane;

public class Hotel implements Booking {
    private Room[] rooms;
    private ArrayList<BookingInfo> bookings;
    private final int MAX_ROOMS = 5;

    public Hotel() {
        rooms = new Room[MAX_ROOMS];
        bookings = new ArrayList<BookingInfo>();
        // Initialize rooms
        for (int i = 0; i <MAX_ROOMS; i++) {
            if (i < 2) {
            rooms[i] = new SingleRoom(i+1, 1);
            } else if (i <4) {
            rooms[i] = new DoubleRoom(i+1, 2);
            } else {
            rooms[i] = new VipRoom(i+1, true);
            }
            }
            }
            public void bookRoom(String name, String phone, Date checkin, Date checkout, RoomType roomType) {
                // Find available room of the specified type
                Room room = null;
                for (int i = 0; i < rooms.length; i++) {
                    if (rooms[i].getRoomType() == roomType && rooms[i].isAvailable()) {
                        room = rooms[i];
                        break;
                    }
                }
                if (room == null) {
                    JOptionPane.showMessageDialog(null,"No rooms of the specified type are available.");
                    System.out.println("No rooms of the specified type are available.");
                    return;
                }
                // Book the room
                room.setAvailable(false);
                BookingInfo booking = new BookingInfo(name, phone, checkin, checkout, room);
                bookings.add(booking);
                // Save booking information to file
                saveBooking(booking);
                JOptionPane.showMessageDialog(null, "Room " + room.getRoomNumber() + " has been booked for " + name + ".");
                System.out.println("Room " + room.getRoomNumber() + " has been booked for " + name + ".");
                RoomAvailability(room, checkout);

            }
            
            public void displayBookings() {
                for (BookingInfo booking : bookings) {
                    System.out.println(booking);
                }
            }
            
            private void saveBooking(BookingInfo booking) {
                try {
                    File file = new File("bookings.txt");
                    FileWriter writer = new FileWriter(file, true);
                    writer.write(booking.toString() + "\n");
                    writer.close();
                } catch (IOException e) {
                    
                    e.printStackTrace();
                }
            }
            private void RoomAvailability(Room room, Date checkout) {
                long currentTime = System.currentTimeMillis();
                long checkoutTime = checkout.getTime();
                long delay = checkoutTime - currentTime;
                
                if (delay > 0) {
                    new Thread(() -> {
                        try {
                            Thread.sleep(delay);
                            room.setAvailable(true);
                            System.out.println("Room " + room.getRoomNumber() + " is now available.");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }).start();
                }
            }
            
        
        }
            