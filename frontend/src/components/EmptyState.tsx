import React from 'react';
import { IonItem, IonLabel } from '@ionic/react';

interface EmptyStateProps {
  message: string;
}

export const EmptyState: React.FC<EmptyStateProps> = ({ message }) => (
  <IonItem lines="none">
    <IonLabel className="ion-text-center">{message}</IonLabel>
  </IonItem>
);
