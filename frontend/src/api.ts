import axios, { AxiosResponse } from 'axios';
import type { GymClass, Booking, CreateBookingPayload } from './types/api';

const classApiBase = process.env.REACT_APP_CLASS_API_URL || 'http://localhost:8081';
const bookingApiBase = process.env.REACT_APP_BOOKING_API_URL || 'http://localhost:8082';

const classApi = axios.create({ baseURL: classApiBase });
const bookingApi = axios.create({ baseURL: bookingApiBase });

export function getClasses(): Promise<AxiosResponse<GymClass[]>> {
  return classApi.get<GymClass[]>('/classes');
}

export function getClass(id: string | number): Promise<AxiosResponse<GymClass>> {
  return classApi.get<GymClass>(`/classes/${id}`);
}

export function createBooking(
  payload: CreateBookingPayload
): Promise<AxiosResponse<Booking>> {
  return bookingApi.post<Booking>('/bookings', payload);
}
