import { useState } from 'react';
import { createClass } from '../api';
import type { CreateClassPayload } from '../types/api';
import type { AsyncState } from '../types/async';

interface UseClassCreateResult {
  state: AsyncState;
  submitClass: (payload: CreateClassPayload) => Promise<void>;
  reset: () => void;
}

export function useClassCreate(): UseClassCreateResult {
  const [state, setState] = useState<AsyncState>({ status: 'idle' });

  const submitClass = async (payload: CreateClassPayload): Promise<void> => {
    setState({ status: 'loading' });

    try {
      await createClass(payload);
      setState({ status: 'success', data: undefined });
    } catch (err: unknown) {
      const axiosError = err as { response?: { data?: { message?: string; error?: string } }; message?: string };
      const data = axiosError.response?.data;
      const errorMessage = data?.message ?? data?.error ?? axiosError.message ?? 'Failed to create class';
      setState({ status: 'error', error: errorMessage });
    }
  };

  const reset = () => setState({ status: 'idle' });

  return {
    state,
    submitClass,
    reset,
  };
}
