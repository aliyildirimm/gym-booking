import { useState } from 'react';
import { createBooking } from '../api';
import type { CreateBookingPayload } from '../types/api';
import type { AsyncState } from '../types/async';

interface UseBookingSubmitResult {
  state: AsyncState;
  submitBooking: (payload: CreateBookingPayload) => Promise<void>;
  reset: () => void;
}

export function useBookingSubmit(): UseBookingSubmitResult {
  const [state, setState] = useState<AsyncState>({ status: 'idle' });

  const submitBooking = async (payload: CreateBookingPayload): Promise<void> => {
    setState({ status: 'loading' });

    try {
      await createBooking(payload);
      setState({ status: 'success', data: undefined });
    } catch (err: unknown) {
      const axiosError = err as { response?: { data?: { message?: string; error?: string } }; message?: string };
      const data = axiosError.response?.data;
      const errorMessage = data?.message ?? data?.error ?? axiosError.message ?? 'Booking failed';
      setState({ status: 'error', error: errorMessage });
    }
  };

  const reset = () => setState({ status: 'idle' });

  return {
    state,
    submitBooking,
    reset,
  };
}
