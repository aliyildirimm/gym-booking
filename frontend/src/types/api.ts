/** Gym class from Class Service API */
export interface GymClass {
  id: number;
  name: string;
  totalCapacity: number;
  availableSpots: number;
}

/** Booking from Booking Service API */
export interface Booking {
  id: number;
  classId: number;
  userName: string;
  createdAt: string;
}

/** Request body for creating a booking */
export interface CreateBookingPayload {
  classId: number;
  userName: string;
}
