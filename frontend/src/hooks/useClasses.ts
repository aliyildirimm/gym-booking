import { useState } from 'react';
import { getClasses } from '../api';
import type { GymClass } from '../types/api';
import type { AsyncState } from '../types/async';

interface UseClassesResult {
  state: AsyncState<GymClass[]>;
  classes: GymClass[];
  fetchClasses: () => Promise<void>;
}

export function useClasses(): UseClassesResult {
  const [state, setState] = useState<AsyncState<GymClass[]>>({ status: 'loading' });

  const fetchClasses = async () => {
    setState({ status: 'loading' });

    try {
      const res = await getClasses();
      setState({ status: 'success', data: res.data ?? [] });
    } catch (err: unknown) {
      const error = err as { message?: string };
      const errorMessage = error.message ?? 'Failed to load classes';
      setState({ status: 'error', error: errorMessage });
    }
  };

  return {
    state,
    classes: state.status === 'success' ? state.data : [],
    fetchClasses,
  };
}
