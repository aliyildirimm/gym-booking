export type AsyncState<T = void> =
  | { status: 'loading' }
  | { status: 'success'; data: T }
  | { status: 'error'; error: string };
