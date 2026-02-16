import axios, { AxiosResponse } from 'axios';
import type { GymClass, Booking, CreateBookingPayload } from './types/api';

const classApiBase = 'http://localhost:8081';
const bookingApiBase = 'http://localhost:8082';

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
