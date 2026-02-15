import React from 'react';

interface ErrorMessageProps {
  message: string;
  className?: string;
}

export const ErrorMessage: React.FC<ErrorMessageProps> = ({
  message,
  className = 'ion-text-center ion-color-danger',
}) => <p className={className} style={{ marginTop: 16 }}>{message}</p>;
