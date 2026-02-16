import React from 'react';
import { IonButton } from '@ionic/react';
import { ErrorMessage } from '../../../components';

interface ClassLoadingErrorProps {
  error: string;
  onBackToClasses: () => void;
}

export const ClassLoadingError: React.FC<ClassLoadingErrorProps> = ({
  error,
  onBackToClasses,
}) => (
  <>
    <ErrorMessage message={error} className="ion-color-danger" />
    <IonButton expand="block" onClick={onBackToClasses}>
      Back to classes
    </IonButton>
  </>
);
