import React from 'react';
import { IonSpinner } from '@ionic/react';

interface LoadingSpinnerProps {
  className?: string;
  name?: 'crescent' | 'circular' | 'dots' | 'lines' | 'lines-small';
}

export const LoadingSpinner: React.FC<LoadingSpinnerProps> = ({
  className = 'ion-text-center ion-padding',
  name = 'crescent',
}) => (
  <div className={className}>
    <IonSpinner name={name} />
  </div>
);
