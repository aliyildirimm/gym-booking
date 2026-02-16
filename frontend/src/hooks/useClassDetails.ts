import { useState } from 'react';
import { getClass } from '../api';
import type { GymClass } from '../types/api';
import type { AsyncState } from '../types/async';

export function useClassDetails() {
  const [state, setState] = useState<AsyncState<GymClass>>({ status: 'idle' });

  const fetchClass = async (classId: string) => {
    setState({ status: 'loading' });

    try {
      const res = await getClass(classId);
      setState({ status: 'success', data: res.data });
    } catch (err: unknown) {
      const axiosError = err as { response?: { data?: { message?: string } }; message?: string };
      const data = axiosError.response?.data;
      const errorMessage = data?.message ?? axiosError.message ?? 'Class not found';
      setState({ status: 'error', error: errorMessage });
    }
  };

  return {
    state,
    fetchClass,
    classDetails: state.status === 'success' ? state.data : null,
  };
}
